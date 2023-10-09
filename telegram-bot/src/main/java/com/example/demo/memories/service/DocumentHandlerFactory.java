package com.example.demo.memories.service;

public interface DocumentHandlerFactory {
     String EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
     DocumentHandler createDocumentHandler(String mimeType);
}
