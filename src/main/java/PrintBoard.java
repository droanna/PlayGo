import java.util.*;

public class PrintBoard extends Board implements PrintBoardInterface{


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
        if (!point.equals("0")) {
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
                    if (currentBoard[i][j].equals("o") || currentBoard[i][j].equals("x")) {
                        return true;
                    }
                    String sign;

                    if (player.getId().equals("P01")) {
                        sign = "o";
                    } else {
                        sign = "x";
                    }
                    currentBoard = currentBoard(currentBoard, player, point);
                    List<String> placesOfSigns = placesOfSigns(currentBoard, board, sign);
                    List<String> neighbouringPoints = new ArrayList<>();
                    neighbouringPoints.add(String.valueOf(point));
                    String currentPoint;
                    if (!neighbouringPoints.isEmpty()) {
                        for (int k = 0; k < neighbouringPoints.size(); k++) {
                            currentPoint = neighbouringPoints.get(k);
                            for (String p : placesOfSigns) {
                                if (Integer.parseInt(p) == (Integer.parseInt(currentPoint) - 1) || Integer.parseInt(p) == (Integer.parseInt(currentPoint) + 1) ||
                                        Integer.parseInt(p) == (Integer.parseInt(currentPoint) + (currentBoard.length / 2 + 1)) || Integer.parseInt(p) == (Integer.parseInt(currentPoint) - (currentBoard.length / 2 + 1))) {
                                    if (!neighbouringPoints.contains(p)) {
                                        neighbouringPoints.add(p);
                                    }
                                }
                            }
                        }
                    }
                    List<String> pointsToRemove = pointsToRemove(currentBoard, board, neighbouringPoints);
                    for (String[] line : currentBoard) {
                        for (String p : line) {
                            if (board[i][j].equals(String.valueOf(point))) {
                                currentBoard[i][j] = board[i][j];
                            }
                        }
                    }
                    if (pointsToRemove.size() == neighbouringPoints.size()) {
                        return true;
                    }
                    return placeOfDeath(player, point, currentBoard, board, i, j);
                }
            }
        }
        return false;
    }


    private boolean placeOfDeath(Player player, String point, String[][] currentBoard, String[][] board, int i, int j) {
        String sign;
        if (board[i][j].equals(point)) {
            if (player.getId().equals("P01")) {
                sign = "x";
            } else {
                sign = "o";
            }
            if (surroundingPlaces(currentBoard, i, j, sign)) {
                return true;
            }
        }
        return false;
    }

    private boolean surroundingPlaces(String[][] currentBoard, int i, int j, String sign) {
        if (i > 0 && j > 0 && i < currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i - 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        } else if (i == 0 && j > 0 && j < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        } else if (i > 0 && j == 0 && i < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i - 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign)) {
                return true;
            }
        } else if (i == 0 && j == 0) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign)) {
                return true;
            }
        } else if (j > 0 && i == currentBoard.length - 1 && j < currentBoard.length - 1) {
            if (currentBoard[i - 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        } else if (i > 0 && j == currentBoard.length - 1 && i < currentBoard.length - 1) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i - 2][j].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        } else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) {
            if (currentBoard[i - 2][j].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        } else if (i == currentBoard.length - 1 && j == 0) {
            if (currentBoard[i - 2][j].equals(sign) && currentBoard[i][j + 2].equals(sign)) {
                return true;
            }
        } else if (j == currentBoard.length - 1 && i == 0) {
            if (currentBoard[i + 2][j].equals(sign) && currentBoard[i][j - 2].equals(sign)) {
                return true;
            }
        }
        return false;
    }


    public String[][] boardAfterTakingPrisoners(String[][] currentBoard, String[][] board, Player player) {

        String sign;

        if (player.getId().equals("P01")) {
            sign = "x";
        } else {
            sign = "o";
        }

        List<String> placesOfSigns = placesOfSigns(currentBoard, board, sign);

        do {
            List<String> neighbouringPoints = neighbouringPoints(currentBoard, placesOfSigns);
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

    private List<String> neighbouringPoints(String[][] currentBoard, List<String> placesOfSigns) {
        List<String> neighbouringPoints = new ArrayList<>();
        String currentPoint;
        Iterator<String> iterator = placesOfSigns.iterator();
        if (!placesOfSigns.isEmpty()) {
            currentPoint = iterator.next();
            neighbouringPoints.add(currentPoint);
        }

        if (!neighbouringPoints.isEmpty()) {
            for (int i = 0; i < neighbouringPoints.size(); i++) {
                currentPoint = neighbouringPoints.get(i);
                for (String p : placesOfSigns) {
                    if (Integer.parseInt(p) == (Integer.parseInt(currentPoint) - 1) || Integer.parseInt(p) == (Integer.parseInt(currentPoint) + 1) ||
                            Integer.parseInt(p) == (Integer.parseInt(currentPoint) + (currentBoard.length / 2 + 1)) || Integer.parseInt(p) == (Integer.parseInt(currentPoint) - (currentBoard.length / 2 + 1))) {
                        if (!neighbouringPoints.contains(p)) {
                            neighbouringPoints.add(p);
                        }
                    }
                }
            }
        }
        return neighbouringPoints;
    }

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

    private List<String> pointsToRemove(String[][] currentBoard, String[][] board, List<String> neighbouringPoints) {
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

    public List<List<String>> placesWithoutSignsAfterEnd(String[][] currentBoard, String[][] board) {
        List<String> placesWithoutSigns = new ArrayList<>();
        List<List<String>> neighbouringEmptyPoints = new ArrayList<>();

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (!currentBoard[i][j].equals("o") && !currentBoard[i][j].equals("x") && !currentBoard[i][j].equals("-") && !currentBoard[i][j].equals("|") && !currentBoard[i][j].equals("")) {
                    placesWithoutSigns.add(board[i][j]);
                }
            }
        }

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
                for (int i = 0; i < currentNeighbouringPoints.size(); i++) {
                    currentPoint = currentNeighbouringPoints.get(i);
                    for (int p = 0; p < placesWithoutSigns.size(); p++) {
                        if (Integer.parseInt(placesWithoutSigns.get(p)) == (Integer.parseInt(currentPoint) - 1) || Integer.parseInt(placesWithoutSigns.get(p)) == (Integer.parseInt(currentPoint) + 1) ||
                                Integer.parseInt(placesWithoutSigns.get(p)) == (Integer.parseInt(currentPoint) + (currentBoard.length / 2 + 1)) || Integer.parseInt(placesWithoutSigns.get(p)) == (Integer.parseInt(currentPoint) - (currentBoard.length / 2 + 1))) {
                            if (!currentNeighbouringPoints.contains(placesWithoutSigns.get(p))) {
                                currentNeighbouringPoints.add(placesWithoutSigns.get(p));
                                placesWithoutSigns.remove(placesWithoutSigns.get(p));
                            }
                        }
                    }
                }
            }
            neighbouringEmptyPoints.add(currentNeighbouringPoints);
            currentNeighbouringPoints = new ArrayList<>();
        } while (placesWithoutSigns.size() != 0);

        return neighbouringEmptyPoints;
    }

    public Integer territory(List<List<String>> neighbouringEmptyPoints, String[][] currentBoard, Player player) {
        String sign;
        int territory = 0;
        List<String> currentTerritory = new ArrayList<>();
        if (player.getId().equals("P01")) {
            sign = "x";
        } else {
            sign = "o";
        }
        for (List<String> list : neighbouringEmptyPoints) {
            for (String point : list) {
                for (int i = 0; i < currentBoard.length; i++) {
                    for (int j = 0; j < currentBoard.length; j++) {
                        if (currentBoard[i][j].equals(point)) {
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
                    }
                }
            }

            if (currentTerritory.size() == list.size()) {
                territory += currentTerritory.size();
            }
        }
        return territory;

    }

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

    public void addPointsForDeadRocks(PlayerOne playerOne, PlayerTwo playerTwo, String rocks) {
        String[] rocksToRemowe = rocks.split(",");
        for (String point : rocksToRemowe) {
            if (point.equals("o")) {
                playerTwo.addPoint(playerTwo);
            } else if (point.equals("x")) {
                playerOne.addPoint(playerOne);
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


