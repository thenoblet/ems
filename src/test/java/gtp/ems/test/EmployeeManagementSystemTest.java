package gtp.ems.test;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import gtp.ems.service.EmployeeManagementSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeManagementSystemTest {

    private EmployeeManagementSystem<UUID> ems;
    private Employee<UUID> emp1, emp2, emp3;
    private UUID emp1Id, emp2Id, emp3Id;

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

    @Test
    void addEmployee_success() {
        Employee<UUID> newEmp = new Employee<>(UUID.randomUUID(), "New Employee", "IT",
                60000.0, 4.0, 2, true);
        assertTrue(ems.addEmployee(newEmp));
    }

    @Test
    void addEmployee_duplicate() {
        assertFalse(ems.addEmployee(emp1));
    }

    @Test
    void addEmployee_null() {
        assertFalse(ems.addEmployee(null));
    }

    @Test
    void getEmployee_success() throws EmployeeNotExistException {
        assertEquals(emp1, ems.getEmployee(emp1Id));
    }

    @Test
    void getEmployee_notFound() {
        assertThrows(EmployeeNotExistException.class, () -> ems.getEmployee(UUID.randomUUID()));
    }

    @Test
    void removeEmployee_success() throws EmployeeNotExistException {
        ems.removeEmployee(emp1Id);
        assertThrows(EmployeeNotExistException.class, () -> ems.getEmployee(emp1Id));
    }

    @Test
    void removeEmployee_notFound() {
        assertThrows(EmployeeNotExistException.class, () -> ems.removeEmployee(UUID.randomUUID()));
    }

    @Test
    void updateEmployeeDetails_success() {
        assertTrue(ems.updateEmployeeDetails(emp1Id, "name", "Yakubu Updated"));
        assertTrue(ems.updateEmployeeDetails(emp1Id, "salary", 80000.0));
    }

    @Test
    void updateEmployeeDetails_invalidField() {
        assertFalse(ems.updateEmployeeDetails(emp1Id, "invalidField", "value"));
    }

    @Test
    void updateEmployeeDetails_typeMismatch() {
        assertFalse(ems.updateEmployeeDetails(emp1Id, "salary", "not a number"));
    }

    @Test
    void getAllEmployees() {
        List<Employee<UUID>> employees = ems.getAllEmployees();
        assertEquals(3, employees.size());
        assertTrue(employees.contains(emp1));
        assertTrue(employees.contains(emp2));
        assertTrue(employees.contains(emp3));
    }

    @Test
    void getEmployeesByDepartment() {
        List<Employee<UUID>> engineering = ems.getEmployeesByDepartment("Engineering");
        assertEquals(2, engineering.size());
        assertTrue(engineering.contains(emp1));
        assertTrue(engineering.contains(emp3));
    }

    @Test
    void searchEmployeesByName() {
        List<Employee<UUID>> patricks = ems.searchEmployeesByName("Patrick");
        assertEquals(2, patricks.size()); // Patrickson Noblet and Patrick Appiah
        assertTrue(patricks.contains(emp2));
        assertTrue(patricks.contains(emp3));
        assertFalse(patricks.contains(emp1));
    }

    @Test
    void getHighPerformingEmployees() {
        List<Employee<UUID>> highPerformers = ems.getHighPerformingEmployees(4.0);
        assertEquals(2, highPerformers.size());
        assertTrue(highPerformers.contains(emp1));
        assertTrue(highPerformers.contains(emp3));
    }

    @Test
    void getEmployeesInSalaryRange() {
        List<Employee<UUID>> midRange = ems.getEmployeesInSalaryRange(70000, 80000);
        assertEquals(1, midRange.size());
        assertTrue(midRange.contains(emp1));
    }

    @Test
    void sortEmployeesByExperience() {
        List<Employee<UUID>> sorted = ems.sortEmployeesByExperience();
        assertEquals(emp3, sorted.get(0)); // 7 years
        assertEquals(emp1, sorted.get(1)); // 5 years
        assertEquals(emp2, sorted.get(2)); // 3 years
    }

    @Test
    void sortEmployeesBySalary() {
        List<Employee<UUID>> sorted = ems.sortEmployeesBySalary();

        assertEquals(emp3, sorted.get(2)); // 85000
        assertEquals(emp1, sorted.get(1)); // 75000
        assertEquals(emp2, sorted.get(0)); // 65000
    }

    @Test
    void sortEmployeesByPerformance() {
        List<Employee<UUID>> sorted = ems.sortEmployeesByPerformance();

        assertEquals(emp2, sorted.get(0));
        assertEquals(emp1, sorted.get(1));
        assertEquals(emp3, sorted.get(2));
    }

    @Test
    void givePerformanceRaise() {
        ems.givePerformanceRaise(4.0, 10.0);

        assertEquals(82500.0, Double.parseDouble(String.format("%.1f", emp1.getSalary())));
        assertEquals(65000.0, Double.parseDouble(String.format("%.1f", emp2.getSalary())));
        assertEquals(93500.0, Double.parseDouble(String.format("%.1f", emp3.getSalary())));
    }

    @Test
    void getTopPaidEmployees() {
        List<Employee<UUID>> topPaid = ems.getTopPaidEmployees(2);

        assertEquals(2, topPaid.size());
        assertTrue(topPaid.contains(emp1));
        assertFalse(topPaid.contains(emp3));
    }

    @Test
    void getAverageSalaryByDepartment() {
        double avgEngineering = ems.getAverageSalaryByDepartment("Engineering");
        assertEquals(80000.0, avgEngineering);

        double avgHR = ems.getAverageSalaryByDepartment("HR");
        assertEquals(65000.0, avgHR);

        double avgEmpty = ems.getAverageSalaryByDepartment("NonExistent");
        assertEquals(0.0, avgEmpty);
    }

    @Test
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