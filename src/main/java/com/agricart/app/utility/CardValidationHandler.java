package com.agricart.app.utility;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class CardValidationHandler {

    private final RestTemplate restTemplate;
    private final Environment env;

    public void deductAmount(String cardType , String cardNumber , String csv , String cardHolder , float amount){
        String postUrl = env.getProperty("mpay.deduct-amount");

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setBearerAuth(env.getProperty("mpay.key"));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("cardType" , cardType);
        map.put("cardNumber" , cardNumber);
        map.put("csv" , csv);
        map.put("cardHolder" , cardHolder);
        map.put("amount" , amount);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Object> response = null;
        try {
            response = this.restTemplate.postForEntity(postUrl, entity, Object.class);
        }catch (Exception e){
            throw new RuntimeException("card validation failed : " + e.getMessage());
        }
        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("card validation success");
        } else {
            throw new RuntimeException("card validation failed");
        }
    }

}
