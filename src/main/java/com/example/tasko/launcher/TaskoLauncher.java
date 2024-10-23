package com.example.tasko.launcher;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TaskoLauncher extends Application {
    private TextArea consoleOutput;
    private Button startButton;
    private Button stopButton;
    private Process serverProcess;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tasko Server Launcher");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: linear-gradient(135deg, #f6f8fb 0%, #e5ebf4 100%);");

        Label titleLabel = new Label("Tasko Server Control Panel");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        startButton = new Button("Start Server");
        startButton.setStyle("-fx-background-color: #3A86FF; -fx-text-fill: white; -fx-font-weight: bold;");
        startButton.setMaxWidth(Double.MAX_VALUE);

        stopButton = new Button("Stop Server");
        stopButton.setStyle("-fx-background-color: #FF595E; -fx-text-fill: white; -fx-font-weight: bold;");
        stopButton.setMaxWidth(Double.MAX_VALUE);
        stopButton.setDisable(true);

        Label outputLabel = new Label("Server Output:");
        outputLabel.setStyle("-fx-font-weight: bold;");

        consoleOutput = new TextArea();
        consoleOutput.setEditable(false);
        consoleOutput.setPrefRowCount(20);
        consoleOutput.setStyle("-fx-font-family: monospace;");
        consoleOutput.setWrapText(true);

        // Status indicator
        Label statusLabel = new Label("Server Status: Stopped");
        statusLabel.setStyle("-fx-font-weight: bold;");

        // Event handlers
        startButton.setOnAction(e -> startServer());
        stopButton.setOnAction(e -> stopServer());

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            statusLabel,
            startButton,
            stopButton,
            new Separator(),
            outputLabel,
            consoleOutput
        );

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startServer() {
        try {
            consoleOutput.appendText("Building project...\n");
            
            // Build the project
            ProcessBuilder buildProcess = new ProcessBuilder("mvn", "clean", "package", "-DskipTests");
            buildProcess.redirectErrorStream(true);
            Process build = buildProcess.start();
            
            // Read build output
            BufferedReader buildReader = new BufferedReader(new InputStreamReader(build.getInputStream()));
            String line;
            while ((line = buildReader.readLine()) != null) {
                String finalLine = line;
                javafx.application.Platform.runLater(() -> consoleOutput.appendText(finalLine + "\n"));
            }
            
            if (build.waitFor() == 0) {
                consoleOutput.appendText("Build successful. Starting server...\n");
                
                // Start the server
                ProcessBuilder serverProcess = new ProcessBuilder(
                    "java", "-jar", "target/tasko-0.0.1-SNAPSHOT.jar"
                );
                serverProcess.redirectErrorStream(true);
                this.serverProcess = serverProcess.start();

                // Read server output
                new Thread(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(this.serverProcess.getInputStream())
                        );
                        String serverLine;
                        while ((serverLine = reader.readLine()) != null) {
                            String finalLine = serverLine;
                            javafx.application.Platform.runLater(() -> {
                                consoleOutput.appendText(finalLine + "\n");
                                if (finalLine.contains("Started TaskoApplication")) {
                                    updateStatus("Running");
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                startButton.setDisable(true);
                stopButton.setDisable(false);
                updateStatus("Starting");
            } else {
                consoleOutput.appendText("Build failed. Check the output for errors.\n");
                updateStatus("Stopped (Build Failed)");
            }
        } catch (Exception e) {
            consoleOutput.appendText("Error: " + e.getMessage() + "\n");
            updateStatus("Error");
        }
    }

    private void stopServer() {
        if (serverProcess != null) {
            serverProcess.destroy();
            serverProcess = null;
            startButton.setDisable(false);
            stopButton.setDisable(true);
            consoleOutput.appendText("Server stopped\n");
            updateStatus("Stopped");
        }
    }

    private void updateStatus(String status) {
        javafx.application.Platform.runLater(() -> {
            Label statusLabel = (Label) ((VBox) startButton.getParent()).getChildren().get(2);
            statusLabel.setText("Server Status: " + status);
            switch (status) {
                case "Running":
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    break;
                case "Stopped":
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    break;
                case "Starting":
                    statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    break;
                case "Error":
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    break;
                default:
                    statusLabel.setStyle("-fx-font-weight: bold;");
            }
        });
    }

    @Override
    public void stop() {
        stopServer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}