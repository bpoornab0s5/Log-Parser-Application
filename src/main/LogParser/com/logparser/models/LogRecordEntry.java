package com.logparser.models;

import java.util.Map;

public class LogRecordEntry {
    private final String logType;
    private final Map<String, String> Log_attributes;

    public LogRecordEntry(String logType, Map<String, String> Log_attributes) {
        this.logType = logType;
        this.Log_attributes = Log_attributes;
    }

    public String getLogType() {
        return logType;
    }

    public Map<String, String> getLog_attributes() {
        return Log_attributes;
    }
}
