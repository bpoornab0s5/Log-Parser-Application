import com.logparser.parser.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseLogParserBuilderTest {
    @Test
    void testGetParserTypeForAPMLog() {
        LogParserBuilder factory = new LogParserBuilder();
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";
        BaseLogParser parser = factory.getParserType(logLine);
        assertTrue(parser instanceof MetricsFileParserBase);
    }

    @Test
    void testGetParserTypeForApplicationLog() {
        LogParserBuilder factory = new LogParserBuilder();
        String logLine = "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";
        BaseLogParser parser = factory.getParserType(logLine);
        assertTrue(parser instanceof AppBaseLogFileParser);
    }

    @Test
    void testGetParserTypeForRequestLog() {
        LogParserBuilder factory = new LogParserBuilder();
        String logLine = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1";
        BaseLogParser parser = factory.getParserType(logLine);
        assertTrue(parser instanceof RequestLogParser);
    }

    @Test
    void testGetParserTypeForInvalidLog() {
        LogParserBuilder factory = new LogParserBuilder();
        String logLine = "invalid_log_format";
        BaseLogParser parser = factory.getParserType(logLine);
        assertNull(parser);
    }
}
