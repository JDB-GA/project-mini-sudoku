package com.sudoku.system.constant;

public final class ErrorConstants {
    public static final String LINES_READ_ERROR = "An error occurred while reading lines";
    public static final String UNEXPECTED_ERROR = "Unexpected error";
    public static final String GENERIC_RESPONSE_ERROR = "Something went wrong";
    public static final String LINES_WRITE_ERROR = "An error occurred while writing lines";
    public static final String INVALID_SUDOKU_FILE = "Sudoku file must have 9 lines with 9 numbers from 0 to 9";
    public static final String INVALID_SUDOKU_ID = "Invalid sudoku id";
    public static final String SUDOKU_NOT_FOUND = "Sudoku not found";
    public static final String FILE_TOO_LARGE = "File is too large";

    private ErrorConstants() {
    }
}
