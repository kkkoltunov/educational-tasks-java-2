package ru.hse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameTest {

  @Test
  public void gameWinSimplePlayer() {
    List<Player> playerList = new ArrayList<>();
    List<Sharper> sharperList = new ArrayList<>();
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    playerList.add(new Player("1 simple player", printStream));
    playerList.add(new Player("2 simple player", printStream));
    playerList.add(new Player("3 simple player", printStream));
    sharperList.add(new Sharper("1 sharper", printStream));

    Game.setPlayerList(playerList);
    Game.setSharperList(sharperList);

    Game.getCardsDeck().setCounterDefault();
    for (var player : playerList) {
      player.start();
    }
    for (var sharper : sharperList) {
      sharper.start();
    }
    Croupier.setStopFalse();
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

    int scoreAllPlayers = 0;
    for (var player : playerList) {
      scoreAllPlayers += player.getScore();
    }
    for (var sharper : sharperList) {
      scoreAllPlayers += sharper.getScore();
    }

    assertEquals(scoreAllPlayers, Game.getCardsDeck().getCounter());
  }

  @Test
  public void gameWinSharper() {
    List<Player> playerList = new ArrayList<>();
    List<Sharper> sharperList = new ArrayList<>();
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    playerList.add(new Player("1 simple player", printStream));
    sharperList.add(new Sharper("1 sharper", printStream));
    sharperList.add(new Sharper("2 sharper", printStream));
    sharperList.add(new Sharper("3 sharper", printStream));

    Game.setPlayerList(playerList);
    Game.setSharperList(sharperList);

    Game.getCardsDeck().setCounterDefault();
    for (var player : playerList) {
      player.start();
    }
    for (var sharper : sharperList) {
      sharper.start();
    }
    Croupier.setStopFalse();
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

    int scoreAllPlayers = 0;
    for (var player : playerList) {
      scoreAllPlayers += player.getScore();
    }
    for (var sharper : sharperList) {
      scoreAllPlayers += sharper.getScore();
    }

    assertEquals(scoreAllPlayers, Game.getCardsDeck().getCounter());
  }

  @Test
  public void nameSharper() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Sharper sharper = new Sharper("First sharper", printStream);
    assertEquals("First sharper", sharper.getNameSharper());
  }

  @Test
  public void scoreSharper() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Sharper sharper = new Sharper("First sharper", printStream);
    assertEquals(0, sharper.getScore());
  }

  @Test
  public void stringSharper() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Sharper sharper = new Sharper("First sharper", printStream);
    assertEquals("First sharper scored 0 points", sharper.toString());
  }

  @Test
  public void namePlayer() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Player player = new Player("Second player", printStream);
    assertEquals("Second player", player.getNamePlayer());
  }

  @Test
  public void scorePlayer() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Player player = new Player("Second player", printStream);
    assertEquals(0, player.getScore());
  }

  @Test
  public void stringPlayer() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Player player = new Player("Second player", printStream);
    assertEquals("Second player scored 0 points", player.toString());
  }

  @Test
  public void stealScoreFirst() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Player player = new Player("Second player", printStream);
    assertEquals(0, player.stealCard(10));
  }

  @Test
  public void stealScoreSecond() {
    PrintStream printStream = new PrintStream(new FileOutputStream(FileDescriptor.out));
    Player player = new Player("Second player", printStream);
    assertEquals(0, player.stealCard(0));
  }

  @Test
  public void counterCards() {
    CardsDeck cardsDeck = new CardsDeck();
    assertEquals(0, cardsDeck.getCounter());
  }

  @Test
  public void decisionCroupier() {
    assertFalse(Croupier.getStop());
  }

  @Test
  public void exceptionThrow() {
    Croupier croupier = new Croupier();
    croupier.start();
    croupier.interrupt();
  }
}