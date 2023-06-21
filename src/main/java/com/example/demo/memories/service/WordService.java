package com.example.demo.memories.service;

import com.example.demo.memories.helper.ExcelHelper;
import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public void save(MultipartFile file){
            List<Word> words = ExcelHelper.extractWordsFromExcel(file);
            wordRepository.saveAll(words);
    }
}
