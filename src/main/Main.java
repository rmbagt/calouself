package main;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.UserController;

public class Main extends Application {
    // Define fixed window dimensions
    public static final double WINDOW_WIDTH = 800;
    public static final double WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("CaLouselF");
            primaryStage.setResizable(false); // Make window non-resizable
            primaryStage.setWidth(WINDOW_WIDTH);
            primaryStage.setHeight(WINDOW_HEIGHT);
            UserController userController = new UserController(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("An error occurred during application startup:");
            e.printStackTrace();
        }
    }
}

