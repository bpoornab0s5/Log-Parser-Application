package com.logparser.aggregators;

import com.logparser.models.LogRecordEntry;

import java.util.*;

public class RequestDataAggregator extends LogDataAggregator {
    @Override
    public String aggregate(List<LogRecordEntry> logs) {
        Map<String, RouteStats_add> statsMap = new HashMap<>();

        // Process each log entry
        for (LogRecordEntry log : logs) {
            String route = log.getLog_attributes().get("request_url");
            int responseTime = Integer.parseInt(log.getLog_attributes().get("response_time_ms"));
            String statusCode = log.getLog_attributes().get("response_status");

            // Ensure a RouteStats_add object exists for the route
            RouteStats_add routeStatsAdd = statsMap.computeIfAbsent(route, k -> new RouteStats_add());

            // Add response time and categorize status code
            routeStatsAdd.addResponseTime(responseTime);
            routeStatsAdd.incrementStatusCodewithcount(statusCode);
        }

        // Convert route stats to JSON
        TreeMap<String, Object> output = new TreeMap<>();
        for (Map.Entry<String, RouteStats_add> entry : statsMap.entrySet()) {
            output.put(entry.getKey(), entry.getValue().ConverttoJson());
        }

        return Convert_to_Json(output);
    }

    // Inner class to manage stats for a specific route
    private static class RouteStats_add {
        private final List<Integer> Request_responseTimes = new ArrayList<>();
        private final Map<String, Integer> Statuscode_count = new HashMap<>();

        public void addResponseTime(int time) {
            Request_responseTimes.add(time);
        }

        public void incrementStatusCodewithcount(String statusCode) {
            String category = categorizeStatusCode(statusCode);

            Statuscode_count.put(category, Statuscode_count.getOrDefault(category, 0) + 1);
        }

        public Map<String, Object> ConverttoJson() {
            LinkedHashMap<String, Object> stats = new LinkedHashMap<>();
            Map<String, Double> completeResponseTimes = new LinkedHashMap<>();

            if (!Request_responseTimes.isEmpty()) {
                Collections.sort(Request_responseTimes);
                // Calculate min, max, and percentiles

                completeResponseTimes.put("min", Request_responseTimes.get(0).doubleValue());
                completeResponseTimes.put("50_percentile", calculatePercentile(50));
                completeResponseTimes.put("90_percentile", calculatePercentile(90));
                completeResponseTimes.put("95_percentile", calculatePercentile(95));
                completeResponseTimes.put("99_percentile", calculatePercentile(99));
                completeResponseTimes.put("max", Request_responseTimes.get(Request_responseTimes.size() - 1).doubleValue());

                stats.put("response_times", completeResponseTimes);
            }
            Map<String, Integer> completeStatusCodes = new LinkedHashMap<>();
            completeStatusCodes.put("2XX", Statuscode_count.getOrDefault("2XX", 0));
            completeStatusCodes.put("4XX", Statuscode_count.getOrDefault("4XX", 0));
            completeStatusCodes.put("5XX", Statuscode_count.getOrDefault("5XX", 0));
            stats.put("status_codes", completeStatusCodes);
            return stats;
        }

        private double calculatePercentile(int percentile) {
            if (Request_responseTimes.isEmpty()) {
                return 0.0; // Return 0 for empty datasets
            }

            if (Request_responseTimes.size() == 1) {
                return Request_responseTimes.get(0); // Return the only value for single-element datasets
            }

            // Calculate rank as an index using BigDecimal for precision
            int size = Request_responseTimes.size();
            double rank = (percentile / 100.0) * (size - 1);
            int lowerIndex = (int) rank;
            int upperIndex = lowerIndex + 1;

            // Ensure upper index doesn't exceed list bounds
            if (upperIndex >= size) {
                return Request_responseTimes.get(lowerIndex);
            }

            // Calculate weighted average instead of interpolation
            double lowerValue = Request_responseTimes.get(lowerIndex);
            double upperValue = Request_responseTimes.get(upperIndex);
            double weight = rank - lowerIndex;

            return roundToTwoDecimalPlaces(lowerValue * (1 - weight) + upperValue * weight);
        }

        private double roundToTwoDecimalPlaces(double value) {
            return Math.round(value * 100.0) / 100.0;
        }

        private String categorizeStatusCode(String statusCode) {
            if (statusCode.startsWith("2")) return "2XX";
            if (statusCode.startsWith("4")) return "4XX";
            if (statusCode.startsWith("5")) return "5XX";
            return "UNKNOWN";
        }
    }
}
