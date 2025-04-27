package gtp.ems.test;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import gtp.ems.service.EmployeeInfoDisplay;
import gtp.ems.service.EmployeeManagementSystem;

import java.util.UUID;

public class Main {
    public static void main(String[] args) throws EmployeeNotExistException {
        EmployeeManagementSystem<UUID> ems = new EmployeeManagementSystem<>();
        EmployeeInfoDisplay<UUID> display = new EmployeeInfoDisplay<>();

        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Patrick Appiah", "IT", 75000, 5.0, 5, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "James Darkwa", "HR", 65000, 4.8, 8, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Bob Johnson", "Finance", 90000, 3.9, 10, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Spencer Dutton", "IT", 82000, 4.5, 3, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Aaliyah", "Marketing", 58000, 4.1, 2, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Raymond Reddington", "IT", 110000, 4.9, 12, true)
        );
        ems.addEmployee(new Employee<>(
                UUID.randomUUID(), "Kwa'appiah", "Operations", 140000, 4.0, 7, false)
        );


        // Display all employee
        display.displayAllEmployees(ems.getAllEmployees());

        // Search and filter
        System.out.println("\nIT Department Employees:");
        display.displayEmployeesFormatted(ems.getEmployeesByDepartment("IT"));

        System.out.println("\nHigh Performing Employees (Rating >= 4.5):");
        display.displayEmployeesFormatted(ems.getHighPerformingEmployees(4.5));

        System.out.println("\nEmployees with 'p' in their name:");
        display.displayEmployeesFormatted(ems.searchEmployeesByName("p"));

        // Sorting
        System.out.println("\nEmployees Sorted by Experience:");
        display.displayEmployeesFormatted(ems.sortEmployeesByExperience());

        System.out.println("\nEmployees Sorted by Salary (Highest First):");
        display.displayEmployeesFormatted(ems.sortEmployeesBySalary());

        System.out.println("\nEmployees Sorted by Performance (Best First):");
        display.displayEmployeesFormatted(ems.sortEmployeesByPerformance());

        // Salary management
        System.out.println("\nGiving 10% raise to employees with rating >= 4.5");
        ems.givePerformanceRaise(4.5, 10);

        System.out.println("\nTop 5 Highest Paid Employees:");
        display.displayEmployeesFormatted(ems.getTopPaidEmployees(5));

        System.out.println("\nAverage Salary in IT Department: $" +
                String.format("%,.2f", ems.getAverageSalaryByDepartment("IT")));

        // Generate department report
        display.generateDepartmentReport(ems.getAllEmployees());

        // Update and remove
        UUID firstEmployeeId = ems.getAllEmployees().getFirst().getEmployeeId();
        ems.updateEmployeeDetails(firstEmployeeId, "salary", 80000.0);
        ems.updateEmployeeDetails(firstEmployeeId, "department", "Engineering");

        System.out.println("\nAfter updating first employee:");
        display.displayAllEmployees(ems.getAllEmployees());

        ems.removeEmployee(firstEmployeeId);
        System.out.println("\nAfter removing first employee:");
        display.displayAllEmployees(ems.getAllEmployees());

        UUID employeeId2 = UUID.randomUUID();
        try {
            Employee<UUID> employee = ems.getEmployee(employeeId2);
            System.out.println("\nAfter updating last employee:");
            System.out.println(employee);
        } catch (EmployeeNotExistException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}