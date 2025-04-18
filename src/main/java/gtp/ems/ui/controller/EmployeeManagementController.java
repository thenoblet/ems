package gtp.ems.ui.controller;

import gtp.ems.exception.EmployeeNotExistException;
import gtp.ems.model.Employee;
import gtp.ems.service.EmployeeManagementSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.UUID;

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

    @FXML private TextField searchField;
    @FXML private TextArea reportArea;

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        expColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        // Load sample data
        loadSampleData();
        employeeTable.setItems(employeeData);
    }

    private void loadSampleData() {
        employeeData.clear();
        employeeData.addAll(ems.getAllEmployees());
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            employeeData.clear();
            employeeData.addAll(ems.searchEmployeesByName(searchTerm));
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        loadSampleData();
    }

    @FXML
    private void handleAddEmployee() {
        // Create dialog for adding new employee
        Dialog<Employee<UUID>> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");

        // Set up dialog buttons
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField salaryField = new TextField();
        TextField ratingField = new TextField();
        TextField expField = new TextField();
        CheckBox activeCheck = new CheckBox("Active");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Department:"), 0, 1);
        grid.add(deptField, 1, 1);
        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);
        grid.add(new Label("Experience:"), 0, 4);
        grid.add(expField, 1, 4);
        grid.add(activeCheck, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Convert result to Employee when Add button is clicked
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
        dialog.showAndWait().ifPresent(employee -> {
            ems.addEmployee(employee);
            loadSampleData();
        });
    }

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

        // Set up dialog buttons
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Create form with current values
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selected.getName());
        TextField deptField = new TextField(selected.getDepartment());
        TextField salaryField = new TextField(String.valueOf(selected.getSalary()));
        TextField ratingField = new TextField(String.valueOf(selected.getPerformanceRating()));
        TextField expField = new TextField(String.valueOf(selected.getYearsOfExperience()));
        CheckBox activeCheck = new CheckBox("Active");
        activeCheck.setSelected(selected.isActive());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Department:"), 0, 1);
        grid.add(deptField, 1, 1);
        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);
        grid.add(new Label("Experience:"), 0, 4);
        grid.add(expField, 1, 4);
        grid.add(activeCheck, 1, 5);

        dialog.getDialogPane().setContent(grid);

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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleApplyFilters() {
        // Get selected filter and sort options
        String filterOption = filterComboBox.getValue();
        String sortOption = sortComboBox.getValue();

        // Apply filtering logic
        filterEmployees(filterOption);

        // Apply sorting logic
        sortEmployees(sortOption);
    }

    private void filterEmployees(String filterOption) {
        // Implement filtering logic based on the selected option
        // Example:
        switch(filterOption) {
            case "Active Only":
                // Filter to show only active employees
                break;
            case "Department":
                // Add department specific filtering
                break;
            case "Salary Range":
                // Add salary range filtering
                break;
            default:
                // Show all employees
        }
    }

    private void sortEmployees(String sortOption) {
        // Implement sorting logic
        // Example:
        switch(sortOption) {
            case "Salary (High to Low)":
                employeeTable.getSortOrder().setAll(salaryColumn);
                salaryColumn.setSortType(TableColumn.SortType.DESCENDING);
                break;
            case "Salary (Low to High)":
                employeeTable.getSortOrder().setAll(salaryColumn);
                salaryColumn.setSortType(TableColumn.SortType.ASCENDING);
                break;
            case "Experience (High to Low)":
                employeeTable.getSortOrder().setAll(expColumn);
                expColumn.setSortType(TableColumn.SortType.DESCENDING);
                break;
            case "Performance Rating":
                employeeTable.getSortOrder().setAll(ratingColumn);
                ratingColumn.setSortType(TableColumn.SortType.DESCENDING);
                break;
            default:
                employeeTable.getSortOrder().clear();
        }
        employeeTable.sort();
    }

    public void handleGenerateReport(ActionEvent actionEvent) {
    }
}