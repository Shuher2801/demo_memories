package com.example.demo.memories.utils;

import com.example.demo.memories.enums.ButtonType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ButtonUtils {

    public static final String INCORRECT_COMMAND = "Sorry, command was not recognized";
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

    public static String convertCollectionToString (Collection<String> collection ){
        return collection.stream().reduce((a, b) -> a + ", " + b).orElseGet(() -> "");
    }
}
