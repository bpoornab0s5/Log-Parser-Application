package com.logparser.parser;

import com.logparser.models.LogRecordEntry;
import com.logparser.utils.LogValidator;

import java.util.*;

public class AppBaseLogFileParser implements BaseLogParser {

    @Override
    public LogRecordEntry parse(String logLine) {
        Map<String, String> log_attributes = parseAttributes(logLine);

        // Validate the parsed attributes
        if (!LogValidator.validate(log_attributes, Arrays.asList("level", "message"), logLine)) {
            return null; // Return null for invalid logs
        }

        // Create and return a new LogRecordEntry
        return new LogRecordEntry("APPLICATION", log_attributes);
    }

    private Map<String, String> parseAttributes(String logLine) {
        Map<String, String> log_attributes = new HashMap<>();

        // Use Scanner for more controlled tokenization
        try (Scanner scanner = new Scanner(logLine)) {
            while (scanner.hasNext()) {
                String part = scanner.next();

                // Split key-value pairs based on the first '='
                int index = part.indexOf('=');
                if (index > 0 && index < part.length() - 1) {
                    String key = part.substring(0, index).trim();
                    String value = part.substring(index + 1).trim();
                    log_attributes.put(key, value);
                }
            }
        }

        return log_attributes;
    }
}
