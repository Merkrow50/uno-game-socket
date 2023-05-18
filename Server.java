import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  private List<Player> players;
  private Game game;
  private ServerSocket serverSocket;

  private static final int PORT = 5000;


  public Server() {
    players = new ArrayList<>();
    game = new Game();

    try {
      serverSocket = new ServerSocket(PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    Server server = new Server();
    server.start();
  }

  public void start() throws IOException {
    System.out.println("Server iniciado. Esperando jogadores conectar...");

    while (players.size() < 2) {
      try {
        Socket socket = serverSocket.accept();
        Player player = new Player(socket);
        players.add(player);

        Thread playerThread = new Thread(player);
        playerThread.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Jogo iniciado...");
    game.addPlayer(players.get(0));
    game.addPlayer(players.get(1));
    game.startGame();

    broadcastMessage("Jogo Iniciado!");

    while (game.isGameInProgress()) {
      execute();
    }

    endGame();
  }

  private void execute() throws IOException {
    Player currentPlayer = players.get(game.getCurrentPlayerIndex());
    Card.Color currentColor = game.getCurrentColor();
    List<Card> cardPlayableList = currentPlayer.getPlayableCards(currentColor);

    if (cardPlayableList.isEmpty()) {
      Card cardToAdd = game.drawCard();
      currentPlayer.getHand().add(cardToAdd);
      game.updateCurrentPlayerIndex();
      broadcastMessage(currentPlayer.getName() + " comprou uma carta!");
      if (game.isGameInProgress()) {
        Player nextPlayer = players.get(game.getCurrentPlayerIndex());
        nextPlayer.sendMessage("Espere pelo seu turno...");
      }

      execute();

    } else  {
      turn();
    }
  }

  private String displayCards(List<Card> cards){
    String hand = "";

    for (int i = 0; i < cards.size(); i++){
     hand = hand.concat(String.format("[(index: %s), (Carta: %s)], \n", i, cards.get(i).toString()));
    }

    return hand;
  }

  private void turn() throws IOException {
    Player currentPlayer = players.get(game.getCurrentPlayerIndex());
    Card.Color currentColor = game.getCurrentColor();
    Card topCard = game.getDiscardPile().get(game.getDiscardPile().size() - 1);

    currentPlayer.sendMessage("Seu Turno! Carta Atual: " + topCard.toString());
    currentPlayer.sendMessage("Suas Cartas: " + displayCards(currentPlayer.getHand()));
    currentPlayer.sendMessage("Movimentos Válidos: " + displayCards(currentPlayer.getPlayableCards(currentColor)));
    currentPlayer.sendMessage("Insira o index da carta valida que voce quer jogar:");

    String input = currentPlayer.receiveMessage();

    try {

      int cardIndex = Integer.parseInt(input);
      Card selectedCard = currentPlayer.getHand().get(cardIndex);

      if (game.processMove(currentPlayer, selectedCard)) {
        broadcastMessage(currentPlayer.getName() + " jogou " + selectedCard.toString());

        if (game.isGameInProgress()) {
          Player nextPlayer = players.get(game.getCurrentPlayerIndex());
          nextPlayer.sendMessage("Espere pelo seu turno...");
        }
      } else {
        currentPlayer.sendMessage("Movimento Invalido! Tente Novamente.");
      }

    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      currentPlayer.sendMessage("Mensagem Invalida! Tente Novamente.");
    }
  }

  private void endGame() throws IOException {
    Player winner = players.get(0);
    int minScore = winner.getPoints();

    for (int i = 1; i < players.size(); i++) {
      Player player = players.get(i);
      int score = player.getPoints();

      if (score < minScore) {
        winner = player;
        minScore = score;
      }
    }

    broadcastMessage("Fim de jogo! " + winner.getName() + " vencedor com " + winner.getPoints() + " pontos");
  }

  private void broadcastMessage(String message) throws IOException {
    for (Player player : players) {
      player.sendMessage(message);
    }
  }

}
