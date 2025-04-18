package gtp.ems.model;

import java.util.Objects;

/**
 * Represents an employee in the company with generic employee ID type.
 * This class implements Comparable to allow sorting by years of experience.
 *
 * @param <T> the type of the employee ID (e.g., String, Integer)
 */
public class Employee<T> implements Comparable<Employee<T>> {
    private final T employeeId;
    private String name;
    private String department;
    private double salary;
    private double performanceRating;
    private int yearsOfExperience;
    private boolean isActive;

    /**
     * Constructs a new Employee with the specified details.
     *
     * @param employeeId the unique identifier for the employee
     * @param name the name of the employee
     * @param department the department the employee belongs to
     * @param salary the employee's salary
     * @param performanceRating the employee's performance rating (typically 0.0-5.0)
     * @param yearsOfExperience the number of years the employee has worked
     * @param isActive whether the employee is currently active
     */
    public Employee(T employeeId, String name, String department, double salary,
                    double performanceRating, int yearsOfExperience, boolean isActive) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
        this.yearsOfExperience = yearsOfExperience;
        this.isActive = isActive;
    }

    /**
     * Returns the employee's ID.
     *
     * @return the employee ID
     */
    public T getEmployeeId() {
        return employeeId;
    }

    /**
     * Returns the employee's name.
     *
     * @return the employee name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's name.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the employee's department.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the employee's department.
     *
     * @param department the new department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns the employee's salary.
     *
     * @return the current salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the employee's salary.
     *
     * @param salary the new salary to set
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Returns the employee's performance rating.
     *
     * @return the performance rating (typically 0.0-5.0)
     */
    public double getPerformanceRating() {
        return performanceRating;
    }

    /**
     * Sets the employee's performance rating.
     *
     * @param performanceRating the new performance rating to set
     */
    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    /**
     * Returns the employee's years of experience.
     *
     * @return the number of years of experience
     */
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Sets the employee's years of experience.
     *
     * @param yearsOfExperience the new years of experience to set
     */
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Returns whether the employee is currently active.
     *
     * @return true if the employee is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the employee's active status.
     *
     * @param active the new active status to set
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Compares this employee with another employee based on years of experience.
     * Employees are sorted in descending order of experience.
     *
     * @param other the other employee to compare to
     * @return a negative integer, zero, or a positive integer as this employee
     *         has less than, equal to, or more years of experience than the other
     */
    @Override
    public int compareTo(Employee<T> other) {
        return Integer.compare(other.yearsOfExperience, this.yearsOfExperience);
    }

    /**
     * Indicates whether some other object is "equal to" this one based on employee ID.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Employee<?> employee = (Employee<?>) o;
        return employeeId.equals(employee.employeeId);
    }

    /**
     * Returns a hash code value for the object based on employee ID.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    /**
     * Returns a string representation of the employee in a formatted table-like style.
     *
     * @return a formatted string representation of the employee
     */
    @Override
    public String toString() {
        return String.format("%-10s %-20s %-10s $%,-10.2f %-5.1f %-5d %-6s",
                employeeId, name, department, salary, performanceRating,
                yearsOfExperience, isActive ? "Yes" : "No");
    }
}