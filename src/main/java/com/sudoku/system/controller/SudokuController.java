package com.sudoku.system.controller;

import com.sudoku.system.constant.FileConstants;
import com.sudoku.system.dto.SudokuResponse;
import com.sudoku.system.service.SudokuStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping("/sudoku")
public class SudokuController {

    private final SudokuStorageService storageService;

    public SudokuController(SudokuStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<SudokuResponse> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SudokuResponse(storageService.upload(file)));
    }

    @GetMapping("/{id}/question")
    public ResponseEntity<Resource> getQuestion(@PathVariable String id) {
        return download(id, FileConstants.QUESTION_SUFFIX);
    }

    @GetMapping("/{id}/answer")
    public ResponseEntity<Resource> getAnswer(@PathVariable String id) {
        return download(id, FileConstants.ANSWER_SUFFIX);
    }

    private ResponseEntity<Resource> download(String id, String suffix) {
        Path path = storageService.getFilePath(id, suffix);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(path.getFileName().toString())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.parseMediaType(FileConstants.CSV_CONTENT_TYPE))
                .body(new FileSystemResource(path));
    }
}
