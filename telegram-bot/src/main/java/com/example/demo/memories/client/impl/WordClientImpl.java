package com.example.demo.memories.client.impl;

import com.example.demo.memories.client.WordClient;
import com.example.demo.memories.dto.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@EnableCaching
public class WordClientImpl implements WordClient {

    @Autowired
    RestTemplate restTemplate;
    @Override
    @Cacheable("wordListCache")
    public List<Word> getAllWords() {
            ResponseEntity<Word[]> response = restTemplate.getForEntity("", Word[].class);
            return Arrays.asList(response.getBody());
    }

    @CachePut(value = "wordListCache", key = "#word.id")
    @Override
    public void updateWord(Word word) {
        var id = word.getId();
        restTemplate.put(String.format("/%d", id), word);
    }
}
