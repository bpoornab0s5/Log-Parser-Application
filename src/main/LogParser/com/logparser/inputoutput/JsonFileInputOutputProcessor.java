package com.logparser.inputoutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonFileInputOutputProcessor implements FileInputOutputProcessor {
    private final Gson gson;

    public JsonFileInputOutputProcessor() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<String> readfile(String fileName) throws IOException {
        // Read the file as a single string and split into lines
        String fileContent = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        return Arrays.asList(fileContent.split("\\R")); // Splits content by any newline character
    }

    @Override
    public void writefile(String fileName, String content) throws IOException {
        // Write JSON content with pretty formatting using Gson
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
            Object json = gson.fromJson(content, Object.class); // Parse JSON to ensure validity
            gson.toJson(json, writer); // Write prettified JSON to the file
        }
    }
}
