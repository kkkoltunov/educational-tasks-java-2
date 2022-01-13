package battleship;

/**
 * Абстрактный класс, который описывает корабль.
 */
public abstract class Ship {

  /**
   * Расположения корабля на поле (горизонтально или вертикально).
   */
  private final LayoutTypeOfShip LAYOUT_TYPE;

  /**
   * Координата х на поле.
   */
  private final int x;

  /**
   * Координата y на поле.
   */
  private final int y;

  /**
   * Конструктор класса.
   *
   * @param layoutTypeOfShip Расположения корабля на поле (горизонтально или вертикально).
   * @param x                Координата х на поле.
   * @param y                Координата y на поле.
   */
  public Ship(int layoutTypeOfShip, int x, int y) {
    if (layoutTypeOfShip == 0) {
      this.LAYOUT_TYPE = LayoutTypeOfShip.HORIZONTAL;
    } else {
      this.LAYOUT_TYPE = LayoutTypeOfShip.VERTICAL;
    }

    this.x = x;
    this.y = y;
  }

  /**
   * Получение расположения корабля на поле.
   *
   * @return LayoutTypeOfShip, значение которого является расположением.
   */
  public LayoutTypeOfShip getLayout() {
    return LAYOUT_TYPE;
  }

  /**
   * Получение координаты x, в которой расположен корабль.
   *
   * @return Число типа int.
   */
  public int getX() {
    return x;
  }

  /**
   * Получение координаты y, в которой расположен корабль.
   *
   * @return Число типа int.
   */
  public int getY() {
    return y;
  }

  /**
   * Получение размера корабля.
   *
   * @return Число типа int.
   */
  public abstract int getSize();

  /**
   * Проверка на затопленность корабля (количество HP > 0).
   *
   * @return true, если корабль затоплен, иначе false.
   */
  public abstract boolean isSank();

  /**
   * Выстрел по кораблю (уменьшение количества HP на 1).
   *
   * @return true, если корабль в результате попадания был затоплен, false иначе.
   */
  public abstract boolean shoot();

  /**
   * Выстрел по кораблю торпедой (обнуление количества HP).
   */
  public abstract void shootTorpedo();

  /**
   * Переопределенный метод для вывода информации о корабле.
   *
   * @return String с информацией о корабле.
   */
  @Override
  public String toString() {
    return "Ship";
  }
}
