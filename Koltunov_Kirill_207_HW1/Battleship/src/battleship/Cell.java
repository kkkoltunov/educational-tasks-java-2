package battleship;

/**
 * Класс, который описывает ячейку поля.
 */
public class Cell {

  /**
   * Ссылка на корабль (или его составляющую), который находится в ячейке.
   */
  private Ship ship;

  /**
   * Значение ячейки.
   */
  private CellValue cellValue;

  /**
   * Конструктор класса.
   */
  public Cell() {
    ship = null;
    cellValue = CellValue.NOT_CHECK;
  }

  /**
   * Получение значения ячейки.
   *
   * @return CellValue, которое указывает на значения ячейки.
   */
  public CellValue getCellValue() {
    return cellValue;
  }

  /**
   * Установка значения ячейки.
   *
   * @param newCellValue CellValue, которое указывает на новое значение ячейки.
   */
  public void setCellValue(CellValue newCellValue) {
    cellValue = newCellValue;
  }

  /**
   * Установка корабля в ячейку.
   *
   * @param ship Ссылка типа Ship на устанавливаемый корабль.
   */
  public void setShip(Ship ship) {
    this.ship = ship;
  }

  /**
   * Получение корабля из ячейки.
   *
   * @return Ссылка типа Ship на корабль в ячейке.
   */
  public Ship getShip() {
    return ship;
  }

  /**
   * Проверка наличия корабля в ячейке.
   *
   * @return true, если корабль обнаружен в ячейке, иначе false.
   */
  public boolean isShipHere() {
    return ship != null;
  }
}
