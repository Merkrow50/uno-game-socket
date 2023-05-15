import java.io.Serializable;

public class Player implements Serializable {

    private String id;
    private String name;

    private Hand hand;

    public Hand getHand() {
        return hand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player [" + this.getId() + "]";
    }
}
