package gtp.ems.service;

import gtp.ems.model.Employee;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeInfoDisplay<T> {

    /**
     * Displays all employees in a formatted table.
     * @param employees Collection of employees to display
     */
    public void displayAllEmployees(Collection<Employee<T>> employees) {
        System.out.println("\nAll Employees:");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-10s %-5s %-5s %-6s%n",
                "ID", "Name", "Department", "Salary", "Rating", "Exp", "Active");
        System.out.println("-----------------------------------------------------------------------------");

        employees.forEach(System.out::println);
    }

    /**
     * Displays a list of employees in a formatted table.
     * @param employees the list of employees to display
     */
    public void displayEmployeesFormatted(List<Employee<T>> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-10s %-5s %-5s %-6s%n",
                "ID", "Name", "Department", "Salary", "Rating", "Exp", "Active");
        System.out.println("-----------------------------------------------------------------------------");

        employees.forEach(System.out::println);
    }

    /**
     * Generates and displays a report of employee counts by department.
     * @param employees Collection of employees to generate report from
     */
    public void generateDepartmentReport(Collection<Employee<T>> employees) {
        System.out.println("\nDepartment Report:");
        System.out.println("----------------------------------------");

        employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
                .forEach((dept, count) -> System.out.printf("%-15s: %d employees%n", dept, count));
    }
}