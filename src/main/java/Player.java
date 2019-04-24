import java.util.Objects;
import java.util.Random;

public abstract class Player {

    public String id;
    private String name;
    private int points;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public Player(String name) {
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public static Player getPlayer(int number, PlayerOne playerOne, PlayerTwo playerTwo) {
        if (number == 0) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    public Player firstPlayer(PlayerOne playerOne, PlayerTwo playerTwo) {
        Random random = new Random();
        int number = random.nextInt(2);
        return getPlayer(number, playerOne, playerTwo);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoint(Player player) {
        int points = player.getPoints();
        player.setPoints(points + 1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
