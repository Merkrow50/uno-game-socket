import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private final String HOST = "localhost";
  private final int PORT = 5000;

  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;

  private Scanner input;

  private Player currentPlayer = new Player();


  public Client() {
    this.input = new Scanner(System.in);
  } // fim do construtor()

  public static void main(String[] args) {
    new Client().start();
  } // fim do método main(String[])

  private void start() {
    connect();
    play();


  } // fim do método start()


  private void connect() {
    System.out.println("Conectando ao servidor...");
    try {
      this.socket = new Socket(HOST, PORT);
      System.out.println("Cliente conectado");

      this.out = new ObjectOutputStream(socket.getOutputStream());

    } catch (Exception e) {
      e.printStackTrace();
    }
  } // fim do método connect()

  private void send(String name) throws IOException {
    Player player = new Player();
    player.setName(name);
    this.out.writeObject(player);
  } // fim do método send(String)

  private void play() {

    try {

      do {
        setPlayer();
        getPlayer();
        run();

      } while (true);

    } catch (Exception e) {
      System.err.println("[CLIENT] Erro no send: " + e.getMessage());
      e.printStackTrace();
    }
  } // fim do método send()

  private void getPlayer() throws IOException, ClassNotFoundException {

    this.in = new ObjectInputStream(socket.getInputStream());
    this.currentPlayer = (Player) in.readObject();
    System.out.println(currentPlayer.toString() + "criado com sucesso!");

  }


  private void run() throws IOException {

    if (currentPlayer.getId().equals("1")) {

      System.out.println("Você é o dono da sala para iniciar o jogo digite [iniciar]: ");

      if (input.nextLine().equals("iniciar")) {
        System.out.print("Iniciando jogo...");
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          out.println("iniciar");
      }
    }

  }

  private void setPlayer() throws IOException {

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

  } // fim do método createPlayer()

}
