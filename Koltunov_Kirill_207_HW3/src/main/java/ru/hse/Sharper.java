package ru.hse;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс описывающий шулера.
 */
public class Sharper extends Thread {

  /**
   * Имя шулера.
   */
  private final String name;

  /**
   * Вывод в поток.
   */
  private final PrintStream printStream;

  /**
   * Число очков у шулера.
   */
  private int score;

  /**
   * Конструктор класса.
   *
   * @param name        Имя шулера.
   * @param printStream Объект потока для вывода.
   */
  public Sharper(String name, PrintStream printStream) {
    this.name = name;
    this.printStream = printStream;
    score = 0;
  }

  /**
   * Get для получения имени шулера.
   *
   * @return Строка с именем.
   */
  public String getNameSharper() {
    return name;
  }

  /**
   * Get для получения счета шулера.
   *
   * @return Число очков типа int.
   */
  public int getScore() {
    return score;
  }

  /**
   * Запуск потока и его работа до поднятия флага в классе крупье.
   */
  @Override
  public void run() {
    AtomicBoolean firstRun = new AtomicBoolean(true);

    while (Croupier.getStop()) {
      try {
        if (Math.random() <= 0.4 && !firstRun.get()) {
          stealCard();
        } else {
          firstRun.set(false);
          selectCard();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UncheckedInterruptedException("Thread was interrupt!");
      }
    }
    printStream.println(this + " and stop!");
  }

  /**
   * Метод для того, чтобы украсть очки у игрока.
   *
   * @throws InterruptedException Прерывание потока во время исполнения.
   */
  private void stealCard() throws InterruptedException {
    int timeSleep;
    synchronized (Game.synchronizer) {
      int indexStillPlayer = (int) (Math.random() * Game.getPlayerList().size());
      int stealScore = Game.getPlayerList().get(indexStillPlayer).stealCard((int) (Math.random() * 9));
      score += stealScore;
      timeSleep = 180 + (int) (Math.random() * 121);
      if (Game.IS_OUTPUT) {
        printStream.println(name + " stealing " + stealScore + " score from " + Game.getPlayerList().get(
            indexStillPlayer).getNamePlayer() + " and sleep " + timeSleep
            + " milliseconds.");
      }
    }
    sleep(timeSleep);
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
