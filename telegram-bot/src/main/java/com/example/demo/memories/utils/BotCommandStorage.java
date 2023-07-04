package com.example.demo.memories.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.memories.enums.BotCommandType.*;


@Component
public class BotCommandStorage {
    private final List<BotCommand> botCommands;

    public BotCommandStorage() {
        botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(EN_RU_ALL.getCommand(), EN_RU_ALL.getDescription()));
        botCommands.add(new BotCommand(RU_EN_ALL.getCommand(), RU_EN_ALL.getDescription()));
        botCommands.add(new BotCommand(EN_RU_NEW.getCommand(), EN_RU_NEW.getDescription()));
        botCommands.add(new BotCommand(RU_EN_NEW.getCommand(), RU_EN_NEW.getDescription()));
        botCommands.add(new BotCommand(EN_RU_OLD.getCommand(), EN_RU_OLD.getDescription()));
        botCommands.add(new BotCommand(RU_EN_OLD.getCommand(), RU_EN_OLD.getDescription()));
        botCommands.add(new BotCommand(GRAMMAR_ALL.getCommand(), GRAMMAR_ALL.getDescription()));
        botCommands.add(new BotCommand(GRAMMAR_NEW.getCommand(), GRAMMAR_NEW.getDescription()));
        botCommands.add(new BotCommand(GRAMMAR_OLD.getCommand(), GRAMMAR_OLD.getDescription()));
    }

    public List<BotCommand> getBotCommands() {
        return botCommands;
    }
}
