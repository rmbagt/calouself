package main;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.UserController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("CaLouselF");
            UserController userController = new UserController(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("An error occurred during application startup:");
            e.printStackTrace();
        }
    }
}

