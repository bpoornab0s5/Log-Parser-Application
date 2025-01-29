**CMPE 202 Individual Project**

**Log Parser Application**

This project is a command-line utility designed to process log files, categorize them into different types (APM, Application, Request), and produce aggregated JSON outputs. It is designed with extensibility in mind, allowing support for additional log types and file formats.

**Key Features:**

APM Logs: Calculates metrics such as minimum, maximum, median, and average values.
Application Logs: Aggregates logs by severity levels (e.g., INFO, ERROR).
Request Logs: Analyzes response times (percentiles) and categorizes HTTP status codes.



**How to Run:**

1. Build the Project

2. mvn clean install  

3. java -cp target/classes LogParserApplication --file input.txt  


**Generated Outputs:**


1. apm.json

2. application.json

3. request.json