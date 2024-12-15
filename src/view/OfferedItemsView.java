package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import main.Main;

public class OfferedItemsView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private ListView<String> offeredItemsView;
    private Button acceptOfferButton;
    private Button declineOfferButton;
    private TextField declineReasonField;
    private Label messageLabel;

    public OfferedItemsView(User user) {
        createOfferedItemsScene(user);
    }

    private void createOfferedItemsScene(User user) {
        layout = new VBox(10);

        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Offered Items");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        offeredItemsView = new ListView<>();
        offeredItemsView.setPrefHeight(400);
        offeredItemsView.setStyle("-fx-border-color: #00bfff; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(10);
        acceptOfferButton = new Button("Accept Offer");
        acceptOfferButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
        acceptOfferButton.setDisable(true);

        declineOfferButton = new Button("Decline Offer");
        declineOfferButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
        declineOfferButton.setDisable(true);

        buttonBox.getChildren().addAll(acceptOfferButton, declineOfferButton);

        declineReasonField = new TextField();
        declineReasonField.setPromptText("Enter reason for declining offer");
        declineReasonField.setVisible(false);

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            offeredItemsView,
            buttonBox,
            declineReasonField,
            messageLabel
        );

        layout.getChildren().addAll(navigationBar, contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        // Enable/disable buttons based on selection
        offeredItemsView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            acceptOfferButton.setDisable(!hasSelection);
            declineOfferButton.setDisable(!hasSelection);
        });

        // Show/hide decline reason field based on decline button
        declineOfferButton.setOnAction(e -> {
            declineReasonField.setVisible(true);
        });
    }

    public Scene getScene() { return scene; }
    public NavigationBar getNavigationBar() { return navigationBar; }
    public ListView<String> getOfferedItemsView() { return offeredItemsView; }
    public Button getAcceptOfferButton() { return acceptOfferButton; }
    public Button getDeclineOfferButton() { return declineOfferButton; }
    public TextField getDeclineReasonField() { return declineReasonField; }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

