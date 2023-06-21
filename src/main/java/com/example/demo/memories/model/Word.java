package com.example.demo.memories.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "EnglishWord")
@Data
@RequiredArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String word;
    private boolean isLearned;

    @ElementCollection
    @CollectionTable(name = "Epithets", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "epithet")
    private Set<String> epithets;

    @ElementCollection
    @CollectionTable(name = "Translation", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "translation")
    private Set<String> translation;
}
