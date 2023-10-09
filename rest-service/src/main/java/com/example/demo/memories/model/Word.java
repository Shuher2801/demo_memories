package com.example.demo.memories.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "EnglishWord")
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(of = "word")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String word;
    private boolean isLearned;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Synonyms", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "synonym")
    @Cache (usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<String> synonyms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Translation", joinColumns = @JoinColumn(name = "word_id"))
    @Column(name = "translation")
    @Cache (usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<String> translation;
}
