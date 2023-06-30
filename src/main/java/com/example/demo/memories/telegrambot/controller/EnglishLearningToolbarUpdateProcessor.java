package com.example.demo.memories.telegrambot.controller;

import com.example.demo.memories.telegrambot.utils.ButtonUtils;
import com.example.demo.memories.telegrambot.enums.ButtonType;
import com.example.demo.memories.telegrambot.service.WordStorageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.demo.memories.telegrambot.utils.ButtonUtils.*;
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
        wordStorageHandler.saveCurrentWordAsLearned(telegramBot.getCurrentOption());
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        editMessageText.setText(wordValue);
        setReplyMarkupAndExecuteMessage(selectKeyboard());
    }

    @Override
    public void processNextWord(Update update) {
        setMessageIdAndChatId(update);
        var wordValue = wordStorageHandler.getWordValue(telegramBot.getCurrentOption());
        editMessageText.setText(wordValue);
        setReplyMarkupAndExecuteMessage(selectKeyboard());
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
        var message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(wordStorageHandler.getWordValue(telegramBot.getCurrentOption()))
                .build();
        var inlineKeyboardMarkup = getInlineKeyboardMarkup(selectKeyboard());
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeMessage(message);
    }

    private void setMessageIdAndChatId(Update update) {
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
    }

    private void setReplyMarkupAndExecuteMessage(List<ButtonType> buttonTypes) {
        var inlineKeyboardMarkup = ButtonUtils.getInlineKeyboardMarkup(buttonTypes);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeMessage(editMessageText);
    }

    private List<ButtonType> selectKeyboard(){
        if(telegramBot.getCurrentOption().getCommand().startsWith("/ru")){
           return russianCommandButtons;
        }else{
           return englishCommandButtons;
        }
    }
}
