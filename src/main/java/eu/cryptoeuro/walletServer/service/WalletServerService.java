package eu.cryptoeuro.walletServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cryptoeuro.wallet.client.CreateTransferCommand;
import eu.cryptoeuro.walletServer.response.Account;
import eu.cryptoeuro.walletServer.response.Nonce;
import eu.cryptoeuro.walletServer.response.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

@Component
@Slf4j
public class WalletServerService {

    private String walletServer = "http://wallet.euro2.ee:8080"; // wallet-server node on AWS

    private final ObjectMapper mapper = new ObjectMapper();

    protected RestTemplate restTemplate = new RestTemplate();


    public Transfer transfer(CreateTransferCommand createTransferCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        /*
        String json = "{"+
                "'sourceAccount':'0xa5f1eea6d0a14c8e37cad8019f67b9ca19768f55',"+
                "'targetAccount': 'a5f1eea6d0a14c8e37cad8019f67b9ca19768f55',"+
                "'amount': 1,"+
                "'fee': 1,"+
                "'nonce': 1,"+
                "'signature': '572416e32187b7cf09eb02b82fa6afbed8357043fa0c899384a288fa7f8da5e216af491a5c54a3e55d616add2c91e69e62c287db1c9ec962be07bc7d0ff489c01c'"+
                "}";
       */

        String json;
        try {
            json = mapper.writeValueAsString(createTransferCommand);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<Object> request = new HttpEntity<Object>(json, headers);

        log.info("Sending create transfer call to wallet");
        log.info("JSON:\n" + request.toString());

        Transfer transfer = restTemplate.postForObject(walletServer+"/v1/transfers", request, Transfer.class);
        log.info("Create transfer response: " + transfer.toString());

        return transfer;

    }

    public Nonce getNonce(String address) {
        Nonce nonce = null;
        try {
            nonce = mapper.readValue(new URL(walletServer+"/v1/accounts/"+address+"/nonce"), Nonce.class);
        } catch (Exception e) {
            log.error("Failed loading nonce from wallet-server", e);
            throw new RuntimeException(e);
        }

        return nonce;
    }

    public Account getAccount(String address) {
        Account account = null;
        log.info("Sending account info call to wallet-server");
        try {
            account = mapper.readValue(new URL(walletServer+"/v1/accounts/"+address), Account.class);
        } catch (Exception e) {
            log.error("Failed loading nonce from wallet-server", e);
            throw new RuntimeException(e);
        }
        log.info("... account info done");

        return account;
    }

    public List<Transfer> getTransfers(String address) {
        List<Transfer> transfers;
        log.info("Getting list of transfers from wallet-server");
        try {
            // TODO : refactor calling wallet server
            transfers = mapper.readValue(new URL(walletServer+"/v1/accounts/0x"+address+"/transfers"), mapper.getTypeFactory().constructCollectionType(List.class, Transfer.class));
        } catch (Exception e) {
            log.error("Failed getting transfer list from wallet-server", e);
            throw new RuntimeException(e);
        }
        log.info("... retrieved transfer list successfully");
        return transfers;
    }

}
