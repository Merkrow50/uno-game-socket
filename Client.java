import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

  private final String HOST = "localhost";
  private final int PORT = 5000;

  private ObjectInputStream in;
  private ObjectOutputStream out;

  private final Scanner input;

  private Player currentPlayer = new Player();

  private static Status status = Status.WAIT;
  private Socket socket;


  public Client() {
    this.input = new Scanner(System.in);
  }

  public static void main(String[] args) {
    new Client().start();
  }

  private void start() {
    connection();
    run();
  }

  private void connection() {
    try {
      this.socket = new Socket(HOST, PORT);
      System.out.println("Cliente conectado");
      this.out = new ObjectOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      System.err.println("[CLIENT] Erro no send: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void run() {

    do {
      setPlayer();
      getPlayer();
      init();
    } while (true);

  }

  private void init() {

    if (currentPlayer.getId().equals("1")) {
      System.out.println("Você é o dono da sala para iniciar o jogo digite [iniciar]: ");

      String iniciar = null;

      while (!Objects.equals(iniciar, "iniciar")) {
        iniciar = input.nextLine();
        if (iniciar.equals("iniciar")) {
          try {
            this.out.writeObject(Status.STARTED);
          } catch (Exception e) {
            System.err.println("[CLIENT] Erro no send: " + e.getMessage());
            e.printStackTrace();
          }
        } else {
          System.out.print("Opção invalida, tente novamente!\n");
        }
      }
      new Thread(t1).start();
    } else {
      System.out.print("Aguardando o dono da sala iniciar o jogo...");
      do {
        try {
          System.out.print("Aguardando o dono da sala iniciar o jogo...\n");
          new Thread(t1).start();
          Thread.sleep(5000L);
        } catch (Exception e) {
          System.err.println("[CLIENT] Erro no send: " + e.getMessage());
          e.printStackTrace();
        }
      } while (status.equals(Status.WAIT));
    }
  }

  private void waitToStartGame() throws IOException, ClassNotFoundException {
    do {
      status = (Status) in.readObject();
    } while (!status.equals(Status.STARTED));
    System.out.println("[Client] iniciando jogo...");
  }

  private final Runnable t1 = () -> {
    try {
      waitToStartGame();
    } catch (Exception e) {
      System.err.println("[SERVER] Erro no answer: " + e.getMessage());
      e.printStackTrace();
    }
  };

  private void getPlayer() {
    try {
      this.in = new ObjectInputStream(socket.getInputStream());
      this.currentPlayer = (Player) in.readObject();
      System.out.println("Player " + currentPlayer.getName() + " criado com sucesso!");
    } catch (Exception e) {
      System.err.println("[CLIENT] Erro no send: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void setPlayer() {

    try {
      boolean valid = true;
      String name;

      do {

        System.out.print("Informe seu nome: ");
        name = input.nextLine();

        valid = name.isBlank();
        if (valid) {
          System.out.println("[ERRO] Nome não pode ser vazio.");
        }

        send(name);

      } while (valid);
    } catch (Exception e) {
      System.err.println("[CLIENT] Erro no send: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void send(String name) throws IOException {
    Player player = new Player();
    player.setName(name);
    this.out.writeObject(player);
  }

}
