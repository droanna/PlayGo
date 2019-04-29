import java.util.Scanner;

public class Game {


    private Scanner SCANNER = new Scanner(System.in);
    private GameFunctions gameFunctions = new GameFunctions();
    private Player firstPlayer;
    private Player secondPlayer;


    public void game(Player player, PlayerOne playerOne, PlayerTwo playerTwo, String[][] boardTwoMovesBefore, String[][] boardOneMoveBefore, String[][] currentBoard, String[][] board) {
        player = player.firstPlayer(playerOne, playerTwo);
        firstPlayer = player;
        System.out.println("Grę rozpoczyna: " + firstPlayer.getName() + " - czarne kamienie");

        secondPlayer(player, playerOne, playerTwo);
        String point;
        String pointOneMoveBefore = "0";
        String pointTwoMovesBefore = "0";

        System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
        point = SCANNER.next();

        do {
            if (!gameFunctions.impossibleMoves(player, point, currentBoard, board)) {

                currentBoard = gameFunctions.currentBoard(currentBoard, player, point);
                currentBoard = gameFunctions.boardAfterTakingPrisoners(currentBoard, board, player);

            } else {
                if (!gameFunctions.conditionalMove(currentBoard, player, point, board)) {
                    do {
                        System.out.println("Niedozwolony ruch - wybierz inne miejsce");
                        System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
                        point = SCANNER.next();
                    } while (gameFunctions.impossibleMoves(player, point, currentBoard, board));
                    currentBoard = gameFunctions.currentBoard(currentBoard, player, point);
                    currentBoard = gameFunctions.boardAfterTakingPrisoners(currentBoard, board, player);
                }
            }

            if (pointTwoMovesBefore.equals("0") || !gameFunctions.ko(boardTwoMovesBefore, currentBoard, point)) {
                gameFunctions.printBoard(currentBoard);
                pointTwoMovesBefore = pointOneMoveBefore;
                pointOneMoveBefore = point;
                if (!pointOneMoveBefore.equals("0")) {
                    boardOneMoveBefore = gameFunctions.boardOneMoveBefore(boardOneMoveBefore, player, pointOneMoveBefore);
                }
            } else {

                for (int i = 0; i < currentBoard.length; i++) {
                    for (int j = 0; j < currentBoard.length; j++) {
                        currentBoard[i][j] = boardOneMoveBefore[i][j];
                    }
                }
                do {
                    System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
                    point = SCANNER.next();
                }
                while (gameFunctions.impossibleMoves(player, point, currentBoard, board) && !gameFunctions.ko(boardTwoMovesBefore, currentBoard, point));
                currentBoard = gameFunctions.currentBoard(currentBoard, player, point);
                currentBoard = gameFunctions.boardAfterTakingPrisoners(currentBoard, board, player);
                gameFunctions.printBoard(currentBoard);
                pointTwoMovesBefore = pointOneMoveBefore;
                pointOneMoveBefore = point;

                if (!pointOneMoveBefore.equals("0")) {
                    boardOneMoveBefore = gameFunctions.boardOneMoveBefore(boardOneMoveBefore, player, pointOneMoveBefore);
                    currentBoard = gameFunctions.boardAfterTakingPrisoners(boardOneMoveBefore, board, player);
                }
            }
            if (player.equals(playerOne)) {
                player = playerTwo;
            } else {
                player = playerOne;
            }
            System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
            point = SCANNER.next();

            if (point.equalsIgnoreCase("pass")) {
                if (player.equals(playerOne)) {
                    player = playerTwo;
                } else {
                    player = playerOne;
                }
                System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
                point = SCANNER.next();
            }
        } while (!point.equalsIgnoreCase("pass"));


        System.out.println("Proszę podać miejsca martwych kamieni (np. 1,2,5), jeżeli nie ma martwych kamieni prosze wpisać 0");
        String deadRocks = SCANNER.next();
        gameFunctions.addPointsForDeadRocks(playerOne, playerTwo, deadRocks, board, currentBoard);
        gameFunctions.removeDeadRocks(currentBoard, board, deadRocks);
        gameFunctions.printBoard(currentBoard);

        result(firstPlayer, secondPlayer, currentBoard, board);
    }

    private void secondPlayer(Player player, PlayerOne playerOne, PlayerTwo playerTwo) {
        if (player.getId().equals("P01")) {
            secondPlayer = playerTwo;
        } else {
            secondPlayer = playerOne;
        }
    }

    private void result(Player firstPlayer, Player secondPlayer, String[][] currentBoard, String[][] board) {
        double komi = 6.5;
        double firstPlayerResult = 0;
        double secondPlayerResult = 0;
        int firstPlayerTerritory = gameFunctions.territory(gameFunctions.placesWithoutSignsAfterEnd(currentBoard, board), currentBoard, firstPlayer);
        int secondPlayerTerritory = gameFunctions.territory(gameFunctions.placesWithoutSignsAfterEnd(currentBoard, board), currentBoard, secondPlayer);
        if (firstPlayerTerritory > secondPlayer.getPoints()) {
            firstPlayerResult = firstPlayerTerritory - secondPlayer.getPoints();
        }
        if (secondPlayerTerritory > firstPlayer.getPoints()) {
            secondPlayerResult = secondPlayerTerritory + komi - firstPlayer.getPoints();
        } else {
            secondPlayerResult = komi;
        }
        System.out.println("Gracz: " + firstPlayer.getName() + ", ilość punktów: " + firstPlayerResult);
        System.out.println("Gracz: " + secondPlayer.getName() + ", ilość punktów: " + secondPlayerResult);
        if (secondPlayerResult > firstPlayerResult) {
            System.out.println("Wygrywa: " + secondPlayer.getName());
        } else if (firstPlayerResult > secondPlayerResult) {
            System.out.println("Wygrywa: " + firstPlayer.getName());
        } else {
            System.out.println("jigo!");
        }
    }
}
