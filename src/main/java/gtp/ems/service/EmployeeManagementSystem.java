package gtp.ems.service;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import java.util.*;

/**
 * A system for managing employee records with various operations including
 * CRUD operations, searching, sorting, and reporting.
 *
 * @param <T> the type of employee ID used in the system
 */
public class EmployeeManagementSystem<T> {
    private final Map<T, Employee<T>> employeeDatabase;

    /**
     * Constructs a new empty EmployeeManagementSystem.
     */
    public EmployeeManagementSystem() {
        this.employeeDatabase = new HashMap<>();
    }

    /**
     * Adds a new employee to the system.
     *
     * @param employee the employee to add
     * @return true if the employee was added successfully, false if the employee
     *         was null or an employee with the same ID already exists
     */
    public boolean addEmployee(Employee<T> employee) {
        if (employee == null || employeeDatabase.containsKey(employee.getEmployeeId())) {
            return false;
        }
        employeeDatabase.put(employee.getEmployeeId(), employee);
        return true;
    }

    public Employee<T> getEmployee(UUID employeeId) throws EmployeeNotExistException {
        Employee<T> employee = employeeDatabase.get(employeeId);

        if (employee == null) {
            throw new EmployeeNotExistException(employeeId);
        }

        return employee;
    }

    /**
     * Removes an employee from the system.
     *
     * @param employeeId the ID of the employee to remove
     * @return true if the employee was found and removed, false otherwise
     */
    public boolean removeEmployee(T employeeId) {
        return employeeDatabase.remove(employeeId) != null;
    }

    /**
     * Updates specific details of an employee.
     *
     * @param employeeId the ID of the employee to update
     * @param field the field to update (name, department, salary, etc.)
     * @param newValue the new value for the field
     * @return true if the update was successful, false if the employee wasn't found
     *         or the field/newValue combination was invalid
     * @throws ClassCastException if the newValue type doesn't match the field type
     */
    public boolean updateEmployeeDetails(T employeeId, String field, Object newValue) {
        Employee<T> employee = employeeDatabase.get(employeeId);
        if (employee == null) {
            return false;
        }

        try {
            switch (field.toLowerCase()) {
                case "name":
                    employee.setName((String) newValue);
                    break;
                case "department":
                    employee.setDepartment((String) newValue);
                    break;
                case "salary":
                    employee.setSalary((double) newValue);
                    break;
                case "performancerating":
                    employee.setPerformanceRating((double) newValue);
                    break;
                case "yearsofexperience":
                    employee.setYearsOfExperience((int) newValue);
                    break;
                case "isactive":
                    employee.setActive((boolean) newValue);
                    break;
                default:
                    return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Retrieves all employees in the system.
     *
     * @return a list containing all employees
     */
    public List<Employee<T>> getAllEmployees() {
        return new ArrayList<>(employeeDatabase.values());
    }

    /**
     * Retrieves employees belonging to a specific department.
     *
     * @param department the department to filter by
     * @return a list of employees in the specified department
     */
    public List<Employee<T>> getEmployeesByDepartment(String department) {
        return employeeDatabase.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .toList();
    }

    /**
     * Searches for employees by name (case-insensitive partial match).
     *
     * @param searchTerm the term to search for in employee names
     * @return a list of employees whose names contain the search term
     */
    public List<Employee<T>> searchEmployeesByName(String searchTerm) {
        return employeeDatabase.values().stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }

    /**
     * Retrieves high-performing employees based on performance rating.
     *
     * @param minRating the minimum performance rating threshold
     * @return a list of employees meeting or exceeding the performance rating
     */
    public List<Employee<T>> getHighPerformingEmployees(double minRating) {
        return employeeDatabase.values().stream()
                .filter(employee -> employee.getPerformanceRating() >= minRating)
                .toList();
    }

    /**
     * Retrieves employees within a specific salary range.
     *
     * @param minSalary the minimum salary (inclusive)
     * @param maxSalary the maximum salary (inclusive)
     * @return a list of employees within the salary range
     */
    public List<Employee<T>> getEmployeesInSalaryRange(double minSalary, double maxSalary) {
        return employeeDatabase.values().stream()
                .filter(employee -> employee.getSalary() >= minSalary && employee.getSalary() <= maxSalary)
                .toList();
    }

    /**
     * Provides an iterator for all employees in the system.
     *
     * @return an iterator for all employees
     */
    public Iterator<Employee<T>> getEmployeeIterator() {
        return employeeDatabase.values().iterator();
    }


    /**
     * Sorts employees by years of experience (descending order).
     *
     * @return a sorted list of employees
     */
    public List<Employee<T>> sortEmployeesByExperience() {
        return employeeDatabase.values().stream()
                .sorted()
                .toList();
    }

    /**
     * Sorts employees by salary (descending order).
     *
     * @return a sorted list of employees
     */
    public List<Employee<T>> sortEmployeesBySalary() {
        return employeeDatabase.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .toList();
    }

    /**
     * Sorts employees by performance rating (descending order).
     *
     * @return a sorted list of employees
     */
    public List<Employee<T>> sortEmployeesByPerformance() {
        return employeeDatabase.values().stream()
                .sorted(new EmployeePerformanceComparator<>())
                .toList();
    }

    /**
     * Comparator for sorting employees by salary (descending order).
     */
    private static class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {
        @Override
        public int compare(Employee<T> firstEmployee, Employee<T> secondEmployee) {
            return Double.compare(firstEmployee.getSalary(), secondEmployee.getSalary());
        }
    }

    /**
     * Comparator for sorting employees by performance rating (descending order).
     */
    private static class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {
        @Override
        public int compare(Employee<T> firstEmployee, Employee<T> secondEmployee) {
            return Double.compare(firstEmployee.getPerformanceRating(), secondEmployee.getPerformanceRating());
        }
    }

    /**
     * Gives a raise to high-performing employees.
     *
     * @param minRating the minimum performance rating to qualify for a raise
     * @param raisePercentage the percentage raise to apply
     */
    public void givePerformanceRaise(double minRating, double raisePercentage) {
        employeeDatabase.values().stream()
                .filter(employee -> employee.getPerformanceRating() >= minRating)
                .forEach(employee -> {
                    double newSalary = employee.getSalary() * (1 + raisePercentage / 100);
                    employee.setSalary(newSalary);
                });
    }

    /**
     * Retrieves the top paid employees in the system.
     *
     * @param count the number of top-paid employees to return
     * @return a list of the highest paid employees
     */
    public List<Employee<T>> getTopPaidEmployees(int count) {
        return employeeDatabase.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .limit(count)
                .toList();
    }

    /**
     * Calculates the average salary for a department.
     *
     * @param department the department to analyse
     * @return the average salary, or 0.0 if the department has no employees
     */
    public double getAverageSalaryByDepartment(String department) {
        return employeeDatabase.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }
}