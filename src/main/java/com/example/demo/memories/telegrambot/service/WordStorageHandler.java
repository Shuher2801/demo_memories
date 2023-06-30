package com.example.demo.memories.telegrambot.service;

import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import com.example.demo.memories.telegrambot.enums.BotCommandType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class WordStorageHandler {

    private Word word;
    private final List<Word> allWords = new ArrayList<>();
    private final List<Word> difficultWords = new ArrayList<>();
    private final List<Word> learnedWords = new ArrayList<>();
    private final WordRepository repository;

    public WordStorageHandler(WordRepository repository) {
        this.repository = repository;
    }

    public Word getCurrentWord() {
        return word;
    }

    public Word getNextWord(BotCommandType option) {
        List<Word> words = selectStorage(option);
        if(words.isEmpty()) {
            switch (option){
                case EN_RU_NEW, RU_EN_NEW -> words.addAll(repository.findAllWordsByIsLearned(false));
                case EN_RU_OLD, RU_EN_OLD -> words.addAll(repository.findAllWordsByIsLearned(true));
                default -> words.addAll(repository.findAll());
            }
        }
        return getRandomAndRemove(words);
    }

    public String getWordValue(BotCommandType option) {
        word = getNextWord(option);
        switch (option){
            case EN_RU_ALL, EN_RU_NEW, EN_RU_OLD -> {return word.getWord();}
            case RU_EN_ALL, RU_EN_NEW, RU_EN_OLD -> {return word.getTranslation().toString();}
            default -> { log.error("Can't determine word, menu isn't supported");
                throw new RuntimeException("This menu isn't supported");
            }
        }
    }

    private List<Word> selectStorage(BotCommandType option){
        switch (option){
            case EN_RU_NEW, RU_EN_NEW -> {return difficultWords;}
            case EN_RU_OLD, RU_EN_OLD -> {return learnedWords;}
            case EN_RU_ALL, RU_EN_ALL -> {return allWords;}
            default -> {
                log.error("Unexpected option in WordStorage class");
                throw new RuntimeException("Unexpected option");
            }
        }
    }

    private  Word getRandomAndRemove(List<Word> words) {
        if(!words.isEmpty()) {
            var random = new Random();
            int index = random.nextInt(words.size());
            word =  words.remove(index);
        }
        return word;
    }

    public void saveCurrentWordAsLearned(BotCommandType currentOption) {
        Word word = getCurrentWord();
        word.setLearned(true);
        repository.save(word);
    }
}
