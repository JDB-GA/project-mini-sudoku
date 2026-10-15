package com.sudoku.system.tools;

import com.sudoku.system.constant.ErrorConstants;
import com.sudoku.system.constant.FileConstants;
import com.sudoku.system.exception.LinesException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvLines {

    private CsvLines() {
    }

    public static List<String[]> getLines(Path linesPath) {
        try {
            return toColumns(Files.readAllLines(linesPath));
        } catch (IOException e) {
            throw new LinesException(ErrorConstants.LINES_READ_ERROR, e);
        }
    }

    public static List<String[]> getLines(MultipartFile file) {
        try {
            return toColumns(new String(file.getBytes(), StandardCharsets.UTF_8).lines().toList());
        } catch (IOException e) {
            throw new LinesException(ErrorConstants.LINES_READ_ERROR, e);
        }
    }

    public static void writeLines(Path linesPath, List<String> lines) {
        try {
            Files.createDirectories(linesPath.getParent());
            Files.write(linesPath, lines);
        } catch (IOException e) {
            throw new LinesException(ErrorConstants.LINES_WRITE_ERROR, e);
        }
    }

    private static List<String[]> toColumns(List<String> lines) {
        return lines.stream()
                .map(line -> line.split(FileConstants.COLUMN_SEPARATOR, -1))
                .toList();
    }
}
