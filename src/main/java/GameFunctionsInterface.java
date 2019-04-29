import java.util.List;

public interface GameFunctionsInterface {

    void printBoard(String[][] boardArray);
    String[][] currentBoard(String[][] currentBoard, Player player, String point);
    String[][] boardTwoMovesBefore(String[][] board, Player player, String point);
    boolean ko(String[][] boardTwoMovesBefore, String[][] currentBoard, String point);
    boolean impossibleMoves(Player player, String point, String[][] currentBoard, String[][] board);
    String[][] boardAfterTakingPrisoners(String[][] currentBoard, String[][] board, Player player);
    Integer territory(List<List<String>> neighbouringEmptyPoints, String[][] currentBoard, Player player);


}
