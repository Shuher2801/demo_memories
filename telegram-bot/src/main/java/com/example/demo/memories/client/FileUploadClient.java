package com.example.demo.memories.client;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface FileUploadClient {
    int uploadFile(Update update);
}
