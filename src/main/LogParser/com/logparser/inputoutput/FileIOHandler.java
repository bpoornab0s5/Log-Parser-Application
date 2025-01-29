package com.logparser.inputoutput;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileIOHandler {
    // Use a volatile instance to ensure thread-safe lazy initialization
    private static FileIOHandler instance;

    private FileIOHandler() {}

    // Double-checked locking for thread-safe singleton
    public static FileIOHandler getInstance() {
        if (instance == null) {
            synchronized (FileIOHandler.class) {
                if (instance == null) {
                    instance = new FileIOHandler();
                }
            }
        }
        return instance;
    }

    public List<String> readFile(String fileName) throws IOException {
        FileInputOutputProcessor fileInputOutputProcessor = FileProcessorBuilder.Create(fileName);
        return fileInputOutputProcessor.readfile(fileName);
    }

    public void writeFile(String fileName, String content) throws IOException {
        FileInputOutputProcessor fileInputOutputProcessor = FileProcessorBuilder.Create(fileName);
        fileInputOutputProcessor.writefile(fileName, content);
    }

    public void ensureOutputFiles(List<String> fileNames) throws IOException {
        // Use Java's Files API for file creation
        for (String fileName : fileNames) {
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                Files.write(path, "{}".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            }
        }
    }
}







