import com.logparser.aggregators.RequestDataAggregator;
import com.logparser.models.LogRecordEntry;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class RequestAggregatorTest {
    @Test
    void testAggregateRequestLogs() {
        List<LogRecordEntry> logs = Arrays.asList(
                new LogRecordEntry("REQUEST", Map.of(
                        "request_url", "/api/update",
                        "response_time_ms", "200",
                        "response_status", "202"
                )),
                new LogRecordEntry("REQUEST", Map.of(
                        "request_url", "/api/update",
                        "response_time_ms", "550",
                        "response_status", "503"
                )),
                new LogRecordEntry("REQUEST", Map.of(
                        "request_url", "/api/update",
                        "response_time_ms", "150",
                        "response_status", "404"
                ))
        );

        RequestDataAggregator aggregator = new RequestDataAggregator();
        String result = aggregator.aggregate(logs);

        assertTrue(result.contains("\"/api/update\""));
        assertTrue(result.contains("\"min\" : 150"));
        assertTrue(result.contains("\"max\" : 550"));
        assertTrue(result.contains("\"50_percentile\" : 200"));
        assertTrue(result.contains("\"2XX\" : 1"));
        assertTrue(result.contains("\"4XX\" : 1"));
        assertTrue(result.contains("\"5XX\" : 1"));
    }

    @Test
    void testAggregateEmptyRequestLogs() {
        List<LogRecordEntry> logs = new ArrayList<>();

        RequestDataAggregator aggregator = new RequestDataAggregator();
        String result = aggregator.aggregate(logs);

        assertEquals("{ }", result);
    }
}
