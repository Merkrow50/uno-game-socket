import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public Client() {
        try {
            socket = new Socket(HOST, PORT);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Insira seu nome: ");
            String playerName = in.readLine();
            outputWriter.println(playerName);

            Thread clientThread = new Thread(new ClientThread(inputReader));
            clientThread.start();

            String message;
            while ((message = in.readLine()) != null) {
                outputWriter.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
