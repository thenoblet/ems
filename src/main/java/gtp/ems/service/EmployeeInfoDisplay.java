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
        printTableHeader();
        employees.forEach(this::printEmployee);
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

        printTableHeader();
        employees.forEach(this::printEmployee);
    }

    /**
     * Prints the table header with consistent column widths
     */
    private void printTableHeader() {
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("%-36s %-20s %-12s %-12s %-6s %-6s %-6s%n",
                "ID", "Name", "Department", "Salary", "Rating", "Exp", "Active");
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * Prints a single employee with consistent formatting
     */
    private void printEmployee(Employee<T> employee) {
        System.out.printf("%-36s %-20s %-12s %-12s %-6.1f %-6d %-6s%n",
                employee.getEmployeeId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getSalaryFormatted(),
                employee.getPerformanceRating(),
                employee.getYearsOfExperience(),
                employee.isActive() ? "Yes" : "No");
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