package com.sparta.kurtis.view;

import com.sparta.kurtis.model.EmployeeDTO;

import java.util.Map;

public class Printer {
    public static void printString(String message) {
        System.out.println(message);
    }

    public static void printEmployeeMapValues(Map<Integer, EmployeeDTO> employeeMap) {
        for (Map.Entry<Integer, EmployeeDTO> entry : employeeMap.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void printPerformanceTestString(String message, long time) {
        System.out.printf(message, time);
    }
}
