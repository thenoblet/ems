package gtp.ems.ui.controller;

import gtp.ems.ui.util.ColorConsoleFormatter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.*;
import java.util.Arrays;

/**
 * The main application class for the Employee Management System.
 * This JavaFX application serves as the entry point for the EMS GUI.
 */
public class EmployeeManagementApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(EmployeeManagementApp.class.getName());
    private static final int MAX_LOG_FILES = 5;
    private static final int MAX_LOG_SIZE = 5 * 1024 * 1024; // 5 MB

    static {
        configureLogging();
    }

    /**
     * Configures logging format, console coloring, and file logging.
     */
    private static void configureLogging() {
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format\n",
                    "%1$tY-%m-%d %1$tH:%1$tM:%1$tS.%1$tL %4$s [%2$s] %5$s%6$s%n");

            Logger appLogger = Logger.getLogger("gtp.ems");
            appLogger.setUseParentHandlers(false); // Don't inherit root handlers

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new ColorConsoleFormatter());
            appLogger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("ems-app.log", false);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            appLogger.addHandler(fileHandler);

            appLogger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Failed to configure logging: " + e.getMessage());
        }
    }


    @Override
    public void start(Stage primaryStage) {
        LOGGER.log(Level.INFO, "Starting Employee Management System application");
        long startTime = System.currentTimeMillis();

        try {
            LOGGER.fine(() -> "Loading FXML file for welcome view");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ems/view/welcome.fxml"));
            Parent root = loader.load();
            LOGGER.fine("FXML file loaded successfully");

            Scene scene = new Scene(root, 800, 600);
            LOGGER.config(() -> String.format("Created main scene with dimensions %dx%d",
                    (int) scene.getWidth(), (int) scene.getHeight()));

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Employee Management System");

            // Log window events
            primaryStage.setOnShown(e -> LOGGER.fine("Primary stage shown"));
            primaryStage.setOnCloseRequest(e -> LOGGER.info("Primary stage close requested"));

            LOGGER.fine("Preparing to show primary stage");
            primaryStage.show();

            long loadTime = System.currentTimeMillis() - startTime;
            LOGGER.info(() -> String.format("Application started successfully in %d ms", loadTime));

        } catch (Exception e) {
            long failTime = System.currentTimeMillis() - startTime;
            LOGGER.log(Level.SEVERE,
                    String.format("Application failed to start after %d ms", failTime), e);
            showErrorAlert(e);
            System.exit(1);
        }
    }

    private void showErrorAlert(Throwable e) {
        try {
            LOGGER.fine("Attempting to show error alert dialog");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load application");

            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
            alert.setContentText("Error: " + errorMessage + "\nPlease check the logs.");

            alert.showAndWait();
            LOGGER.info("Error alert displayed to user");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Critical failure - could not show error dialog", ex);
        }
    }

    public static void main(String[] args) {
        LOGGER.config(() -> "Launching JavaFX application with args: " + Arrays.toString(args));
        try {
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable);
            });

            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal error during application launch", e);
            System.exit(1);
        }
    }
}
