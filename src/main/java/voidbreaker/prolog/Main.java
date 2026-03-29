package voidbreaker.prolog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class Main extends Application {

    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws Exception {
        LocalDate currentDate = LocalDate.now();
        LocalDate cutoffDate = LocalDate.of(2026, 10, 22);

        if (currentDate.isBefore(cutoffDate)) {
            stg = primaryStage;
            primaryStage.setResizable(false);
            primaryStage.setAlwaysOnTop(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);

            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            primaryStage.setTitle("VEIMS");
            primaryStage.setScene(new Scene(root, 900, 600));

            // --- Set JavaFX Stage Icon ---
            try {
                Image icon = new Image(getClass().getResourceAsStream("/stagelogo.png"));
                primaryStage.getIcons().add(icon);
                System.out.println("✅ Stage icon set successfully!");
            } catch (Exception e) {
                System.err.println("❌ Failed to load stage icon: " + e.getMessage());
            }

            // --- Attempt to Set Windows Taskbar Icon  ---
            try {
                if (Taskbar.isTaskbarSupported()) {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        URL iconUrl = getClass().getResource("/stagelogo.png");
                        if (iconUrl != null) {
                            java.awt.Image taskbarImage = Toolkit.getDefaultToolkit().getImage(iconUrl);
                            taskbar.setIconImage(taskbarImage);
                            System.out.println("✅ Taskbar icon applied!");
                        } else {
                            System.err.println("⚠️ Taskbar icon resource not found!");
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to set taskbar icon: " + e.getMessage());
            }

            primaryStage.show();
        } else {
            System.out.println("Error: 00010VEXP");
            primaryStage.close();
        }
    }

    // Allows other controllers to change scene
    public static void changeScene(String fxml, double width, double height) {
        try {
            Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
            stg.getScene().setRoot(pane);
            stg.setWidth(width);
            stg.setHeight(height);
            stg.centerOnScreen();
            System.out.println("✅ Scene changed to: " + fxml);
        } catch (IOException e) {
            System.err.println("❌ Failed to change scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
