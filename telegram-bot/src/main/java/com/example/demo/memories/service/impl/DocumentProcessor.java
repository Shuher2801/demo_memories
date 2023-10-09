package com.example.demo.memories.service.impl;

import com.example.demo.memories.controller.TelegramBot;
import com.example.demo.memories.service.DocumentHandler;
import com.example.demo.memories.service.DocumentHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class DocumentProcessor {
    private static final String PROCESSING_MESSAGE = "File is processing";
    private static final String SUCCESSFUL_MESSAGE = "Words were uploaded successfully. You can start learning";
    private static final String ERROR_MESSAGE = "An error occurred while processing the document";
    private TelegramBot telegramBot;
    private final Map<String, ExcelDocumentHandlerFactory> documentHandlers;
    @Autowired
    public DocumentProcessor(Map<String, ExcelDocumentHandlerFactory> documentHandlers) {
        this.documentHandlers = documentHandlers;
    }
    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void processDocument(Update update){
        var document = update.getMessage().getDocument();
        String mimeType = document.getMimeType();

        telegramBot.sendMessage(update, PROCESSING_MESSAGE);

        DocumentHandlerFactory documentHandlerFactory = documentHandlers.get(mimeType);

        if(documentHandlerFactory != null) {
            CompletableFuture.runAsync(() -> {
                DocumentHandler documentHandler = documentHandlerFactory.createDocumentHandler(mimeType);
                documentHandler.handle(update);
            }).thenRun(() -> {
                telegramBot.sendMessage(update, SUCCESSFUL_MESSAGE);
                    })
            .exceptionally(ex -> {
                //todo: handle all exceptional cases
                log.error(ex.getMessage());
                telegramBot.sendMessage(update, ERROR_MESSAGE);
                return null;
            });
        }
    }
}
