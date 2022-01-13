package ru.hse;

import java.util.Objects;

/**
 * Класс описывающий точку с координатами.
 */
public final class Coord2D {

  /**
   * Координата по х.
   */
  private final double x;

  /**
   * Координата по у.
   */
  private final double y;

  /**
   * Конструктор.
   *
   * @param x Координата по х.
   * @param y Координата по у.
   */
  public Coord2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Getter для координаты х.
   *
   * @return Число типа double.
   */
  public double getX() {
    return x;
  }

  /**
   * Getter для координаты y.
   *
   * @return Число типа double.
   */
  public double getY() {
    return y;
  }

  /**
   * Метод для сравнения координат.
   *
   * @param o Объект для сравнения.
   * @return true, если объекты равны, false иначе.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord2D coord2D = (Coord2D) o;
    return Double.compare(coord2D.x, x) == 0 && Double.compare(coord2D.y, y) == 0;
  }

  /**
   * Вычисление хеша для точки.
   *
   * @return Значение хеша.
   */
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  /**
   * Информация об объекте.
   *
   * @return Строка с данными.
   */
  @Override
  public String toString() {
    return "x = " + x + ", y = " + y;
  }
}
