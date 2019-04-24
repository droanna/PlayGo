import java.util.Scanner;

public class Game {


    private Scanner SCANNER = new Scanner(System.in);
    private PrintBoard printBoard = new PrintBoard();
    private Player firstPlayer;
    private Player secondPlayer;


    public void game(Player player, PlayerOne playerOne, PlayerTwo playerTwo, String[][] boardTwoMovesBefore, String[][] boardOneMoveBefore, String[][] currentBoard, String[][] board) {
        player = player.firstPlayer(playerOne, playerTwo);
        firstPlayer = player;
        System.out.println("Grę rozpoczyna: " + firstPlayer.getName() + " - czarne kamienie");

        if (player.getId().equals("P01")) {
            secondPlayer = playerTwo;
        } else {
            secondPlayer = playerOne;
        }
        String point;
        String pointOneMoveBefore = "0";
        String pointTwoMovesBefore = "0";

        System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
        point = SCANNER.next();

        do {
            if (!printBoard.impossibleMoves(player, point, currentBoard, board)) {

                currentBoard = printBoard.currentBoard(currentBoard, player, point);
                currentBoard = printBoard.boardAfterTakingPrisoners(currentBoard, board, player);

            } else {
                Integer pointsBeforeMove = player.getPoints();
                String[][] boardAfterMove = printBoard.currentBoard(currentBoard, player, point);
                currentBoard = printBoard.boardAfterTakingPrisoners(currentBoard, board, player);
                Integer pointsAfterMove = player.getPoints();
                if (pointsBeforeMove.equals(pointsAfterMove)) {
                    for (int i = 0; i < currentBoard.length; i++) {
                        for (int j = 0; j < currentBoard.length; j++) {
                            if (board[i][j].equals(String.valueOf(point))) {
                                currentBoard[i][j] = board[i][j];
                            }
                        }
                    }
                    do {
                        System.out.println("Niedozwolony ruch - wybierz inne miejsce");
                        System.out.println(player.getName() + ": Miejsce postawienia kamienia (aby zakończyć wpisz: pass): ");
                        point = SCANNER.next();
                    } while (printBoard.impossibleMoves(player, point, currentBoard, board));
                    currentBoard = printBoard.currentBoard(currentBoard, player, point);
                    currentBoard = printBoard.boardAfterTakingPrisoners(currentBoard, board, player);
                }
            }

            if (pointTwoMovesBefore.equals("0") || !printBoard.ko(boardTwoMovesBefore, currentBoard, point)) {
                printBoard.printBoard(currentBoard);
                pointTwoMovesBefore = pointOneMoveBefore;
                pointOneMoveBefore = point;
                if (!pointTwoMovesBefore.equals("0")) {
                    boardTwoMovesBefore = printBoard.boardTwoMovesBefore(boardTwoMovesBefore, player, pointTwoMovesBefore);
                }
                if (!pointOneMoveBefore.equals("0")) {
                    boardOneMoveBefore = printBoard.boardOneMoveBefore(boardOneMoveBefore, player, pointOneMoveBefore);
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
                while (printBoard.impossibleMoves(player, point, currentBoard, board) && !printBoard.ko(boardTwoMovesBefore, currentBoard, point));
                currentBoard = printBoard.currentBoard(currentBoard, player, point);
                currentBoard = printBoard.boardAfterTakingPrisoners(currentBoard, board, player);
                printBoard.printBoard(currentBoard);
                pointTwoMovesBefore = pointOneMoveBefore;
                pointOneMoveBefore = point;

                if (!pointTwoMovesBefore.equals("0")) {
                    boardTwoMovesBefore = printBoard.boardTwoMovesBefore(boardTwoMovesBefore, player, pointTwoMovesBefore);
                    currentBoard = printBoard.boardAfterTakingPrisoners(boardTwoMovesBefore, board, player);
                }
                if (!pointOneMoveBefore.equals("0")) {
                    boardOneMoveBefore = printBoard.boardOneMoveBefore(boardOneMoveBefore, player, pointOneMoveBefore);
                    currentBoard = printBoard.boardAfterTakingPrisoners(boardOneMoveBefore, board, player);
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
        printBoard.addPointsForDeadRocks(playerOne, playerTwo, deadRocks);
        printBoard.removeDeadRocks(currentBoard, board, deadRocks);
        printBoard.printBoard(currentBoard);

        result(firstPlayer, secondPlayer, currentBoard, board);
    }

    private void result(Player firstPlayer, Player secondPlayer, String[][] currentBoard, String[][] board) {
        double komi = 6.5;
        double firstPlayerResult = 0;
        double secondPlayerResult = 0;
        int firstPlayerTerritory = printBoard.territory(printBoard.placesWithoutSignsAfterEnd(currentBoard, board), currentBoard, firstPlayer);
        int secondPlayerTerritory = printBoard.territory(printBoard.placesWithoutSignsAfterEnd(currentBoard, board), currentBoard, secondPlayer);
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
