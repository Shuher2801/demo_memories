package com.example.demo.memories.unit.controller;

import com.example.demo.memories.controller.FileUploadController;
import com.example.demo.memories.controller.WordController;
import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("unit-test")
@ExtendWith(MockitoExtension.class)
public class WordControllerTest {
    @Mock
    WordRepository wordRepositoryMock;

    WordController wordController;
    @BeforeEach
    public void setup() {
        wordController = new WordController(wordRepositoryMock);
    }

    @Test
    public void testGetWord(){
        Long id = 1L;
        Word word = new Word();
        word.setWord("test");
        word.setTranslation(Set.of("тест"));
        Optional<Word> optionalWord = Optional.of(word);
        when(wordRepositoryMock.findById(id)).thenReturn(optionalWord);

        assertEquals(wordController.getWord(id), word);
        verify(wordRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void testGetWordThrowException(){
        Long id = 1L;
        Word word = new Word();
        word.setWord("test");
        word.setTranslation(Set.of("тест"));

        ResponseStatusException exception  = assertThrows(ResponseStatusException.class, () -> {
            wordController.getWord(id);
        });

        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        verify(wordRepositoryMock, times(1)).findById(id);
    }

    @Test
    void testSaveWord() {
        // Arrange
        Long id = 1L;
        Word word = new Word();
        word.setId(id);
        word.setWord("test");
        word.setTranslation(Set.of("тест"));

        when(wordRepositoryMock.save(word)).thenReturn(word);

        Long result = wordController.saveWord(word);

        assertEquals(1L, result);
        verify(wordRepositoryMock, times(1)).save(word);
    }

    @Test
    void testUpdateWord() {
        // Arrange
        Long id = 1L;
        Word word = new Word();
        word.setId(id);
        word.setWord("test");
        word.setTranslation(Set.of("тест"));

        Word updatedWord = new Word();
        updatedWord.setId(id);
        updatedWord.setWord("New Word");
        when(wordRepositoryMock.findById(id)).thenReturn(Optional.of(word));
        when(wordRepositoryMock.save(word)).thenReturn(updatedWord);

        Word result = wordController.updateWord(updatedWord, id);

        assertEquals(updatedWord.getWord(), result.getWord());
        verify(wordRepositoryMock, times(1)).findById(id);
        verify(wordRepositoryMock, times(1)).save(word);
    }

    @Test
    void testUpdateWordWhenWordDoesNotExist() {
        // Arrange
        Long id = 1L;
        Word newWord = new Word();
        newWord.setId(id);
        newWord.setWord("New Word");
        when(wordRepositoryMock.findById(id)).thenReturn(Optional.empty());
        when(wordRepositoryMock.save(newWord)).thenReturn(newWord);

        Word result = wordController.updateWord(newWord, id);

        assertEquals(newWord.getWord(), result.getWord());
        verify(wordRepositoryMock, times(1)).findById(id);
        verify(wordRepositoryMock, times(1)).save(newWord);
    }

    @Test
    void testDeleteWord() {
        Long id = 1L;

        wordController.deleteWord(id);

        verify(wordRepositoryMock, times(1)).deleteById(id);
    }

    @Test
    void testGetWordsPage() {
        Pageable pageable = Pageable.unpaged();
        Page<Word> wordPage = mock(Page.class);
        when(wordRepositoryMock.findAll(pageable)).thenReturn(wordPage);

        Page<Word> result = wordController.getWordsPage(pageable);

        assertEquals(wordPage, result);
        verify(wordRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    void testGetAll() {
        List<Word> words = List.of(new Word(), new Word());
        when(wordRepositoryMock.findAll()).thenReturn(words);

        List<Word> result = wordController.getAll();

        assertEquals(words, result);
        verify(wordRepositoryMock, times(1)).findAll();
    }
}
