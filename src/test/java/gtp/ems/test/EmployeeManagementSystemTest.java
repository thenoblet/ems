package gtp.ems.test;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import gtp.ems.service.EmployeeManagementSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EmployeeManagementSystem} implementation.
 * This class contains unit tests to verify the functionality of the Employee Management System.
 */
@DisplayName("Employee Management System Tests")
class EmployeeManagementSystemTest {

    private EmployeeManagementSystem<UUID> ems;
    private Employee<UUID> emp1, emp2, emp3;
    private UUID emp1Id, emp2Id, emp3Id;

    /**
     * Sets up the test environment before each test method execution.
     * Initializes the EmployeeManagementSystem and adds sample employees for testing.
     */
    @BeforeEach
    void setUp() {
        ems = new EmployeeManagementSystem<>();

        emp1Id = UUID.randomUUID();
        emp2Id = UUID.randomUUID();
        emp3Id = UUID.randomUUID();

        emp1 = new Employee<>(emp1Id, "Yakubu", "Engineering",
                75000.0, 4.5, 5, true);
        emp2 = new Employee<>(emp2Id, "Patrickson Noblet", "HR",
                65000.0, 3.8, 3, true);
        emp3 = new Employee<>(emp3Id, "Patrick Appiah", "Engineering",
                85000.0, 4.8, 7, true);

        ems.addEmployee(emp1);
        ems.addEmployee(emp2);
        ems.addEmployee(emp3);
    }

    /**
     * Tests successful addition of a new employee to the system.
     */
    @Test
    @DisplayName("Test success adding new employee")
    void addEmployee_success() {
        Employee<UUID> newEmp = new Employee<>(UUID.randomUUID(), "New Employee", "IT",
                60000.0, 4.0, 2, true);
        assertTrue(ems.addEmployee(newEmp));
    }

    /**
     * Tests adding a duplicate employee to the system.
     */
    @Test
    @DisplayName("Test adding duplicate employee")
    void addEmployee_duplicate() {
        assertFalse(ems.addEmployee(emp1));
    }

    /**
     * Tests adding a null employee to the system.
     */
    @Test
    @DisplayName("Test adding no (null) employee")
    void addEmployee_null() {
        assertFalse(ems.addEmployee(null));
    }

    /**
     * Tests successful retrieval of an employee from the system.
     * @throws EmployeeNotExistException if the employee doesn't exist
     */
    @Test
    @DisplayName("Test success getting employee")
    void getEmployee_success() throws EmployeeNotExistException {
        assertEquals(emp1, ems.getEmployee(emp1Id));
    }

    /**
     * Tests retrieval of a non-existent employee from the system.
     */
    @Test
    @DisplayName("Test getting nonexisting employee")
    void getEmployee_notFound() {
        assertThrows(EmployeeNotExistException.class, () -> ems.getEmployee(UUID.randomUUID()));
    }

    /**
     * Tests successful removal of an employee from the system.
     * @throws EmployeeNotExistException if the employee doesn't exist
     */
    @Test
    @DisplayName("Test success removing employee")
    void removeEmployee_success() throws EmployeeNotExistException {
        ems.removeEmployee(emp1Id);
        assertThrows(EmployeeNotExistException.class, () -> ems.getEmployee(emp1Id));
    }

    /**
     * Tests removal of a non-existent employee from the system.
     */
    @Test
    @DisplayName("Test removing nonexistent employee")
    void removeEmployee_notFound() {
        assertThrows(EmployeeNotExistException.class, () -> ems.removeEmployee(UUID.randomUUID()));
    }

    /**
     * Tests successful update of employee details.
     */
    @Test
    @DisplayName("Test success updating employee details")
    void updateEmployeeDetails_success() {
        assertTrue(ems.updateEmployeeDetails(emp1Id, "name", "Yakubu Updated"));
        assertTrue(ems.updateEmployeeDetails(emp1Id, "salary", 80000.0));
    }

    /**
     * Tests updating employee details with invalid fields.
     */
    @Test
    @DisplayName("Test updating employee details with invalid fields")
    void updateEmployeeDetails_invalidField() {
        assertFalse(ems.updateEmployeeDetails(emp1Id, "invalidField", "value"));
    }

    /**
     * Tests updating employee details with type mismatch.
     */
    @Test
    @DisplayName("Test updating employee type mismatch")
    void updateEmployeeDetails_typeMismatch() {
        assertFalse(ems.updateEmployeeDetails(emp1Id, "salary", "not a number"));
    }

    /**
     * Tests retrieval of all employees from the system.
     */
    @Test
    @DisplayName("Test getting all employees")
    void getAllEmployees() {
        List<Employee<UUID>> employees = ems.getAllEmployees();
        assertEquals(3, employees.size());
        assertTrue(employees.contains(emp1));
        assertTrue(employees.contains(emp2));
        assertTrue(employees.contains(emp3));
    }

