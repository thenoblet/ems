package gtp.ems.ui.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController {
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // Simulate loading process
        statusLabel.setText("Initializing system...");
        progressIndicator.setProgress(0.3);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            progressIndicator.setProgress(0.6);
            statusLabel.setText("Loading employee data...");

            PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
            pause2.setOnFinished(e2 -> {
                progressIndicator.setProgress(1.0);
                statusLabel.setText("Ready!");
                loadMainApplication();
            });
            pause2.play();
        });
        pause.play();
    }

    private void loadMainApplication() {
        try {
            // Close welcome screen
            Stage stage = (Stage) progressIndicator.getScene().getWindow();
            stage.close();

            // Load main application
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ems/view/employee_management.fxml"));
            loader.setController(null);
            Scene scene = new Scene(loader.load(), 900, 600);
            // Parent root = loader.load();
            Stage mainStage = new Stage();
            mainStage.setTitle("Employee Management System");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}