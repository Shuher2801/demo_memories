package com.example.demo.memories.service;

import com.example.demo.memories.client.WordClient;
import com.example.demo.memories.enums.BotCommandType;
import com.example.demo.memories.dto.Word;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.memories.enums.BotCommandType.*;
import static com.example.demo.memories.utils.MessageUtils.convertCollectionToString;

@Service
@Slf4j
public class WordStorageHandler {
    private Word word = new Word();
    private BotCommandType previousOption;
    private List<Word> persistedWords;
    private List<Word> words = new ArrayList<>();
    private final WordClient wordClient;

    public WordStorageHandler(WordClient wordClient) {
        this.wordClient = wordClient;
    }

    public Word getCurrentWord() {
        return word;
    }

    public Word getNextWord(BotCommandType option) {

        if(persistedWords == null){
            persistedWords = wordClient.getAllWords();
        }

        if(words.isEmpty()) {
            switch (option){
                case EN_RU_NEW, RU_EN_NEW, GRAMMAR_NEW -> words = new ArrayList<>(persistedWords
                        .stream()
                        .filter(w -> !w.isLearned())
                        .toList());
                case EN_RU_OLD, RU_EN_OLD, GRAMMAR_OLD -> words = new ArrayList<>(persistedWords
                        .stream()
                        .filter(Word::isLearned)
                        .toList());
                default -> words = new ArrayList<>(persistedWords);
            }
        }
        return getRandomAndRemove();
    }

    public String getWordValue(BotCommandType option) {
        updateWordsBasedOnOption(option);

        word = getNextWord(option);
        if(word.getWord() == null){
            return StringUtils.EMPTY;
        }
        switch (option){
            case EN_RU_ALL, EN_RU_NEW, EN_RU_OLD -> {return word.getWord();}
            case RU_EN_ALL, RU_EN_NEW, RU_EN_OLD -> {return convertCollectionToString(word.getTranslation());}
            default -> { log.error("Can't determine word, menu isn't supported");
                throw new RuntimeException("This menu isn't supported");
            }
        }
    }

    private void updateWordsBasedOnOption(BotCommandType option) {
        if(previousOption != null && !previousOption.equals(option)){
            word = new Word();
            if(EN_RU_OLD.equals(option) || RU_EN_OLD.equals(option)){
                words = new ArrayList<>(words.stream().filter(Word::isLearned).toList());
            }
            if(EN_RU_NEW.equals(option) || RU_EN_NEW.equals(option)){
                words = new ArrayList<>(words.stream().filter(w -> !w.isLearned()).toList());
            }
        }
        previousOption = option;
    }

    private  Word getRandomAndRemove() {
        if(!words.isEmpty()) {
            var random = new Random();
            int index = random.nextInt(words.size());
            word =  words.remove(index);
        }
        return word;
    }

    public void saveIsLearned(boolean status) {
        Word word = getCurrentWord();
        word.setLearned(status);
        wordClient.updateWord(word);
        persistedWords = wordClient.getAllWords();
    }
}
