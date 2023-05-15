import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String id;

    private String name;

    private ArrayList<UnoCard> playerHand = new ArrayList<UnoCard>();

    public void setPlayerHand(ArrayList<UnoCard> playerHand) {
        this.playerHand = playerHand;
    }

    public ArrayList<UnoCard> getPlayerHand() {
        return playerHand;
    }

    public int getHandSize() {
        return playerHand.size();
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

    public String displayHand() {
        String hand = "";
        for (int i = 0; i < playerHand.size(); i++){
            hand = hand.concat("[" + playerHand.get(i).toString() + "]");
        }
        return hand;
    }

    @Override
    public String toString() {
        return "Player [" + this.getId() + "]";
    }
}
