
public class PlayerOne extends Player {


    public PlayerOne(String id, String name) {
        super(name);
        super.id = "P01";
    }

    public PlayerOne() {
        this.id = "P01";
    }

    @Override
    public String getId() {
        return id;
    }


}
