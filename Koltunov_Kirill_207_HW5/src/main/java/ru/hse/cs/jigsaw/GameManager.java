package ru.hse.cs.jigsaw;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс управляющий ходом игры.
 */
public class GameManager {

    /**
     * Количество строка на игровом поле.
     */
    private final int COUNT_ROWS = 9;

    /**
     * Количество столбцов на игровом поле.
     */
    private final int COUNT_COLUMNS = 9;

    /**
     * Константа определяющая голубой цвет.
     */
    private final String BLUE_COLOR = "9e98d9";

    /**
     * Константа определяющая серый цвет.
     */
    private final String GREY_COLOR = "cacde0";

    /**
     * Панель из которой берут элементы.
     */
    private final GridPane dragGridPane;

    /**
     * Панель, в которую дропают элементы (игровое поле).
     */
    private final GridPane dropGridPane;

    /**
     * Текстовое поле, в котором выводится количество успешно поставленных фигур.
     */
    private final Label scoreLabel;

    /**
     * Координата по X появления фигуры на поле.
     */
    private double spawnGridX;

    /**
     * Координата по Y появления фигуры на поле.
     */
    private double spawnGridY;

    /**
     * Количество блоков из которых состоит фигура.
     */
    private double countBlocks;

    /**
     * Цвет фигуры.
     */
    private String colorOfFigure;

    /**
     * Количество успешно поставленных фигур на поле.
     */
    private int score = 0;

    /**
     * Конструктор.
     *
     * @param dragGridPane Панель, которую перетаскивают.
     * @param dropGridPane Панель, в которую бросают элементы.
     * @param scoreLabel   Поле для вывода счета игры.
     */
    public GameManager(GridPane dragGridPane, GridPane dropGridPane, Label scoreLabel) {
        this.dragGridPane = dragGridPane;
        this.dropGridPane = dropGridPane;
        this.scoreLabel = scoreLabel;
        initializeGameField();
        addHandlers();
        generateNewFigure();
    }

    /**
     * Перезапуск игры и обновление поля.
     */
    public void restartGame() {
        initializeGameField();
        score = 0;
    }

    /**
     * Создание игрового поля.
     */
    private void initializeGameField() {
        while (dropGridPane.getChildren().size() > 0) {
            dropGridPane.getChildren().remove(0);
        }

        for (int i = 0; i < COUNT_ROWS; ++i) {
            for (int j = 0; j < COUNT_COLUMNS; ++j) {
                Pane pane = new Pane();
                pane.setPrefSize(45, 45);
                if ((i <= 2 && j >= 3 && j <= 5) || (i >= 3 && i <= 5 && j <= 2) ||
                        (i >= 3 && i <= 5 && j >= 6) || (i >= 6 && j >= 3 && j <= 5)) {
                    pane.setStyle("-fx-background-color: #" + GREY_COLOR + "; -fx-border-color: #" + BLUE_COLOR + ";");
                } else {
                    String WHITE_COLOR = "fcfcfc";
                    pane.setStyle("-fx-background-color: #" + WHITE_COLOR + "; -fx-border-color: #" + BLUE_COLOR + ";");
                }
                dropGridPane.add(pane, j, i);
            }
        }
    }

    /**
     * Выбор случайного цвета для фигуры.
     */
    private void generateColorForFigure() {
        int colorNumber = (int) (Math.random() * 6);
        if (colorNumber == 0) {
            colorOfFigure = "701e68";
        } else if (colorNumber == 1) {
            colorOfFigure = "e63030";
        } else if (colorNumber == 2) {
            colorOfFigure = "dec41d";
        } else if (colorNumber == 3) {
            colorOfFigure = "ff4800";
        } else if (colorNumber == 4) {
            colorOfFigure = "0037ff";
        } else if (colorNumber == 5) {
            colorOfFigure = "b8274b";
        } else if (colorNumber == 6) {
            colorOfFigure = "11ff00";
        }
    }

    /**
     * Создание нового объекта Pane для GridPane.
     * @return Объект типа Pane.
     */
    private Pane generateNewPane() {
        Pane newPane = new Pane();
        Glow glow = new Glow();
        glow.setLevel(0.9);
        newPane.setEffect(glow);
        newPane.setPrefSize(45, 45);
        newPane.setStyle("-fx-background-color: #" + colorOfFigure + "; -fx-border-color: #000000;");
        return newPane;
    }

