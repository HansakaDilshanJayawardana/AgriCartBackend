package com.agricart.app.utility;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class DeliveryHandler {

    private final RestTemplate restTemplate;
    private final Environment env;

    public void sendToDelivery(String address , String phone , long cartId){
        String postUrl = env.getProperty("delivery.send-data");

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setBearerAuth(env.getProperty("delivery.key"));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("address" , address);
        map.put("phone" , phone);
        map.put("cartId" , cartId);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Object> response = null;
        try {
            response = this.restTemplate.postForEntity(postUrl, entity, Object.class);
        }catch (Exception e){
            throw new RuntimeException("delivery service unavailable : " + e.getMessage());
        }
        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("delivery info accepted");
        } else {
            throw new RuntimeException("delivery service unavailable");
        }
    }

}
