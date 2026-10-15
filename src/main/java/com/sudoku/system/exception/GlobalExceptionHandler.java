package com.sudoku.system.exception;

import com.sudoku.system.constant.ErrorConstants;
import com.sudoku.system.tools.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LinesException.class)
    public ResponseEntity<String> handleLines(LinesException e) {
        AppLogger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception e) {
        AppLogger.error(ErrorConstants.UNEXPECTED_ERROR, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorConstants.GENERIC_RESPONSE_ERROR);
    }

    @ExceptionHandler(InvalidSudokuException.class)
    public ResponseEntity<String> handleInvalidSudoku(InvalidSudokuException e) {
        AppLogger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SudokuNotFoundException.class)
    public ResponseEntity<String> handleSudokuNotFound(SudokuNotFoundException e) {
        AppLogger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
