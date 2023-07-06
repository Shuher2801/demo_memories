package com.example.demo.memories.client;

import com.example.demo.memories.dto.Word;
import java.util.List;

public interface WordClient {
    List<Word> getAllWords();
    void updateWord(Word word);
}
