package com.example.demo.memories.service.impl;

import com.example.demo.memories.client.FileUploadClient;
import com.example.demo.memories.service.DocumentHandler;
import com.example.demo.memories.service.DocumentHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class ExcelDocumentHandler implements DocumentHandler {
    private final FileUploadClient client;
    public ExcelDocumentHandler(FileUploadClient client) {
        this.client = client;
    }
    @Override
    public void handle(Update update) {
        validateDocument(update);
        int response = client.uploadFile(update);
        if(response != 200){
            log.error("response status is " + response);
            throw new RuntimeException("response status is " + response);
        }
    }

    private void validateDocument(Update update){
        Document document = update.getMessage().getDocument();
        if(!DocumentHandlerFactory.EXCEL_TYPE.equals(document.getMimeType())){
            log.error("unsupported file format, upload excel file");
            throw new RuntimeException("unsupported file format, upload excel file");
        }
        if(document.getFileSize() > 800000L){
            log.error("too big size of file");
            throw new RuntimeException("too big size of file");
        }
    }
}
