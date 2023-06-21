package com.example.demo.memories.helper;

import com.example.demo.memories.exception.FileValidationException;
import com.example.demo.memories.model.Word;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Slf4j
public class ExcelHelper {

    private static final String SHEET = "Words";
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static List<Word> extractWordsFromExcel(MultipartFile file) {
        validateFile(file);
        List<Word> words;
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheet(SHEET);

            Iterator<Row> rowIterator = sheet.iterator();
            words = new ArrayList<>();

            int rowNumber = 0;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.iterator();
                Word word = new Word();
                int cellIdx = 0;

                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

                    switch (cellIdx) {
                        case 1 -> {
                            String englishWord = currentCell.getStringCellValue();
                            word.setWord(englishWord);
                        }
                        case 2 -> {
                            String epithet = currentCell.getStringCellValue();
                            String[] splitArray = epithet.split(",");
                            word.setEpithets(new HashSet<>(Arrays.asList(splitArray)));
                        }
                        case 3 -> {
                            String translate = currentCell.getStringCellValue();
                            String[] splitArray = translate.split(",");
                            word.setTranslation(new HashSet<>(Arrays.asList(splitArray)));
                        }
                        default -> {
                        }
                    }
                    cellIdx++;
                }
                words.add(word);
            }
        } catch (IOException e) {
            String message = String.format("fail to parse Excel file: %s", e.getMessage());
            log.error(message, e);
            throw new RuntimeException(message);
        }
        return words;
    }

    public static void validateFile(MultipartFile file){

        if (!TYPE.equals(file.getContentType())) {
            log.error("invalid file type");
            throw new FileValidationException("invalid file type");
        }
    }
}
