package com.example.demo.memories.integration.repository;

import com.example.UploadApplication;
import com.example.demo.memories.integration.ContainersEnvironment;
import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UploadApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WordRepositoryIT extends ContainersEnvironment {

    @Autowired
    private WordRepository wordRepository;

    @Test
    void testSave(){
        Word word = getWord();

        wordRepository.save(word);

        Word persisted = wordRepository.findAll().get(0);
        assertNotNull(persisted.getId());
        assertEquals(persisted.getWord(), "Hello");
    }

    @Test
    void testFindById(){
        Word word = getWord();

        Word savedWord = wordRepository.save(word);

        Word persisted = wordRepository.findById(savedWord.getId()).get();
        assertNotNull(persisted.getId());
        assertEquals(persisted.getWord(), "Hello");
    }

    @Test
    void testDelete(){
        Word word = getWord();

        Word savedWord = wordRepository.save(word);

        Word persisted = wordRepository.findById(savedWord.getId()).get();
        Long id = persisted.getId();
        assertNotNull(id);
        assertEquals(persisted.getWord(), "Hello");

        wordRepository.deleteById(id);
        Optional<Word> byId = wordRepository.findById(id);
        assertTrue(byId.isEmpty());
    }

    private  Word getWord() {
        Word word = new Word();
        word.setWord("Hello");
        word.setSynonyms(Set.of("Hi"));
        word.setTranslation(Set.of("Привет"));
        return word;
    }
}
