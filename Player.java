import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
  private Socket socket;
  private BufferedReader reader;
  private BufferedWriter writer;
  private List<Card> hand;

  private String name;

  public Player(Socket socket) {
    this.socket = socket;
    hand = new ArrayList<>();

    try {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      sendMessage("Bem Vindo ao Uno!");
      String message = reader.readLine();
      setName(message);

      while (true) {
        message = reader.readLine();
        System.out.println("Mensagem recebida de " + getName() + ": " + message);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeConections();
    }
  }

  public void closeConections() {
    try {
      reader.close();
      writer.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void sendMessage(String message) throws IOException {
    writer.write(message);
    writer.newLine();
    writer.flush();
  }

  public String receiveMessage() throws IOException {
    return reader.readLine();
  }

  public List<Card> getHand() {
    return hand;
  }

  public void drawCard(Card card) {
    hand.add(card);
  }

  public List<Card> getPlayableCards(Card.Color currentColor) {
    List<Card> playableCards = new ArrayList<>();
    for (Card card : hand) {
      if (card.getColor() == currentColor || card.getColor() == Card.Color.CURINGA ||
          card.getType() == Card.Type.CURINGA || card.getType() == Card.Type.CURINGA_COMPRAR_QUATRO) {
        playableCards.add(card);
      }
    }
    return playableCards;
  }

  public int getPoints() {
    int point = 0;
    for (Card card : hand) {
      if (card.getType() == Card.Type.NUMERO) {
        point += card.getNumber();
      } else if (card.getType() == Card.Type.CURINGA || card.getType() == Card.Type.CURINGA_COMPRAR_QUATRO) {
        point += 50;
      } else {
        point += 20;
      }
    }
    return point;
  }
}
