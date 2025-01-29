package com.logparser.aggregators;

import com.logparser.models.LogRecordEntry;

import java.util.*;

public class ApplicationDataDataAggregator extends LogDataAggregator {
    @Override
    public String aggregate(List<LogRecordEntry> logs) {
        // Use streams to count logs by severity levels
        LinkedHashMap<String, Integer> map = logs.stream()
                .map(log -> log.getLog_attributes().get("level"))
                .collect(
                        LinkedHashMap::new,
                        (m, level) -> m.put(level, m.getOrDefault(level, 0) + 1),
                        LinkedHashMap::putAll
                );

        // Convert the map to JSON
        return Convert_to_Json(map);
    }
}
