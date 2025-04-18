package gtp.ems.ui.util;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

public class DialogUtils {

    public static Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        errorLabel.setPadding(new Insets(0, 0, 0, 5));
        errorLabel.setMinWidth(120); // Ensure space for full messages
        errorLabel.setWrapText(true); // Allow text to wrap if needed
        return errorLabel;
    }

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

        // Enable/disable action button
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

private static boolean isValidDouble(String input) {
    try {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        Double.parseDouble(input.trim());
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

private static boolean isValidInteger(String input) {
    try {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        Integer.parseInt(input.trim());
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
}
