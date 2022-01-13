package ru.hse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Класс с тестами для библиотеки.
 */
class SpaceLibraryTest {

  /**
   * Проверка получения корня мирового пространства.
   */
  @Test
  void getRoot() {
    Space space = new Space(1, 1);
    assertEquals(new Coord2D(1, 1), space.getRoot());
  }

  /**
   * Добавление point с null родителем.
   */
  @Test
  void addPointWithNullParent() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(1, 1);
    space.addPoint(1, 2, null);
    assertEquals("Parent is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Добавление корректной точки.
   */
  @Test
  void addPointCorrect() {
    Space space = new Space(1, 1);
    assertTrue(space.addPoint(1, 2, space));
  }

  /**
   * Добавление одинаковых точек.
   */
  @Test
  void addPointEquals() {
    Space space = new Space(1, 1);
    space.addPoint(1, 2, space);
    assertFalse(space.addPoint(1, 2, space));
  }

  /**
   * Добавление Origin с null родителем.
   */
  @Test
  void addOriginWithNullParent() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(1, 1);
    space.addOrigin(1, 2, null);
    assertEquals("Parent is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Добавление корректной системы координат.
   */
  @Test
  void addOriginCorrect() {
    Space space = new Space(1, 1);
    assertTrue(space.addOrigin(1, 2, space));
  }

  /**
   * Добавление одинаковых систем координат.
   */
  @Test
  void addOriginEquals() {
    Space space = new Space(1, 1);
    space.addOrigin(1, 2, space);
    assertFalse(space.addOrigin(1, 2, space));
  }

  /**
   * Добавление точки, у которой координаты совпадают с корнем.
   */
  @Test
  void addRootEqualsPoint() {
    Space space = new Space(1, 1);
    assertFalse(space.addPoint(1, 1, space));
  }

  /**
   * Очистка мирового пространства.
   */
  @Test
  void clearSpace() {
    Space space = new Space(1, 1);
    space.addPoint(1, 2, space);
    space.addOrigin(2, 2, space);
    space.clearSpace();
    assertTrue(space.addPoint(1, 2, space));
    assertTrue(space.addOrigin(2, 2, space));
  }

  /**
   * Получение несуществующей точки.
   */
  @Test
  void getPointByCoordinatesNotExsist() {
    Space space = new Space(0, 0);
    assertNull(space.getPointByCoordinates(1, 1));
  }

  /**
   * Получение существующей точки.
   */
  @Test
  void getPointByCoordinates() {
    Space space = new Space(0, 0);
    space.addPoint(1, 1, space);
    assertEquals(space.getPointByCoordinates(1, 1), new Point(1, 1, space));
  }

  /**
   * Получение несуществующей координатной плоскости.
   */
  @Test
  void getOriginByCoordinatesNull() {
    Space space = new Space(0, 0);
    assertNull(space.getOriginByCoordinates(1, 1));
  }

  /**
   * Получение существующей координатной плоскости.
   */
  @Test
  void getOriginByCoordinates() {
    Space space = new Space(0, 0);
    space.addOrigin(1, 1, space);
    assertEquals(space.getOriginByCoordinates(1, 1), new Origin(1, 1, space, space));
  }

  /**
   * Вывод в консоль пустого мирового пространства.
   */
  @Test
  void checkPrintNull() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    Space space = new Space(0, 0);
    space.printAllData(new PrintStream(output));
    assertEquals("""
        --- Origins:\r
        Is empty!\r
        --- Points:\r
        Is empty!\r
        """, output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Вывод в консоль мирового пространства.
   */
  @Test
  void checkPrint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    Space space = new Space(0, 0);
    space.addPoint(1, 1, space);
    space.addOrigin(2, 2, space);
    space.printAllData(new PrintStream(output));
    assertEquals("""
        --- Origins:\r
        Origin: position: x = 2.0, y = 2.0, parent: space: root: x = 0.0, y = 0.0, count children: 0\r
        Count origins: 1\r
        --- Points:\r
        Point: position: x = 1.0, y = 1.0, parent: space: root: x = 0.0, y = 0.0\r
        Count points: 1\r
        """, output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Создание цикла в графе.
   */
  @Test
  void checkDAGException() {
    Space space = new Space(0, 0);
    space.addOrigin(1, 2, space);
    Origin origin = space.getOriginByCoordinates(1, 2);
    space.addOrigin(2, 3, origin);
    Origin origin1 = space.getOriginByCoordinates(2, 3);
    space.addOrigin(3, 4, origin1);
    Origin origin2 = space.getOriginByCoordinates(3, 4);

    origin.addChildren(origin1);
    origin1.addChildren(origin2);

    DAGException dagException = Assertions.assertThrows(DAGException.class,
        () -> origin2.addChildren(origin));
    assertEquals("Cycle detected!", dagException.getMessage());
  }

  /**
   * Создание координатной плоскости с null родителем.
   */
  @Test
  void createOriginWithNullParent() {
    Space space = new Space(0, 0);
    NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
        () -> new Origin(1, 1, null, space));
    assertEquals("Null parent detected!", nullPointerException.getMessage());
  }

  /**
   * Получение родителя координатной плоскости.
   */
  @Test
  void getParentOfOrigin() {
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    assertEquals(origin.getParent(), space);
  }

  /**
   * Установка null родителя координатной плоскости.
   */
  @Test
  void setOriginParentNull() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    origin.setParent(null);
    assertEquals("New parent is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Установка родителя координатной плоскости.
   */
  @Test
  void setParentOrigin() {
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    Origin originNew = new Origin(1, 1, space, space);
    originNew.setParent(origin);
  }

  /**
   * Установка null позиции для координатной плоскости.
   */
  @Test
  void setPositionOriginNull() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    origin.setPosition(null);
    assertEquals("Coord is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Установка позиции для координатной плоскости.
   */
  @Test
  void setPositionOrigin() {
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    origin.setPosition(new Coord2D(1, 1));
  }

  /**
   * Сравнение одинаковых координатных плоскостей.
   */
  @Test
  void equalsSameOrigin() {
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    origin.equals(origin);
  }

  /**
   * Сравнение координатной плоскости с null.
   */
  @Test
  void equalsNullOrigin() {
    Space space = new Space(0, 0);
    Origin origin = new Origin(1, 1, space, space);
    origin.equals(null);
  }

  /**
   * Создание точки на плоскости с null родителем.
   */
  @Test
  void createPointWithNullParent() {
    NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
        () -> new Point(1, 1, null));
    assertEquals("Null parent detected!", nullPointerException.getMessage());
  }

  /**
   * Получение объема координатной плоскости без детей.
   */
  @Test
  void getBoundOriginNull() {
    Space space = new Space(1, 2);
    Origin origin = new Origin(2, 3, space, space);
    BoundBox boundBox = origin.getBoundBox();
    assertEquals(boundBox.getData(), List.of(new Coord2D(0, 0), new Coord2D(0, 0)));
  }

  /**
   * Получение объема координатной плоскости с детьми (по старым точкам).
   */
  @Test
  void getBoundOriginFirst() {
    Space space = new Space(1, 2);
    Origin origin = new Origin(2, 3, space, space);
    origin.addChildren(new Point(1, 1, origin));
    origin.addChildren(new Point(-1, -1, origin));
    origin.addChildren(new Point(2, 3, origin));
    BoundBox boundBox = origin.getBoundBox();
    assertEquals(List.of(new Coord2D(-1, -1), new Coord2D(2, 3)), boundBox.getData());
  }

  /**
   * Получение объема координатной плоскости с детьми (создание новых точек).
   */
  @Test
  void getBoundOriginSecond() {
    Space space = new Space(1, 2);
    Origin origin = new Origin(2, 3, space, space);
    origin.addChildren(new Point(-5, 1, origin));
    origin.addChildren(new Point(-1, 1, origin));
    origin.addChildren(new Point(1, -1, origin));
    origin.addChildren(new Point(1, 3, origin));
    origin.addChildren(new Point(2, 3, origin));
    origin.addChildren(new Point(2, 8, origin));
    BoundBox boundBox = origin.getBoundBox();
    assertEquals(List.of(new Coord2D(-5, -1), new Coord2D(2, 8)), boundBox.getData());
  }

  /**
   * Получение объема координатной плоскости с детьми + детьми детей.
   */
  @Test
  void getBoundOriginThird() {
    Space space = new Space(1, 2);
    Origin origin = new Origin(2, 3, space, space);
    Origin origin1 = new Origin(10, 10, origin, space);
    origin1.addChildren(new Point(-10, -10, space));
    origin1.addChildren(new Point(10, 10, space));
    origin.addChildren(origin1);
    origin.addChildren(new Point(-5, 1, origin));
    BoundBox boundBox = origin.getBoundBox();
    assertEquals(List.of(new Coord2D(-10, -10), new Coord2D(10, 10)), boundBox.getData());
  }

  /**
   * Вывод информации о координатной плоскости в консоль.
   */
  @Test
  void printChildrenOrigin() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    Space space = new Space(0, 0);
    space.addOrigin(1, 1, space);
    Origin origin = space.getOriginByCoordinates(1, 1);
    origin.addChildren(new Point(1, 2, origin));
    origin.addChildren(new Point(1.3, 2.2, origin));
    origin.printAllChildren(new PrintStream(output));
    assertEquals("""
        - Children\r
        Point: position: x = 1.0, y = 2.0, parent: Origin: position: x = 1.0, y = 1.0, parent: space: root: x = 0.0, y = 0.0, count children: 2\r
        Point: position: x = 1.3, y = 2.2, parent: Origin: position: x = 1.0, y = 1.0, parent: space: root: x = 0.0, y = 0.0, count children: 2\r
        """, output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Получение родителя точки.
   */
  @Test
  void getParentOfPoint() {
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    assertEquals(point.getParent(), space);
  }

  /**
   * Установка null родителя точки.
   */
  @Test
  void setPointParentNull() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    point.setParent(null);
    assertEquals("Parent is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Установка родителя точки.
   */
  @Test
  void setParentPoint() {
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    Origin origin = new Origin(1, 1, space, space);
    point.setParent(origin);
  }

  /**
   * Установка null позиции для точки.
   */
  @Test
  void setPositionNullPoint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    point.setPosition(null);
    assertEquals("Coord is null!\r\n", output.toString());
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

  /**
   * Установка позиции для точки.
   */
  @Test
  void setPositionPoint() {
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    point.setPosition(new Coord2D(1, 1));
  }

  /**
   * Сравнение одинаковых объектов точки.
   */
  @Test
  void equalsSamePoint() {
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    point.equals(point);
  }

  /**
   * Сравнение точки c null.
   */
  @Test
  void equalsNullPoint() {
    Space space = new Space(0, 0);
    Point point = new Point(1, 1, space);
    point.equals(null);
  }

  /**
   * Сравнение одинаковых объектов координат.
   */
  @Test
  void equalsSameCoord2D() {
    Coord2D coord2D = new Coord2D(1, 1);
    coord2D.equals(coord2D);
  }

  /**
   * Сравнение координаты c null.
   */
  @Test
  void equalsNullCoord2D() {
    Coord2D coord2D = new Coord2D(1.2, 1.3);
    coord2D.equals(null);
  }
}