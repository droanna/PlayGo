import java.util.Objects;

public class PlayerTwo extends Player {


    public PlayerTwo(String id, String name) {
        super(name);
        this.id = "P02";
    }

    @Override
    public String getId() {
        return "P02";
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }


}



