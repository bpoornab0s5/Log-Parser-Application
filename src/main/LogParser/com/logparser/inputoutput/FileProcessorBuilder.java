package com.logparser.inputoutput;

public class FileProcessorBuilder {
    public static FileInputOutputProcessor Create(String fileName) {
        if (fileName.endsWith(".txt")) {
            return new TextFileInputOutputProcessor();
        } else if (fileName.endsWith(".json")) {
            return new JsonFileInputOutputProcessor();
        }

        throw new UnsupportedOperationException("File type not supported for: " + fileName);
    }
}
