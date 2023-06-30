package com.example.demo.memories.telegrambot.controller;

import com.example.demo.memories.telegrambot.utils.BotCommandStorage;
import com.example.demo.memories.telegrambot.enums.BotCommandType;
import com.example.demo.memories.telegrambot.enums.ButtonType;
import com.example.demo.memories.telegrambot.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.demo.memories.telegrambot.utils.ButtonUtils.INCORRECT_COMMAND;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private BotCommandType currentOption = null;
    @Autowired
    private EnglishLearningToolbarUpdateProcessor englishLearningToolbarUpdateProcessor;
    @Autowired
    private GeneralOptionsUpdateProcessor generalOptionsUpdateProcessor;
    @Autowired
    private BotCommandStorage botCommandStorage;
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig){
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @PostConstruct
    public void init(){
        englishLearningToolbarUpdateProcessor.registerBot(this);
        generalOptionsUpdateProcessor.registerBot(this);
        try{
            var setMyCommands = SetMyCommands.builder()
                    .commands(botCommandStorage.getBotCommands())
                    .scope(new BotCommandScopeDefault())
                    .languageCode(null).build();
            this.execute(setMyCommands);
        }catch (TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update == null){
            log.error("Received update is null");
            return;
        }
        if(update.hasMessage() && update.getMessage().hasText()){
            updateByMessage(update);
        }
        if(update.hasCallbackQuery()){
            updateByCallbackQuery(update);
        }
    }

    private void updateByMessage(Update update) {
        var messageText = update.getMessage().getText();

        var command = BotCommandType.fromCommand(messageText);
        if(command== null){
            sendMessage(update, INCORRECT_COMMAND);
            return;
        }
        currentOption = command;
        switch (command) {
            case START -> generalOptionsUpdateProcessor.processStartCommand(update);
            case EN_RU_ALL, RU_EN_ALL, RU_EN_OLD, EN_RU_OLD, EN_RU_NEW, RU_EN_NEW ->
                    englishLearningToolbarUpdateProcessor.processLearningCommand(update);
        }
    }

    private void updateByCallbackQuery(Update update) {
        String callBackData = update.getCallbackQuery().getData();

        var buttonType = ButtonType.fromButtonType(callBackData);
        if(buttonType == null){
            sendMessage(update, INCORRECT_COMMAND);
            return;
        }

        switch (buttonType){
            case RUSSIAN -> englishLearningToolbarUpdateProcessor.processTranslate(update);
            case SYNONYM -> englishLearningToolbarUpdateProcessor.processSynonyms(update);
            case ENGLISH -> englishLearningToolbarUpdateProcessor.processEnglishWord(update);
            case NEXT -> englishLearningToolbarUpdateProcessor.processNextWord(update);
            case LEARNED -> englishLearningToolbarUpdateProcessor.processLearnedWord(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    private void sendMessage(Update update, String text) {
        var message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(text)
                .build();
        executeMessage(message);
    }
    public void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred during execution of SendMessage: " + e);
            throw new RuntimeException(e);
        }
    }

    public void executeMessage(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Error occurred during execution of EditMessageText: " + e);
            throw new RuntimeException(e);
        }
    }

    public BotCommandType getCurrentOption() {
        return currentOption;
    }
}