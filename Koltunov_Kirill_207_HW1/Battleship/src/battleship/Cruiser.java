package battleship;

/**
 * Класс, который описывает крейсер (занимает 3 ячейки на поле).
 */
public class Cruiser extends Ship {

  /**
   * Размер корабля на поле (количество занимаемых им ячеек).
   */
  private final int SIZE = 3;

  /**
   * Количество HP.
   */
  public int health;

  /**
   * Конструктор класса.
   *
   * @param layoutTypeOfShip Расположения корабля на поле (горизонтально или вертикально).
   * @param x                Координата х на поле.
   * @param y                Координата y на поле.
   */
  public Cruiser(int layoutTypeOfShip, int x, int y) {
    super(layoutTypeOfShip, x, y);
    health = SIZE;
  }

  /**
   * Получение размера корабля.
   *
   * @return Число типа int.
   */
  public int getSize() {
    return SIZE;
  }

  /**
   * Проверка на затопленность корабля (количество HP > 0).
   *
   * @return true или false в зависимости от исхода.
   */
  public boolean isSank() {
    return health <= 0;
  }

  /**
   * Выстрел по кораблю (уменьшение количества HP на 1).
   *
   * @return true, если корабль в результате попадания был затоплен, false иначе.
   */
  public boolean shoot() {
    --health;
    return isSank();
  }

  /**
   * Выстрел по кораблю торпедой (обнуление количества HP).
   */
  public void shootTorpedo() {
    health = 0;
  }

  /**
   * Переопределенный метод для вывода информации о корабле.
   *
   * @return String с информацией о корабле.
   */
  @Override
  public String toString() {
    return "крейсер (трехпалубный корабль)";
  }
}
