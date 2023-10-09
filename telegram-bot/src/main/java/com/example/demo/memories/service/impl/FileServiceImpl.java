package com.example.demo.memories.service.impl;

import com.example.demo.memories.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${bot.token}")
    private String token;
    @Override
    public byte[] processDoc(Message telegramMessage) {
        var telegramDoc = telegramMessage.getDocument();
        var fileId = telegramDoc.getFileId();
        var response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            return getBinaryContent(response);
        } else {
            log.error("Bad response from telegram service: " + response);
            throw new RuntimeException("Bad response from telegram service: " + response);
        }
    }
    private ResponseEntity<String> getFilePath(String fileId) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        var request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );
    }

    private byte[] getBinaryContent(ResponseEntity<String> response) {
        var filePath = getFilePath(response);
        var fileInByte = downloadFile(filePath);

        return fileInByte;
    }

    private String getFilePath(ResponseEntity<String> response) {
        var jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private byte[] downloadFile(String filePath) {
        var fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(urlObj.toExternalForm(), e);
        }
    }
}
