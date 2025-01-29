import com.logparser.aggregators.ApplicationDataDataAggregator;
import com.logparser.models.LogRecordEntry;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationDataAggregatorTest {
    @Test
    void testAggregateApplicationLogs() {
        List<LogRecordEntry> logs = Arrays.asList(
                new LogRecordEntry("APPLICATION", Map.of("level", "ERROR")),
                new LogRecordEntry("APPLICATION", Map.of("level", "INFO")),
                new LogRecordEntry("APPLICATION", Map.of("level", "ERROR")),
                new LogRecordEntry("APPLICATION", Map.of("level", "INFO")),
                new LogRecordEntry("APPLICATION", Map.of("level", "INFO"))
        );

        ApplicationDataDataAggregator aggregator = new ApplicationDataDataAggregator();
        String result = aggregator.aggregate(logs);

        assertTrue(result.contains("\"INFO\" : 3"));
        assertTrue(result.contains("\"ERROR\" : 2"));
    }

    @Test
    void testAggregateEmptyApplicationLogs() {
        List<LogRecordEntry> logs = new ArrayList<>();

        ApplicationDataDataAggregator aggregator = new ApplicationDataDataAggregator();
        String result = aggregator.aggregate(logs);

        assertEquals("{ }", result);
    }
}
