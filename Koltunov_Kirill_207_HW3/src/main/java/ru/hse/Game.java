package ru.hse;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс определяющий игру и настройки для нее.
 */
public class Game {

  /**
   * Переменная, которая отвечает за необходимость вывода информации в консоль во время игры. Если
   * необходимо НЕ выводить в консоль ход игры просто измените значение на false.
   */
  public static final boolean IS_OUTPUT = false;

  /**
   * Объект для синхронизации по всем потокам.
   */
  public static final Object synchronizer = new Object();

  /**
   * Колода карт.
   */
  private static final CardsDeck cardsDeck = new CardsDeck();

  /**
   * Вывод информации в поток.
   */
  public static final PrintStream printStream = new PrintStream(
      new FileOutputStream(FileDescriptor.out));

  /**
   * Список игроков.
   */
  private static List<Player> playerList = new ArrayList<>();

  /**
   * Список шулеров.
   */
  private static List<Sharper> sharperList = new ArrayList<>();

  /**
   * Сканер для ввода данных с консоли.
   */
  private static final Scanner scanner = new Scanner(System.in);

  /**
   * Метод для чтения данных типа int с консоли.
   *
   * @param output Строка, которая выводится перед вводом.
   * @return Число типа int.
   */
  public static int readIntNumber(String output) {
    printStream.print(output);
    AtomicInteger input = new AtomicInteger();

    while (scanner.hasNext()) {
      if (scanner.hasNextInt()) {
        input.set(scanner.nextInt());
        break;
      }
      scanner.next();
      printStream.println("Incorrect input! Try again.");
      printStream.print(output);
    }

    return input.get();
  }

  /**
   * Get для получения колоды карт.
   *
   * @return Ссылка на колоду.
   */
  static CardsDeck getCardsDeck() {
    return cardsDeck;
  }

  /**
   * Установка списка игроков.
   *
   * @param playerList Ссылка на новый список игроков.
   */
  static void setPlayerList(List<Player> playerList) {
    Game.playerList = playerList;
  }

  /**
   * Получение списка игроков.
   *
   * @return Ссылка на список игроков.
   */
  static List<Player> getPlayerList() {
    return playerList;
  }

  /**
   * Установка списка шулеров.
   *
   * @param sharperList Ссылка на список шулеров.
   */
  static void setSharperList(List<Sharper> sharperList) {
    Game.sharperList = sharperList;
  }

  /**
   * Точка входа в программу.
   */
  public static void main(String[] args) {
    // Получение данных.
    AtomicInteger countPlayers = new AtomicInteger();
    do {
      countPlayers.set(readIntNumber("Input number of honest players [from 1 to 10]: "));
    } while (countPlayers.get() < 1 || countPlayers.get() > 10);

    AtomicInteger countSharpers = new AtomicInteger();
    do {
      countSharpers.set(readIntNumber("Input number of sharpers [from 0 to 10]: "));
    } while (countSharpers.get() < 0 || countSharpers.get() > 10);

    // Создание игроков и шулеров.
    for (int i = 0; i < countPlayers.get(); ++i) {
      playerList.add(new Player((i + 1) + " simple player", printStream));
    }
    for (int i = 0; i < countSharpers.get(); ++i) {
      sharperList.add(new Sharper((i + 1) + " sharper", printStream));
    }

    printStream.println("Game start!");
    // Запуск потоков с игроками, шулерами и крупье.
    for (var player : playerList) {
      player.start();
    }
    for (var sharper : sharperList) {
      sharper.start();
    }
    new Croupier().start();

    // Синхронизация всех потоков.
    try {
      for (var player : playerList) {
        player.join();
      }
      for (var sharper : sharperList) {
        sharper.join();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UncheckedInterruptedException("Thread was interrupt!");
    }

    // Поиск победителя игры.
    AtomicInteger maxScorePlayer = new AtomicInteger();
    AtomicInteger maxScoreSharper = new AtomicInteger();

    AtomicInteger iterator = new AtomicInteger();
    for (var player : playerList) {
      if (player.getScore() > maxScorePlayer.get()) {
        maxScorePlayer.set(player.getScore());
      }
      iterator.incrementAndGet();
    }

    iterator.set(0);
    for (var sharper : sharperList) {
      if (sharper.getScore() > maxScoreSharper.get()) {
        maxScoreSharper.set(sharper.getScore());
      }
      iterator.incrementAndGet();
    }

    if (maxScorePlayer.get() >= maxScoreSharper.get()) {
      for (var player : playerList) {
        if (player.getScore() == maxScorePlayer.get()) {
          printStream.println(player.getNamePlayer() + " win!");
        }
      }
    } else {
      for (var sharper : sharperList) {
        if (sharper.getScore() == maxScoreSharper.get()) {
          printStream.println(sharper.getNameSharper() + " win!");
        }
      }
    }

    AtomicInteger scoreAllPlayers = new AtomicInteger();
    for (var player : playerList) {
      scoreAllPlayers.addAndGet(player.getScore());
    }
    for (var sharper : sharperList) {
      scoreAllPlayers.addAndGet(sharper.getScore());
    }

    // Подсчет карт выданных и карт, которые находятся на руках у игроков.
    printStream.println(
        "There are " + scoreAllPlayers + " score-cards in the hands of players and sharpers.");
    printStream.println("Total of " + cardsDeck.getCounter() + " cards were issued. ");
  }
}
