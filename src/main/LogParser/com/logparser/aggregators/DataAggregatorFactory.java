package com.logparser.aggregators;

public class DataAggregatorFactory {
    public static LogDataAggregator getAggregatortypes(String logType) {
        switch (logType) {
            case "APM":
                return new MetricsDataDataAggregator();
            case "APPLICATION":
                return new ApplicationDataDataAggregator();
            case "REQUEST":
                return new RequestDataAggregator();
            default:
                throw new IllegalArgumentException("Unknown log type: " + logType);
        }
    }
}
