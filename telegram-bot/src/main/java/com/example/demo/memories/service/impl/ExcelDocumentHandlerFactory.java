package com.example.demo.memories.service.impl;

import com.example.demo.memories.client.FileUploadClient;
import com.example.demo.memories.service.DocumentHandler;
import com.example.demo.memories.service.DocumentHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(DocumentHandlerFactory.EXCEL_TYPE)
public class ExcelDocumentHandlerFactory implements DocumentHandlerFactory{
    @Autowired
    private FileUploadClient client;
    @Override
    public DocumentHandler createDocumentHandler(String mimeType) {
        return new ExcelDocumentHandler(client);
    }
}
