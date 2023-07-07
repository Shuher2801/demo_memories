package com.example.demo.memories.repository;


import com.example.demo.memories.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllWordsByIsLearned(boolean isLearned);
}
