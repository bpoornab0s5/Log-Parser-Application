package com.logparser.aggregators;

import com.logparser.models.LogRecordEntry;

import java.util.*;

public class MetricsDataDataAggregator extends LogDataAggregator {
    @Override
    public String aggregate(List<LogRecordEntry> logs) {
        // Group metrics and their values
        Map<String, List<Double>> metrics_Hashmap = new HashMap<>();
        logs.forEach(log -> {
            String metric = log.getLog_attributes().get("metric");
            double value = Double.parseDouble(log.getLog_attributes().get("value"));
            metrics_Hashmap.computeIfAbsent(metric, k -> new ArrayList<>()).add(value);
        });

        TreeMap<String, Map<String, Double>> aggregatedResults = new TreeMap<>();

        // Process and calculate statistics for each metric
        metrics_Hashmap.forEach((metric, values) -> {
            // Use streams to sort the values and calculate statistics
            List<Double> sortedValues = values.stream().sorted().toList();
            double min = sortedValues.get(0); // First element in sorted list
            double max = sortedValues.get(sortedValues.size() - 1); // Last element in sorted list
            double avg = calculateAverage(sortedValues); // Average calculation using helper method
            double median = calculateMedian(sortedValues); // Median calculation using helper method

            // Store calculated metrics in a map
            LinkedHashMap<String, Double> doubleLinkedHashMap = new LinkedHashMap<>();
            doubleLinkedHashMap.put("minimum", min);
            doubleLinkedHashMap.put("median", median);
            doubleLinkedHashMap.put("average", avg);
            doubleLinkedHashMap.put("max", max);

            aggregatedResults.put(metric, doubleLinkedHashMap);
        });

        return Convert_to_Json(aggregatedResults);
    }

    private double calculateAverage(List<Double> values) {
        // Calculate average using a for-loop for clarity
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return roundToTwoDecimalPlaces(sum / values.size());
    }

    private double calculateMedian(List<Double> sortedValues) {
        // Calculate median for a sorted list
        int size = sortedValues.size();
        if (size % 2 == 1) {
            return sortedValues.get(size / 2); // Middle element for odd size
        } else {
            return roundToTwoDecimalPlaces((sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2.0); // Average of two middle elements for even size
        }
    }

    private double roundToTwoDecimalPlaces(double value) {
        // Round the value to two decimal places
        return Math.round(value * 100.0) / 100.0;
    }

}


