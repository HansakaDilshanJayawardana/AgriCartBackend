package com.agricart.app.utility;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
@Log4j
public class SmsHandler {

    private final RestTemplate restTemplate;
    private final Environment env;

    public void sendSms(String phoneNumber , String message){
        String postUrl = env.getProperty("sms.single-sms");

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setBearerAuth(env.getProperty("sms.key"));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("phone" , phoneNumber);
        map.put("message" , message);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Object> response = null;
        try {
            response = this.restTemplate.postForEntity(postUrl, entity, Object.class);
        }catch (Exception e){
            throw new RuntimeException("sms service unavailable : " + e.getMessage());
        }
        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("sms sent");
        } else {
            throw new RuntimeException("sms service unavailable");
        }
    }

    public void sendConfMessage(String phoneNumber , long msg){
        this.sendSms(phoneNumber , "please conform your payment : " + msg);
    }

    public int generateSmsKey(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }

}
