package com.example.demo.memories.dto;

import lombok.Data;
import java.util.Set;

@Data
public class Word {
    private Long id;
    private String word;
    private boolean isLearned;
    private Set<String> synonyms;
    private Set<String> translation;
}
