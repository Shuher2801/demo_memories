package com.example.demo.memories.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface DocumentHandler {
    void handle(Update update);
}
