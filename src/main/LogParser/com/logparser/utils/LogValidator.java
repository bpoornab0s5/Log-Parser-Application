package com.logparser.utils;

import java.util.List;
import java.util.Map;

public class LogValidator {
    public static boolean validate(Map<String, String> attributes, List<String> requiredFields, String logLine) {
        // Use Stream API to check if all required fields are present
        return requiredFields.stream().allMatch(attributes::containsKey);
    }
}
