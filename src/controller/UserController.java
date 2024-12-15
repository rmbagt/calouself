package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import connect.Connect;
import model.User;
import view.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserController {
    private Connect connect;
    private LoginView loginView;
    private RegisterView registerView;
    private DashboardView dashboardView;
    private UploadItemView uploadItemView;
    private WishlistView wishlistView;
    private PurchaseHistoryView purchaseHistoryView;
    private AdminView adminView;
    private Stage stage;
    private User currentUser;
    private ItemController itemController;
    private WishlistController wishlistController;
    private TransactionController transactionController;
    private AdminController adminController;
    
    public UserController(Stage stage) {
        this.stage = stage;
        this.connect = Connect.getInstance();
        this.loginView = new LoginView();
        this.registerView = new RegisterView();
        this.itemController = new ItemController();
        this.wishlistController = new WishlistController();
        this.transactionController = new TransactionController();
        this.adminController = new AdminController(stage);
        
        setupEventHandlers();
        showLoginScene();
    }
    
    private void setupEventHandlers() {
        // Login view handlers        
        loginView.getRegisterButton().setOnAction(e -> showRegisterScene());
        loginView.getLoginButton().setOnAction(e -> handleLogin());
        loginView.getNavigationBar().getLogoutMenuItem().setOnAction(e -> showLoginScene());
        
        // Register view handlers
        registerView.getRegisterButton().setOnAction(e -> handleRegister());
        registerView.getBackToLoginButton().setOnAction(e -> showLoginScene());
        
        // Dashboard view handlers
        
    }
    
    private void setupDashboardEventHandlers() {
        NavigationBar nav = dashboardView.getNavigationBar();
        nav.getLogoutMenuItem().setOnAction(e -> handleLogout());
        nav.getBrowseItemsMenuItem().setOnAction(e -> handleBrowseItems());
        
        if (currentUser.getRole().equals("Seller")) {
            if (nav.getUploadItemMenuItem() != null) {
                nav.getUploadItemMenuItem().setOnAction(e -> handleUploadItem());
            }
        } else if (currentUser.getRole().equals("Buyer")) {
            if (nav.getViewWishlistMenuItem() != null) {
                nav.getViewWishlistMenuItem().setOnAction(e -> handleViewWishlist());
            }
            if (nav.getViewPurchaseHistoryMenuItem() != null) {
                nav.getViewPurchaseHistoryMenuItem().setOnAction(e -> handleViewPurchaseHistory());
            }
            dashboardView.getAddToWishlistButton().setOnAction(e -> handleAddToWishlist());
            dashboardView.getPurchaseButton().setOnAction(e -> handlePurchaseItem());
        }
        
        dashboardView.getRefreshButton().setOnAction(e -> handleRefreshItems());
    }
    
    private void setupUploadItemEventHandlers() {
        NavigationBar nav = uploadItemView.getNavigationBar();
        nav.getLogoutMenuItem().setOnAction(e -> handleLogout());
        nav.getBrowseItemsMenuItem().setOnAction(e -> showDashboardScene(currentUser));
        uploadItemView.getUploadButton().setOnAction(e -> handleItemUpload());
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
    
    private void showDashboardScene(User user) {
        this.currentUser = user;
        this.dashboardView = new DashboardView(user);
        setupDashboardEventHandlers();
        loadItems();
        stage.setScene(dashboardView.getScene());
        stage.setTitle("CaLouselF - Dashboard");
        dashboardView.setWelcomeMessage("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
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
            loginView.showSuccessMessage("Admin login successful");
            showAdminDashboardScene();
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
                showDashboardScene(user);
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
    
    private void handleLogout() {
        currentUser = null;
        showLoginScene();
    }
    
    private void handleBrowseItems() {
        loadItems();
    }
    
    private void handleUploadItem() {
        if (currentUser.getRole().equals("Seller")) {
            uploadItemView = new UploadItemView(currentUser);
            setupUploadItemEventHandlers();
            stage.setScene(uploadItemView.getScene());
            stage.setTitle("CaLouselF - Upload Item");
        } else {
            System.out.println("Only sellers can upload items");
        }
    }
    
    private void handleItemUpload() {
        itemController.handleItemUpload(uploadItemView, currentUser);
    }
    
    
    private void handleAddToWishlist() {
        itemController.handleAddToWishlist(dashboardView, currentUser);
    }
    
    private void handleViewWishlist() {
        if (currentUser.getRole().equals("Buyer")) {
            wishlistView = new WishlistView(currentUser);
            setupWishlistEventHandlers();
            wishlistController.loadWishlistItems(wishlistView, currentUser);
            stage.setScene(wishlistView.getScene());
            stage.setTitle("CaLouselF - Wishlist");
        } else {
            System.out.println("Only buyers can view wishlist");
        }
    }
    
    private void handleViewPurchaseHistory() {
        if (currentUser.getRole().equals("Buyer")) {
            purchaseHistoryView = new PurchaseHistoryView(currentUser);
            setupPurchaseHistoryEventHandlers();
            transactionController.loadPurchaseHistory(purchaseHistoryView, currentUser);
            stage.setScene(purchaseHistoryView.getScene());
            stage.setTitle("CaLouselF - Purchase History");
        } else {
            System.out.println("Only buyers can view purchase history");
        }
    }
    
    private void handleRefreshItems() {
        itemController.loadItems(dashboardView, currentUser);
    }
    
    private void loadItems() {
        itemController.loadItems(dashboardView, currentUser);
    }
    
    private void setupWishlistEventHandlers() {
        NavigationBar nav = wishlistView.getNavigationBar();
        nav.getLogoutMenuItem().setOnAction(e -> handleLogout());
        nav.getBrowseItemsMenuItem().setOnAction(e -> showDashboardScene(currentUser));
        if (nav.getViewPurchaseHistoryMenuItem() != null) {
            nav.getViewPurchaseHistoryMenuItem().setOnAction(e -> handleViewPurchaseHistory());
        }
        wishlistView.getRemoveFromWishlistButton().setOnAction(e -> wishlistController.handleRemoveFromWishlist(wishlistView, currentUser));
    }
    
    private void setupPurchaseHistoryEventHandlers() {
        NavigationBar nav = purchaseHistoryView.getNavigationBar();
        nav.getLogoutMenuItem().setOnAction(e -> handleLogout());
        nav.getBrowseItemsMenuItem().setOnAction(e -> showDashboardScene(currentUser));
        if (nav.getViewWishlistMenuItem() != null) {
            nav.getViewWishlistMenuItem().setOnAction(e -> handleViewWishlist());
        }
    }
    
    private void setupAdminEventHandlers() {
        adminView.getApproveButton().setOnAction(e -> adminController.handleApproveItem(adminView));
        adminView.getDeclineButton().setOnAction(e -> adminController.handleDeclineItem(adminView));
        adminView.getNavigationBar().getLogoutMenuItem().setOnAction(e -> {
            adminController.handleLogout();
            showLoginScene();
        });
    }
    
    private void showAdminDashboardScene() {
        this.adminView = new AdminView();
        setupAdminEventHandlers();
        adminController.loadPendingItems(adminView);
        stage.setScene(adminView.getScene());
        stage.setTitle("CaLouselF - Admin Dashboard");
    }

    private void handlePurchaseItem() {
        String selectedItem = dashboardView.getItemListView().getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            dashboardView.showMessage("Please select an item to purchase", true);
            return;
        }

        String itemName = selectedItem.split(" - ")[0];
        try {
            String query = "SELECT item_id FROM items WHERE item_name = ? AND status = 'approved'";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String itemId = rs.getString("item_id");
                transactionController.handlePurchaseItem(itemId, currentUser);
                dashboardView.showMessage("Purchase successful!", false);
                handleRefreshItems(); // Refresh the items list
            } else {
                dashboardView.showMessage("Item not available for purchase", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dashboardView.showMessage("Error processing purchase: " + e.getMessage(), true);
        }
    }
    
}

