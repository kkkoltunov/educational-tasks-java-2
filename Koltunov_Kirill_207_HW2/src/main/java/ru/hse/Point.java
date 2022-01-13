package ru.hse;

import java.util.Objects;

/**
 * Класс описывающий точку на мировой системе координат.
 */
public class Point implements ObjectCoordinateSystem {

  /**
   * Координата на плоскости.
   */
  private Coord2D position;

  /**
   * Ссылка на родительский узел.
   */
  private ParentNode parent;

  /**
   * Конструктор.
   *
   * @param x         Координата по x.
   * @param y         Координата по y.
   * @param newParent Ссылка на родителя.
   */
  public Point(double x, double y, ParentNode newParent) {
    position = new Coord2D(x, y);

    if (newParent == null) {
      throw new NullPointerException("Null parent detected!");
    }
    parent = newParent;
  }

  /**
   * Getter для получения родителя.
   *
   * @return Ссылка на родителя.
   */
  public ParentNode getParent() {
    return parent;
  }

  /**
   * Setter для установки родителя.
   *
   * @param newParent Ссылка на родителя.
   */
  public void setParent(ParentNode newParent) {
    if (newParent != null) {
      parent = newParent;
    } else {
      System.out.println("Parent is null!");
    }
  }

  /**
   * Getter для получения позиции.
   *
   * @return Coord2D точка пространства.
   */
  @Override
  public Coord2D getPosition() {
    return position;
  }

  /**
   * Установка новой точки на пространстве.
   *
   * @param newValue Ссылка на новую точку.
   */
  @Override
  public void setPosition(Coord2D newValue) {
    if (newValue != null) {
      position = newValue;
    } else {
      System.out.println("Coord is null!");
    }
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
    Point point = (Point) o;
    return Objects.equals(position, point.position);
  }

  /**
   * Вычисление хеша для точки.
   *
   * @return Значение хеша.
   */
  @Override
  public int hashCode() {
    return Objects.hash(position, parent);
  }

  /**
   * Информация об объекте.
   *
   * @return Строка с данными.
   */
  @Override
  public String toString() {
    return "Point: " +
        "position: " + position +
        ", parent: " + parent;
  }
}
