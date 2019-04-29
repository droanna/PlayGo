import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private PlayerOne playerOne = new PlayerOne();
    private PlayerTwo playerTwo = new PlayerTwo();
    private Player player;


    @Test
    void getPlayerTest() {
        Player chosenPlayerOne = Player.getPlayer(0, playerOne, playerTwo);
        Player chosenPlayerTwo = Player.getPlayer(1, playerOne, playerTwo);
        Assertions.assertThat(chosenPlayerOne).isExactlyInstanceOf(PlayerOne.class);
        Assertions.assertThat(chosenPlayerTwo).isExactlyInstanceOf(PlayerTwo.class);
    }

    @Test
    void addPointIfPlayerHasNoPoints() {
        playerOne.setPoints(0);
        player = playerOne;
        player.addPoint(player);
        int points = playerOne.getPoints();
        Assertions.assertThat(points).isEqualTo(1);
    }

    @Test
    void addPointIfPlayerHasPoints() {
        playerOne.setPoints(2);
        player = playerOne;
        player.addPoint(player);
        player.addPoint(player);
        int points = playerOne.getPoints();
        Assertions.assertThat(points).isEqualTo(4);
    }
}