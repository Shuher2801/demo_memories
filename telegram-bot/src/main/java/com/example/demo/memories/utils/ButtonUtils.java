package com.example.demo.memories.utils;

import com.example.demo.memories.enums.BotCommandType;
import com.example.demo.memories.enums.ButtonType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ButtonUtils {
    public static List<ButtonType> russianCommandButtons = Arrays.asList(
            ButtonType.ENGLISH,
            ButtonType.SYNONYM,
            ButtonType.LEARNED,
            ButtonType.NEXT
    );
    public static List<ButtonType> synonymCommandButtons = Arrays.asList(
            ButtonType.ENGLISH,
            ButtonType.RUSSIAN,
            ButtonType.LEARNED,
            ButtonType.NEXT
    );
    public static List<ButtonType> englishCommandButtons = Arrays.asList(
            ButtonType.RUSSIAN,
            ButtonType.SYNONYM,
            ButtonType.LEARNED,
            ButtonType.NEXT
    );
    public static List<ButtonType> learnedEnglishCommandButtons = Arrays.asList(
            ButtonType.RUSSIAN,
            ButtonType.SYNONYM,
            ButtonType.REPEAT,
            ButtonType.NEXT
    );
    public static List<ButtonType> learnedRussianCommandButtons = Arrays.asList(
            ButtonType.ENGLISH,
            ButtonType.SYNONYM,
            ButtonType.REPEAT,
            ButtonType.NEXT
    );
    public static List<ButtonType> grammarCommandButtons = Arrays.asList(
            ButtonType.PROMPT,
            ButtonType.SKIP
    );


    public static InlineKeyboardMarkup getInlineKeyboardMarkup(List<ButtonType> buttonTypes) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        buttonTypes.forEach(buttonType -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonType.getButtonText());
            button.setCallbackData(buttonType.name());
            rowInline.add(button);
        });
        rowsInline.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    public static List<ButtonType> selectKeyboard(BotCommandType command){
        switch (command) {
            case EN_RU_NEW, EN_RU_ALL ->{return englishCommandButtons;}
            case RU_EN_NEW, RU_EN_ALL ->{return russianCommandButtons;}
            case EN_RU_OLD -> {return learnedEnglishCommandButtons;}
            case RU_EN_OLD -> {return learnedRussianCommandButtons;}
            case GRAMMAR_NEW, GRAMMAR_ALL, GRAMMAR_OLD -> {return grammarCommandButtons;}
            default -> throw new RuntimeException("ineligible command in selectKeyboard method");
        }
    }
}
