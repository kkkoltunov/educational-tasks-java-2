package ru.hse;

/**
 * Интерфейс, который реализуют объекты являющиеся некой сущностью на плоскости.
 */
public interface ObjectCoordinateSystem {

  /**
   * Getter для получения позиции.
   *
   * @return Coord2D точка пространства.
   */
  public Coord2D getPosition();

  /**
   * Установка новой точки на пространстве.
   *
   * @param newValue newValue Ссылка на новую точку.
   */
  public void setPosition(Coord2D newValue);
}
