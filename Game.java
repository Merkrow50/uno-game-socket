import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

  private List<Player> players;
  private int currentPlayer;

  public int roundsPlayed = 1;
  private UnoDeck deck;
  private ArrayList<UnoCard> stockpile;

  private Color validColor;
  private Value validValue;

  boolean gameDirection;
  private ObjectOutputStream out;

  private PrintWriter message;

  public Game(List<Player> players, Socket socket) throws IOException {

    deck = new UnoDeck();
    deck.shuffle();

    stockpile = new ArrayList<UnoCard>();

    this.players = players;
    gameDirection = false;

    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.message = new PrintWriter(socket.getOutputStream(), true);

    dealCards();

  }


  private void dealCards(){

    for (int i = 0; i < players.size(); i++){
      ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
      players.get(i++).setPlayerHand(hand);
    }

    System.out.printf(players.toString());
  }

  public void start(Game game) {

    UnoCard unoCard = deck.drawCard();

    validColor = unoCard.getColor();
    validValue = unoCard.getValue();


    if(unoCard.getValue() == Value.Curinga){
      start(game);
    }

    if (unoCard.getValue() == Value.CuringaMaisQuatro || unoCard.getValue() == Value.MaisDois){
      start(game);
    }

    if (unoCard.getValue() == Value.Pular){
      message.println(players.get(currentPlayer).getName() + "foi pulado!");

      if (!gameDirection) {
        currentPlayer = (currentPlayer + 1) % players.size();
      } else {
        currentPlayer = (currentPlayer - 1) % players.size();
        if (currentPlayer == -1){
          currentPlayer = players.size() - 1;
        }
      }

    }

    if (unoCard.getValue() == Value.Reverse){
      message.println(players.get(currentPlayer).getName() + "A direção do jogo foi alterada!");
      gameDirection ^= true;

      currentPlayer = players.size() - 1;
    }

    stockpile.add(unoCard);
  }

  public UnoCard getTopCard() {
    return new UnoCard(validColor, validValue);
  }

  public List<Player> getPlayers() {
    return players;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public int getPreviousPlayer(int i) {
    int index = this.currentPlayer - i;

    if (index == -1){
      index = players.size() - 1;
    }

    return index;
  }

  public boolean isGameOver(){
    for (int i = 0; i < players.size(); i++) {
      if (getPlayerHand(i).isEmpty()){
        return true;
      }
    }
    return false;
  }

  public UnoCard getPlayerCard(int pid, int choice) {
    ArrayList<UnoCard> hand = getPlayerHand(pid);
    return hand.get(choice);
  }

  public ArrayList<UnoCard> getPlayerHand(int pid) {
    return players.get(pid).getPlayerHand();
  }


  public boolean validCardPlay(UnoCard card) {
    return card.getColor().equals(validColor) || card.getValue().equals(validValue);
  }


  public void checkPlayerTurn(int pid) throws InvalidParameterException {
    if(this.currentPlayer != pid){
      throw new InvalidParameterException("Não é a vez do usuário informado!");
    }
  }

  public void submitDrasw(int pid) {
    checkPlayerTurn(pid);

    if (deck.isEmpty()){
        deck.replaceDeckWith(stockpile);
        deck.shuffle();
    }

    getPlayerHand(pid).add(deck.drawCard());

    if (!gameDirection){
      currentPlayer = (currentPlayer + 1) % players.size();
    }

    if(gameDirection) {
      currentPlayer = (currentPlayer - 1) % players.size();
      if (currentPlayer == -1){
        currentPlayer = players.size() - 1;
      }
    }

  }

  public void setCardColor(Color color) {
      validColor = color;
  }


}
