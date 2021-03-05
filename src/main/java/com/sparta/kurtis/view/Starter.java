package com.sparta.kurtis.view;

import com.sparta.kurtis.controller.EmployeeManager;
import com.sparta.kurtis.model.EmployeeDAO;

public class Starter {
    public static void start() {
        EmployeeManager object = new EmployeeManager();

        object.initialise("resources/employees.csv", 100, false);

    }
}
