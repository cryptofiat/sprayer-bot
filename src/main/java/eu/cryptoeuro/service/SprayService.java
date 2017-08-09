package eu.cryptoeuro.service;

import eu.cryptoeuro.walletServer.FeeConstant;
import eu.cryptoeuro.accountIdentity.response.AccountsResponse;
import eu.cryptoeuro.domain.Spray;
import eu.cryptoeuro.accountIdentity.service.AccountIdentityService;

import eu.cryptoeuro.accountIdentity.response.LdapResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.cryptoeuro.rest.CreateSprayCommand;
import eu.cryptoeuro.walletServer.command.CreateTransferCommand;
import eu.cryptoeuro.walletServer.response.Transfer;
import eu.cryptoeuro.walletServer.service.WalletServerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import java.io.IOException;

import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;
import org.apache.commons.codec.binary.Base64;
import java.nio.ByteBuffer;
import java.util.List;

@Component
@Slf4j
public class SprayService {

    @Autowired
    private AccountIdentityService accountIdentityService;

    @Autowired
    private WalletServerService walletServerService;

    private String senderPrivateKey = "TODO read from conf";
    private String senderAccount = "TODO read from conf";
    private Long sprayAmount = 1L;
    // If balance falls below this then send slack message that funds are running out
    private Long sprayAmountThreshold = 100L;


    public Spray spray(CreateSprayCommand createSprayCommand) {
        Spray result = new Spray();
        LdapResponse ldap = accountIdentityService.getLdap(createSprayCommand.getIdCode());
        eu.cryptoeuro.accountIdentity.response.Account receiverIdentityAccount = accountIdentityService.getAddress(createSprayCommand.getIdCode());

        if (hasReceivedTransfers(receiverIdentityAccount.getAddress())) {
            result.setResult(false);
            return result;
        }

        eu.cryptoeuro.walletServer.response.Account senderWalletAccount = walletServerService.getAccount(senderAccount);

        // Check that it is bigger than sprayAmount
        if (senderWalletAccount.getBalance() < sprayAmount) {
            throw new RuntimeException("Spray service out of money");
        }

        // If smaller than threshold then send slack message that funds are running out
        if (senderWalletAccount.getBalance() < sprayAmountThreshold) {
            // TODO: Send slack message
        }

        ECKey signer = ECKey.fromPrivate(Hex.decode(without0x(senderPrivateKey)));
        byte[] signatureArg;

        try {
            signatureArg = signDelegate(FeeConstant.FEE, sprayAmount, senderWalletAccount.getNonce() + 1, without0x(receiverIdentityAccount.getAddress()), signer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CreateTransferCommand createTransferCommand = new CreateTransferCommand();
        createTransferCommand.setSourceAccount(senderAccount);
        createTransferCommand.setTargetAccount("0x" + receiverIdentityAccount.getAddress());
        createTransferCommand.setAmount(sprayAmount);
        createTransferCommand.setFee(FeeConstant.FEE);
        createTransferCommand.setNonce(senderWalletAccount.getNonce() + 1);
        createTransferCommand.setSignature(Hex.toHexString(signatureArg));

        Transfer transfer = walletServerService.transfer(createTransferCommand);
        result.setResult(true);
        result.setBlockHash(transfer.getBlockHash());

        //send to transfer-info
        return result;

    }

    private boolean hasReceivedTransfers(String address) {
        List<Transfer> transfers = walletServerService.getTransfers(address);
        if (transfers.isEmpty())
                return false;
        else
            return true;
    }

    // TODO: Refactor signing methods to separate package
    public byte[] signDelegate(long fee, long amount, long nonce, String address, ECKey signer) throws IOException {
        ByteArrayOutputStream hashInput = new ByteArrayOutputStream( );
        hashInput.write(uint256(nonce));
        hashInput.write(Hex.decode(without0x(address)));
        hashInput.write(uint256(amount));
        hashInput.write(uint256(fee));

        byte[] hashOutput = HashUtil.sha3(hashInput.toByteArray());
        String strSig = signer.sign(hashOutput).toBase64();

        // Because contract expects the sig concatenated in different order than canonical
        byte[] byteSig = new byte[65];
        System.arraycopy(Base64.decodeBase64(strSig),1,byteSig,0,64);
        System.arraycopy(Base64.decodeBase64(strSig),0,byteSig,64,1);
        return byteSig;
    }

    protected String without0x(String hex) {
        return hex.startsWith("0x") ? hex.substring(2) : hex;
    }

    public byte[] uint256(long val) {
        ByteBuffer bytes = ByteBuffer.allocate(32);
        bytes.putLong(32-Long.BYTES,val);
        return bytes.array();
    }

}
