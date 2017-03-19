package eu.cryptoeuro.accountIdentity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cryptoeuro.accountIdentity.response.Account;
import eu.cryptoeuro.accountIdentity.response.AccountsResponse;
import eu.cryptoeuro.accountIdentity.response.LdapResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URL;

import java.io.IOException;

@Component
@Slf4j
public class AccountIdentityService {

    private String accountIdentityServer = "http://id.euro2.ee:8080"; // account-identity node on AWS

    public LdapResponse getLdap(String idCode) {
        ObjectMapper mapper = new ObjectMapper();
        LdapResponse obj = null;
        try {
            obj = mapper.readValue(new URL(accountIdentityServer+"/v1/ldap/"+idCode), LdapResponse.class);
        } catch (Exception e) {
            //log.error("Failed loading ldap data from account-identity", e);
            // TODO: Figure out the reasond and throw Validation exception if needed
            throw new RuntimeException();
        }
        return obj;
    }

    public Account getAddress(String idCode) {
        ObjectMapper mapper = new ObjectMapper();
        AccountsResponse accountsResponse = null;
        try {
            accountsResponse = mapper.readValue(new URL(accountIdentityServer+"/v1/accounts?ownerId="+idCode), AccountsResponse.class);
        } catch (Exception e) {
            //log.error("Failed loading account data from account-identity", e);
            // TODO: Figure out the reasond and throw Validation exception if needed
            throw new RuntimeException(e);
        }

        Account account = accountsResponse.getAccounts().get(0);

        return account;
    }
}
