package ru.hse;

import java.util.List;
import java.util.Set;

/**
 * Класс описывающий прямоугольник, со сторонами, параллельными осям координат, определяемый
 * координатами своей диагонали. Необходим для определения минимального объема пространства.
 */
public class BoundBox {

  /**
   * Множество точек для какого-то пространства.
   */
  private final Set<Point> data;

  /**
   * Конструктор.
   *
   * @param data Множество точек для какого-то пространства.
   */
  public BoundBox(Set<Point> data) {
    this.data = data;
  }

  /**
   * Получение координат диагонали прямоугольника.
   *
   * @return Список координат (минимальная точка и максимальная точка).
   */
  public List<Coord2D> getData() {
    if (data.size() == 0) {
      return List.of(new Coord2D(0, 0), new Coord2D(0, 0));
    }

    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    double maxY = Double.MIN_VALUE;

    for (Point point : data) {
      Coord2D pointPosition = point.getPosition();
      if (pointPosition.getX() < minX) {
        minX = pointPosition.getX();
      }
      if (pointPosition.getX() > maxX) {
        maxX = pointPosition.getX();
      }
      if (pointPosition.getY() < minY) {
        minY = pointPosition.getY();
      }
      if (pointPosition.getY() > maxY) {
        maxY = pointPosition.getY();
      }
    }

    return List.of(new Coord2D(minX, minY), new Coord2D(maxX, maxY));
  }
}
