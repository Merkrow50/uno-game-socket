import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
  private List<Player> players;
  private List<Card> drawPile;
  private List<Card> discardPile;
  private boolean reverseDirection;
  private int currentPlayerIndex;
  private boolean inProgress;

  public Game() {
    players = new ArrayList<>();
    drawPile = new ArrayList<>();
    discardPile = new ArrayList<>();
    reverseDirection = false;
    currentPlayerIndex = 0;
    inProgress = false;
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void startGame() {
    if (players.size() >= 2) {
      createDeck();
      shuffleDeck();
      dealCards();

      Card firstCard = drawCard();
      discardPile.add(firstCard);

      inProgress = true;
    }
  }

  public boolean isGameInProgress() {
    return inProgress;
  }

  public List<Card> getDiscardPile() {
    return discardPile;
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public Card.Color getCurrentColor() {
    return discardPile.get(discardPile.size() - 1).getColor();
  }

  public Card drawCard() {
    if (drawPile.isEmpty()) {
      reshuffleDiscardPile();
    }
    return drawPile.remove(drawPile.size() - 1);
  }

  public boolean processMove(Player player, Card card) {
    if (player == players.get(currentPlayerIndex) && isValidMove(card)) {
      player.getHand().remove(card);
      discardPile.add(card);
      applyCardEffects(card);

      if (player.getHand().isEmpty()) {
        inProgress = false;
      } else if (card.getType() != Card.Type.PULAR && card.getType() != Card.Type.INVERTER) {
        updateCurrentPlayerIndex();
      }

      return true;
    }

    return false;
  }

  private boolean isValidMove(Card card) {
    Card topCard = discardPile.get(discardPile.size() - 1);

    if (card.getColor() == topCard.getColor() || card.getType() == topCard.getType() ||
        card.getColor() == Card.Color.CURINGA) {
      return true;
    }

    for (Card c : players.get(currentPlayerIndex).getHand()) {
      if (c.getColor() == topCard.getColor() && c.getType() == Card.Type.NUMERO) {
        return false;
      }
    }

    if (topCard.getType() == Card.Type.CURINGA_COMPRAR_QUATRO && card.getType() == Card.Type.CURINGA_COMPRAR_QUATRO) {
      return true;
    }

    return false;
  }
  private void applyCardEffects(Card card) {
    switch (card.getType()) {
      case PULAR:
        updateCurrentPlayerIndex();
        break;
      case INVERTER:
        reverseDirection = !reverseDirection;
        break;
      case COMPRAR_DOIS:
        updateCurrentPlayerIndex();
        drawCards(2);
        break;
      case CURINGA_COMPRAR_QUATRO:
        updateCurrentPlayerIndex();
        drawCards(4);
        break;
      default:
        break;
    }
  }

  public void updateCurrentPlayerIndex() {
    if (reverseDirection) {
      currentPlayerIndex--;
      if (currentPlayerIndex < 0) {
        currentPlayerIndex = players.size() - 1;
      }
    } else {
      currentPlayerIndex++;
      if (currentPlayerIndex >= players.size()) {
        currentPlayerIndex = 0;
      }
    }
  }

  private void drawCards(int numCards) {
    Player currentPlayer = players.get(currentPlayerIndex);
    for (int i = 0; i < numCards; i++) {
      Card card = drawCard();
      currentPlayer.drawCard(card);
    }
  }

  private void createDeck() {
    for (Card.Color color : Card.Color.values()) {
      if (color != Card.Color.CURINGA) {
        for (int i = 0; i <= 9; i++) {
          drawPile.add(new Card(Card.Type.NUMERO, color, i));
        }
        drawPile.add(new Card(Card.Type.PULAR, color));
        drawPile.add(new Card(Card.Type.INVERTER, color));
        drawPile.add(new Card(Card.Type.COMPRAR_DOIS, color));
      }
    }
    for (int i = 0; i < 4; i++) {
      drawPile.add(new Card(Card.Type.CURINGA, Card.Color.CURINGA));
      drawPile.add(new Card(Card.Type.CURINGA_COMPRAR_QUATRO, Card.Color.CURINGA));
    }
  }

  private void shuffleDeck() {
    Collections.shuffle(drawPile);
  }

  private void dealCards() {
    for (Player player : players) {
      for (int i = 0; i < 7; i++) {
        Card card = drawCard();
        player.drawCard(card);
      }
    }
  }

  private void reshuffleDiscardPile() {
    Card lastCard = discardPile.remove(discardPile.size() - 1);
    drawPile.addAll(discardPile);
    discardPile.clear();
    discardPile.add(lastCard);
    shuffleDeck();
  }
}
