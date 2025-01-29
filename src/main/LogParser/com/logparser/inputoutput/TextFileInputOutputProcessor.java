package com.logparser.inputoutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TextFileInputOutputProcessor extends JsonFileInputOutputProcessor implements FileInputOutputProcessor {
    @Override
    public List<String> readfile(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }

    @Override
    public void writefile(String fileName, String content) throws IOException {
        Files.writeString(Paths.get(fileName), content);
    }
}
