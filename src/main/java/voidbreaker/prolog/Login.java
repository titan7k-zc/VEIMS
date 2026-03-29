package voidbreaker.prolog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Login {

    // Store usernames and passwords
    private final Map<String, String> credentials = new HashMap<>();
    @FXML private Button closewin;
    @FXML private Button minwin;
    @FXML private Button loginbtn;
    @FXML private Label wrongLogIn;
    @FXML private TextField username;
    @FXML private PasswordField password;

    @FXML private TextField serverIP;
    @FXML private RadioButton serverh;
    @FXML private RadioButton serverc;

    // Moveable window
    @FXML
    private AnchorPane titlebar;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // server section
        ToggleGroup servertg = new ToggleGroup();
        serverh.setToggleGroup(servertg);
        serverc.setToggleGroup(servertg);

        serverIP.setText(getLocalIp());
        serverIP.setDisable(true);
        servertg.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == serverh) {
                serverIP.setText(getLocalIp());
                serverIP.setDisable(true);
            } else if (newToggle == serverc) {
                String configIp = getconfip();
                serverIP.setText(configIp);
                serverIP.setDisable(false);
            }
        });


        // Make window draggable
        titlebar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titlebar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titlebar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        username.setOnAction(event -> password.requestFocus());
        password.setOnAction(this::userLogIn);
    }

    // Login button action
    public void userLogIn(ActionEvent event) {
        String inputUsername = username.getText();
        String inputPassword = password.getText();



        // Update config IP based on user selection
        if (serverc.isSelected()) {
            String newIp = serverIP.getText();
            try {
                HostUpdator.setConfigIP(newIp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (serverh.isSelected()) {
            HostUpdator.main(new String[]{});
        }


        if (inputUsername.isEmpty() && inputPassword.isEmpty()) {
            wrongLogIn.setStyle("-fx-text-fill: red;");
            wrongLogIn.setText("Please enter your data.");
            username.requestFocus();
        } //else if (credentials.containsKey(inputUsername) && credentials.get(inputUsername).equals(inputPassword)) {
        else if (FirebaseAuth.checkLogin(inputUsername, inputPassword)) {
            wrongLogIn.setStyle("-fx-text-fill: green;");
            wrongLogIn.setText("Success!");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
                Parent root = loader.load();
                Home homeController = loader.getController();
                homeController.setLoggedInUser(inputUsername); // Pass the username to Home controller
                homeController.setAdmin(inputUsername.startsWith("Admin"));
                Stage stage = (Stage) loginbtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setWidth(1200);
                stage.setHeight(900);
                stage.centerOnScreen();

            } catch (Exception e) {
                e.printStackTrace();
                wrongLogIn.setStyle("-fx-text-fill: red;");
                wrongLogIn.setText("Failed to load home screen.");
            }
        } else {
            wrongLogIn.setStyle("-fx-text-fill: red;");
            wrongLogIn.setText("Wrong username or password!");
            username.setText("");
            password.setText("");
            username.requestFocus();
        }
    }

    // Close button action
    @FXML
    private void closeApp(ActionEvent event) {
        Stage stage = (Stage) closewin.getScene().getWindow();
        stage.close();
    }

    // Minimize button action
    @FXML
    private void minimizeApp(ActionEvent event) {
        Stage stage = (Stage) minwin.getScene().getWindow();
        stage.setIconified(true);
    }

    public static String getconfip() {
        String filePath = "C:\\ProgramData\\prolog\\configs\\config.json";
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            return ""; // File does not exist or is not a regular file
        }

        StringBuilder jsonContent = new StringBuilder();
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonContent.append((char) ch);
            }
        } catch (IOException e) {
            return ""; // Error reading file, treat as invalid
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            if (jsonArray.length() > 0) {
                JSONObject firstObject = jsonArray.getJSONObject(0);
                if (firstObject.has("ip")) {
                    return firstObject.getString("ip");
                }
            }
        } catch (JSONException e) {
            // Invalid JSON format, do nothing
        }
        return "";
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

}
