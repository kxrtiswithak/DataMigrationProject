package com.sparta.kurtis.util;

import com.sparta.kurtis.start.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingClass {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void traceLog(String message) {
        logger.trace(message);
    }

    public static void errorLog(String message) {
        logger.error(message);
    }


}
