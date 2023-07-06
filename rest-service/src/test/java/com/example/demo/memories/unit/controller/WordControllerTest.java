package com.example.demo.memories.unit.controller;

import com.example.demo.memories.controller.FileUploadController;
import com.example.demo.memories.exception.FileValidationException;
import com.example.demo.memories.message.ResponseMessage;
import com.example.demo.memories.service.WordService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("unit-test")
public class WordControllerTest {

    @Mock
    WordService wordServiceMock;
    @InjectMocks
    FileUploadController wordController;

    @Test
    public void testUploadFileSuccessfully(){

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("TestFile");

        ResponseEntity<ResponseMessage> response =  wordController.uploadFile(file);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("File TestFile was uploaded successfully", response.getBody().getMessage());
        verify(wordServiceMock, times(1)).parseAndSave(file);
    }

    @Test
    public void testUploadFileThrowsFileValidationException(){

        MultipartFile file = mock(MultipartFile.class);
        FileValidationException exception = new FileValidationException("Invalid file type");
        doThrow(exception).when(wordServiceMock).parseAndSave(any());

        ResponseEntity<ResponseMessage> response =  wordController.uploadFile(file);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("Invalid file type, please upload correct excel file", Objects.requireNonNull(response.getBody()).getMessage());
        verify(wordServiceMock, times(1)).parseAndSave(file);
    }

    @Test
    public void testUploadFileThrowsException(){

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("TestFile");

        RuntimeException exception = new RuntimeException("Invalid file type");
        doThrow(exception).when(wordServiceMock).parseAndSave(file);

        ResponseStatusException responseStatusException =
                assertThrows(ResponseStatusException.class, ()-> wordController.uploadFile(file));
        assertEquals(responseStatusException.getStatus(), HttpStatus.EXPECTATION_FAILED);
        assertEquals("Could not upload the file: TestFile", responseStatusException.getReason());
        verify(wordServiceMock, times(1)).parseAndSave(file);
    }
}
