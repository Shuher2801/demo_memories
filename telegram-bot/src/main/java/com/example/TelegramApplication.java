package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramApplication {

    public static void main(String[] args) {
        //temporary workaround to catch exceptions during development
    try{
        SpringApplication.run(TelegramApplication.class, args);
    }catch (Exception e)
    {
         e.printStackTrace();
    }

    }
}

