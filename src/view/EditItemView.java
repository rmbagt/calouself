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

public class EditItemView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private TextField itemNameField;
    private TextField itemCategoryField;
    private TextField itemSizeField;
    private TextField itemPriceField;
    private Button saveButton;
    private Button cancelButton;
    private Label messageLabel;

    public EditItemView(User user, Item item) {
        createEditItemScene(user, item);
    }

    private void createEditItemScene(User user, Item item) {
        layout = new VBox();
        
        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Edit Item");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        itemNameField = new TextField(item.getItem_name());
        itemNameField.setPromptText("Item Name");

        itemCategoryField = new TextField(item.getCategory());
        itemCategoryField.setPromptText("Item Category");

        itemSizeField = new TextField(item.getSize());
        itemSizeField.setPromptText("Item Size");

        itemPriceField = new TextField(String.valueOf(item.getPrice()));
        itemPriceField.setPromptText("Item Price");

        saveButton = new Button("Save Changes");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");

        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            new Label("Item Name:"),
            itemNameField,
            new Label("Item Category:"),
            itemCategoryField,
            new Label("Item Size:"),
            itemSizeField,
            new Label("Item Price:"),
            itemPriceField,
            buttonBox,
            messageLabel
        );

        layout.getChildren().add(navigationBar);
        layout.getChildren().add(contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public TextField getItemNameField() {
        return itemNameField;
    }

    public TextField getItemCategoryField() {
        return itemCategoryField;
    }

    public TextField getItemSizeField() {
        return itemSizeField;
    }

    public TextField getItemPriceField() {
        return itemPriceField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

