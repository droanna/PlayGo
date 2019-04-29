import java.util.Scanner;

public class Main {

    private static PlayerOne playerOne;
    private static PlayerTwo playerTwo;
    private static Game game = new Game();
    private static GameFunctions gameFunctions = new GameFunctions();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Player player = new Player() {};


    public static void main(String[] args) {
        System.out.println("Podaj wielekość planszy: 19 - dla 19x19, 13 dla 13x13 lub 9 dla 9x9");

        int size = SCANNER.nextInt();

        System.out.println("Gracz 1: Podaj imię:");
        String playerOneName = SCANNER.next();
        System.out.println();

        System.out.println("Gracz 2: Podaj imię:");
        String playerTwoName = SCANNER.next();

        String[][] board = gameFunctions.board(size - 1);
        String[][] currentBoard = gameFunctions.board(size - 1);
        String[][] boardTwoMovesBefore = gameFunctions.board(size - 1);
        String[][] boardOneMoveBefore = gameFunctions.board(size - 1);
        System.out.println("Plansza:");
        gameFunctions.printBoard(board);
        System.out.println();


        playerOne = new PlayerOne("P01", null);
        playerOne.setName(playerOneName);
        playerTwo = new PlayerTwo("P02", null);
        playerTwo.setName(playerTwoName);
        game.game(player, playerOne, playerTwo, boardTwoMovesBefore, boardOneMoveBefore, currentBoard, board);


    }
}
