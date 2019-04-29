import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


class GameFunctionsTest {
    private GameFunctions gameFunctions = new GameFunctions();
    private Board board = new Board();
    private PlayerOne playerOne = new PlayerOne();
    private PlayerTwo playerTwo = new PlayerTwo();

    @Test
    void boardOneMoveBeforeTest() {
        String[][] finalArray = gameFunctions.boardOneMoveBefore(board.board(9), playerOne, "1");
        String point = finalArray[0][0];
        Assertions.assertThat(point).isEqualTo("o");
    }

    @Test
    void boardTwoMovesBeforeTest() {
        String[][] finalArray = gameFunctions.boardTwoMovesBefore(board.board(9), playerOne, "1");
        String point = finalArray[0][0];
        Assertions.assertThat(point).isEqualTo("x");
    }

    @Test
    void currentBoardTest() {
        String[][] finalArray = gameFunctions.currentBoard(board.board(9), playerOne, "1");
        String point = finalArray[0][0];
        Assertions.assertThat(point).isEqualTo("o");
    }

    @Test
    void koTest() {
        String[][] initialBoard = board.board(9);
        String[][] currentBoard = gameFunctions.currentBoard(initialBoard, playerOne, "1");
        String[][] boardTwoMovesBefore = gameFunctions.currentBoard(initialBoard, playerOne, "1");
        Boolean koState = gameFunctions.ko(boardTwoMovesBefore, currentBoard, "1");
        Assertions.assertThat(koState).isTrue();

    }

    @Test
    void impossibleMovesIfOccupiedPlaceTest() {
        String[][] initialBoard = board.board(9);
        String[][] currentBoard = gameFunctions.currentBoard(board.board(9), playerOne, "1");
        Boolean impossibleMoveCheck = gameFunctions.impossibleMoves(playerOne, "1", currentBoard, initialBoard);
        Assertions.assertThat(impossibleMoveCheck).isTrue();
    }

