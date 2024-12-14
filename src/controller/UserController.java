package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import connect.Connect;
import model.User;
import view.LoginView;
import view.RegisterView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class UserController {
    private Connect connect;
    private LoginView loginView;
    private RegisterView registerView;
    private Stage stage;
    
    public UserController(Stage stage) {
        this.stage = stage;
        this.connect = Connect.getInstance();
        this.loginView = new LoginView();
        this.registerView = new RegisterView();
        
        setupEventHandlers();
        showLoginScene();
    }
    
    private void setupEventHandlers() {
        // Login view handlers
        loginView.getLoginButton().setOnAction(e -> handleLogin());
        loginView.getRegisterButton().setOnAction(e -> showRegisterScene());
        
        // Register view handlers
        registerView.getRegisterButton().setOnAction(e -> handleRegister());
        registerView.getBackToLoginButton().setOnAction(e -> showLoginScene());
    }
    
    private void showLoginScene() {
        loginView.clearFields();
        stage.setScene(loginView.getScene());
        stage.setTitle("CaLouselF - Login");
    }
    
    private void showRegisterScene() {
        registerView.clearFields();
        stage.setScene(registerView.getScene());
        stage.setTitle("CaLouselF - Register");
    }
    
    private void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = loginView.getPasswordField().getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showErrorMessage("Please fill in all fields");
            return;
        }
        
        // Check for admin login
        if (username.equals("admin") && password.equals("admin")) {
            // TODO: Navigate to admin page
            loginView.showSuccessMessage("Admin login successful");
            return;
        }
        
        try {
            String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("User_id"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Phone_Number"),
                    rs.getString("Address"),
                    rs.getString("Role")
                );
                loginView.showSuccessMessage("Login successful");
                // TODO: Navigate to appropriate page based on user role
            } else {
                loginView.showErrorMessage("Invalid username or password");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            loginView.showErrorMessage("Database error occurred");
        }
    }
    
    private void handleRegister() {
        String username = registerView.getUsernameField().getText();
        String password = registerView.getPasswordField().getText();
        String phoneNumber = registerView.getPhoneNumberField().getText();
        String address = registerView.getAddressField().getText();
        String role = ((RadioButton) registerView.getRoleGroup().getSelectedToggle()).getText();
        
        // Validate all inputs according to sequence diagram
        if (!validateRegistrationInput(username, password, phoneNumber, address)) {
            return;
        }
        
        // Check if username already exists
        if (!isUsernameAvailable(username)) {
            registerView.showErrorMessage("Username already exists");
            return;
        }
        
        // Create new user
        try {
            String userId = UUID.randomUUID().toString();
            String query = "INSERT INTO users (User_id, Username, Password, Phone_Number, Address, Role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, phoneNumber);
            ps.setString(5, address);
            ps.setString(6, role);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                registerView.showSuccessMessage("Registration successful! Please login.");
                // Wait 2 seconds before switching to login scene
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> showLoginScene());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                registerView.showErrorMessage("Registration failed. Please try again.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            registerView.showErrorMessage("Database error occurred");
        }
    }
    
    private boolean validateRegistrationInput(String username, String password, String phoneNumber, String address) {
        if (!User.validateUsername(username)) {
            registerView.showErrorMessage("Username must be at least 3 characters long");
            return false;
        }
        
        if (!User.validatePassword(password)) {
            registerView.showErrorMessage("Password must be at least 8 characters long and include a special character (!@#$%^&*)");
            return false;
        }
        
        if (!User.validatePhoneNumber(phoneNumber)) {
            registerView.showErrorMessage("Phone number must start with +62 followed by 10 digits");
            return false;
        }
        
        if (!User.validateAddress(address)) {
            registerView.showErrorMessage("Address cannot be empty");
            return false;
        }
        
        return true;
    }
    
    private boolean isUsernameAvailable(String username) {
        try {
            String query = "SELECT Username FROM users WHERE Username = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return !rs.next(); // Returns true if username is available (not found in database)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

