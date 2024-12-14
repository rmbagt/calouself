package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterView {
    private Scene scene;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField phoneNumberField;
    private TextArea addressField;
    private ToggleGroup roleGroup;
    private Button registerButton;
    private Button backToLoginButton;
    private Label messageLabel;
    private VBox formContainer;

    public RegisterView() {
        initializeComponents();
    }

    private void initializeComponents() {
        formContainer = new VBox(10);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(25));

        // Title
        Text sceneTitle = new Text("Create New Account");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        formContainer.getChildren().add(sceneTitle);

        // Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Username
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("At least 3 characters");
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Min 8 chars with special char (!@#$%^&*)");
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Phone Number
        Label phoneLabel = new Label("Phone Number:");
        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("+62 followed by 10 digits");
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneNumberField, 1, 2);

        // Address
        Label addressLabel = new Label("Address:");
        addressField = new TextArea();
        addressField.setPrefRowCount(3);
        addressField.setPromptText("Enter your full address");
        grid.add(addressLabel, 0, 3);
        grid.add(addressField, 1, 3);

        // Role selection
        Label roleLabel = new Label("Role:");
        roleGroup = new ToggleGroup();
        RadioButton buyerRadio = new RadioButton("Buyer");
        RadioButton sellerRadio = new RadioButton("Seller");
        buyerRadio.setToggleGroup(roleGroup);
        sellerRadio.setToggleGroup(roleGroup);
        buyerRadio.setSelected(true);
        HBox roleBox = new HBox(10, buyerRadio, sellerRadio);
        grid.add(roleLabel, 0, 4);
        grid.add(roleBox, 1, 4);

        formContainer.getChildren().add(grid);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        registerButton = new Button("Register");
        backToLoginButton = new Button("Back to Login");
        buttonBox.getChildren().addAll(registerButton, backToLoginButton);
        formContainer.getChildren().add(buttonBox);

        // Message label
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);
        formContainer.getChildren().add(messageLabel);

        scene = new Scene(formContainer, 450, 600);
    }

    public Scene getScene() {
        return scene;
    }

    // Getters
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public TextField getPhoneNumberField() { return phoneNumberField; }
    public TextArea getAddressField() { return addressField; }
    public ToggleGroup getRoleGroup() { return roleGroup; }
    public Button getRegisterButton() { return registerButton; }
    public Button getBackToLoginButton() { return backToLoginButton; }

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
        phoneNumberField.clear();
        addressField.clear();
        messageLabel.setText("");
    }

    public Button getBackButton() {
        return backToLoginButton;
    }
}

