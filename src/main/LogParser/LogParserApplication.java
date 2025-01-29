import com.logparser.aggregators.LogDataAggregator;
import com.logparser.aggregators.DataAggregatorFactory;
import com.logparser.models.LogRecordEntry;
import com.logparser.inputoutput.FileIOHandler;
import com.logparser.parser.BaseLogParser;
import com.logparser.parser.LogParserBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LogParserApplication {
    public static void main(String[] args) {
        // Validate CLI arguments
        if (args.length < 2 || !"--file".equals(args[0])) {
            System.out.println("Usage: java LogParserApplication --file <filename.txt>");
            return;
        }

        String inputFileName = args[1];
        File inputFile = new File(inputFileName);

        // Validate the input file
        if (!inputFile.exists() || !inputFile.canRead()) {
            System.out.println("The input file does not exist");
            return;
        }

        FileIOHandler fileIOHandler = FileIOHandler.getInstance();

        try {
            System.out.println("Reading log file: " + inputFileName);
            List<String> logLines = fileIOHandler.readFile(inputFileName);

            // Initialize categorized logs
            Map<String, List<LogRecordEntry>> categorizedLogs = new HashMap<>();

            for (String logLine : logLines) {
                try {
                    // Get the appropriate parser for the log line
                    BaseLogParser parser = LogParserBuilder.getParserType(logLine);

                    if (parser != null) {
                        LogRecordEntry entry = parser.parse(logLine);
                        if (entry != null) {
                            categorizedLogs
                                    .computeIfAbsent(entry.getLogType(), k -> new ArrayList<>())
                                    .add(entry);
                        } else {
                            System.out.println("Skipped log line for missing required fields: " + logLine);
                        }
                    } else {
                        System.out.println("No parser available for log line: " + logLine);
                    }
                } catch (Exception ex) {
                    System.out.println("Error parsing log line: " + logLine);
                    ex.printStackTrace();
                }
            }

            // Aggregate and write outputs
            System.out.println("Aggregating and writing categorized logs...");
            List<String> outputFiles = new ArrayList<>();

            for (Map.Entry<String, List<LogRecordEntry>> category : categorizedLogs.entrySet()) {
                LogDataAggregator aggregator = DataAggregatorFactory.getAggregatortypes(category.getKey());

                if (aggregator != null) {
                    String outputFileName = category.getKey().toLowerCase() + ".json";
                    String jsonOutput = aggregator.aggregate(category.getValue());
                    fileIOHandler.writeFile(outputFileName, jsonOutput);
                    outputFiles.add(outputFileName);
                } else {
                    System.out.println("No aggregator available for : " + category.getKey());
                }
            }

            // Ensure all required output files
            List<String> requiredFiles = Arrays.asList("apm.json", "application.json", "request.json");
            fileIOHandler.ensureOutputFiles(requiredFiles);

            System.out.println("Log processing completed. created: " + outputFiles);
        } catch (IOException e) {
            System.out.println("Error during file I/O operations.");
            e.printStackTrace();
        }
    }
}
