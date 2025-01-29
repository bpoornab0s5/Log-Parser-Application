package com.logparser.parser;

public class LogParserBuilder {
    public static BaseLogParser getParserType(String logLine) {
        if (logLine.contains("metric=")) {
            return new MetricsFileParserBase();
        } else if (logLine.contains("level=")) {
            return new AppBaseLogFileParser();
        } else if (logLine.contains("request_method=")) {
            return new RequestLogParser();
        }
        return null; // Ignore unsupported log lines
    }
}
