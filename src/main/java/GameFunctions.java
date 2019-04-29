import java.util.*;

public class GameFunctions extends Board implements GameFunctionsInterface {


    public void printBoard(String[][] boardArray) {

        for (String[] s : boardArray) {
            for (String q : s) {
                System.out.print(q + '\t');
            }
            System.out.println();
        }
    }


    public String[][] boardTwoMovesBefore(String[][] board, Player player, String point) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (player.getId().equals("P01") && board[i][j].equals(point)) {
                    board[i][j] = "x";
                } else if (player.getId().equals("P02") && board[i][j].equals(point)) {
                    board[i][j] = "o";
                }
            }
        }
        return board;
    }

    public String[][] boardOneMoveBefore(String[][] board, Player player, String point) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (player.getId().equals("P01") && board[i][j].equals(point)) {
                    board[i][j] = "o";
                } else if (player.getId().equals("P02") && board[i][j].equals(point)) {
                    board[i][j] = "x";
                }
            }
        }
        return board;
    }


    public String[][] currentBoard(String[][] currentBoard, Player player, String point) {

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (player.getId().equals("P01") && currentBoard[i][j].equals(point)) {
                    currentBoard[i][j] = "o";
                } else if (player.getId().equals("P02") && currentBoard[i][j].equals(point)) {
                    currentBoard[i][j] = "x";
                }
            }
        }

        return currentBoard;
    }

    public boolean ko(String[][] boardTwoMovesBefore, String[][] currentBoard, String point) {
        if (!point.equalsIgnoreCase("pass")) {
            if (Arrays.deepEquals(boardTwoMovesBefore, currentBoard)) {
                System.out.println("Stan ko - wybierz inny punkt");
                return true;
            }
        }
        return false;
    }

    public boolean impossibleMoves(Player player, String point, String[][] currentBoard, String[][] board) {
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (board[i][j].equals(point)) {
                    //Sprawdzenie, czy w danym miejscu jest już jakikolwiek kamień
                    if (currentBoard[i][j].equals("o") || currentBoard[i][j].equals("x")) {
                        return true;
                    }

                    //Sprawdzenie, czy dołożenie kamienia nie spowoduje, że kamienie gracza zostaną zakładnikami
                    String sign = getSign(player, "o", "x");
                    currentBoard = currentBoard(currentBoard, player, point);
                    List<String> placesOfSigns = placesOfSigns(currentBoard, board, sign);
                    List<String> neighbouringPoints = neighbouringPoints(currentBoard, placesOfSigns, board);
                    List<String> pointsToRemove = pointsToRemove(currentBoard, board, neighbouringPoints);
                    if (pointsToRemove.size() == neighbouringPoints.size()) {
                        undoMove(point, currentBoard, board);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getSign(Player player, String o, String x) {
        String sign;
        if (player.getId().equals("P01")) {
            sign = o;
        } else {
            sign = x;
        }
        return sign;
    }

    //Sprawdzenie, czy dołożenie kamienia w niedozwolone miejsce nie spowoduje zbicia kamieni przeciwnika, co umożliwia taki ruch
    public boolean conditionalMove(String[][] currentBoard, Player player, String point, String[][] board) {
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (board[i][j].equals(point)) {
                    if (currentBoard[i][j].equals("o") || currentBoard[i][j].equals("x")) {
                        return false;
                    }
                    Integer pointsBeforeMove = player.getPoints();
                    currentBoard = currentBoard(currentBoard, player, point);
                    currentBoard = boardAfterTakingPrisoners(currentBoard, board, player);
                    Integer pointsAfterMove = player.getPoints();
                    if (pointsBeforeMove.equals(pointsAfterMove)) {
                        undoMove(point, currentBoard, board);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private String[][] undoMove(String point, String[][] currentBoard, String[][] board) {
        for (int k = 0; k < currentBoard.length; k++) {
            for (int l = 0; l < currentBoard.length; l++) {
                if (board[k][l].equals(point)) {
                    currentBoard[k][l] = board[k][l];
                }
            }
        }
        return currentBoard;
    }

    //Plansza po wzięciu jeńców
    public String[][] boardAfterTakingPrisoners(String[][] currentBoard, String[][] board, Player player) {

        String sign = getSign(player, "x", "o");

        List<String> placesOfSigns = placesOfSigns(currentBoard, board, sign);

        do {
            List<String> neighbouringPoints = neighbouringPoints(currentBoard, placesOfSigns, board);
            List<String> pointsToRemove = pointsToRemove(currentBoard, board, neighbouringPoints);

            if (pointsToRemove.size() == neighbouringPoints.size()) {
                for (String s : pointsToRemove) {
                    for (int i = 0; i < currentBoard.length; i++) {
                        for (int j = 0; j < currentBoard.length; j++) {
                            if (board[i][j].equals(s)) {
                                currentBoard[i][j] = s;
                                placesOfSigns.remove(s);
                                player.addPoint(player);
                            }
                        }
                    }
                }
            } else {
                for (String n : neighbouringPoints) {
                    placesOfSigns.remove(n);
                }
                pointsToRemove.clear();
                neighbouringPoints.clear();
            }
        }
        while (placesOfSigns.size() != 0);
        return currentBoard;
    }

    //Metoda zwracająca aktualny ciąg znaków tego samego typu
    private List<String> neighbouringPoints(String[][] currentBoard, List<String> placesOfSigns, String[][] board) {
        List<String> neighbouringPoints = new ArrayList<>();
        String currentPoint;
        Iterator<String> iterator = placesOfSigns.iterator();
        if (!placesOfSigns.isEmpty()) {
            currentPoint = iterator.next();
            neighbouringPoints.add(currentPoint);
        }

        if (!neighbouringPoints.isEmpty()) {
            for (int k = 0; k < neighbouringPoints.size(); k++) {
                currentPoint = neighbouringPoints.get(k);
                for (String p : placesOfSigns) {
                    for (int i = 0; i < currentBoard.length; i++) {
                        for (int j = 0; j < currentBoard.length; j++) {
                            if (board[i][j].equals(currentPoint)) {
                                if (i > 0 && j > 0 && i < currentBoard.length - 1 && j < currentBoard.length - 1) {
                                    if (board[i + 2][j].equals(p) || board[i - 2][j].equals(p) || board[i][j + 2].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i == 0 && j > 0 && j < currentBoard.length - 1) {
                                    if (board[i + 2][j].equals(p) || board[i][j + 2].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i > 0 && j == 0 && i < currentBoard.length - 1) {
                                    if (board[i + 2][j].equals(p) || board[i - 2][j].equals(p) || board[i][j + 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i == 0 && j == 0) {
                                    if (board[i + 2][j].equals(p) || board[i][j + 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (j > 0 && i == currentBoard.length - 1 && j < currentBoard.length - 1) {
                                    if (board[i - 2][j].equals(p) || board[i][j + 2].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i > 0 && j == currentBoard.length - 1 && i < currentBoard.length - 1) {
                                    if (board[i + 2][j].equals(p) || board[i - 2][j].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) {
                                    if (board[i - 2][j].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (i == currentBoard.length - 1 && j == 0) {
                                    if (board[i - 2][j].equals(p) || board[i][j + 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                } else if (j == currentBoard.length - 1 && i == 0) {
                                    if (board[i + 2][j].equals(p) || board[i][j - 2].equals(p)) {
                                        if (!neighbouringPoints.contains(p)) {
                                            neighbouringPoints.add(p);
                                        }
                                    }
                                }

                            }
                        }
                    }


                }
            }
        }
        return neighbouringPoints;
    }


    //Metoda zwracająca listę wszystkich znaków danego typu
    private List<String> placesOfSigns(String[][] currentBoard, String[][] board, String sign) {
        List<String> placesOfSigns = new ArrayList<>();
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].equals(sign)) {
                    placesOfSigns.add(board[i][j]);
                }
            }
        }
        return placesOfSigns;
    }

    //Metoda zwracająca miejsca kamieni, które będą wzięte jako jeńcy przez przeciwnika
    private List<String> pointsToRemove(String[][] currentBoard, String[][]
            board, List<String> neighbouringPoints) {
        List<String> pointsToRemove = new ArrayList<>();
        for (String p : neighbouringPoints) {
            for (int i = 0; i < currentBoard.length; i++) {
                for (int j = 0; j < currentBoard.length; j++) {
                    if (board[i][j].equals(p)) {
                        if (i > 0 && j > 0 && i < currentBoard.length - 1 && j < currentBoard.length - 1) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i == 0 && j > 0 && j < currentBoard.length - 1) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i > 0 && j == 0 && i < currentBoard.length - 1) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i == 0 && j == 0) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (j > 0 && i == currentBoard.length - 1 && j < currentBoard.length - 1) {
                            if (!(currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i > 0 && j == currentBoard.length - 1 && i < currentBoard.length - 1) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) {
                            if (!(currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (i == currentBoard.length - 1 && j == 0) {
                            if (!(currentBoard[i - 2][j].equals(String.valueOf(Integer.parseInt(p) - (currentBoard.length / 2 + 1))) || currentBoard[i][j + 2].equals(String.valueOf(Integer.parseInt(p) + 1)))) {
                                pointsToRemove.add(p);
                            }
                        } else if (j == currentBoard.length - 1 && i == 0) {
                            if (!(currentBoard[i + 2][j].equals(String.valueOf(Integer.parseInt(p) + (currentBoard.length / 2 + 1))) || currentBoard[i][j - 2].equals(String.valueOf(Integer.parseInt(p) - 1)))) {
                                pointsToRemove.add(p);
                            }
                        }

                    }
                }
            }
        }
        return pointsToRemove;
    }

    //Metoda zwracająca listę list ciągów punktów, które nie są zajęte prze kamienie
    public List<List<String>> placesWithoutSignsAfterEnd(String[][] currentBoard, String[][] board) {
        List<String> placesWithoutSigns = new ArrayList<>();
        List<List<String>> neighbouringEmptyPoints = new ArrayList<>();

        emptyPlaces(currentBoard, board, placesWithoutSigns);

        String currentPoint;
        List<String> currentNeighbouringPoints = new ArrayList<>();

        do {
            Iterator<String> iterator = placesWithoutSigns.iterator();
            if (!placesWithoutSigns.isEmpty()) {
                currentPoint = iterator.next();
                currentNeighbouringPoints.add(currentPoint);
                placesWithoutSigns.remove(currentPoint);
            }

            if (!currentNeighbouringPoints.isEmpty()) {
                for (int k = 0; k < currentNeighbouringPoints.size(); k++) {
                    currentPoint = currentNeighbouringPoints.get(k);
                    for (int p = 0; p < placesWithoutSigns.size(); p++) {
                        String currentPlaceWithoutSign = placesWithoutSigns.get(p);
                        for (int i = 0; i < currentBoard.length; i++) {
                            for (int j = 0; j < currentBoard.length; j++) {
                                if (currentBoard[i][j].equals(currentPoint)) {
                                    neighbouringEmptyPoints(currentBoard, placesWithoutSigns, currentNeighbouringPoints, currentPlaceWithoutSign, i, j);

                                }
                            }
                        }
                    }
                }
            }
            neighbouringEmptyPoints.add(currentNeighbouringPoints);
            currentNeighbouringPoints = new ArrayList<>();
        }
        while (placesWithoutSigns.size() != 0);

        return neighbouringEmptyPoints;
    }

    //Wszystkie miejsca na planszy bez kamieni
    private void emptyPlaces(String[][] currentBoard, String[][] board, List<String> placesWithoutSigns) {
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (!currentBoard[i][j].equals("o") && !currentBoard[i][j].equals("x") && !currentBoard[i][j].equals("-") && !currentBoard[i][j].equals("|") && !currentBoard[i][j].equals("")) {
                    placesWithoutSigns.add(board[i][j]);
                }
            }
        }
    }

    //Sprawdzenie, czy dany kamień sąsiaduje z innymi tego samego typu i dodanie go do listy
    private void neighbouringEmptyPoints(String[][] currentBoard, List<String> placesWithoutSigns, List<String> currentNeighbouringPoints, String currentPlaceWithoutSign, int i, int j) {
        if (i > 0 && j > 0 && i < currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i == 0 && j > 0 && j < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i > 0 && j == 0 && i < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i == 0 && j == 0) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (j > 0 && i == currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i > 0 && j == currentBoard.length - 1 && i < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) {
            if (currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (i == currentBoard.length - 1 && j == 0) {
            if (currentBoard[i - 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j + 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        } else if (j == currentBoard.length - 1 && i == 0) {
            if (currentBoard[i + 2][j].equals(currentPlaceWithoutSign) || currentBoard[i][j - 2].equals(currentPlaceWithoutSign)) {
                if (!currentNeighbouringPoints.contains(currentPlaceWithoutSign)) {
                    currentNeighbouringPoints.add(currentPlaceWithoutSign);
                    placesWithoutSigns.remove(currentPlaceWithoutSign);
                }
            }
        }
    }

    //Obliczenie finalnego terytorium gracza wraz z dodaniem punktów za posiadane terytorium
    public Integer territory(List<List<String>> neighbouringEmptyPoints, String[][]
            currentBoard, Player player) {
        int territory = 0;
        List<String> currentTerritory = new ArrayList<>();
        String sign = getSign(player, "x", "o");
        for (List<String> list : neighbouringEmptyPoints) {
            for (String point : list) {
                for (int i = 0; i < currentBoard.length; i++) {
                    for (int j = 0; j < currentBoard.length; j++) {
                        if (currentBoard[i][j].equals(point)) {
                            checkSurroundingPlaces(currentBoard, currentTerritory, sign, point, i, j);
                        }
                    }
                }
            }

            if (currentTerritory.size() == list.size()) {
                territory += currentTerritory.size();
            }
            currentTerritory.clear();
        }
        return territory;

    }

    //Metoda sprawdza otaczające punkty, jeżeli nie ma na nich kamieni przeciwnika, dodaje punkt do terytorium gracza
    private void checkSurroundingPlaces(String[][] currentBoard, List<String> currentTerritory, String sign, String point, int i, int j) {
        if (i > 0 && j > 0 && i < currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i == 0 && j > 0 && j < currentBoard.length - 1) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i > 0 && j == 0 && i < currentBoard.length - 1) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i == 0 && j == 0) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (j > 0 && i == currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (!currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i > 0 && j == currentBoard.length - 1 && i < currentBoard.length - 1) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) {
            if (!currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (i == currentBoard.length - 1 && j == 0) {
            if (!currentBoard[i - 2][j].equals(sign) && !currentBoard[i][j + 2].equals(sign)) {
                currentTerritory.add(point);
            }
        } else if (j == currentBoard.length - 1 && i == 0) {
            if (!currentBoard[i + 2][j].equals(sign) && !currentBoard[i][j - 2].equals(sign)) {
                currentTerritory.add(point);
            }
        }
    }

    //Usunięcie kamieni uznanych za martwe po zakończeniu gry
    public String[][] removeDeadRocks(String[][] currentBoard, String[][] board, String rocks) {
        String[] rocksToRemowe = rocks.split(",");
        for (String point : rocksToRemowe) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (point.equals(board[i][j])) {
                        currentBoard[i][j] = board[i][j];
                    }
                }
            }
        }
        return currentBoard;
    }

    //Dodanie punktów odpowiednim graczom za kamienie uznane za martwe po zakończeniu gry
    public void addPointsForDeadRocks(PlayerOne playerOne, PlayerTwo playerTwo, String rocks, String[][] board, String[][] currentBoard) {
        String[] rocksToRemowe = rocks.split(",");
        for (String point : rocksToRemowe) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (point.equals(board[i][j])) {
                        if (currentBoard[i][j].equals("o")) {
                            playerTwo.addPoint(playerTwo);
                        } else if (currentBoard[i][j].equals("x")) {
                            playerOne.addPoint(playerOne);
                        }
                    }
                }
            }
        }
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}


