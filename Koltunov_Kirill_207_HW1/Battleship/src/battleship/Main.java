package battleship;

import java.util.Scanner;

/**
 * Класс, в котором описаны основные методы для запуска игры, то есть получение данных о ее
 * параметрах от пользователя.
 * <p>
 * Применен Google Code Style: https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml
 */
public class Main {

  /**
   * Точка входа в программу.
   *
   * @param args Параметры игры. 1 параметр - ширина поля, 2 параметр - длина поля, 3 параметр -
   *             количество торпед, 4 параметр - количество кораблей типа Carrier, 5 параметр -
   *             количество кораблей типа Battleship, 6 параметр - количество кораблей типа Cruiser,
   *             7 параметр - количество кораблей типа Destroyer, 8 параметр - количество кораблей
   *             типа Submarine.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Game.setScanner(scanner);
    System.out.println("✅✅✅ Приветствую в игре \"Морской бой\"! ✅✅✅\n");
    setSettingsForGame(args);
  }

  /**
   * Настройка параметров игры. Формируется два массива с данными, в первом - данные о поле, во
   * втором данные о кораблях.
   *
   * @param args Параметры игры. 1 параметр - ширина поля, 2 параметр - длина поля, 3 параметр -
   *             количество торпед, 4 параметр - количество кораблей типа Carrier, 5 параметр -
   *             количество кораблей типа Battleship, 6 параметр - количество кораблей типа Cruiser,
   *             7 параметр - количество кораблей типа Destroyer, 8 параметр - количество кораблей
   *             типа Submarine.
   */
  private static void setSettingsForGame(String[] args) {
    int[] dataBattleField;
    int[] countShips;

    if (checkArgs(args)) {
      dataBattleField = new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1]),
          Integer.parseInt(args[2])};
      countShips = new int[]{Integer.parseInt(args[3]), Integer.parseInt(args[4]),
          Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7])};
    } else {
      dataBattleField = inputDataBattleField();
      System.out.println("!!Внимание максимальное количество кораблей оценено \"снизу\", " +
          "не стоит предавать большого значения этому показателю!!");
      if (dataBattleField[0] * dataBattleField[1] <= 30) {
        errorMessage1();
      }

      countShips = inputDataShips(dataBattleField[0], dataBattleField[1]);
    }

    Game game = new Game(dataBattleField[0], dataBattleField[1], dataBattleField[2], countShips);
    if (!game.shipsArrangement()) {
      System.out.println("\n✗✗✗ Не удалось сгенерировать поле с данными параметрами! ✗✗✗\n");
    } else {
      try {
        game.startGame();
        printInfoAboutWin();
      } catch (Exception exception) {
        System.out.println("Во время работы программы произошла ошибка: " + exception.getMessage());
      }
    }
  }

  /**
   * Проверка параметров игры, полученных через консоль.
   *
   * @param args Параметры игры. 1 параметр - ширина поля, 2 параметр - длина поля, 3 параметр -
   *             количество торпед, 4 параметр - количество кораблей типа Carrier, 5 параметр -
   *             количество кораблей типа Battleship, 6 параметр - количество кораблей типа Cruiser,
   *             7 параметр - количество кораблей типа Destroyer, 8 параметр - количество кораблей
   *             типа Submarine.
   * @return Значение true или false в зависимости от успешности проверки.
   */
  static boolean checkArgs(String[] args) {
    if (args.length != 8) {
      return false;
    }

    try {
      if (Integer.parseInt(args[0]) <= 0 || Integer.parseInt(args[0]) > 50) {
        return false;
      }

      if (Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > 50) {
        return false;
      }

      if (Integer.parseInt(args[2]) < 0
          || Integer.parseInt(args[2]) > Integer.parseInt(args[0]) * Integer.parseInt(args[1])) {
        return false;
      }

      for (int i = 3; i < args.length; ++i) {
        if (Integer.parseInt(args[i]) < 0 && Integer.parseInt(args[i]) > 1000) {
          return false;
        }
      }
    } catch (Exception exception) {
      return false;
    }

    return true;
  }

  /**
   * Ввод данных об игровом поле. Формируется массив из двух элементов, первый - ширина поля, второй
   * - длина.
   *
   * @return Массив с данными о размерностях поля и количества торпед.
   */
  private static int[] inputDataBattleField() {
    int[] inputData = new int[3];
    System.out.println("Для начала необходимо задать длину и ширину поля.");

    do {
      inputData[0] = Game.readIntNumber("✏✏✏ Введите ширину поля [от 1 до 50]: ");
    } while (inputData[0] < 1 || inputData[0] > 50);

    do {
      inputData[1] = Game.readIntNumber("✏✏✏ Введите длину поля [от 1 до 50]: ");
    } while (inputData[1] < 1 || inputData[1] > 50);

    do {
      inputData[2] = Game.readIntNumber("✏✏✏ Введите количество торпед [от 0 до " +
          inputData[0] * inputData[1] + "]: ");
    } while (inputData[2] < 0 || inputData[2] > inputData[0] * inputData[1]);

    return inputData;
  }

  /**
   * Ввод данных о кораблях. Данные о количестве строк и столбцов необходимы для установки
   * параметров ввода.
   *
   * @param countLines   Число строк поля.
   * @param countColumns Число столбцов поля.
   * @return Массив с данными о кораблях в порядке убывания размерности кораблей.
   */
  private static int[] inputDataShips(int countLines, int countColumns) {
    int[] inputData = new int[5];
    int countRemainderCells = countLines * countColumns;

    System.out.println("\n✏✏✏ Необходимо ввести информацию о кораблях. ✏✏✏\n");

    do {
      if (countRemainderCells >= 5 && (countLines >= 5 || countColumns >= 5)) {
        inputData[0] = inputDataShip(countRemainderCells, "авианосцев (пятипалубный корабль)", 6);
      }
      countRemainderCells -= inputData[0] * 6;
      if (countRemainderCells >= 4 && (countLines >= 4 || countColumns >= 4)) {
        inputData[1] = inputDataShip(countRemainderCells, "линкоров (четырехпалубный корабль)", 5);
      }
      countRemainderCells -= inputData[1] * 5;
      if (countRemainderCells >= 3 && (countLines >= 3 || countColumns >= 3)) {
        inputData[2] = inputDataShip(countRemainderCells, "крейсеров (трехпалубный корабль)", 4);
      }
      countRemainderCells -= inputData[2] * 4;
      if (countRemainderCells >= 2 && (countLines >= 2 || countColumns >= 2)) {
        inputData[3] = inputDataShip(countRemainderCells, "эсминцев (двухпалубный корабль)", 3);
      }
      countRemainderCells -= inputData[3] * 3;
      if (countRemainderCells >= 1 && (countLines >= 1 || countColumns >= 1)) {
        inputData[4] = inputDataShip(countRemainderCells, "субмарин (однопалубный корабль)", 2);
      }
      countRemainderCells -= inputData[4] * 2;
    } while (countRemainderCells == countLines * countColumns);
    return inputData;
  }

  /**
   * Ввод данных о конкретном корабле.
   *
   * @param countRemainderCells Оставшиеся ячейки для размещения кораблей.
   * @param shipName            Название кораблей.
   * @param shipMinVolume       Минимальное количество ячеек занимаемых кораблем.
   * @return Количество кораблей для расстановки.
   */
  private static int inputDataShip(int countRemainderCells, String shipName, int shipMinVolume) {
    int countShips;

    if (countRemainderCells == shipMinVolume - 1) {
      countRemainderCells = shipMinVolume = 1;
    }

    do {
      countShips = Game.readIntNumber("✏✏✏ Введите количество " + shipName +
          " [от 0 до ~" + countRemainderCells / shipMinVolume + "]: ");
    } while (countShips < 0 || countShips > countRemainderCells / shipMinVolume && countShips != 1);

    return countShips;
  }

  /**
   * Сообщение об ошибке.
   */
  private static void errorMessage1() {
    System.out.println("\n✗✗✗ P.S. Ввод данных о некоторых кораблях будет запрещен. " +
        "Они попросту не поместятся на нашем поле! ✗✗✗");
  }

  /**
   * Сообщение о выигрыше.
   */
  private static void printInfoAboutWin() {
    System.out.println("░██╗░░░░░░░██╗██╗███╗░░██╗");
    System.out.println("░██║░░██╗░░██║██║████╗░██║");
    System.out.println("░╚██╗████╗██╔╝██║██╔██╗██║");
    System.out.println("░░████╔═████║░██║██║╚████║");
    System.out.println("░░╚██╔╝░╚██╔╝░██║██║░╚███║");
    System.out.println("░░░╚═╝░░░╚═╝░░╚═╝╚═╝░░╚══╝");
  }
}
