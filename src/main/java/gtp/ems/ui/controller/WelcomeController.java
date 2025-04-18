package gtp.ems.ui.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the welcome/splash screen of the Employee Management System.
 * <p>
 * This controller manages the initial loading animation sequence and transitions
 * to the main application window. It provides visual feedback during system
 * initialization through a progress indicator and status messages.
 * </p>
 */
public class WelcomeController {

    /**
     * The progress indicator that visually represents the loading progress.
     * Value ranges from 0.0 (0%) to 1.0 (100%).
     */
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * The label that displays the current loading status message.
     */
    @FXML
    private Label statusLabel;

    /**
     * Initialises the controller after the FXML fields have been injected.
     *
     * Starts a simulated loading sequence with three distinct phases:
     *
     *  - System initialization (30% progress)
     *  - Employee data loading (60% progress)
     *  - Completion (100% progress)
     *
     * Each phase lasts 1 second and updates both the progress indicator
     * and status messages
     */
    @FXML
    public void initialize() {
        // Initial loading phase (30% complete)
        statusLabel.setText("Initialising EMS...");
        progressIndicator.setProgress(0.3);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            progressIndicator.setProgress(0.6);
            statusLabel.setText("Loading employee data...");

            PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
            pause2.setOnFinished(e2 -> {
                progressIndicator.setProgress(1.0);
                statusLabel.setText("Ready!");
                loadMainApplication(); // Transition to main application
            });
            pause2.play();
        });
        pause.play();
    }

    /**
     * Loads and displays the main application window.
     * This method:
     *   Closes the current welcome screen
     *   Loads the main application FXML file
     *   Creates a new stage for the main application
     *   Sets the window title and dimensions
     *   Displays the main application window
     *
     * If an error occurs during loading, the stack trace is printed to standard error.
     */
    private void loadMainApplication() {
        try {
            // Close the current welcome window
            Stage stage = (Stage) progressIndicator.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ems/view/employee_management.fxml")
            );

            loader.setController(null);

            Scene scene = new Scene(loader.load(), 900, 600);

            // Configure and show main application window
            Stage mainStage = new Stage();
            mainStage.setTitle("Employee Management System");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}