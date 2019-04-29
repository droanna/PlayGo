import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board = new Board();

    @Test
    void boardTest() {
        String[][] gameBoard = board.board(9);
        Assertions.assertThat(gameBoard[0][0]).isEqualTo("1");
        Assertions.assertThat(gameBoard[0][16]).isEqualTo("9");
        Assertions.assertThat(gameBoard[1][0]).isEqualTo("|");
        Assertions.assertThat(gameBoard[1][1]).isEqualTo("");
        Assertions.assertThat(gameBoard[0][1]).isEqualTo("-");
    }
}