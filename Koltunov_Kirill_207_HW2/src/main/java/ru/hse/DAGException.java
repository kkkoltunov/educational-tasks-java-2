package ru.hse;

/**
 * Класс для создания собственных исключений.
 */
public class DAGException extends RuntimeException {

  /**
   * Конструктор.
   *
   * @param message Сообщение об ошибке.
   */
  public DAGException(String message) {
    super(message);
  }
}
