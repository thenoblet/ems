package gtp.ems.ui.util;

import gtp.ems.exception.InvalidSalaryException;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;


/**
 * Utility class for creating and configuring common dialog components.
 * Provides methods for form layout, validation, and error display.
 */
public class DialogUtils {

    /**
     * Creates a styled error label for form validation messages.
     *
     * @return A configured Label with red text, small font, proper padding,
     *         and text wrapping enabled
     */
    public static Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        errorLabel.setPadding(new Insets(0, 0, 0, 5));
        errorLabel.setMinWidth(120); // Ensure space for full messages
        errorLabel.setWrapText(true); // Allow text to wrap if needed
        return errorLabel;
    }

    /**
     * Configures a standard layout for form GridPanes.
     * Sets consistent spacing, padding, and column constraints.
     *
     * @param grid The GridPane to configure
     */
    public static void configureGridLayout(GridPane grid) {
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Set column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        col1.setPrefWidth(80); // Label column

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Input field column
        col2.setPrefWidth(150);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.NEVER);
        col3.setPrefWidth(120); // Error message column

        grid.getColumnConstraints().addAll(col1, col2, col3);
    }

    /**
     * Sets up comprehensive form validation for employee data fields.
     * Validates in real-time and updates error messages accordingly.
     *
     * @param nameField TextField for employee name
     * @param nameError Label for name validation errors
     * @param deptField TextField for department
     * @param deptError Label for department validation errors
     * @param salaryField TextField for salary
     * @param salaryError Label for salary validation errors
     * @param ratingField TextField for performance rating
     * @param ratingError Label for rating validation errors
     * @param expField TextField for years of experience
     * @param expError Label for experience validation errors
     * @param actionButton The button to enable/disable based on validation
     */
    public static void setupValidation(
            TextField nameField, Label nameError,
            TextField deptField, Label deptError,
            TextField salaryField, Label salaryError,
            TextField ratingField, Label ratingError,
            TextField expField, Label expError,
            Node actionButton
    ) {
        Runnable validateInput = () -> {
            if (!nameField.isFocused() && !deptField.isFocused() &&
                    !salaryField.isFocused() && !ratingField.isFocused() &&
                    !expField.isFocused()) {
                return;
            }

            boolean nameValid = !nameField.getText().trim().isEmpty();
            boolean deptValid = !deptField.getText().trim().isEmpty();
            boolean salaryValid = isValidDouble(salaryField.getText());
            boolean ratingValid = isValidDouble(ratingField.getText());
            boolean expValid = isValidInteger(expField.getText());

            // Update error messages
            nameError.setText(nameValid ? "" : "Required");
            deptError.setText(deptValid ? "" : "Required");
            salaryError.setText(salaryValid ? "" : "Enter valid number");
            ratingError.setText(ratingValid ? "" : "Enter valid number");
            expError.setText(expValid ? "" : "Enter whole number");

            actionButton.setDisable(!(nameValid && deptValid && salaryValid && ratingValid && expValid));
        };

        // Add focus listeners to track first interaction
        Consumer<TextField> trackInteraction = field -> {
            field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (wasFocused && !isNowFocused) validateInput.run();
            });
        };

        trackInteraction.accept(nameField);
        trackInteraction.accept(deptField);
        trackInteraction.accept(salaryField);
        trackInteraction.accept(ratingField);
        trackInteraction.accept(expField);

        // Add listeners to all fields
        nameField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        deptField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        salaryField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        ratingField.textProperty().addListener((obs, old, newVal) -> validateInput.run());
        expField.textProperty().addListener((obs, old, newVal) -> validateInput.run());

        // Initial validation
        validateInput.run();
    }

    /**
     * Validates if a string can be parsed as a double and if the input salary
     * is negative.
     *
     * @param input The string to validate
     * @return true if the input is a valid double and not negative, false otherwise
     */
    private static boolean isValidDouble(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                return false;
            }

            double value = Double.parseDouble(input.trim());

            if (value <= 0) {
                throw new InvalidSalaryException(input);
            }

            return true;
        } catch (InvalidSalaryException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if a string can be parsed as an integer.
     *
     * @param input The string to validate
     * @return true if the input is a valid integer, false otherwise
     */
    private static boolean isValidInteger(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                return false;
            }

            int value = Integer.parseInt(input.trim());
            return value > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
