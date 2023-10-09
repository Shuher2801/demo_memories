package com.example.demo.memories.client.impl;

import com.example.demo.memories.client.FileUploadClient;
import com.example.demo.memories.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class FileUploadClientImpl implements FileUploadClient {
    private static final String FILE = "file";
    @Value("${service.file_upload.uri}")
    private String fileUploadUri;
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public int uploadFile(Update update) {

        byte[] fileContents = fileService.processDoc(update.getMessage());
        ByteArrayResource fileResource = new ByteArrayResource(fileContents) {
            @Override
            public String getFilename() {
                return update.getMessage().getDocument().getFileName();
            }
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(FILE, fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                fileUploadUri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return responseEntity.getStatusCodeValue();
    }
}
