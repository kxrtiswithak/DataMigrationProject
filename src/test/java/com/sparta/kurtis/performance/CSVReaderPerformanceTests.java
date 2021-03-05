package com.sparta.kurtis.performance;

import com.sparta.kurtis.controller.EmployeeManager;
import com.sparta.kurtis.util.Printer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

public class CSVReaderPerformanceTests extends PerformanceTests {
    private static String sampleFilePath = "resources/employees.csv";
    private static String largeFilePath = "resources/EmployeeRecordsLarge.csv";

    @BeforeEach
    public void setup() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("employee large for range of threads")
    @ParameterizedTest(name = "test no {index}: employee large {0} threads batched is {1}")
    @CsvSource({
            "0, false",
            "0, false",
            "10, false",
            "20, false",
            "30, false",
            "40, false",
            "50, false",
            "60, false",
            "70, false",
            "80, false",
            "0, true",
            "10, true",
            "20, true",
            "30, true",
            "40, true",
            "50, true",
            "60, true",
            "70, true",
            "80, true",
    })
    public void a2Test(int threadNumber, boolean isBatched) {
        start = System.currentTimeMillis();
        // employeeManager.initialise(largeFilePath, threadNumber, isBatched);
        end = System.currentTimeMillis();

        String batchedOutput = isBatched ? "batched" : "not batched";

        Printer.printPerformanceTestString("employee sample " + threadNumber + " threads " + batchedOutput + ": %dms%n", end - start);
    }



    @DisplayName("employee sample for range of threads")
    @ParameterizedTest(name = "test no {index}: employee sample {0} threads batched is {1}")
    @CsvSource({
            "10, true",
            /*
            "0, false",
            "10, false",
            "20, false",
            "30, false",
            "40, false",
            "50, false",
            "60, false",
            "70, false",
            "80, false",
            "0, true",
            "10, true",
            "20, true",
            "30, true",
            "40, true",
            "50, true",
            "60, true",
            "70, true",
            "80, true",
            */
    })
    public void a1Test(int threadNumber, boolean isBatched) {
        EmployeeManager employeeManager = new EmployeeManager();
        start = System.currentTimeMillis();
        employeeManager.initialise(sampleFilePath, threadNumber, isBatched);
        end = System.currentTimeMillis();

        String batchedOutput = isBatched ? "batched" : "not batched";

        Printer.printPerformanceTestString("employee sample " + threadNumber + " threads " + batchedOutput + ": %dms%n", end - start);
    }
}
