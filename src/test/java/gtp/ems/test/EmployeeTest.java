package gtp.ems.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;

import gtp.ems.model.Employee;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * A test class for verifying the functionality of the {@link gtp.ems.model.Employee} class.
 * This class contains unit tests for various aspects of the Employee class including
 * construction, getters, setters, comparison, equality, and string representation.
 *
 * The tests cover both typical usage scenarios and edge cases to ensure robustness.
 *
 * @see gtp.ems.model.Employee
 */
@DisplayName("Employee Tests")
class EmployeeTest {

    private Employee<String> activeEmployee;
    private Employee<Integer> inactiveEmployee;

    /**
     * Sets up test fixtures before each test method execution.
     * Initialises two Employee instances with different ID types and statuses.
     */
    @BeforeEach
    void setUp() {
        activeEmployee = new Employee<>("EMP001", "Patrick", "Engineering",
                75000.0, 4.5, 5, true);
        inactiveEmployee = new Employee<>(1001, "Elias", "HR",
                65000.0, 3.8, 3, false);
    }

    /**
     * Tests the constructor and getter methods of the Employee class.
     * Verifies that all fields are properly initialized and accessible.
     */
    @Test
    @DisplayName("Test Constructor and Getters")
    void testConstructorAndGetters() {
        // Test cases for activeEmployee
        assertEquals("EMP001", activeEmployee.getEmployeeId());
        assertEquals("Patrick", activeEmployee.getName());
        assertEquals("Engineering", activeEmployee.getDepartment());
        assertEquals(75000.0, activeEmployee.getSalary());
        assertEquals(4.5, activeEmployee.getPerformanceRating());
        assertEquals(5, activeEmployee.getYearsOfExperience());
        assertTrue(activeEmployee.isActive());

        // Test cases for inactiveEmployee
        assertEquals(1001, inactiveEmployee.getEmployeeId());
        assertEquals("Elias", inactiveEmployee.getName());
        assertEquals("HR", inactiveEmployee.getDepartment());
        assertEquals(65000.0, inactiveEmployee.getSalary());
        assertEquals(3.8, inactiveEmployee.getPerformanceRating());
        assertEquals(3, inactiveEmployee.getYearsOfExperience());
        assertFalse(inactiveEmployee.isActive());
    }

    /**
     * Tests the setter methods of the Employee class.
     * Verifies that all mutable fields can be properly updated.
     */
    @Test
    @DisplayName("Test Setters")
    void testSetters() {
        activeEmployee.setName("The Noblet");
        activeEmployee.setDepartment("DevOps");
        activeEmployee.setSalary(80000.0);
        activeEmployee.setPerformanceRating(4.8);
        activeEmployee.setYearsOfExperience(6);
        activeEmployee.setActive(false);

        assertEquals("The Noblet", activeEmployee.getName());
        assertEquals("DevOps", activeEmployee.getDepartment());
        assertEquals(80000.0, activeEmployee.getSalary());
        assertEquals(4.8, activeEmployee.getPerformanceRating());
        assertEquals(6, activeEmployee.getYearsOfExperience());
        assertFalse(activeEmployee.isActive());
    }

    /**
     * Tests the formatted salary output.
     * Verifies that salary values are properly formatted with two decimal places.
     */
    @Test
    @DisplayName("Test getSalaryFormatted")
    void testGetSalaryFormatted() {
        assertEquals("75000.00", activeEmployee.getSalaryFormatted());
        assertEquals("65000.00", inactiveEmployee.getSalaryFormatted());

        activeEmployee.setSalary(123456.789);
        assertEquals("123456.79", activeEmployee.getSalaryFormatted());
    }

    /**
     * Tests the comparison functionality between Employee instances.
     * Verifies that employees are compared based on years of experience in descending order.
     */
    @Test
    @DisplayName("Test compareTo - sorting by years of experience (descending)")
    void testCompareTo() {
        Employee<String> juniorEmployee = new Employee<>("EMP002", "Dennis", "Marketing",
                50000.0, 4.0, 2, true);
        Employee<String> seniorEmployee = new Employee<>("EMP003", "Taylor", "Sales",
                90000.0, 4.7, 8, true);

        assertTrue(activeEmployee.compareTo(juniorEmployee) < 0);
        assertTrue(activeEmployee.compareTo(seniorEmployee) > 0);
        assertEquals(0, activeEmployee.compareTo(
                new Employee<>("EMP004", "Oluchi", "IT", 60000.0, 3.5, 5, true)));
    }

