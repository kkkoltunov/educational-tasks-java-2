package ru.hse;

/**
 * Класс определяющий крупье.
 */
public class Croupier extends Thread {

  /**
   * Переменная, которая идентифицирует состояние игры (продолжается или закончена).
   */
  private static boolean stop = false;

  /**
   * Get для установки информации о состоянии игры по умолчанию.
   */
  public static void setStopFalse() {
    Croupier.stop = false;
  }

  /**
   * Get для получения информации о состоянии игры.
   *
   * @return Состояние игры типа boolean.
   */
  public static boolean getStop() {
    return !stop;
  }

  /**
   * Запуск потока и усыпление его на 10 секунд.
   */
  @Override
  public void run() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedInterruptedException("Thread was interrupt");
    }
    stop = true;
  }
}