    @Test
    void impossibleMaoveIfLosingOwnRocks() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = gameFunctions.currentBoard(board.board(8), playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "4");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "12");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "2");
        Boolean impossibleMoveCheck = gameFunctions.impossibleMoves(playerTwo, "3", currentBoard, initialBoard);
        Assertions.assertThat(impossibleMoveCheck).isTrue();
    }

    @Test
    void impossibleMoveIfSorroundedByOpponentsRocks() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = gameFunctions.currentBoard(board.board(8), playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "3");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "11");
        Boolean impossibleMoveCheck = gameFunctions.impossibleMoves(playerTwo, "2", currentBoard, initialBoard);
        Assertions.assertThat(impossibleMoveCheck).isTrue();

    }

    @Test
    void checkIfNotImpossibleMove() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        Boolean impossibleMoveCheck = gameFunctions.impossibleMoves(playerOne, "1", currentBoard, initialBoard);
        Assertions.assertThat(impossibleMoveCheck).isFalse();
    }

    @Test
    void conditionalMoveIfTrue() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "2");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "10");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "19");
        Boolean conditionalMove = gameFunctions.conditionalMove(currentBoard, playerTwo, "1", initialBoard);
        Assertions.assertThat(conditionalMove).isTrue();
    }

    @Test
    void conditionalMoveWhenPointIsOccupied() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "15");
        Boolean conditionalMove = gameFunctions.conditionalMove(currentBoard, playerOne, "15", initialBoard);
        Assertions.assertThat(conditionalMove).isFalse();
    }

    @Test
    void conditionalMoveIfFalse() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "19");
        Boolean conditionalMove = gameFunctions.conditionalMove(currentBoard, playerOne, "10", initialBoard);
        Assertions.assertThat(conditionalMove).isFalse();
    }

    @Test
    void boardAfterTakingPrisonersWhenPrisonersAreTaken() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "3");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "2");
        currentBoard = gameFunctions.boardAfterTakingPrisoners(currentBoard, initialBoard, playerTwo);
        String point = currentBoard[0][2];
        Assertions.assertThat(point).isEqualTo("2");
    }

    @Test
    void boardAfterTakingPrisonersWhenPrisonersArentTaken() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "2");
        currentBoard = gameFunctions.boardAfterTakingPrisoners(currentBoard, initialBoard, playerTwo);
        String point = currentBoard[0][2];
        Assertions.assertThat(point).isEqualTo("o");
    }

    @Test
    void placesWithoutSignsAfterEnd() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "3");
        List<List<String>> neighbouringEmptyPoints = gameFunctions.placesWithoutSignsAfterEnd(currentBoard, initialBoard);
        Assertions.assertThat(neighbouringEmptyPoints).isNotNull();
        Assertions.assertThat(neighbouringEmptyPoints).size().isEqualTo(2);
        Assertions.assertThat(neighbouringEmptyPoints).element(0).asList().size().isEqualTo(1);
        Assertions.assertThat(neighbouringEmptyPoints).element(1).asList().size().isEqualTo(77);
    }

    @Test
    void territoryIfOneTerritory() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "3");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "15");
        List<List<String>> neighbouringEmptyPoints = gameFunctions.placesWithoutSignsAfterEnd(currentBoard, initialBoard);
        Integer territory = gameFunctions.territory(neighbouringEmptyPoints, currentBoard, playerTwo);
        Assertions.assertThat(territory).isEqualTo(1);
    }

    @Test
    void territoryIfMoreTerritories() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "3");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "4");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "14");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "15");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "7");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "22");
        List<List<String>> neighbouringEmptyPoints = gameFunctions.placesWithoutSignsAfterEnd(currentBoard, initialBoard);
        Integer territory = gameFunctions.territory(neighbouringEmptyPoints, currentBoard, playerTwo);
        Assertions.assertThat(territory).isEqualTo(3);
    }

    @Test
    void territoryIfTwoPlayersHaveTerritories() {
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "3");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "4");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "14");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "15");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "7");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "22");
        List<List<String>> neighbouringEmptyPoints = gameFunctions.placesWithoutSignsAfterEnd(currentBoard, initialBoard);
        Integer territoryPlayerOne = gameFunctions.territory(neighbouringEmptyPoints, currentBoard, playerOne);
        Integer territoryPlayerTwo = gameFunctions.territory(neighbouringEmptyPoints, currentBoard, playerTwo);
        Assertions.assertThat(territoryPlayerOne).isEqualTo(1);
        Assertions.assertThat(territoryPlayerTwo).isEqualTo(2);
    }

    @Test
    void removeDeadRocksIfPointsIndicatedRocksTestForOnePoint(){
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "3");
        currentBoard = gameFunctions.removeDeadRocks(currentBoard, initialBoard, "1");
        Assertions.assertThat(currentBoard[0][0]).isEqualTo("1");
        Assertions.assertThat(currentBoard[0][4]).isEqualTo("o");
        Assertions.assertThat(currentBoard[2][2]).isEqualTo("o");
    }

    @Test
    void removeDeadRocksIfPointsIndicatedRocksTestForMorePoints(){
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "11");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "3");
        currentBoard = gameFunctions.removeDeadRocks(currentBoard, initialBoard, "1,11,3");
        Assertions.assertThat(currentBoard[0][0]).isEqualTo("1");
        Assertions.assertThat(currentBoard[0][4]).isEqualTo("3");
        Assertions.assertThat(currentBoard[2][2]).isEqualTo("11");
    }

    @Test
    void addPointsForDeadRocks(){
        String[][] initialBoard = board.board(8);
        String[][] currentBoard = board.board(8);
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "1");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerOne, "2");
        currentBoard = gameFunctions.currentBoard(currentBoard, playerTwo, "10");
        gameFunctions.addPointsForDeadRocks(playerOne, playerTwo, "1,2,10", initialBoard, currentBoard);
        int pointsPlayerOne = playerOne.getPoints();
        int pointsPlayerTwo = playerTwo.getPoints();
        Assertions.assertThat(pointsPlayerOne).isEqualTo(1);
        Assertions.assertThat(pointsPlayerTwo).isEqualTo(2);
    }

}