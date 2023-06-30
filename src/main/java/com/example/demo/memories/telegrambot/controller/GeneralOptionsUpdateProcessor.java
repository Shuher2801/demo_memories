package com.example.demo.memories.telegrambot.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GeneralOptionsUpdateProcessor {

    private TelegramBot telegramBot;
    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processStartCommand(Update update){
        String name = update.getMessage().getChat().getFirstName();
        String answer = String.format("Hi, %s, nice to meet you", name);
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(answer);
        telegramBot.executeMessage(message);
    }
}
