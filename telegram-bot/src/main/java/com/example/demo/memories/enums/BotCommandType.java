package com.example.demo.memories.enums;

public enum BotCommandType {
    START("/start", "start interaction"),
    EN_RU_NEW("/en_ru_new", "english-russian new"),
    RU_EN_NEW("/ru_en_new", "russian-english new"),
    EN_RU_ALL("/en_ru_all", "english-russian"),
    RU_EN_ALL("/ru_en_all", "russian-english"),
    EN_RU_OLD("/en_ru_old", "english-russian old"),
    RU_EN_OLD("/ru_en_old", "russian-english old"),
    GRAMMAR_ALL("/grammar_all", "grammar all words"),
    GRAMMAR_NEW("/grammar_new", "grammar new words"),
    GRAMMAR_OLD("/grammar_old", "grammar old words");



    private final String command;
    private final String description;

    BotCommandType(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static BotCommandType fromCommand(String command) {
        for (BotCommandType botCommand : BotCommandType.values()) {
            if (botCommand.getCommand().equalsIgnoreCase(command)) {
                return botCommand;
            }
        }
        return null;
    }
}
