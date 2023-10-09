package com.example.demo.memories.service.impl;

import com.example.demo.memories.helper.ExcelHelper;
import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import com.example.demo.memories.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    @Override
    public void   parseAndSave(MultipartFile file){
            List<Word> words = ExcelHelper.extractWordsFromExcel(file);
            try {
                wordRepository.saveAll(words);
            } catch (DataIntegrityViolationException e){
                List<Word> allWord = wordRepository.findAll();
                HashSet<Word> wordSet = new HashSet<>(allWord);
                words.removeIf(wordSet::contains);
                wordRepository.saveAll(words);
            }
    }
}

