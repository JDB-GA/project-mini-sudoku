package com.sudoku.system.exception;

public class SudokuNotFoundException extends RuntimeException {
    public SudokuNotFoundException(String message) {
        super(message);
    }
}