package com.sparta.kurtis.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final List<Thread> threads = new ArrayList<>();
    private static final List<EmployeeDTO> employeeRecordsDuplicates = new ArrayList<>();
    private static final Map<String, EmployeeDTO> employeeRecords = new HashMap<>();
    private static final Map<String, EmployeeDTO> employeeRecordsBatch = new HashMap<>();

    public static void read(String filePath, int noOfThreads, boolean isBatched) {
        String row;
        boolean isFirstLine = true;
        boolean isThreaded = noOfThreads > 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            long noOfRows = Files.lines(Paths.get(filePath)).count() - 1;

            while ((row = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                addToEmployeeRecords(row);

                if (isThreaded) {
                    if (employeeRecordsBatch.size() > noOfRows / noOfThreads) {
                        sendBatch(isBatched);
                        killThreads();
                    }
                }
            }

            if (isThreaded) {
                sendBatch(isBatched);
                killThreads();
            } else {
                employeeDAO.insertIntoTable(employeeRecords, isBatched);
            }

            bufferedReader.close();
            LoggingClass.traceLog("employees.csv file read");
        } catch (IOException e) {
            e.printStackTrace();
            LoggingClass.errorLog("error reading file");
        }
    }

    private static void sendBatch(boolean isBatched) {
        Object lock = new Object();
        Runnable runnable = () -> {
            synchronized (lock) {
                employeeDAO.insertIntoTable(employeeRecordsBatch, isBatched);
                employeeRecordsBatch.clear();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        threads.add(thread);
    }

    private static void killThreads() {
        while (threads.size() > 0) {
            threads.removeIf(thread -> !thread.isAlive());
        }
    }

    private static void addToEmployeeRecords(String row) {
        String[] employeeRow = row.split(",");
        EmployeeDTO employeeDTO = new EmployeeDTO(employeeRow);

        EmployeeDTO hasBeenPut = employeeRecords.putIfAbsent(employeeRow[0], employeeDTO);
        if (hasBeenPut == null) {
            employeeRecordsBatch.put(employeeRow[0], employeeDTO);
        } else {
            employeeRecordsDuplicates.add(employeeDTO);
            LoggingClass.traceLog("employee id '" + employeeRow[0] + "' duplicate added to duplicates");

        }
    }
}
