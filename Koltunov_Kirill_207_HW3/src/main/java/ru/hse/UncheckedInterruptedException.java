package ru.hse;

/**
 * Класс описывающий собственное исключение о прерывании потока.
 */
public class UncheckedInterruptedException extends RuntimeException {

  /**
   * Конструктор класса.
   *
   * @param message Строка с сообщением об ошибке.
   */
  public UncheckedInterruptedException(String message) {
    super(message);
  }
}