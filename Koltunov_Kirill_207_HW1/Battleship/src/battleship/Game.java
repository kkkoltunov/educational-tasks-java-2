package battleship;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Класс, который описывает игру.
 */
public class Game {

  /**
   * Количество строк на поле.
   */
  private final int COUNT_LINES;

  /**
   * Количество столбцов на поле.
   */
  private final int COUNT_COLUMNS;

  /**
   * Сканер, необходим для считывания данных из консоли.
   */
  private static Scanner scanner;

  /**
   * Объект поля, на котором происходит игра.
   */
  private final BattleField BATTLEFIELD;

  /**
   * Массив с данными о кораблях в порядке убывания размерности кораблей.
   */
  private final int[] SHIPS_ON_BATTLEFIELD;

  /**
   * Массив ссылок на корабли, которые находятся на поле.
   */
  private final Ship[] SET_SHIPS_ON_BATTLEFIELD;

  /**
   * Счетчик количества выстрелов.
   */
  private int countShots;

  /**
   * Счетчик количества выстрелов торпедами.
   */
  private int countTorpedoShots;

  /**
   * Счетчик оставшегося количества торпед.
   */
  private int countTorpedo;

  /**
   * Счетчик количества кораблей для заполнения массива.
   */
  private int counterShips = 0;

  /**
   * Конструктор класса.
   *
   * @param countLines          Количество строк на поле.
   * @param countColumns        Количество столбцов на поле.
   * @param countTorpedo        Количество торпед.
   * @param allShipsOnGameField Массив с данными о кораблях в порядке убывания размерности
   *                            кораблей.
   */
  public Game(int countLines, int countColumns, int countTorpedo, int[] allShipsOnGameField) {
    this.COUNT_LINES = countLines;
    this.COUNT_COLUMNS = countColumns;
    this.countTorpedo = countTorpedo;
    this.SHIPS_ON_BATTLEFIELD = allShipsOnGameField;
    BATTLEFIELD = new BattleField(countLines, countColumns);

    int countShips = 0;
    for (int countShip : allShipsOnGameField) {
      countShips += countShip;
    }

    SET_SHIPS_ON_BATTLEFIELD = new Ship[countShips];
    countShots = 0;
  }

