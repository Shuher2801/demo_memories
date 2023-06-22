package com.example.demo.memories.controller;

import com.example.demo.memories.exception.FileValidationException;
import com.example.demo.memories.message.ResponseMessage;
import com.example.demo.memories.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@Slf4j
public class WordController {

    private final WordService wordService;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            wordService.save(file);
            String message = String.format("File %s was uploaded successfully", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (FileValidationException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage(String.format("%s, please upload correct excel file", e.getMessage())));
        }
        catch (Exception e) {
            String message = String.format("Could not upload the file: %s", file.getOriginalFilename());
            log.error(message, e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, message, e);
        }
    }
}
