package gtp.ems.ui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeeManagementApp extends Application {
    @Override
    public void start(Stage primaryStage) {

        System.out.println("FXML Resource: " + getClass().getResource("/ems/view/welcome.fxml"));
        try {
            // Load welcome screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ems/view/welcome.fxml"));
            Parent root = loader.load();

            // Set up the scene
            Scene scene = new Scene(root, 800, 600);

            // Configure the stage
            primaryStage.initStyle(StageStyle.UNDECORATED); // Better for splash screens
            primaryStage.setScene(scene);
            primaryStage.setTitle("Employee Management System");
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Failed to load FXML file:");
            e.printStackTrace();
            showErrorAlert();
            System.exit(1);
        }
    }

    private void showErrorAlert() {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load application");
            alert.setContentText("The application could not start. Please check the logs.");
            alert.showAndWait();
        } catch (Exception e) {
            // Fallback if JavaFX fails completely
            System.err.println("Could not even show error dialog!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}