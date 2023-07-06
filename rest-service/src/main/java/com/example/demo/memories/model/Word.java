package com.example.demo.memories.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Synonyms", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "synonym")
    private Set<String> synonyms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Translation", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "translation")
    private Set<String> translation;
}
