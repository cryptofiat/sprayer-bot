package eu.cryptoeuro.walletServer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cryptoeuro.walletServer.command.CreateTransferCommand;
import eu.cryptoeuro.walletServer.response.Nonce;
import eu.cryptoeuro.walletServer.response.Account;
import eu.cryptoeuro.walletServer.response.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Slf4j
public class WalletServerService {

    private String walletServer = "http://wallet.euro2.ee:8080"; // wallet-server node on AWS

    protected RestTemplate restTemplate = new RestTemplate();


    public Transfer transfer(CreateTransferCommand createTransferCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<String>(createTransferCommand.toString(), headers);

        log.info("Sending create transfer call to wallet");
        log.info("JSON:\n"+request.toString());
        Transfer transfer = restTemplate.postForObject(walletServer+"/v1/transfers", request, Transfer.class);
        log.info("Create transfer response: " + transfer.toString());

        return transfer;

    }

    public Nonce getNonce(String address) {
        ObjectMapper mapper = new ObjectMapper();
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
        ObjectMapper mapper = new ObjectMapper();
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

}
