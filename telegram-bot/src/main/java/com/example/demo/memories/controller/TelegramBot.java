package com.example.demo.memories.controller;

import com.example.demo.memories.config.BotConfig;
import com.example.demo.memories.enums.BotCommandType;
import com.example.demo.memories.enums.ButtonType;
import com.example.demo.memories.service.impl.DocumentProcessor;
import com.example.demo.memories.service.impl.EnglishLearningToolbarUpdateProcessor;
import com.example.demo.memories.service.impl.GeneralOptionsUpdateProcessor;
import com.example.demo.memories.service.impl.GrammarCheckUpdateProcessor;
import com.example.demo.memories.utils.BotCommandStorage;
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
import javax.annotation.PostConstruct;
import java.util.Optional;
import static com.example.demo.memories.utils.MessageUtils.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String SHEET = "Words";

    private static final String GRAMMAR ="/grammar";
    private BotCommandType currentOption = null;
    @Autowired
    private EnglishLearningToolbarUpdateProcessor englishLearningToolbarUpdateProcessor;
    @Autowired
    private GeneralOptionsUpdateProcessor generalOptionsUpdateProcessor;
    @Autowired
    private GrammarCheckUpdateProcessor grammarProcessor;
    @Autowired
    private BotCommandStorage botCommandStorage;
    @Autowired
    private DocumentProcessor documentProcessor;
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig){
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @PostConstruct
    public void init(){
        englishLearningToolbarUpdateProcessor.registerBot(this);
        generalOptionsUpdateProcessor.registerBot(this);
        grammarProcessor.registerBot(this);
        documentProcessor.registerBot(this);
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

        Optional.ofNullable(update)
                .filter(u -> u.hasMessage() && u.getMessage().hasText())
                .ifPresent(this::updateByMessage);

        Optional.ofNullable(update)
                .filter(u -> u.hasMessage() && u.getMessage().hasDocument())
                .ifPresent(this::processDocument);

        Optional.ofNullable(update)
                .filter(Update::hasCallbackQuery)
                .ifPresent(this::updateByCallbackQuery);

        if(update == null){
            log.error("Received update is null");
        }
    }

    private void updateByMessage(Update update) {
        var messageText = update.getMessage().getText().trim().toLowerCase();

        if(messageText.startsWith("/")){
            commandProcessing(messageText, update);
        } else {
            textProcessing(messageText, update);
        }
    }

    private void textProcessing(String messageText, Update update) {
        if(currentOption!=null && currentOption.getCommand().startsWith(GRAMMAR)){
            grammarProcessor.checkAnswer(update, messageText);
        } else {
            sendMessage(update, CHOSE_OPTION);
        }
    }

    private void commandProcessing(String messageText, Update update) {
        var command = BotCommandType.fromCommand(messageText);
        if(command == null){
            sendMessage(update, INCORRECT_COMMAND);
            return;
        }
        currentOption = command;
        switch (command) {
            case START -> generalOptionsUpdateProcessor.processStartCommand(update);
            case EN_RU_ALL, RU_EN_ALL, RU_EN_OLD, EN_RU_OLD, EN_RU_NEW, RU_EN_NEW ->
                    englishLearningToolbarUpdateProcessor.processLearningCommand(update);
            case GRAMMAR_ALL, GRAMMAR_NEW, GRAMMAR_OLD -> grammarProcessor.process(update);
        }
    }

    private void processDocument(Update update) {
        documentProcessor.processDocument(update);
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
            case REPEAT -> englishLearningToolbarUpdateProcessor.processLearnAgain(update);
            case PROMPT -> grammarProcessor.processOnPrompt(update);
            case SKIP -> grammarProcessor.processOnSkip(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    public void sendMessage(Update update, String text) {
        var message = createSendMessage(update, text);
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
