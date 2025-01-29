import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BaseLogParserApplicationTest {
    @Test
    void testEndToEndProcessing() throws IOException {
        String inputFileName = "test_input.txt";
        List<String> sampleLogs = List.of(
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72",
                "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1",
                "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1"
        );
        Files.write(Paths.get(inputFileName), sampleLogs);

        LogParserApplication.main(new String[]{"--file", inputFileName});

        assertTrue(Files.exists(Paths.get("apm.json")));
        assertTrue(Files.exists(Paths.get("application.json")));
        assertTrue(Files.exists(Paths.get("request.json")));

        String apmOutput = Files.readString(Paths.get("apm.json"));
        assertTrue(apmOutput.contains("\"cpu_usage_percent\""));

        String applicationOutput = Files.readString(Paths.get("application.json"));
        assertTrue(applicationOutput.contains("\"INFO\": 1"));

        String requestOutput = Files.readString(Paths.get("request.json"));

        assertTrue(requestOutput.contains("\"/api/update\""));

        Files.delete(Paths.get("apm.json"));
        Files.delete(Paths.get("application.json"));
        Files.delete(Paths.get("request.json"));
        Files.delete(Paths.get(inputFileName));
    }
}
