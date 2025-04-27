package gtp.ems.ui.util;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ColorConsoleFormatter extends Formatter {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    @Override
    public String format(LogRecord record) {
        String color;
        if (record.getLevel() == Level.SEVERE) {
            color = RED;
        } else if (record.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else {
            color = GREEN;
        }

        return String.format("%s%s %s [%s] %s%s%n",
                color,
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date(record.getMillis())),
                record.getLevel().getName(),
                record.getLoggerName(),
                formatMessage(record),
                RESET
        );
    }
}
