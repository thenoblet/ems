package gtp.ems.ui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main application class for the Employee Management System.
 * This JavaFX application serves as the entry point for the EMS GUI.
 */
public class EmployeeManagementApp extends Application {

    /**
     * The main entry point for the JavaFX application.
     * Loads the initial FXML view and configures the primary stage.
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file for the welcome view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ems/view/welcome.fxml"));
            Parent root = loader.load();

            // Create the scene with default dimensions
            Scene scene = new Scene(root, 800, 600);

            // Configure the stage properties
            primaryStage.initStyle(StageStyle.UNDECORATED); // Removes window decorations (good for splash screens)
            primaryStage.setScene(scene);
            primaryStage.setTitle("Employee Management System");
            primaryStage.show();

        } catch (Exception e) {
            // Handle any errors during application startup
            System.err.println("Failed to load FXML file:");
            e.printStackTrace();
            showErrorAlert();
            System.exit(1);
        }
    }

    /**
     * Displays an error alert when the application fails to start.
     * Provides a fallback mechanism if JavaFX fails completely.
     */
    private void showErrorAlert() {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load application");
            alert.setContentText("The application could not start. Please check the logs.");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Could not even show error dialog!");
        }
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}