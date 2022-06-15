package ru.hse.cs.jigsaw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Точка входа в программу.
 */
public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Jigsaw");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(800);
        stage.setMaxWidth(800);
        stage.setMaxHeight(1000);
        stage.show();
    }
}