package eu.cryptoeuro.service;

import eu.cryptoeuro.accountIdentity.response.AccountsResponse;
import eu.cryptoeuro.accountIdentity.response.Account;
import eu.cryptoeuro.domain.Spray;
import eu.cryptoeuro.accountIdentity.service.AccountIdentityService;

import eu.cryptoeuro.accountIdentity.response.LdapResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.cryptoeuro.rest.CreateSprayCommand;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URL;

import java.io.IOException;

@Component
@Slf4j
public class SprayService {

    @Autowired
    private AccountIdentityService accountIdentityService;

    public Spray spray(CreateSprayCommand createSprayCommand) {
        LdapResponse ldap = accountIdentityService.getLdap(createSprayCommand.getIdCode());
        Account accounts = accountIdentityService.getAddress(createSprayCommand.getIdCode());

        return new Spray();

    }
}
