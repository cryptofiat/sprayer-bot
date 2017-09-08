package eu.cryptoeuro.walletServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cryptoeuro.domain.TransferInfoRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TransferInfoService {

    private String transferInfo = "http://wallet.euro2.ee:8000/";
    private ObjectMapper mapper = new ObjectMapper();

    protected RestTemplate restTemplate = new RestTemplate();

    public TransferInfoRecord send(String blockHash, TransferInfoRecord transferInfoRecord) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        String json;
        try {
            json = mapper.writeValueAsString(transferInfoRecord);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<Object> request = new HttpEntity<>(json, headers);

        log.info("Sending a spray to transfer info");
        log.info("JSON:\n" + request.toString());

        TransferInfoRecord response = restTemplate.postForObject(transferInfo+blockHash, request, TransferInfoRecord.class);
        return response;
    }
}
