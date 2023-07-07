package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;

@SpringBootApplication(exclude = HazelcastAutoConfiguration.class)
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