    /**
     * Добавление новых столбцов и строк в GridPane.
     * @param countRows Количество строк, которое необходимо добавить.
     * @param countColumns Количество столбцов, которое необходимо добавить.
     */
    private void addRowsAndColumns(int countRows, int countColumns) {
        for (int i = 0; i < countRows; ++i) {
            dragGridPane.addRow(i);
        }

        for (int i = 0; i < countColumns; ++i) {
            dragGridPane.addColumn(i);
        }
    }

    /**
     * Создание новой фигуры (отрисовка ее в объекте GridPane).
     */
    private void generateNewFigure() {
        generateColorForFigure();
        int numberOfFigure = (int) (Math.random() * 30);

        if (numberOfFigure <= 11) {
            countBlocks = 4;
        } else if (numberOfFigure <= 19) {
            countBlocks = 5;
        } else if (numberOfFigure <= 21) {
            countBlocks = 3;
        } else if (numberOfFigure == 22) {
            countBlocks = 1;
        } else if (numberOfFigure <= 26) {
            countBlocks = 3;
        } else if (numberOfFigure <= 30) {
            countBlocks = 4;
        }

        while (dragGridPane.getChildren().size() > 0) {
            dragGridPane.getChildren().remove(0);
        }

        while (dragGridPane.getRowConstraints().size() > 0) {
            dragGridPane.getRowConstraints().remove(0);
        }

        while (dragGridPane.getColumnConstraints().size() > 0) {
            dragGridPane.getColumnConstraints().remove(0);
        }

        if (numberOfFigure == 0) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 0);
        } else if (numberOfFigure == 1) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 2, 1);
        } else if (numberOfFigure == 2) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 2);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 0);
        } else if (numberOfFigure == 3) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 2, 1);
        } else if (numberOfFigure == 4) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 2);
        } else if (numberOfFigure == 5) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 2, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 0, 1);
        } else if (numberOfFigure == 6) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 2);
        } else if (numberOfFigure == 7) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
        } else if (numberOfFigure == 8) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 2);
        } else if (numberOfFigure == 9) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
        } else if (numberOfFigure == 10) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
        } else if (numberOfFigure == 11) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 2, 1);
        } else if (numberOfFigure == 12) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 2);
            dragGridPane.add(generateNewPane(), 2, 2);
            dragGridPane.add(generateNewPane(), 2, 1);
            dragGridPane.add(generateNewPane(), 2, 0);
        } else if (numberOfFigure == 13) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 2);
            dragGridPane.add(generateNewPane(), 2, 2);
        } else if (numberOfFigure == 14) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
        } else if (numberOfFigure == 15) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 2, 1);
            dragGridPane.add(generateNewPane(), 2, 2);
        } else if (numberOfFigure == 16) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 2);
            dragGridPane.add(generateNewPane(), 2, 2);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 0);
        } else if (numberOfFigure == 17) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 2);
        } else if (numberOfFigure == 18) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 2, 1);
        } else if (numberOfFigure == 19) {
            addRowsAndColumns(3, 3);

            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 2, 1);
            dragGridPane.add(generateNewPane(), 2, 2);
            dragGridPane.add(generateNewPane(), 2, 0);
        } else if (numberOfFigure == 20) {
            addRowsAndColumns(1, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
        } else if (numberOfFigure == 21) {
            addRowsAndColumns(3, 1);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
        } else if (numberOfFigure == 22) {
            addRowsAndColumns(1, 1);

            dragGridPane.add(generateNewPane(), 0, 0);
        } else if (numberOfFigure == 23) {
            addRowsAndColumns(2, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 0);
        } else if (numberOfFigure == 24) {
            addRowsAndColumns(2, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
        } else if (numberOfFigure == 25) {
            addRowsAndColumns(2, 2);

            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 0, 1);
        } else if (numberOfFigure == 26) {
            addRowsAndColumns(2, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
        } else if (numberOfFigure == 27) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 0, 2);
            dragGridPane.add(generateNewPane(), 1, 1);
        } else if (numberOfFigure == 28) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 0);
            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 2, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
        } else if (numberOfFigure == 29) {
            addRowsAndColumns(3, 2);

            dragGridPane.add(generateNewPane(), 1, 0);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 1, 2);
            dragGridPane.add(generateNewPane(), 0, 1);
        } else if (numberOfFigure == 30) {
            addRowsAndColumns(2, 3);

            dragGridPane.add(generateNewPane(), 0, 1);
            dragGridPane.add(generateNewPane(), 1, 1);
            dragGridPane.add(generateNewPane(), 2, 1);
            dragGridPane.add(generateNewPane(), 1, 0);
        }

        dragGridPane.setMinHeight(45 * dragGridPane.getRowCount());
        dragGridPane.setMinWidth(45 * dragGridPane.getColumnCount());
    }

    /**
     * Проверка Pane для вставки в него куска фигуры.
     * @param pane Объект для проверки.
     * @param screenBounds BoundBox Pane, в который выполняется вставка.
     * @param screenBoundsSet BoundBox Pane, который вставляется.
     * @return true, если проверка успешно пройдена, false - иначе.
     */
    private boolean validatePane(Pane pane, Bounds screenBounds, Bounds screenBoundsSet) {
        return (pane.getStyle().contains(BLUE_COLOR) || pane.getStyle().contains(GREY_COLOR)) &&
                Math.abs(screenBoundsSet.getMinX() - screenBounds.getMinX()) < 22 &&
                Math.abs(screenBoundsSet.getMinY() - screenBounds.getMinY()) < 22 &&
                Math.abs(screenBoundsSet.getMaxX() - screenBounds.getMaxX()) < 22 &&
                Math.abs(screenBoundsSet.getMaxY() - screenBounds.getMaxY()) < 22;
    }

    /**
     * Навешивание обработчиков событий на gridPane'ы.
     */
    @SuppressWarnings({"unchecked cast", "rawtypes"}) // Подавлены эксепшены при приведении к List<Pane>
    private void addHandlers() {
        dragGridPane.setOnDragDetected(event -> {
            spawnGridX = event.getSceneX();
            spawnGridY = event.getSceneY();
            event.consume();
        });

        dragGridPane.setOnMouseDragged(mouseEvent -> {
            dragGridPane.setTranslateX(mouseEvent.getSceneX() - spawnGridX);
            dragGridPane.setTranslateY(mouseEvent.getSceneY() - spawnGridY);
            dragGridPane.setLayoutX(mouseEvent.getSceneX());
            dragGridPane.setLayoutY(mouseEvent.getSceneY());
            mouseEvent.consume();
        });

        dragGridPane.setOnMouseReleased(mouseDragEvent -> {
            List<Pane> panesForSet = (List) dragGridPane.getChildren();
            List<Pane> panesForChangeColor = new ArrayList<>();
            for (int j = 0; j < countBlocks; ++j) {
                Pane paneSet = panesForSet.get(j);
                Bounds screenBoundsSet = paneSet.localToScene(paneSet.getBoundsInLocal());
                for (int i = 0; i < COUNT_ROWS * COUNT_COLUMNS; ++i) {
                    Pane pane = (Pane) dropGridPane.getChildren().get(i);
                    Bounds screenBounds = pane.localToScene(pane.getBoundsInLocal());
                    if (validatePane(pane, screenBounds, screenBoundsSet)) {
                        panesForChangeColor.add(pane);
                    }
                }
            }

            Glow glow = new Glow();
            glow.setLevel(0.9);
            if (countBlocks == panesForChangeColor.size()) {
                for (Pane pane : panesForChangeColor) {
                    pane.setStyle("-fx-background-color: #" + colorOfFigure + "; -fx-border-color: #000000;");
                    pane.setEffect(glow);
                }
                scoreLabel.setText("Набранные очки: " + ++score);
                generateNewFigure();
            }

            dragGridPane.setLayoutX(spawnGridX);
            dragGridPane.setLayoutY(spawnGridY);
            dragGridPane.setTranslateX(0);
            dragGridPane.setTranslateY(0);
            mouseDragEvent.consume();
        });
    }


}
