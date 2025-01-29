package com.logparser.parser;

import com.logparser.models.LogRecordEntry;

public interface BaseLogParser {
    LogRecordEntry parse(String logLine);
}