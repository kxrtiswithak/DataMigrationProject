package com.sparta.kurtis.model;

import com.sparta.kurtis.util.LoggingClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmployeeDTO {
    private String empId;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private char gender;
    private String email;
    private java.sql.Date dob;
    private java.sql.Date joinDate;
    private int salary;

    public EmployeeDTO(String[] columns) {
        this.empId = columns[0];
        this.namePrefix = columns[1];
        this.firstName = columns[2];
        this.middleInitial = columns[3];
        this.lastName = columns[4];
        this.gender = columns[5].charAt(0);
        this.email = columns[6];
        this.dob = parseDate(columns[7]);
        this.joinDate = parseDate(columns[8]);
        this.salary = Integer.parseInt(columns[9]);
    }

    public String getEmpId() {
        return empId;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public char getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public java.sql.Date getDob() {
        return dob;
    }

    public java.sql.Date getJoinDate() {
        return joinDate;
    }

    public int getSalary() {
        return salary;
    }

    public static java.sql.Date parseDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        java.sql.Date sqlDate = null;
        try {
            Date tempDate = formatter.parse(date);
            sqlDate = new java.sql.Date(tempDate.getTime());
        } catch (ParseException e) {
            LoggingClass.errorLog("date not parsed");
            e.printStackTrace();
        }
        return sqlDate;
    }

    public static String formatDate(java.sql.Date sqlDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("E dd MMM yyyy", Locale.ENGLISH);
        return formatter.format(sqlDate);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", namePrefix='" + namePrefix + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleInitial='" + middleInitial + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", dob='" +  formatDate(dob) + '\'' +
                ", joinDate='" + formatDate(joinDate) + '\'' +
                ", salary=£" + salary +
                '}';
    }
}
