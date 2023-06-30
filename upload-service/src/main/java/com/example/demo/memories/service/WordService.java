package com.example.demo.memories.service;

import org.springframework.web.multipart.MultipartFile;

public interface WordService {
    void parseAndSave(MultipartFile file);
}
