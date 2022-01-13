package ru.hse;

import java.io.PrintStream;

/**
 * Класс описывающий игрока.
 */
public class Player extends Thread {

  /**
   * Имя игрока.
   */
  private final String name;

  /**
   * Вывод в поток.
   */
  private final PrintStream printStream;

  /**
   * Число очков у игрока.
   */
  private int score;

  /**
   * Конструктор класса.
   *
   * @param name        Имя игрока.
   * @param printStream Объект потока для вывода.
   */
  public Player(String name, PrintStream printStream) {
    this.name = name;
    this.printStream = printStream;
    score = 0;
  }

  /**
   * Get для получения имени игрока.
   *
   * @return Строка с именем.
   */
  public String getNamePlayer() {
    return name;
  }

  /**
   * Get для получения счета игрока.
   *
   * @return Число очков типа int.
   */
  public int getScore() {
    return score;
  }

  /**
   * Метод для того, чтобы украсть очки.
   *
   * @param stealScore Количество очков, которые хотят украсть.
   * @return Число очков типа int.
   */
  public int stealCard(int stealScore) {
    synchronized (Game.synchronizer) {
      if (stealScore > score) {
        int newScore = score;
        score = 0;
        return newScore;
      }
      score -= stealScore;
      return stealScore;
    }
  }

  /**
   * Запуск потока и его работа до поднятия флага в классе крупье.
   */
  @Override
  public void run() {
    while (Croupier.getStop()) {
      try {
        selectCard();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UncheckedInterruptedException("Thread was interrupt!");
      }
    }
    printStream.println(this + " and stop!");
  }

  /**
   * Выбор карты из колоды.
   *
   * @throws InterruptedException Прерывание потока во время исполнения.
   */
  private void selectCard() throws InterruptedException {
    int timeSleep;
    synchronized (Game.synchronizer) {
      int card = Game.getCardsDeck().getCard();
      score += card;
      timeSleep = 100 + (int) (Math.random() * 101);
      if (Game.IS_OUTPUT) {
        printStream.println(
            name + " get card with score " + card + " and sleep " + timeSleep
                + " milliseconds.");
      }
    }
    sleep(timeSleep);
  }

  /**
   * Информация об объекте.
   *
   * @return Строка с информацией об объекте.
   */
  @Override
  public String toString() {
    return name + " scored " + score + " points";
  }
}
