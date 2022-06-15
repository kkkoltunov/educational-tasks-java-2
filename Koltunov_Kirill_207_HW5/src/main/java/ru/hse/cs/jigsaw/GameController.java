package ru.hse.cs.jigsaw;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Главный контроллер программы.
 */
public class GameController {

    /**
     * Специальный объект для обновления времени игры.
     */
    private Timeline timeline;

    /**
     * Вспомогательный класс для игры.
     */
    private GameManager gameManager;

    /**
     * Инициализация класса игры, обновление лейблов и запуск отсчета времени игры.
     */
    @FXML
    public void initialize() {
        gameManager = new GameManager(dragGridPane, dropGridPane, scoreLabel);
        labelUpdate();
        startTime();
    }

    /**
     * Обновление лейблов.
     */
    private void labelUpdate() {
        scoreLabel.setText("Набранные очки: 0");
        timeLabel.setText("Время игры: 00:00:00");
    }

    /**
     * Запуск подсчета времени игры.
     */
    private void startTime() {
        LocalTime startTime = LocalTime.now();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalTime currentTime = LocalTime.now();
            long seconds = ChronoUnit.SECONDS.between(startTime, currentTime) % 60;
            long minutes = ChronoUnit.MINUTES.between(startTime, currentTime) % 60;
            long hours = ChronoUnit.HOURS.between(startTime, currentTime);
            Platform.runLater(() -> timeLabel.setText("Время игры: " +
                    (hours >= 10 ? "" + hours : "0" + hours) + ":" +
                    (minutes >= 10 ? "" + minutes : "0" + minutes) + ":" +
                    (seconds >= 10 ? "" + seconds : "0" + seconds)));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Панель из которой берут элементы.
     */
    @FXML
    private GridPane dragGridPane;

    /**
     * Панель, в которую дропают элементы (игровое поле).
     */
    @FXML
    private GridPane dropGridPane;

    /**
     * Текстовое поле, в котором выводится количество успешно поставленных фигур.
     */
    @FXML
    private Label scoreLabel;

    /**
     * Текстовое поле, в котором выводится время от старта игры.
     */
    @FXML
    private Label timeLabel;

    /**
     * Событие, которое возникает при нажатии кнопки "Завершить игру".
     */
    @FXML
    void clickStopGameButton() {
        timeline.stop();
        ButtonType startNewGame = new ButtonType("Начать новую игру", ButtonBar.ButtonData.OK_DONE);
        ButtonType exitGame = new ButtonType("Выйти", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                scoreLabel.getText() + System.lineSeparator() + timeLabel.getText(),
                startNewGame, exitGame);
        alert.setTitle(null);
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() == exitGame) {
            Platform.exit();
        } else if (result.get() == startNewGame) {
            gameManager.restartGame();
            labelUpdate();
            startTime();
        }
    }
}
