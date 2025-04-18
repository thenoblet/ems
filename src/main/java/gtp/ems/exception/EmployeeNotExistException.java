package gtp.ems.exception;

import java.util.UUID;

public class EmployeeNotExistException extends Exception {
    private final UUID employeeId;

    public EmployeeNotExistException(UUID employeeId) {
        super("Employee with ID " + employeeId + " does not exist");
        this.employeeId = employeeId;
    }

    public EmployeeNotExistException(UUID employeeId, String message) {
        super(message);
        this.employeeId = employeeId;
    }

    public EmployeeNotExistException(UUID employeeId, String message, Throwable cause) {
        super(message, cause);
        this.employeeId = employeeId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }
}