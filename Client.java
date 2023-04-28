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


    public Client() throws IOException {
        this.input = new Scanner(System.in);
        this.in = new ObjectInputStream(socket.getInputStream());
    } // fim do construtor()

    public static void main(String[] args) throws IOException {
        new Client().start();
    } // fim do método main(String[])

    private void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            play(socket);
        } catch (Exception e) {
            System.err.println("[CLIENT] Erro no start: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método start()

    private void play(Socket socket) {
        try {

            do {
                createPlayer();
                run();

            } while (true);


        } catch (Exception e) {
            System.err.println("[CLIENT] Erro no send: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método send()


    private void run(){

        if(currentPlayer.getId().equals("1")) {
            if(input.nextLine().equals("iniciar")){
                System.out.print("Iniciando jogo...");
            }
        }

    }


    private void createPlayer() throws IOException, ClassNotFoundException {

        boolean valid = true;

        do {
            System.out.print("Informe seu nome: ");
            currentPlayer.setName(input.nextLine());

            valid = !currentPlayer.getName().isBlank();
            if (!valid) {
                System.out.println("[ERRO] Nome não pode ser vazio.");
            }

            this.out.writeObject(currentPlayer);

        } while (!valid);


        this.currentPlayer = (Player) in.readObject();
        System.out.println("Olá " + currentPlayer.toString());

    } // fim do método createPlayer()

}
