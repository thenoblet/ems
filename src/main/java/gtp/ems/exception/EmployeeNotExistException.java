package gtp.ems.exception;

import java.util.UUID;

/**
 * Exception thrown when attempting to perform operations on an employee
 * that does not exist in the system.
 *
 * This exception includes the invalid employee ID that caused the exception
 * to be thrown, allowing for better error handling and debugging.
 */
public class EmployeeNotExistException extends Exception {

    /**
     * The UUID of the non-existent employee that caused this exception
     */
    private final UUID employeeId;

    /**
     * Constructs a new exception with a default message.
     *
     * @param employeeId the UUID of the non-existent employee
     */
    public EmployeeNotExistException(UUID employeeId) {
        super("Employee with ID " + employeeId + " does not exist");
        this.employeeId = employeeId;
    }

    /**
     * Constructs a new exception with a custom message.
     *
     * @param employeeId the UUID of the non-existent employee
     * @param message the detail message describing the error
     */
    public EmployeeNotExistException(UUID employeeId, String message) {
        super(message);
        this.employeeId = employeeId;
    }

    /**
     * Constructs a new exception with a custom message and cause.
     *
     * @param employeeId the UUID of the non-existent employee
     * @param message the detail message describing the error
     * @param cause the underlying cause of this exception
     */
    public EmployeeNotExistException(UUID employeeId, String message, Throwable cause) {
        super(message, cause);
        this.employeeId = employeeId;
    }

    /**
     * Returns the ID of the non-existent employee that caused this exception.
     *
     * @return the UUID of the non-existent employee
     */
    public UUID getEmployeeId() {
        return employeeId;
    }
}