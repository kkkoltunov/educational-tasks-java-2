package battleship;

/**
 * Перечисление, которые указывает на значение ячейки.
 */
public enum CellValue {
  /**
   * Ячейка не проверена (◯).
   */
  NOT_CHECK,

  /**
   * Ячейка проверена, выстрел мимо (•).
   */
  CHECK_MISS,

  /**
   * Ячейка проверена, попадание по кораблю (×).
   */
  CHECK_HIT,

  /**
   * Ячейка проверена, корабль потоплен (#).
   */
  SUNK
}