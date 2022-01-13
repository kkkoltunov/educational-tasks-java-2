package ru.hse;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Система координат.
 */
public class Origin implements ObjectCoordinateSystem, ParentNode {

  /**
   * Координата на плоскости.
   */
  private Coord2D position;

  /**
   * Ссылка на родительский узел.
   */
  private ParentNode parent;

  /**
   * Множество детей, в котором могут находиться origin и point.
   */
  private final Set<ObjectCoordinateSystem> children;

  /**
   * Ссылка на объект мирового пространства, в котором находится объект.
   */
  private final Space space;

  /**
   * Конструктор.
   *
   * @param x         Координата х.
   * @param y         Координата у.
   * @param newParent Ссылка на родительский узел.
   * @param newSpace  Ссылка на объект мирового пространства, в котором находится объект.
   */
  public Origin(double x, double y, ParentNode newParent, Space newSpace) {
    position = new Coord2D(x, y);
    children = new HashSet<>();
    space = newSpace;

    if (newParent == null) {
      throw new NullPointerException("Null parent detected!");
    }
    parent = newParent;
  }

  /**
   * Добавление детей.
   *
   * @param newObject Ссылка на объект типа Origin или Point.
   */
  public void addChildren(ObjectCoordinateSystem newObject) {
    if (newObject != null) {
      children.add(newObject);
    } else {
      System.out.println("Children is null!");
    }

    space.checkCycle();
  }

  /**
   * Getter для получения ссылки на объект родителя.
   *
   * @return Ссылка типа ParentNode.
   */
  public ParentNode getParent() {
    return parent;
  }

  /**
   * Рекурсивный метод для поиска цикла в графе.
   *
   * @param originSet Множество точек, которые идут от корня дл этой точки.
   */
  public void findCycles(Set<Origin> originSet) {
    Set<Origin> origins = getChildren();
    for (Origin origin : origins) {
      for (Origin originGet : originSet) {
        if (origin.equals(originGet)) {
          throw new DAGException("Cycle detected!");
        }
      }
    }

    originSet.add(this);
    for (Origin origin : origins) {
      origin.findCycles(originSet);
    }
  }

  /**
   * Получение множества детей.
   *
   * @return Объект типа Set.
   */
  public Set<Origin> getChildren() {
    Set<Origin> origins = new HashSet<>();
    for (ObjectCoordinateSystem objectCoordinateSystem : children) {
      if (objectCoordinateSystem instanceof Origin) {
        origins.add((Origin) objectCoordinateSystem);
      }
    }
    return origins;
  }

  /**
   * Получение множества точек.
   *
   * @return Объект типа Point.
   */
  public Set<Point> getPoints() {
    Set<Point> points = new HashSet<>();
    for (ObjectCoordinateSystem objectCoordinateSystem : children) {
      if (objectCoordinateSystem instanceof Point) {
        points.add((Point) objectCoordinateSystem);
      }
    }
    return points;
  }

  /**
   * Получение объема пространства.
   *
   * @return Ссылка на объект BoundBox.
   */
  public BoundBox getBoundBox() {
    Set<Point> points = new HashSet<>();
    for (ObjectCoordinateSystem objectCoordinateSystem : children) {
      if (objectCoordinateSystem instanceof Point) {
        points.add((Point) objectCoordinateSystem);
      } else {
        points.addAll(((Origin) objectCoordinateSystem).getPoints());
      }
    }
    return new BoundBox(points);
  }

  /**
   * Уставка родителя для точки.
   *
   * @param newParent Ссылка на объект типа ParentNode (Space или Origin).
   */
  public void setParent(ParentNode newParent) {
    if (newParent != null) {
      parent = newParent;
    } else {
      System.out.println("New parent is null!");
    }
  }

  /**
   * Вывод информации о детях в консоль.
   */
  public void printAllChildren(PrintStream printStream) {
    printStream.println("- Children");
    if (children.size() == 0) {
      printStream.println("Is empty!");
    } else {
      children.forEach(printStream::println);
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
   * @param newValue newValue Ссылка на новую точку.
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
    Origin origin = (Origin) o;
    return Objects.equals(position, origin.position);
  }

  /**
   * Вычисление хеша для точки.
   *
   * @return Значение хеша.
   */
  @Override
  public int hashCode() {
    return Objects.hash(position);
  }

  /**
   * Информация об объекте.
   *
   * @return Строка с данными.
   */
  @Override
  public String toString() {
    return "Origin: " +
        "position: " + position +
        ", parent: " + parent +
        ", count children: " + children.size();
  }
}
