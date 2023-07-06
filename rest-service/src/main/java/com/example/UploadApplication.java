package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UploadApplication {

    public static void main(String[] args) {
        //temporary workaround to catch exceptions during development
        try {
            SpringApplication.run(UploadApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
