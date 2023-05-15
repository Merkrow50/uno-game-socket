import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

  private static final int PORT = 5000;

  private final ArrayList<Player> players = new ArrayList<>();

  private Socket socket;

  private Status status = Status.WAIT;

  private ObjectOutputStream out;

  private ObjectInputStream in;

  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }

  private void start() {
    System.out.println("[SERVER] Iniciando o servidor...");
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("[SERVER] Servidor iniciado");

      while (true) {
        System.out.println("[SERVER] Aguardando conexão...");
        socket = serverSocket.accept();
        System.out.println("[SERVER] Conexão aceita");
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        init();

      }
    } catch (Exception e) {
      System.err.println("[SERVER] Erro no start: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void registry() throws IOException, ClassNotFoundException {

    Player player = (Player) in.readObject();
    player.setId(String.valueOf(players.size() + 1));

    System.out.println("[SERVER] Registrando jogador...");

    if (this.players.size() >= 10) {
      System.err.println("[SERVER] ERRO: Limite de jogadores atingido");
    } else {
      this.players.add(player);
      System.out.println("[SERVER] Jogador registrado [" + players.size() + "/10]");
      this.out.writeObject(player);
    }

  }

  private void init() {
    try {
      registry();
      new Thread(t2).start();
    } catch (Exception e) {
      System.err.println("[SERVER] Erro no answer: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void waitToStartGame() throws IOException, ClassNotFoundException {
    do {
      this.status = (Status) in.readObject();
    } while (!status.equals(Status.STARTED));
    System.out.println("[SERVER] iniciando jogo...");
  }

  private final Runnable t2 = () -> {
    try {
      waitToStartGame();
    } catch (Exception e) {
      System.err.println("[SERVER] Erro no answer: " + e.getMessage());
      e.printStackTrace();
    }
  };

}
