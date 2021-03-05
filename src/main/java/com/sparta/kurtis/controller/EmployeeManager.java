package com.sparta.kurtis.controller;

import com.sparta.kurtis.model.CSVReader;
import com.sparta.kurtis.model.EmployeeDAO;

public class EmployeeManager {
    public void initialise(String filePath, int noOfThreads, boolean isBatched) {
        resetDatabase();
        readAndWrite(filePath, noOfThreads, isBatched);
    }

    private void resetDatabase() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.createEmployeesTable();
    }

    private void readAndWrite(String filePath, int noOfThreads, boolean isBatched) {
        CSVReader.read(filePath, noOfThreads, isBatched);
    }
}
