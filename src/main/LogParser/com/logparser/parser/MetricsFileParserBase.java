package com.logparser.parser;

import com.logparser.models.LogRecordEntry;
import com.logparser.utils.LogValidator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetricsFileParserBase implements BaseLogParser {
    @Override
    public LogRecordEntry parse(String logLine) {
        Map<String, String> attributes = parseAttributes(logLine);

        if (!LogValidator.validate(attributes, Arrays.asList("metric", "value"), logLine)) {
            return null;
        }

        return new LogRecordEntry("APM", attributes);
    }
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(\\S+)=(\\S+)");
    private Map<String, String> parseAttributes(String logLine) {
        Map<String, String> log_attributes = new HashMap<>();
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(logLine);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            log_attributes.put(key, value);
        }

        return log_attributes;

    }
}