package gtp.ems.ui.controller;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import gtp.ems.service.EmployeeManagementSystem;
import gtp.ems.ui.util.DialogUtils;


import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * The main controller class for the Employee Management System UI.
 * Handles all user interactions and manages the display of employee data.
 */
public class EmployeeManagementController {
    private final EmployeeManagementSystem<UUID> ems = new EmployeeManagementSystem<>();
    private final ObservableList<Employee<UUID>> employeeData = FXCollections.observableArrayList();

    @FXML private TableView<Employee<UUID>> employeeTable;
    @FXML private TableColumn<Employee<UUID>, UUID> idColumn;
    @FXML private TableColumn<Employee<UUID>, String> nameColumn;
    @FXML private TableColumn<Employee<UUID>, String> deptColumn;
    @FXML private TableColumn<Employee<UUID>, Double> salaryColumn;
    @FXML private TableColumn<Employee<UUID>, Double> ratingColumn;
    @FXML private TableColumn<Employee<UUID>, Integer> expColumn;
    @FXML private TableColumn<Employee<UUID>, Boolean> activeColumn;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField searchField;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     * Sets up table columns, cell factories, and loads sample data.
     */
    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        expColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));

        // Special handling for active status with checkbox
        activeColumn.setCellValueFactory(cellData -> {
            Employee<UUID> employee = cellData.getValue();
            return new SimpleBooleanProperty(employee.isActive());
        });

        activeColumn.setCellValueFactory(cellData -> {
            Employee<UUID> employee = cellData.getValue();
            return new SimpleBooleanProperty(employee.isActive());
        });

        activeColumn.setCellFactory(col -> new TableCell<Employee<UUID>, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Employee<UUID> employee = getTableRow().getItem();
                    if (employee != null) {
                        employee.setActive(checkBox.isSelected());
                        ems.updateEmployeeDetails(employee.getEmployeeId(), "isActive", checkBox.isSelected());
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                }
            }
        });

        // Load sample data
        loadSampleData();
        employeeTable.setItems(employeeData);
    }

    /**
     * Loads all employees from the management system into the observable list.
     */
    private void loadSampleData() {
        employeeData.clear();
        employeeData.addAll(ems.getAllEmployees());
    }


    /**
     * Handles the search action triggered by the search field.
     * Filters employees by name based on the search term.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            employeeData.clear();
            employeeData.addAll(ems.searchEmployeesByName(searchTerm));
        }
    }

    /**
     * Clears the current search and reloads all employees.
     */
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        loadSampleData();
    }

    /**
     * Handles the "Add Employee" action.
     * Shows a dialog for entering new employee details and adds the employee
     * to the system if validation passes.
     */
    @FXML
    private void handleAddEmployee() {
        // Create dialog for adding new employee
        Dialog<Employee<UUID>> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setResizable(true); // Allow manual resizing
        dialog.getDialogPane().setPrefWidth(500);

        // Set up dialog buttons
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create form
        GridPane grid = new GridPane();
        DialogUtils.configureGridLayout(grid);

        grid.setHgap(10);
        grid.setVgap(5);  // Reduced vertical gap for error labels
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Form fields
        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField salaryField = new TextField();
        TextField ratingField = new TextField();
        TextField expField = new TextField();
        CheckBox activeCheck = new CheckBox("Active");

        // Error labels
        Label nameError = DialogUtils.createErrorLabel();
        Label deptError = DialogUtils.createErrorLabel();
        Label salaryError = DialogUtils.createErrorLabel();
        Label ratingError = DialogUtils.createErrorLabel();
        Label expError = DialogUtils.createErrorLabel();

        // Add components to grid with error labels
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(nameError, 2, 0);

        grid.add(new Label("Department:"), 0, 1);
        grid.add(deptField, 1, 1);
        grid.add(deptError, 2, 1);

        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(salaryError, 2, 2);

        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);
        grid.add(ratingError, 2, 3);

        grid.add(new Label("Experience:"), 0, 4);
        grid.add(expField, 1, 4);
        grid.add(expError, 2, 4);

        grid.add(activeCheck, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Get the Add button node to enable/disable it
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        DialogUtils.setupValidation(
                nameField, nameError,
                deptField, deptError,
                salaryField, salaryError,
                ratingField, ratingError,
                expField, expError,
                addButtonNode
        );

        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    return new Employee<>(
                            UUID.randomUUID(),
                            nameField.getText(),
                            deptField.getText(),
                            Double.parseDouble(salaryField.getText()),
                            Double.parseDouble(ratingField.getText()),
                            Integer.parseInt(expField.getText()),
                            activeCheck.isSelected()
                    );
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers for salary, rating and experience");
                    return null;
                }
            }
            return null;
        });

        // Process the result
        Optional<Employee<UUID>> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            ems.addEmployee(employee);
            loadSampleData();
        });
    }

    /**
     * Handles the "Edit Employee" action.
     * Shows a pre-filled dialog for editing the selected employee's details.
     */
    @FXML
    private void handleEditEmployee() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to edit");
            return;
        }

        // Create dialog for editing employee
        Dialog<Employee<UUID>> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setResizable(true); // Allow manual resizing
        dialog.getDialogPane().setPrefWidth(500);

        // Set up dialog buttons
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Create form with current values
        GridPane grid = new GridPane();
        DialogUtils.configureGridLayout(grid);
        grid.setHgap(10);
        grid.setVgap(5);  // Reduced vertical gap for error labels
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField nameField = new TextField(selected.getName());
        TextField deptField = new TextField(selected.getDepartment());
        TextField salaryField = new TextField(String.valueOf(selected.getSalary()));
        TextField ratingField = new TextField(String.valueOf(selected.getPerformanceRating()));
        TextField expField = new TextField(String.valueOf(selected.getYearsOfExperience()));
        CheckBox activeCheck = new CheckBox("Active");
        activeCheck.setSelected(selected.isActive());

        // Create error labels
        Label nameError = DialogUtils.createErrorLabel();
        Label deptError = DialogUtils.createErrorLabel();
        Label salaryError = DialogUtils.createErrorLabel();
        Label ratingError = DialogUtils.createErrorLabel();
        Label expError = DialogUtils.createErrorLabel();

        // Add components to grid with error labels
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(nameError, 2, 0);

        grid.add(new Label("Department:"), 0, 1);
        grid.add(deptField, 1, 1);
        grid.add(deptError, 2, 1);

        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(salaryError, 2, 2);

        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);
        grid.add(ratingError, 2, 3);

        grid.add(new Label("Experience:"), 0, 4);
        grid.add(expField, 1, 4);
        grid.add(expError, 2, 4);

        grid.add(activeCheck, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Get the Save button node to enable/disable it
        Node saveButtonNode = dialog.getDialogPane().lookupButton(saveButton);
        saveButtonNode.setDisable(false); // Start enabled since we have valid initial values

        DialogUtils.setupValidation(
                nameField, nameError,
                deptField, deptError,
                salaryField, salaryError,
                ratingField, ratingError,
                expField, expError,
                saveButtonNode
        );

        // Focus name field initially
        Platform.runLater(nameField::requestFocus);

        // Convert result to Employee when Save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                try {
                    selected.setName(nameField.getText());
                    selected.setDepartment(deptField.getText());
                    selected.setSalary(Double.parseDouble(salaryField.getText()));
                    selected.setPerformanceRating(Double.parseDouble(ratingField.getText()));
                    selected.setYearsOfExperience(Integer.parseInt(expField.getText()));
                    selected.setActive(activeCheck.isSelected());
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers for salary, rating and experience");
                    return null;
                }
            }
            return null;
        });

        // Process the result
        dialog.showAndWait().ifPresent(employee -> {
            ems.updateEmployeeDetails(employee.getEmployeeId(), "name", employee.getName());
            ems.updateEmployeeDetails(employee.getEmployeeId(), "department", employee.getDepartment());
            ems.updateEmployeeDetails(employee.getEmployeeId(), "salary", employee.getSalary());
            ems.updateEmployeeDetails(employee.getEmployeeId(), "performanceRating", employee.getPerformanceRating());
            ems.updateEmployeeDetails(employee.getEmployeeId(), "yearsOfExperience", employee.getYearsOfExperience());
            ems.updateEmployeeDetails(employee.getEmployeeId(), "isActive", employee.isActive());
            employeeTable.refresh();
        });
    }

    /**
     * Handles the "Delete Employee" action.
     * Shows a confirmation dialog before removing the selected employee.
     */
    @FXML
    private void handleDeleteEmployee() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Employee");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ems.removeEmployee(selected.getEmployeeId());
                employeeData.remove(selected);
            }
        });
    }

    /**
     * Displays an error alert with the given title and message.
     *
     * @param title the alert title
     * @param message the alert content message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Applies the currently selected filters and sorting options.
     */
    @FXML
    private void handleApplyFilters() {
        String filterOption = filterComboBox.getValue();
        String sortOption = sortComboBox.getValue();

        List<Employee<UUID>> filteredEmployees = applyFilters(filterOption);
        List<Employee<UUID>> sortedEmployees = applySorting(sortOption, filteredEmployees);

        employeeData.setAll(sortedEmployees);
    }

    /**
     * Applies the selected filter to the employee list.
     *
     * @param filterOption the filter to apply
     * @return the filtered list of employees
     */
    private List<Employee<UUID>> applyFilters(String filterOption) {
        if (filterOption == null) {
            return ems.getAllEmployees();
        }

        return switch (filterOption) {
            case "Active Only" -> ems.getAllEmployees().stream()
                    .filter(Employee::isActive)
                    .collect(Collectors.toList());
            case "Department" -> {
                // Show dialog to select department
                List<String> departments = ems.getAllEmployees().stream()
                        .map(Employee::getDepartment)
                        .distinct()
                        .collect(Collectors.toList());

                ChoiceDialog<String> dialog = new ChoiceDialog<>(departments.getFirst(), departments);
                dialog.setTitle("Filter by Department");
                dialog.setHeaderText("Select Department");
                dialog.setContentText("Choose department:");

                Optional<String> result = dialog.showAndWait();
                yield result.map(dept -> ems.getEmployeesByDepartment(dept))
                        .orElse(ems.getAllEmployees());
            }
            case "Salary Range" -> {
                // Show dialog for salary range
                TextInputDialog dialog = new TextInputDialog("1000-5000");
                dialog.setTitle("Filter by Salary Range");
                dialog.setHeaderText("Enter salary range (min-max)");
                dialog.setContentText("Range:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        String[] range = result.get().split("-");
                        double min = Double.parseDouble(range[0].trim());
                        double max = Double.parseDouble(range[1].trim());
                        yield ems.getEmployeesInSalaryRange(min, max);
                    } catch (Exception e) {
                        showAlert("Invalid Input", "Please enter range in format 'min-max'");
                    }
                }
                yield ems.getAllEmployees();
            }
            default -> ems.getAllEmployees();
        };
    }

    /**
     * Applies the selected sorting to the employee list.
     *
     * @param sortOption the sorting criteria to apply
     * @param employees the list of employees to sort
     * @return the sorted list of employees
     */
    private List<Employee<UUID>> applySorting(String sortOption, List<Employee<UUID>> employees) {
        if (sortOption == null) {
            return employees;
        }

        return switch (sortOption) {
            case "Salary (High to Low)" -> employees.stream()
                    .sorted(Comparator.comparingDouble(Employee<UUID>::getSalary).reversed())
                    .collect(Collectors.toList());
            case "Salary (Low to High)" -> employees.stream()
                    .sorted(Comparator.comparingDouble(Employee<UUID>::getSalary))
                    .collect(Collectors.toList());
            case "Experience (High to Low)" -> employees.stream()
                    .sorted(Comparator.comparingInt(Employee<UUID>::getYearsOfExperience).reversed())
                    .collect(Collectors.toList());
            case "Experience (Low to High)" -> employees.stream()
                    .sorted(Comparator.comparingInt(Employee<UUID>::getYearsOfExperience))
                    .collect(Collectors.toList());
            case "Performance Rating" -> employees.stream()
                    .sorted(Comparator.comparingDouble(Employee<UUID>::getPerformanceRating).reversed())
                    .collect(Collectors.toList());
            default -> employees;
        };
    }
}