package com.example.demo.memories.controller;

import com.example.demo.memories.enums.ButtonType;
import com.example.demo.memories.dto.Word;
import com.example.demo.memories.service.WordStorageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;

import static com.example.demo.memories.utils.ButtonUtils.*;
import static com.example.demo.memories.utils.MessageUtils.*;

@Component
public class GrammarCheckUpdateProcessor {
    private final EditMessageText editMessageText =  new EditMessageText();
    private Word word;
    private TelegramBot telegramBot;
    private final WordStorageHandler wordStorageHandler;

    public GrammarCheckUpdateProcessor(WordStorageHandler wordStorageHandler) {
        this.wordStorageHandler = wordStorageHandler;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processOnSkip(Update update){
        setMessageIdAndChatId(update);
        word = wordStorageHandler.getNextWord(telegramBot.getCurrentOption());
        editMessageText.setText(convertCollectionToString(word.getTranslation()));
        setReplyMarkupAndExecuteMessage(selectKeyboard(telegramBot.getCurrentOption()));
    }

    public void processOnPrompt(Update update){
        setMessageIdAndChatId(update);
        editMessageText.setText(word.getWord());
        setReplyMarkupAndExecuteMessage(List.of(ButtonType.SKIP));
    }

    public void process(Update update) {
        telegramBot.sendMessage(update, TRANSLATE_WORD);
        word = wordStorageHandler.getNextWord(telegramBot.getCurrentOption());
        createAndSendMessage(update, word.getTranslation());
    }

    public void checkAnswer(Update update, String answer){
        if(answer.equals(word.getWord())){
            telegramBot.sendMessage(update, CONGRATS);
            word = wordStorageHandler.getNextWord(telegramBot.getCurrentOption());
            createAndSendMessage(update, word.getTranslation());
        }else{
            telegramBot.sendMessage(update, WRONG_ANSWER);
            createAndSendMessage(update, word.getTranslation());
        }
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

    private void createAndSendMessage(Update update, Set<String> translate){
        var message = createSendMessage(update, convertCollectionToString(translate));
        var inlineKeyboardMarkup = getInlineKeyboardMarkup(selectKeyboard(telegramBot.getCurrentOption()));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeMessage(message);
    }
}
