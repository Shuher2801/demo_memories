package com.example.demo.memories.controller;

import com.example.demo.memories.model.Word;
import com.example.demo.memories.repository.WordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/words")
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordRepository wordRepository;

    @Operation(summary = "get a word by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Found the word",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Word.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not find word with id:",
                    content = @Content) })
    @GetMapping("/{id}")
    public Word getWord(@Parameter(description = "id of word to be searched") @PathVariable Long id){
        return wordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find word with id: %d", id)));
    }

    @Operation(description = "save word")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long saveWord(@RequestBody @NonNull Word word){
        log.info(word.toString());
        Word savedWord = wordRepository.save(word);
        return savedWord.getId();
    }

    @Operation(description = "update word by id")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Word updateWord(@RequestBody Word newWord, @PathVariable Long id){
        return wordRepository.findById(id)
                .map(word -> { word.setWord(newWord.getWord());
                word.setTranslation(newWord.getTranslation());
                word.setSynonyms(newWord.getSynonyms());
                word.setLearned(newWord.isLearned());
                return wordRepository.save(word);
                })
                .orElseGet(() -> wordRepository.save(newWord));
    }

    @Operation(description = "delete word by id")
    @DeleteMapping("/{id}")
    public void deleteWord(@PathVariable Long id){
        wordRepository.deleteById(id);
    }

    @Operation(description = "return page of words")
    @GetMapping("/page")
    public Page<Word> getWordsPage (@ParameterObject Pageable pageable){
       return wordRepository.findAll(pageable);
    }

    @Operation(description = "return list of all words")
    @GetMapping()
    public List<Word> getAll(){
        return wordRepository.findAll();
    }
}
