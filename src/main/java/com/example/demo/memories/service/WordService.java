package com.example.demo.memories.service;

import com.example.demo.memories.model.Word;
import org.springframework.web.multipart.MultipartFile;

public interface WordService {
    void parseAndSave(MultipartFile file);
}
