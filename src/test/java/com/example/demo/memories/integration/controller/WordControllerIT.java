package com.example.demo.memories.integration.controller;

import com.example.demo.memories.controller.WordController;
import com.example.demo.memories.exception.FileValidationException;
import com.example.demo.memories.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("integration-test")
@WebMvcTest(WordController.class)
class WordControllerIT {

    @MockBean
    private WordService wordService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadFileSuccess() throws Exception {

        doNothing().when(wordService).parseAndSave(any());

        MockMultipartFile file = new MockMultipartFile("file", "test-file.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        mockMvc.perform(multipart("/api/excel/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                .value("File test-file.xlsx was uploaded successfully"));

        then(wordService).should().parseAndSave(file);
    }

    @Test
    public void testUploadFileFileValidationException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test-file.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        // Throw a FileValidationException when the service method is called
        doThrow(new FileValidationException("Invalid file type")).when(wordService).parseAndSave(any());

        mockMvc.perform(multipart("/api/excel/upload")
                .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                .value("Invalid file type, please upload correct excel file"));

        // Verify that the service method was called
        then(wordService).should().parseAndSave(file);
    }

    @Test
    public void testUploadFileException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test-file.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

        RuntimeException ex = new RuntimeException("Internal server error");
        // Throw an exception when the service method is called
        doThrow(ex).when(wordService).parseAndSave(any());

        mockMvc.perform(multipart("/api/excel/upload")
                .file(file))
                .andExpect(status().isExpectationFailed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("Could not upload the file: test-file.xlsx",
                        ((ResponseStatusException)result.getResolvedException()).getReason()))
                .andExpect(result -> assertEquals(HttpStatus.EXPECTATION_FAILED,
                        ((ResponseStatusException)result.getResolvedException()).getStatusCode()));

        // Verify that the service method was called
        then(wordService).should().parseAndSave(file);
    }
}
