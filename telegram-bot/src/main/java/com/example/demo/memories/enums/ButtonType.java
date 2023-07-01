package com.example.demo.memories.enums;

public enum ButtonType {
    ENGLISH("En"),
    RUSSIAN("Ru"),
    SYNONYM("Synonym"),
    LEARNED("Learned"),//✅
    REPEAT("Repeat"),
    NEXT(">>");

    private final String buttonText;

    ButtonType(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public static ButtonType fromButtonType(String buttonType){
        for(ButtonType keyboardButtonType: ButtonType.values()){
            if(keyboardButtonType.name().equals(buttonType)){
                return keyboardButtonType;
            }
        }
        return null;
    }
}
