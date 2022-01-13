package battleship;

/**
 * Класс, который описывает игровое поле с кораблями.
 */
public class BattleField {

  /**
   * Массив ячеек всего поля.
   */
  private final Cell[][] CELLS;

  /**
   * Число строк поля.
   */
  private final int COUNT_LINES;

  /**
   * Число столбцов поля.
   */
  private final int COUNT_COLUMNS;

  /**
   * Конструктор класса.
   *
   * @param COUNT_LINES   Число строк поля.
   * @param COUNT_COLUMNS Число столбцов поля.
   */
  public BattleField(int COUNT_LINES, int COUNT_COLUMNS) {
    CELLS = new Cell[COUNT_LINES][COUNT_COLUMNS];
    this.COUNT_LINES = COUNT_LINES;
    this.COUNT_COLUMNS = COUNT_COLUMNS;

    for (int i = 0; i < COUNT_LINES; ++i) {
      for (int j = 0; j < COUNT_COLUMNS; ++j) {
        CELLS[i][j] = new Cell();
      }
    }
  }

  /**
   * Возврат ссылки на корабль по координатам ячейки.
   *
   * @param x Координата по х.
   * @param y Координата по y.
   * @return Ссылка на корабль.
   */
  public Ship getShip(int x, int y) {
    return CELLS[x][y].getShip();
  }

  /**
   * Установка корабля в ячейку.
   *
   * @param x    Координата по x.
   * @param y    Координата по y.
   * @param ship Ссылка на корабль.
   */
  public void setShipInCell(int x, int y, Ship ship) {
    if (ship.getLayout() == LayoutTypeOfShip.HORIZONTAL) {
      for (int i = y; i < y + ship.getSize(); ++i) {
        CELLS[x][i].setShip(ship);
      }
    } else {
      for (int i = x; i < x + ship.getSize(); ++i) {
        CELLS[i][y].setShip(ship);
      }
    }
  }

  /**
   * Обновление ячеек затопленного корабля.
   *
   * @param ship Корабль, который затопили.
   */
  public void sankShip(Ship ship) {
    if (ship.getLayout() == LayoutTypeOfShip.HORIZONTAL) {
      for (int i = ship.getY(); i < ship.getY() + ship.getSize(); ++i) {
        CELLS[ship.getX()][i].setCellValue(CellValue.SUNK);
      }
    } else {
      for (int i = ship.getX(); i < ship.getX() + ship.getSize(); ++i) {
        CELLS[i][ship.getY()].setCellValue(CellValue.SUNK);
      }
    }
  }

  /**
   * Получение значения ячейки по координатам.
   *
   * @param x Координата х.
   * @param y Координата y.
   * @return CellValue, которое указывает на значения ячейки.
   */
  public CellValue getCellValue(int x, int y) {
    return CELLS[x][y].getCellValue();
  }

  /**
   * Изменение значения ячейки по координатам.
   *
   * @param x            Координата x.
   * @param y            Координат у.
   * @param newCellValue CellValue, которое указывает на новое значения ячейки.
   */
  public void changeValueCell(int x, int y, CellValue newCellValue) {
    CELLS[x][y].setCellValue(newCellValue);
  }

  /**
   * Проверка на наличие корабля по координатам.
   *
   * @param x Координата х.
   * @param y Координата y.
   * @return true или false в зависимости от наличия корабля в ячейке.
   */
  public boolean isShipHere(int x, int y) {
    return CELLS[x][y].isShipHere();
  }

  /**
   * Вывод поля со сгенерированными кораблями (удобно использовать при проверке работоспособности
   * программы).
   */
  public void printShips() {
    for (int i = 0; i < COUNT_LINES; ++i) {
      for (int j = 0; j < COUNT_COLUMNS; ++j) {
        if (CELLS[i][j].isShipHere()) {
          System.out.print(1);
        } else {
          System.out.print(0);
        }
      }
      System.out.println();
    }
  }

  /**
   * Вывод поля с ячейками (игровое поле без отображения кораблей).
   */
  public void printField() {
    String space = "  ";
    String separator = new String(new char[(COUNT_COLUMNS * 3) + 3]).replace('\0', '~');
    System.out.println(separator);

    printNumbersOfColumns();
    for (int i = 0; i < CELLS.length; ++i) {
      System.out.print(i + 1);

      if (COUNT_LINES > 9 && i < 9) {
        System.out.print(" ");
      }

      for (int j = 0; j < CELLS[i].length; ++j) {
        if (CELLS[i][j].getCellValue() == CellValue.NOT_CHECK) {
          System.out.print(space + "◯");
        } else if (CELLS[i][j].getCellValue() == CellValue.CHECK_MISS) {
          System.out.print(space + "•");
        } else if (CELLS[i][j].getCellValue() == CellValue.CHECK_HIT) {
          System.out.print(space + "×");
        } else {
          System.out.print(space + "#");
        }
      }
      System.out.println();
    }
    System.out.println(separator);
  }

  /**
   * Вспомогательный метод для вывода поля на экран. Вывод номеров столбцов.
   */
  public void printNumbersOfColumns() {
    if (COUNT_LINES > 9) {
      System.out.print("  ");
    } else {
      System.out.print(" ");
    }

    for (int i = 0; i < COUNT_COLUMNS; ++i) {
      if (i <= 9) {
        System.out.print("  " + (i + 1));
      } else {
        System.out.print(" " + (i + 1));
      }
    }
    System.out.println();
  }
}
