package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {
    private Scene scene;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label messageLabel;

    public LoginView() {
        createLoginScene();
    }

    private void createLoginScene() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25));

        Text sceneTitle = new Text("Welcome to CaLouselF");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        vbox.getChildren().add(sceneTitle);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 0);

        usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 1);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        vbox.getChildren().add(grid);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);

        loginButton = new Button("Sign In");
        registerButton = new Button("Register");
        hbBtn.getChildren().addAll(loginButton, registerButton);

        vbox.getChildren().add(hbBtn);

        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        vbox.getChildren().add(messageLabel);

        scene = new Scene(vbox, 300, 275);
    }

    public Scene getScene() {
        return scene;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.RED);
    }

    public void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.GREEN);
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}

