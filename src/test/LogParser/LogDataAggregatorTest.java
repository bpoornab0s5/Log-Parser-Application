import com.logparser.aggregators.LogDataAggregator;
import com.logparser.models.LogRecordEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LogDataAggregatorTest {

    @Test
    void testConvertToJson() {
        // Create a dummy implementation of LogDataAggregator for testing
        LogDataAggregator aggregator = new LogDataAggregator() {
            @Override
            public String aggregate(java.util.List<LogRecordEntry> logs) {
                return null; // Not used in this test
            }
        };

        // Input object to convert to JSON
        Map<String, Object> sampleData = Map.of(
                "key1", "value1",
                "key2", 42,
                "nested", Map.of("subKey", "subValue")
        );

        // Expected JSON string
        String expectedJson = "{\n" +
                "  \"key1\" : \"value1\",\n" +
                "  \"key2\" : 42,\n" +
                "  \"nested\" : {\n" +
                "    \"subKey\" : \"subValue\"\n" +
                "  }\n" +
                "}";

        // Call the toJson method
        String actualJson = aggregator.Convert_to_Json(sampleData);

        // Use Jackson's ObjectMapper to compare JSON structures
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            assertEquals(
                    objectMapper.readTree(expectedJson),
                    objectMapper.readTree(actualJson)
            );
        } catch (Exception e) {
            fail("JSON comparison failed: " + e.getMessage());
        }
    }

    @Test
    void testConvertToJsonWithEmptyObject() {
        // Create a dummy implementation of LogDataAggregator for testing
        LogDataAggregator aggregator = new LogDataAggregator() {
            @Override
            public String aggregate(java.util.List<LogRecordEntry> logs) {
                return null; // Not used in this test
            }
        };

        // Input empty object
        Map<String, Object> emptyData = Map.of();

        // Expected JSON for an empty object
        String expectedJson = "{ }";

        // Call the toJson method
        String actualJson = aggregator.Convert_to_Json(emptyData);

        // Assert equality
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testConvertToJsonThrowsExceptionForInvalidInput() {
        // Create a dummy implementation of LogDataAggregator for testing
        LogDataAggregator aggregator = new LogDataAggregator() {
            @Override
            public String aggregate(java.util.List<LogRecordEntry> logs) {
                return null; // Not used in this test
            }
        };

        // Input invalid object (Jackson can handle almost anything, so this test may be hard to trigger)
        Object invalidData = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Invalid object");
            }
        };

        // Assert that an exception is thrown
        assertThrows(RuntimeException.class, () -> aggregator.Convert_to_Json(invalidData));
    }
}
