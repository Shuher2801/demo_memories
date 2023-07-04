package com.example.demo.memories.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

public class MessageUtils {
    public static final String INCORRECT_COMMAND = "Sorry, command was not recognized";
    public static final String NO_WORDS = "Sorry, there are no words for this option";
    public static final String TRANSLATE_WORD = "Translate the word (phrase)";
    public static final String CONGRATS = "Congrats, this is the right answer. Try the following word";
    public static final String WRONG_ANSWER = "Wrong answer, try again";
    public static final String CHOSE_OPTION = "Chose option from menu";

    public static SendMessage createSendMessage(Update update, String message){
        return  SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(message)
                .build();
    }
    public static String convertCollectionToString (Collection<String> collection ){
        return collection.stream().reduce((a, b) -> a + ", " + b).orElse("");
    }
}
