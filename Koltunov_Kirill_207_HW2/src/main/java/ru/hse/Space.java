package ru.hse;

import java.io.PrintStream;
import java.util.*;

/**
 * Класс описывающий мировую систему координат.
 */
public class Space implements ParentNode {

  /**
   * Точка начала координат (корень).
   */
  private final Coord2D root;

  /**
   * Set, в котором находятся все point на системе координат.
   */
  private final Set<Point> points;

  /**
   * Set, в котором находятся все origin на системе координат.
   */
  private final Set<Origin> origins;

  /**
   * Конструктор.
   *
   * @param x Координата по x.
   * @param y Координата по y.
   */
  public Space(double x, double y) {
    root = new Coord2D(x, y);
    points = new HashSet<>();
    origins = new HashSet<>();
  }

  /**
   * Getter для корня.
   *
   * @return Coord2D, который содержит в себе координаты по х и у.
   */
  public Coord2D getRoot() {
    return root;
  }

  /**
   * Получение позиции Point по координатам х и у.
   *
   * @param x Координата по x.
   * @param y Координата по y.
   * @return Объект Point, если по координатам имеется точка, иначе null.
   */
  public Point getPointByCoordinates(double x, double y) {
    for (Point point : points) {
      Coord2D position = point.getPosition();
      if (Double.compare(position.getX(), x) == 0 && Double.compare(position.getY(), y) == 0) {
        return point;
      }
    }

    return null;
  }

  public Origin getOriginByCoordinates(double x, double y) {
    for (Origin origin : origins) {
      Coord2D position = origin.getPosition();
      if (Double.compare(position.getX(), x) == 0 && Double.compare(position.getY(), y) == 0) {
        return origin;
      }
    }

    return null;
  }

  /**
   * Проверка корректности новых параметров для точки перед добавлением их в систему координат.
   *
   * @param x Координата по x.
   * @param y Координата по y.
   * @return true если коллизия найдена, false иначе.
   */
  private boolean checkCollision(double x, double y) {
    if (Double.compare(root.getX(), x) == 0 && Double.compare(root.getY(), y) == 0) {
      return true;
    }

    for (Point point : points) {
      Coord2D position = point.getPosition();
      if (Double.compare(position.getX(), x) == 0 && Double.compare(position.getY(), y) == 0) {
        return true;
      }
    }

    for (Origin origin : origins) {
      Coord2D position = origin.getPosition();
      if (Double.compare(position.getX(), x) == 0 && Double.compare(position.getY(), y) == 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Проверка на циклы в графе.
   */
  public void checkCycle() {
    for (Origin origin : origins) {
      origin.findCycles(new HashSet<>());
    }
  }

  /**
   * Добавление точки по координатам.
   *
   * @param x         Координата по x.
   * @param y         Координата по y.
   * @param newParent Ссылка на объект родителя.
   * @return true если точка добавлена, false иначе.
   */
  public boolean addPoint(double x, double y, ParentNode newParent) {
    if (newParent == null) {
      System.out.println("Parent is null!");
      return false;
    }
    if (checkCollision(x, y)) {
      System.out.println(
          "An object has already been placed according to these coordinates" + " x = " + x
              + ", y = " + y + "!");
      return false;
    }
    points.add(new Point(x, y, newParent));
    return true;
  }

  /**
   * Добавление origin по координатам.
   *
   * @param x         Координата по x.
   * @param y         Координата по y.
   * @param newParent Ссылка на объект родителя.
   * @return true если origin добавлен, false иначе.
   */
  public boolean addOrigin(double x, double y, ParentNode newParent) {
    if (newParent == null) {
      System.out.println("Parent is null!");
      return false;
    }
    if (checkCollision(x, y)) {
      System.out.println(
          "An object has already been placed according to these coordinates" + " x = " + x
              + ", y = " + y + "!");
      return false;
    }
    origins.add(new Origin(x, y, newParent, this));
    return true;
  }

  /**
   * Вывод информации о всех origin и point в консоль.
   */
  public void printAllData(PrintStream printStream) {
    printStream.println("--- Origins:");
    if (origins.size() == 0) {
      printStream.println("Is empty!");
    } else {
      origins.forEach(printStream::println);
      printStream.println("Count origins: " + origins.size());
    }

    printStream.println("--- Points:");

    if (points.size() == 0) {
      printStream.println("Is empty!");
    } else {
      points.forEach(printStream::println);
      printStream.println("Count points: " + points.size());
    }
  }

  /**
   * Очистка пространства, удаление всех origin и point.
   */
  public void clearSpace() {
    points.clear();
    origins.clear();
  }

  /**
   * Информация об объекте.
   *
   * @return Строка с данными.
   */
  @Override
  public String toString() {
    return "space: root: " + root;
  }
}
