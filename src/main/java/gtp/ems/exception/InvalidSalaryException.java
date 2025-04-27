package gtp.ems.exception;

public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String salary) {
        super(String.format("Salary cannot be less than or equal to zero(0). Received: '%s'", salary));
    }
}
