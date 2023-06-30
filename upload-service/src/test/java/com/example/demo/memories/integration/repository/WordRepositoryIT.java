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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UploadApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WordRepositoryIT extends ContainersEnvironment {

    @Autowired
    private WordRepository wordRepository;

    @Test
    void testRepo(){
        Word word = new Word();
        word.setWord("Hello");
        Set<String> epithets = new HashSet<>();
        epithets.add("Hi");
        word.setSynonyms(epithets);
        Set<String> translate = new HashSet<>();
        translate.add("привет");
        word.setTranslation(translate);

        wordRepository.save(word);

        Word persisted = wordRepository.findAll().get(0);
        assertNotNull(persisted.getId());
        assertEquals(persisted.getWord(), "Hello");
    }
}
