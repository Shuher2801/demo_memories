package com.example.demo.memories.helper;

import com.example.demo.memories.exception.FileValidationException;
import com.example.demo.memories.model.Word;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ExcelHelperTest {

    @Mock
    MultipartFile multipartFile;
    @Test
    public void testExtractWordsFromExcelSuccessfully() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Words");

        // Create header row
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Id");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Word");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Epithet");
        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("Translation");

        // Create data row
        Row dataRow = sheet.createRow(1);
        Cell dataCell1 = dataRow.createCell(0);
        dataCell1.setCellValue(1);
        Cell dataCell2 = dataRow.createCell(1);
        dataCell2.setCellValue("Test");
        Cell dataCell3 = dataRow.createCell(2);
        dataCell3.setCellValue("epithet1,epithet2");
        Cell dataCell4 = dataRow.createCell(3);
        dataCell4.setCellValue("translation1,translation2");

        // Convert workbook to bytes and create a MockMultipartFile
        byte[] fileBytes = workbookToBytes(workbook);

        MultipartFile file = new MockMultipartFile("test-file.xlsx", fileBytes);
        when(multipartFile.getContentType()).thenReturn(ExcelHelper.TYPE);
        when(multipartFile.getInputStream()).thenReturn(file.getInputStream());
        List<Word> words = ExcelHelper.extractWordsFromExcel(multipartFile);

        assertEquals(1, words.size());

        Word word = words.get(0);
        assertEquals("Test", word.getWord());

        Set<String> expectedEpithets = new HashSet<>();
        expectedEpithets.add("epithet1");
        expectedEpithets.add("epithet2");
        assertEquals(expectedEpithets, word.getEpithets());

        Set<String> expectedTranslations = new HashSet<>();
        expectedTranslations.add("translation1");
        expectedTranslations.add("translation2");
        assertEquals(expectedTranslations, word.getTranslation());
    }

    @Test
    void testExtractWordsFromExcelInvalidFileType() {
        when(multipartFile.getContentType()).thenReturn("application/pdf");

        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                ExcelHelper.extractWordsFromExcel(multipartFile));

        assertEquals("Invalid file type", exception.getMessage());
    }

    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
