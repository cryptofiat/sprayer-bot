package eu.cryptoeuro.service;

import eu.cryptoeuro.domain.Spray;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SprayService {

    public Spray spray() {

        return new Spray();

    }
}