    /**
     * Tests the equals and hashCode methods.
     * Verifies that equality is based solely on employee ID and that hashCode is consistent with equals.
     */
    @Test
    @DisplayName("Test equals and hashCode")
    void testEqualsAndHashCode() {
        Employee<String> sameIdEmployee = new Employee<>("EMP001", "Different Name",
                "Different Dept", 100000.0, 1.0, 1, false);
        Employee<String> differentIdEmployee = new Employee<>("EMP999", "Patrick",
                "Engineering", 75000.0, 4.5, 5, true);

        assertEquals(activeEmployee, sameIdEmployee);
        assertNotEquals(activeEmployee, differentIdEmployee);
        assertEquals(activeEmployee.hashCode(), sameIdEmployee.hashCode());
        assertNotEquals(activeEmployee.hashCode(), differentIdEmployee.hashCode());

        // Test with null and different class
        assertNotEquals(null, activeEmployee);
        assertNotEquals("Not an Employee", activeEmployee);
    }

    /**
     * Parameterized test for the toString method.
     * Verifies that the string representation of employees follows the expected format.
     *
     * @param id The employee ID
     * @param name The employee name
     * @param dept The department name
     * @param salary The salary value
     * @param rating The performance rating
     * @param exp The years of experience
     * @param active The active status ("Yes" or "No")
     */
    @ParameterizedTest
    @CsvSource({
            "EMP001, Patrick, Engineering, 75000.00, 4.5, 5, Yes",
            "1001, Elias, HR, 65000.00, 3.8, 3, No"
    })
    @DisplayName("Test toString formatting")
    void testToString(String id, String name, String dept, double salary,
                      double rating, int exp, String active) {
        Employee<?> employee = active.equalsIgnoreCase("Yes") ? activeEmployee : inactiveEmployee;
        String expected = String.format("%-10s %-20s %-10s $%,-10.2f %-5.1f %-5d %-6s",
                id, name, dept, salary, rating, exp, active);

        assertEquals(expected, employee.toString());
    }

    /**
     * Parameterized test for performance rating boundaries.
     * Verifies that the performance rating can be set to various valid values.
     *
     * @param rating The performance rating value to test
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 2.5, 5.0, 3.14159})
    @DisplayName("Test performance rating boundary values")
    void testPerformanceRatingBoundaries(double rating) {
        activeEmployee.setPerformanceRating(rating);
        assertEquals(rating, activeEmployee.getPerformanceRating());
    }

    /**
     * Tests the Employee class with different ID types.
     * Verifies that both String and Integer ID types work correctly.
     */
    @Test
    @DisplayName("Test employee with different ID types")
    void testDifferentIdTypes() {
        Employee<Integer> intIdEmployee = new Employee<>(123, "Number ID", "IT",
                60000.0, 3.5, 4, true);
        Employee<String> stringIdEmployee = new Employee<>("ABC123", "String ID", "IT",
                60000.0, 3.5, 4, true);

        assertEquals(123, intIdEmployee.getEmployeeId());
        assertEquals("ABC123", stringIdEmployee.getEmployeeId());
    }

    /**
     * Tests edge cases for salary values.
     * Verifies handling of zero salary and rounding of large values.
     */
    @Test
    @DisplayName("Test edge cases for salary")
    void testSalaryEdgeCases() {
        activeEmployee.setSalary(0.0);
        assertEquals(0.0, activeEmployee.getSalary());
        assertEquals("0.00", activeEmployee.getSalaryFormatted());

        activeEmployee.setSalary(999999.999);
        assertEquals(999999.999, activeEmployee.getSalary());
        assertEquals("1000000.00", activeEmployee.getSalaryFormatted());
    }
}