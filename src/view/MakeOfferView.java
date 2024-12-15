package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import model.Item;
import main.Main;

public class MakeOfferView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private Label itemDetailsLabel;
    private TextField offerPriceField;
    private Button submitOfferButton;
    private Button cancelButton;
    private Label messageLabel;

    public MakeOfferView(User user, Item item, double currentHighestOffer) {
        createMakeOfferScene(user, item, currentHighestOffer);
    }

    private void createMakeOfferScene(User user, Item item, double currentHighestOffer) {
        layout = new VBox(10);
        
        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Make an Offer");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        itemDetailsLabel = new Label(String.format(
            "Item: %s\nCategory: %s\nSize: %s\nOriginal Price: Rp %.2f\nCurrent Highest Offer: Rp %.2f",
            item.getItem_name(), item.getCategory(), item.getSize(), 
            item.getPrice(), currentHighestOffer
        ));

        offerPriceField = new TextField();
        offerPriceField.setPromptText("Enter your offer price");

        submitOfferButton = new Button("Submit Offer");
        submitOfferButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");

        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(submitOfferButton, cancelButton);

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            itemDetailsLabel,
            new Label("Your Offer Price:"),
            offerPriceField,
            buttonBox,
            messageLabel
        );

        layout.getChildren().addAll(navigationBar, contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
    }

    public Scene getScene() { return scene; }
    public NavigationBar getNavigationBar() { return navigationBar; }
    public TextField getOfferPriceField() { return offerPriceField; }
    public Button getSubmitOfferButton() { return submitOfferButton; }
    public Button getCancelButton() { return cancelButton; }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearFields() {
        offerPriceField.clear();
        messageLabel.setText("");
    }
}

