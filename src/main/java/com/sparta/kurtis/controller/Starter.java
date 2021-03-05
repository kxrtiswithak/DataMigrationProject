package com.sparta.kurtis.controller;

import com.sparta.kurtis.model.EmployeeDAO;

public class Starter {
    public static void start() {
        EmployeeManager object = new EmployeeManager();

        object.initialise("resources/EmployeeRecordsLarge.csv", 100, false);
    }
}
