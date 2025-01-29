package com.logparser.aggregators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logparser.models.LogRecordEntry;

import java.util.List;

public abstract class LogDataAggregator {
    public abstract String aggregate(List<LogRecordEntry> logs);

    // Utility method to convert a Java object to a JSON string
    public String Convert_to_Json(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error in converting from txt to JSON", e);
        }
    }
}
