package com.sparta.kurtis.model;

import com.sparta.kurtis.view.Printer;

import java.sql.*;
import java.util.Map;

import static com.sparta.kurtis.model.EmployeeDTO.formatDate;

public class EmployeeDAO {

    private static final String URL = "jdbc:mysql://" + System.getenv("JDBC_DB");
    private static Connection connection = null;
    private Map<String, EmployeeDTO> employeeRecords = null;
    private boolean isBatched = false;

    // private static final String SELECT_EMPLOYEES_WHERE = "SELECT first_name FROM employees WHERE employee_id = ?;";
    // private static final String UPDATE_FIRST_NAME_WHERE = "UPDATE persons SET first_name=? WHERE employee_id=? AND first_name !=?;";
    private static final String TRUNCATE_TABLE = "TRUNCATE employees";

    private static final String SELECT_EMPLOYEES = "SELECT * FROM employees;";
    private static final String INSERT_INTO = "INSERT INTO employees " +
            "(employee_id, title, first_name, middle_initial, last_name, gender, email, birth_date, join_date, salary) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?);";
    private static final String DROP_TABLE = "DROP TABLE employees;";
    private static final String CREATE_TABLE = "CREATE TABLE employees (" +
            "employee_id VARCHAR(10) NOT NULL PRIMARY KEY,\n" +
            "title VARCHAR(5),\n" +
            "first_name VARCHAR(30),\n" +
            "middle_initial CHAR(1),\n" +
            "last_name VARCHAR(30),\n" +
            "gender CHAR(1),\n" +
            "email VARCHAR(40),\n" +
            "birth_date DATE,\n" +
            "join_date DATE,\n" +
            "salary INTEGER" +
            ");";

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, System.getenv("JDBC_USERNAME"), System.getenv("JDBC_PASSWORD"));
        } catch (SQLException e) {
            LoggingClass.errorLog("database connection failure");
            e.printStackTrace();
        }
    }

    private void disconnectFromDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            LoggingClass.errorLog("database disconnection failure");
            e.printStackTrace();
        }
    }

    public void selectEmployees() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_EMPLOYEES);

            if (resultSet != null) {
                selectQuery(resultSet, false);
                LoggingClass.traceLog("employees table selected");
            } else {
                Printer.printString("no data exists!");
            }
        } catch (SQLException e) {
            LoggingClass.errorLog("select query failure");
            e.printStackTrace();
        } finally {
            disconnectFromDatabase();
        }
    }

    public void selectQuery(ResultSet resultSet, boolean verbose) throws SQLException {
        Printer.printString(String.format("employee_id title first_name middle_initial last_name gender email birth_date join_date salary%n"));
        while (resultSet.next()) {
            if (verbose) {
                Printer.printString(String.format("%-30s %s %s %s %s %s %s %s %s %d%n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        formatDate(resultSet.getDate(8)),
                        formatDate(resultSet.getDate(9)),
                        resultSet.getInt(10)
                ));
            }
        }
        resultSet.close();
    }

    public void createEmployeesTable() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }

            if (employeesTableExists()) {
                // dropTable();
                truncateTable();
                LoggingClass.traceLog("employees table already exists, data truncated");
            } else {
                createTable();
                LoggingClass.traceLog("employees table created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingClass.errorLog("create employees table failure");
        } finally {
            disconnectFromDatabase();
        }
    }

    private boolean employeesTableExists() {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "employees", null);
            if (resultSet.next()) {
                LoggingClass.traceLog("employees table already exists");
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            LoggingClass.errorLog("database metadata failure");
            e.printStackTrace();
        }
        return false;
    }

    private void dropTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeLargeUpdate(DROP_TABLE);
        statement.close();
    }

    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeLargeUpdate(CREATE_TABLE);
        statement.close();
    }

    public void insertIntoTable(Map<String, EmployeeDTO> employeeRecords, boolean isBatched) {
        this.employeeRecords = employeeRecords;
        this.isBatched = isBatched;

        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }
            if (isBatched) {
                connection.setAutoCommit(false);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO);
            for (EmployeeDTO employee : this.employeeRecords.values()) {
                insertIntoQuery(employee, preparedStatement);
            }
            if (isBatched) {
                preparedStatement.executeBatch();
                connection.commit();
            }
            preparedStatement.close();


            LoggingClass.traceLog("CSV data inserted into employees table");
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingClass.errorLog("inserting into table failure");
        } finally {
            disconnectFromDatabase();
        }
    }

    private void insertIntoQuery(EmployeeDTO employee, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, employee.getEmpId());
        preparedStatement.setString(2, employee.getNamePrefix());
        preparedStatement.setString(3, employee.getFirstName());
        preparedStatement.setString(4, employee.getMiddleInitial());
        preparedStatement.setString(5, employee.getLastName());
        preparedStatement.setString(6, String.valueOf(employee.getGender()));
        preparedStatement.setString(7, employee.getEmail());
        preparedStatement.setDate(8, employee.getDob());
        preparedStatement.setDate(9, employee.getJoinDate());
        preparedStatement.setInt(10, employee.getSalary());

        if (isBatched) {
            preparedStatement.addBatch();
        } else {
            preparedStatement.executeUpdate();
        }

    }

    private void truncateTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(TRUNCATE_TABLE);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

}