  /**
   * Расстановка кораблей на поле.
   *
   * @return false в случае невозможности расстановки кораблей, иначе true.
   */
  public boolean shipsArrangement() {
    for (int i = 0; i < SHIPS_ON_BATTLEFIELD.length; ++i) {
      if (SHIPS_ON_BATTLEFIELD[i] != 0) {
        if (!generateShip(SHIPS_ON_BATTLEFIELD[i], SHIPS_ON_BATTLEFIELD.length - i)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Генерация нового корабля на поле.
   *
   * @param countShips Количество кораблей данного экземпляра.
   * @param sizeShip   Размер корабля
   * @return false в случае невозможности расстановки кораблей, иначе true.
   */
  private boolean generateShip(int countShips, int sizeShip) {
    for (int i = 0; i < countShips; ++i) {
      int[] layoutAndPosition = generatePosition(sizeShip);
      int countRegenerations = 0;

      int regeneratePosition = 250;
      while (!checkGeneratePositionForShip(layoutAndPosition[0], layoutAndPosition[1],
          layoutAndPosition[2],
          sizeShip) && countRegenerations < regeneratePosition) {
        layoutAndPosition = generatePosition(sizeShip);
        ++countRegenerations;
      }

      if (countRegenerations == 250) {
        return false;
      }

      Ship ship = selectShipBySize(layoutAndPosition, sizeShip);
      SET_SHIPS_ON_BATTLEFIELD[counterShips++] = ship;
      setShipOnBattleField(layoutAndPosition[1], layoutAndPosition[2], ship);
    }
    return true;
  }

  /**
   * Создание объекта корабля по координатам и размеру.
   *
   * @param layoutAndPosition Расположения корабля на поле (горизонтально или вертикально).
   * @param sizeShip          Размер корабля.
   * @return Ссылка на корабль.
   */
  private Ship selectShipBySize(int[] layoutAndPosition, int sizeShip) {
    Ship ship = null;

    if (sizeShip == 1) {
      ship = new Submarine(layoutAndPosition[0], layoutAndPosition[1], layoutAndPosition[2]);
    } else if (sizeShip == 2) {
      ship = new Destroyer(layoutAndPosition[0], layoutAndPosition[1], layoutAndPosition[2]);
    } else if (sizeShip == 3) {
      ship = new Cruiser(layoutAndPosition[0], layoutAndPosition[1], layoutAndPosition[2]);
    } else if (sizeShip == 4) {
      ship = new Battleship(layoutAndPosition[0], layoutAndPosition[1], layoutAndPosition[2]);
    } else if (sizeShip == 5) {
      ship = new Carrier(layoutAndPosition[0], layoutAndPosition[1], layoutAndPosition[2]);
    }

    return ship;
  }

  /**
   * Генерация координат для корабля.
   *
   * @param sizeShip Размер корабля.
   * @return Массив типа int, состоящий из расположения корабля на поле, координаты x, координаты y.
   */
  private int[] generatePosition(int sizeShip) {
    Random random = new Random();
    int[] layoutAndPosition = new int[3];

    layoutAndPosition[0] = random.nextInt(2);
    if (layoutAndPosition[0] == 0) { // HORIZONTAL
      if (sizeShip <= COUNT_COLUMNS) {
        layoutAndPosition[1] = random.nextInt(COUNT_LINES);
        layoutAndPosition[2] = random.nextInt(COUNT_COLUMNS - sizeShip + 1);
      } else {
        layoutAndPosition[0] = -1;
      }
    } else { // VERTICAL
      if (sizeShip <= COUNT_LINES) {
        layoutAndPosition[1] = random.nextInt(COUNT_LINES - sizeShip + 1);
        layoutAndPosition[2] = random.nextInt(COUNT_COLUMNS);
      } else {
        layoutAndPosition[0] = -1;
      }
    }
    return layoutAndPosition;
  }

  /**
   * Проверка сгенерированной позиции для корабля.
   *
   * @param layout Расположение на поле.
   * @param x      Координата х.
   * @param y      Координата у.
   * @param size   Размер корабля.
   * @return false в случае невозможности расстановки кораблей, иначе true.
   */
  private boolean checkGeneratePositionForShip(int layout, int x, int y, int size) {
    if (layout == 0) {
      return checkHorizontalPositionForShip(x, y, size);
    } else if (layout == 1) {
      return checkVerticalPositionForShip(x, y, size);
    }
    return false;
  }

  /**
   * Проверка сгенерированной позиции для корабля в горизонтальном расположении.
   *
   * @param x    Координата х.
   * @param y    Координата у.
   * @param size Размер корабля.
   * @return false в случае невозможности расстановки кораблей, иначе true.
   */
  private boolean checkHorizontalPositionForShip(int x, int y, int size) {
    for (int i = -1; i <= size; ++i) {
      int currentColumn = y + i;
      if ((currentColumn < COUNT_COLUMNS) && (currentColumn >= 0)) {
        for (int j = -1; j < 2; ++j) {
          int currentLine = x + j;
          if ((currentLine < COUNT_LINES) && (currentLine >= 0)) {
            if (BATTLEFIELD.isShipHere(currentLine, currentColumn)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Проверка сгенерированной позиции для корабля в горизонтальном расположении.
   *
   * @param x    Координата х.
   * @param y    Координата у.
   * @param size Размер корабля.
   * @return false в случае невозможности расстановки кораблей, иначе true.
   */
  private boolean checkVerticalPositionForShip(int x, int y, int size) {
    for (int i = -1; i <= size; ++i) {
      int currentLine = x + i;
      if ((currentLine < COUNT_LINES) && (currentLine >= 0)) {
        for (int j = -1; j < 2; ++j) {
          int currentColumn = y + j;
          if ((currentColumn < COUNT_COLUMNS) && (currentColumn >= 0)) {
            if (BATTLEFIELD.isShipHere(currentLine, currentColumn)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Начало игры (цикл до поражения всех кораблей).
   */
  public void startGame() {
    String separator = new String(new char[25]).replace('\0', '✅');
    // Раскомментируйте для проверки расстановки кораблей.
    // BATTLEFIELD.printShips();

    System.out.println(
        "\n" + separator + "\n" + "Поле успешно сгенерировано, начинаем!" + "\n" + separator
            + "\n");

    while (!isGameFinished()) {
      BATTLEFIELD.printField();
      boolean isTorpedoShoot = false;
      if (countTorpedo > 0) {
        isTorpedoShoot = readTorpedoesShoot(
            "✏✏✏ Чтобы произвести выстрел торпедой введите \"YES\", иначе что угодно"
                + " (\"EXIT\" - чтобы закончить игру): ");
      }
      if (!isTorpedoShoot) {
        System.out.println(
            "\n✅✅✅ Будет произведен обычный выстрел, количество торпед: " + countTorpedo
                + "! ✅✅✅\n");
      }

      int x = readIntNumber("✏✏✏ Введите номер строки, по которой желаете произвести выстрел: ");
      int y = readIntNumber("✏✏✏ Введите номер столбца, по которому желаете произвести выстрел: ");
      makeShoot(x - 1, y - 1, isTorpedoShoot);
    }
    BATTLEFIELD.printField();
    System.out.println(
        "\n✅✅✅ Количество совершенных обычных выстрелов: " + (countShots - countTorpedoShots)
            + " ✅✅✅\n");
    System.out.println(
        "✅✅✅ Количество совершенных торпедами выстрелов: " + countTorpedoShots + " ✅✅✅\n");
  }

  /**
   * Выстрел по кораблю.
   *
   * @param x         Координата х.
   * @param y         Координата у.
   * @param isTorpedo Выстрел торпедой.
   */
  public void makeShoot(int x, int y, boolean isTorpedo) {
    if (x < 0 || x >= COUNT_LINES || y < 0 || y >= COUNT_COLUMNS) {
      System.out.println("\n✗✗✗ Вы вышли за границу поля! ✗✗✗\n");
      return;
    }
    if (BATTLEFIELD.getCellValue(x, y) != CellValue.NOT_CHECK) {
      System.out.println("\n✗✗✗ По данной ячейке уже был произведен выстрел! ✗✗✗\n");
      return;
    }

    ++countShots;
    if (BATTLEFIELD.isShipHere(x, y)) {
      Ship ship = BATTLEFIELD.getShip(x, y);

      if (isTorpedo) {
        ship.shootTorpedo();
        --countTorpedo;
        ++countTorpedoShots;
      }
      if (ship.shoot()) {
        System.out.println("\n✅✅✅ Потопил " + ship + "! ✅✅✅\n");
        BATTLEFIELD.sankShip(ship);
      } else {
        System.out.println("\n✅✅✅ Ранил! ✅✅✅\n");
        BATTLEFIELD.changeValueCell(x, y, CellValue.CHECK_HIT);
      }
    } else {
      if (isTorpedo) {
        --countTorpedo;
        ++countTorpedoShots;
      }
      System.out.println("\n✗✗✗ Мимо, давай дальше! ✗✗✗\n");
      BATTLEFIELD.changeValueCell(x, y, CellValue.CHECK_MISS);
    }
  }

  /**
   * Проверка на конец игры (выход в случае, если все корабли уничтожены).
   *
   * @return false, если корабли еще остались на поле, иначе true.
   */
  boolean isGameFinished() {
    for (int i = 0; i < counterShips; ++i) {
      if (!SET_SHIPS_ON_BATTLEFIELD[i].isSank()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Установка корабля на поле (отметка в ячейках).
   *
   * @param x    Координата х.
   * @param y    Координата у.
   * @param ship Ссылка на корабль.
   */
  private void setShipOnBattleField(int x, int y, Ship ship) {
    BATTLEFIELD.setShipInCell(x, y, ship);
  }

  /**
   * Установка сканера для считывания данных с консоли.
   *
   * @param inputScanner Ссылка на объект Scanner.
   */
  public static void setScanner(Scanner inputScanner) {
    scanner = inputScanner;
  }

  /**
   * Чтение int из консоли.
   *
   * @param output Информация, которая выводится перед считыванием.
   * @return Считанное число типа int.
   */
  public static int readIntNumber(String output) {
    System.out.print(output);
    int input = 0;

    while (scanner.hasNext()) {
      if (scanner.hasNextInt()) {
        input = scanner.nextInt();
        break;
      }
      scanner.next();
      System.out.println("✗✗✗ Данные введены некорректно! Попробуйте еще раз. ✗✗✗");
      System.out.print(output);
    }

    return input;
  }

  /**
   * Считывание информации о выстреле торпедой или досрочном завершении работы программы.
   *
   * @param output Информация, которая выводится перед считыванием.
   * @return true, если будет произведен выстрел торпедой, false иначе.
   */
  public static boolean readTorpedoesShoot(String output) {
    System.out.print(output);
    String input = scanner.next();

    if (input.toLowerCase(Locale.ROOT).equals("exit")) {
      printInfoAboutLose();
      System.exit(0);
    }

    return input.toLowerCase(Locale.ROOT).equals("yes");
  }

  /**
   * Вывод информации о проигрыше (досрочном завершении работы программы).
   */
  private static void printInfoAboutLose() {
    System.out.println("\n██╗░░░░░░█████╗░░██████╗███████╗");
    System.out.println("██║░░░░░██╔══██╗██╔════╝██╔════╝");
    System.out.println("██║░░░░░██║░░██║╚█████╗░█████╗░░");
    System.out.println("██║░░░░░██║░░██║░╚═══██╗██╔══╝░░");
    System.out.println("███████╗╚█████╔╝██████╔╝███████╗");
    System.out.println("╚══════╝░╚════╝░╚═════╝░╚══════╝");
  }
}
