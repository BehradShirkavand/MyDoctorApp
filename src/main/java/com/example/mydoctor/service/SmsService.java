package com.example.mydoctor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class SmsService {

    @Value("${api}")
    private String api; 

    private RestTemplate restTemplate;

    @Autowired
    public SmsService(RestTemplate theRestTemplate) {
        this.restTemplate = theRestTemplate;
    }

    public void sendSms(String phoneNumber, String message) {
        
        String url = "https://api.kavenegar.com/v1/" + api + "/sms/send.json";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("sender", "2000660110");
        form.add("receptor", phoneNumber);
        form.add("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}

