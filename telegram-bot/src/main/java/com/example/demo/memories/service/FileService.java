package com.example.demo.memories.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    byte[] processDoc(Message telegramMessage);
}
