package eu.cryptoeuro.service;

import eu.cryptoeuro.accountIdentity.response.LdapResponse;
import eu.cryptoeuro.accountIdentity.service.AccountIdentityService;
import eu.cryptoeuro.domain.Spray;
import eu.cryptoeuro.rest.CreateSprayCommand;
import eu.cryptoeuro.transferInfo.command.TransferInfoRecord;
import eu.cryptoeuro.transferInfo.service.TransferInfoService;
import eu.cryptoeuro.util.KeyUtil;
import eu.cryptoeuro.wallet.client.CreateTransferCommand;
import eu.cryptoeuro.wallet.client.WalletClientService;
import eu.cryptoeuro.walletServer.FeeConstant;
import eu.cryptoeuro.walletServer.response.Transfer;
import eu.cryptoeuro.walletServer.service.WalletServerService;
import lombok.extern.slf4j.Slf4j;
import org.ethereum.crypto.ECKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SprayService {

    private AccountIdentityService accountIdentityService;
    private WalletServerService walletServerService;
    private WalletClientService walletClientService;
    private TransferInfoService transferInfoService;
    private KeyUtil keyUtil;

    private String senderAccountAddress = "0x90d0e61c5846780a6608bacbd77633b067bb13fc";
    // If balance falls below this then send slack message that funds are running out
    private Long sprayAmountThreshold = 100L;

    @Autowired
    public SprayService(AccountIdentityService accountIdentityService, WalletServerService walletServerService, WalletClientService walletClientService, TransferInfoService transferInfoService, KeyUtil keyUtil) {
        this.accountIdentityService = accountIdentityService;
        this.walletServerService = walletServerService;
        this.walletClientService = walletClientService;
        this.transferInfoService = transferInfoService;
        this.keyUtil = keyUtil;
    }

    public Spray spray(CreateSprayCommand createSprayCommand) {
        Spray result = new Spray();
        eu.cryptoeuro.accountIdentity.response.Account receiverIdentityAccount = accountIdentityService.getAddress(createSprayCommand.getIdCode());

        if (false && hasReceivedTransfers(receiverIdentityAccount.getAddress())) {
            log.info("Spraying the account is not allowed - recipient already got some money.");
            return result;
        }
        Long sprayAmount = createSprayCommand.getAmount();
        eu.cryptoeuro.walletServer.response.Account senderWalletAccount = walletServerService.getAccount(senderAccountAddress);

        // Check that it is bigger than sprayAmount
        if (senderWalletAccount.getBalance() < sprayAmount) {
            throw new RuntimeException("Spray service out of money");
        }

        // If smaller than threshold then send slack message that funds are running out
        if (senderWalletAccount.getBalance() < sprayAmountThreshold) {
            // TODO: Send slack message
        }

        ECKey key = keyUtil.getSprayerKey();

        CreateTransferCommand createTransferCommand = walletClientService.createAndSignCreateTransferCommand(senderAccountAddress, senderWalletAccount.getNonce(), receiverIdentityAccount.getAddress(), sprayAmount, key, FeeConstant.FEE);

        Transfer transfer = walletServerService.transfer(createTransferCommand);
        result.setTransferId(transfer.getId());

        TransferInfoRecord transferInfoRecord = new TransferInfoRecord();
        transferInfoRecord.setSenderIdCode("99900010050");
        transferInfoRecord.setReceiverIdCode(createSprayCommand.getIdCode());
        transferInfoRecord.setReferenceText("Hello from a generous bot!");

        transferInfoService.send(without0x(transfer.getId()), transferInfoRecord);
        log.info("Spray successful for recipient " + createSprayCommand.getIdCode());
        return result;

    }

    private boolean hasReceivedTransfers(String address) {
        List<Transfer> transfers = walletServerService.getTransfers(address);
        return (transfers.size() != 0);
    }

    protected String without0x(String hex) {
        return hex.startsWith("0x") ? hex.substring(2) : hex;
    }


}
