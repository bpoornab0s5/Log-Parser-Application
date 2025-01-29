package com.logparser.inputoutput;

import java.io.IOException;
import java.util.List;

public interface FileInputOutputProcessor {
    List<String> readfile(String fileName) throws IOException;
    void writefile(String fileName, String content) throws IOException;
}
