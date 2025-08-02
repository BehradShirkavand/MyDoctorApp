package com.example.mydoctor.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {


    public void sendSms(String phoneNumber, String message) {

        // You can use the api of your SMS service in this method
        
        System.out.println("The Otp code for number " + phoneNumber + " is " + message);
    }
}