    /**
     * Tests retrieval of employees by department.
     */
    @Test
    @DisplayName("Test getting employees by department")
    void getEmployeesByDepartment() {
        List<Employee<UUID>> engineering = ems.getEmployeesByDepartment("Engineering");
        assertEquals(2, engineering.size());
        assertTrue(engineering.contains(emp1));
        assertTrue(engineering.contains(emp3));
    }

    /**
     * Tests searching employees by name.
     */
    @Test
    @DisplayName("Test searching employees by name")
    void searchEmployeesByName() {
        List<Employee<UUID>> patricks = ems.searchEmployeesByName("Patrick");
        assertEquals(2, patricks.size()); // Patrickson Noblet and Patrick Appiah
        assertTrue(patricks.contains(emp2));
        assertTrue(patricks.contains(emp3));
        assertFalse(patricks.contains(emp1));
    }

    /**
     * Tests retrieval of high performing employees.
     */
    @Test
    @DisplayName("Test getting high performing employees")
    void getHighPerformingEmployees() {
        List<Employee<UUID>> highPerformers = ems.getHighPerformingEmployees(4.0);
        assertEquals(2, highPerformers.size());
        assertTrue(highPerformers.contains(emp1));
        assertTrue(highPerformers.contains(emp3));
    }

    /**
     * Tests retrieval of employees within a specific salary range.
     */
    @Test
    @DisplayName("Test getting employees in salary range")
    void getEmployeesInSalaryRange() {
        List<Employee<UUID>> midRange = ems.getEmployeesInSalaryRange(70000, 80000);
        assertEquals(1, midRange.size());
        assertTrue(midRange.contains(emp1));
    }

    /**
     * Tests sorting employees by experience.
     */
    @Test
    @DisplayName("Test sorting employees by experience")
    void sortEmployeesByExperience() {
        List<Employee<UUID>> sorted = ems.sortEmployeesByExperience();
        assertEquals(emp3, sorted.get(0)); // 7 years
        assertEquals(emp1, sorted.get(1)); // 5 years
        assertEquals(emp2, sorted.get(2)); // 3 years
    }

    /**
     * Tests sorting employees by salary.
     */
    @Test
    @DisplayName("Test sorting employees by salary")
    void sortEmployeesBySalary() {
        List<Employee<UUID>> sorted = ems.sortEmployeesBySalary();

        assertEquals(emp3, sorted.get(2)); // 85000
        assertEquals(emp1, sorted.get(1)); // 75000
        assertEquals(emp2, sorted.get(0)); // 65000
    }

    /**
     * Tests sorting employees by performance rating.
     */
    @Test
    @DisplayName("Test sorting employees by performance")
    void sortEmployeesByPerformance() {
        List<Employee<UUID>> sorted = ems.sortEmployeesByPerformance();

        assertEquals(emp2, sorted.get(0));
        assertEquals(emp1, sorted.get(1));
        assertEquals(emp3, sorted.get(2));
    }

    /**
     * Tests giving performance-based raises to employees.
     */
    @Test
    @DisplayName("Test giving performance raises")
    void givePerformanceRaise() {
        ems.givePerformanceRaise(4.0, 10.0);

        assertEquals(82500.0, Double.parseDouble(String.format("%.1f", emp1.getSalary())));
        assertEquals(65000.0, Double.parseDouble(String.format("%.1f", emp2.getSalary())));
        assertEquals(93500.0, Double.parseDouble(String.format("%.1f", emp3.getSalary())));
    }

    /**
     * Tests retrieval of top paid employees.
     */
    @Test
    @DisplayName("Test getting top paid employees")
    void getTopPaidEmployees() {
        List<Employee<UUID>> topPaid = ems.getTopPaidEmployees(2);

        assertEquals(2, topPaid.size());
        assertTrue(topPaid.contains(emp1));
        assertFalse(topPaid.contains(emp3));
    }

    /**
     * Tests calculation of average salary by department.
     */
    @Test
    @DisplayName("Test getting average salary by department")
    void getAverageSalaryByDepartment() {
        double avgEngineering = ems.getAverageSalaryByDepartment("Engineering");
        assertEquals(80000.0, avgEngineering);

        double avgHR = ems.getAverageSalaryByDepartment("HR");
        assertEquals(65000.0, avgHR);

        double avgEmpty = ems.getAverageSalaryByDepartment("NonExistent");
        assertEquals(0.0, avgEmpty);
    }

    /**
     * Tests the employee iterator functionality.
     */
    @Test
    @DisplayName("Test employee iterator")
    void getEmployeeIterator() {
        int count = 0;
        var iterator = ems.getEmployeeIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }
}