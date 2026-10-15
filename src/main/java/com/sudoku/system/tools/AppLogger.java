package com.sudoku.system.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppLogger {

    private static final Logger log =
            LoggerFactory.getLogger(AppLogger.class);

    private AppLogger() {
        // to Prevent creating instances outside
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(String message, Throwable e) {
        log.error(message, e);
    }

    public static void warn(String message) {
        log.warn(message);
    }
}