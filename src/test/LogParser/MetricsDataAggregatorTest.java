import com.logparser.aggregators.MetricsDataDataAggregator;
import com.logparser.models.LogRecordEntry;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MetricsDataAggregatorTest {
    @Test
    void testAggregateAPMLogs() {
        List<LogRecordEntry> logs = Arrays.asList(
                new LogRecordEntry("APM", Map.of("metric", "cpu_usage_percent", "value", "72")),
                new LogRecordEntry("APM", Map.of("metric", "cpu_usage_percent", "value", "90")),
                new LogRecordEntry("APM", Map.of("metric", "cpu_usage_percent", "value", "78"))
        );

        MetricsDataDataAggregator aggregator = new MetricsDataDataAggregator();
        String result = aggregator.aggregate(logs);
        assertTrue(result.contains("\"minimum\" : 72.0"));
        assertTrue(result.contains("\"median\" : 78.0"));
        assertTrue(result.contains("\"average\" : 80.0"));
        assertTrue(result.contains("\"max\" : 90.0"));
    }

    @Test
    void testAggregateEmptyAPMLogs() {
        List<LogRecordEntry> logs = new ArrayList<>();

        MetricsDataDataAggregator aggregator = new MetricsDataDataAggregator();
        String result = aggregator.aggregate(logs);

        assertEquals("{ }", result);
    }
}
