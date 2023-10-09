package com.example.demo.memories.service.impl;

import com.example.demo.memories.controller.TelegramBot;
import com.example.demo.memories.service.ToolbarProcessor;
import com.example.demo.memories.enums.ButtonType;
import com.example.demo.memories.service.WordStorageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.demo.memories.utils.ButtonUtils.*;
import static com.example.demo.memories.utils.MessageUtils.*;

@Slf4j
@Component
public class EnglishLearningToolbarUpdateProcessor implements ToolbarProcessor {
    private final EditMessageText editMessageText =  new EditMessageText();
    private TelegramBot telegramBot;
    private final WordStorageHandler wordStorageHandler;

    public EnglishLearningToolbarUpdateProcessor(WordStorageHandler wordStorageHandler) {
        this.wordStorageHandler = wordStorageHandler;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void processTranslate(Update update) {
        setMessageIdAndChatId(update);
        var word = wordStorageHandler.getCurrentWord();
        editMessageText.setText(convertCollectionToString(word.getTranslation()));
        setReplyMarkupAndExecuteMessage(russianCommandButtons);
    }

    @Override
    public void processSynonyms(Update update) {
        setMessageIdAndChatId(update);
        var word = wordStorageHandler.getCurrentWord();
        editMessageText.setText(convertCollectionToString(word.getSynonyms()));
        setReplyMarkupAndExecuteMessage(synonymCommandButtons);
    }

    @Override
    public void processLearnedWord(Update update) {
        wordStorageHandler.saveIsLearned(true);
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        editMessageText.setText(wordValue);
        setReplyMarkupAndExecuteMessage(selectKeyboard(telegramBot.getCurrentOption()));
    }

    @Override
    public void processLearnAgain(Update update) {
        wordStorageHandler.saveIsLearned(false);
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        editMessageText.setText(wordValue);
        setReplyMarkupAndExecuteMessage(selectKeyboard(telegramBot.getCurrentOption()));
    }

    @Override
    public void processNextWord(Update update) {
        setMessageIdAndChatId(update);
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        editMessageText.setText(wordValue);
        setReplyMarkupAndExecuteMessage(selectKeyboard(telegramBot.getCurrentOption()));
    }

    @Override
    public void processEnglishWord(Update update) {
        setMessageIdAndChatId(update);
        var word = wordStorageHandler.getCurrentWord();
        editMessageText.setText(word.getWord());
        setReplyMarkupAndExecuteMessage(englishCommandButtons);
    }

    @Override
    public void processLearningCommand(Update update) {
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        if(wordValue.isEmpty()){
            telegramBot.sendMessage(update, NO_WORDS);
            return;
        }
        var message = createSendMessage(update, wordValue);
        var inlineKeyboardMarkup = getInlineKeyboardMarkup(selectKeyboard(telegramBot.getCurrentOption()));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeMessage(message);
    }

    private void setMessageIdAndChatId(Update update) {
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
    }

    private void setReplyMarkupAndExecuteMessage(List<ButtonType> buttonTypes) {
        var inlineKeyboardMarkup = getInlineKeyboardMarkup(buttonTypes);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeMessage(editMessageText);
    }
}
