package ru.hse;

/**
 * Класс определяющий колоду карт.
 */
public class CardsDeck {

  /**
   * Счётчик карт выданных игрокам (необходим для проверки успешности синхронизации потоков).
   */
  private int counter = 0;

  /**
   * Получение случайной карты со значением от 1 до 10.
   *
   * @return Случайное число от 1 до 10 типа int.
   */
  public int getCard() {
    synchronized (Game.synchronizer) {
      int number = 1 + (int) (Math.random() * 10);
      counter += number;
      return number;
    }
  }

  /**
   * Get для получения количества выданных карт
   *
   * @return Сумма очков на всех выданных картах типа int.
   */
  public int getCounter() {
    return counter;
  }

  /**
   * Set для установки значения счетчика по умолчанию.
   */
  public void setCounterDefault() {
    counter = 0;
  }
}
