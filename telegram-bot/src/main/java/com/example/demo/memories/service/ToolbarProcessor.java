package com.example.demo.memories.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ToolbarProcessor {
    void processTranslate(Update update);
    void processSynonyms(Update update);
    void processLearnedWord(Update update);
    void processLearnAgain(Update update);
    void processNextWord(Update update);
    void processEnglishWord(Update update);
    void processLearningCommand(Update update);
}
