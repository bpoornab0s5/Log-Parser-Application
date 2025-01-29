package com.logparser.parser;

import com.logparser.models.LogRecordEntry;
import com.logparser.utils.LogValidator;

import java.util.*;

public class RequestLogParser implements BaseLogParser {

    @Override
    public LogRecordEntry parse(String logLine) {
        Map<String, String> Log_attributes = parseAttributes(logLine);

        // Validate attributes against required keys
        if (!LogValidator.validate(Log_attributes, Arrays.asList("request_method", "response_status", "request_url", "response_time_ms"), logLine)) {
            return null; // Return null for invalid logs
        }

        // Normalize `request_url` to remove surrounding quotes
        Log_attributes.computeIfPresent("request_url", this::removeSurroundingQuotes);

        return new LogRecordEntry("REQUEST", Log_attributes);
    }

    private Map<String, String> parseAttributes(String logLine) {
        Map<String, String> attributes = new HashMap<>();

        // Use Scanner for efficient tokenization
        try (Scanner scanner = new Scanner(logLine)) {
            while (scanner.hasNext()) {
                String part = scanner.next();

                // Split key-value pairs based on the first '='
                int index = part.indexOf('=');
                if (index > 0 && index < part.length() - 1) {
                    String key = part.substring(0, index).trim();
                    String value = part.substring(index + 1).trim();
                    attributes.put(key, value);
                }
            }
        }

        return attributes;
    }

    private String removeSurroundingQuotes(String key, String value) {
        // Remove surrounding quotes from the value if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}

