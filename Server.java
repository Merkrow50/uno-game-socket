import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 5000;

    private ArrayList<Player> players;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    } // fim do método main(String[])

    private void start() {
        System.out.println("[SERVER] Iniciando o servidor...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Servidor iniciado");

            while (true) {
                System.out.println("[SERVER] Aguardando conexão...");
                Socket socket = serverSocket.accept();
                System.out.println("[SERVER] Conexão aceita");

                answer(socket);

            }
        } catch (Exception e) {
            System.err.println("[SERVER] Erro no start: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método start()

    private void registry(Player player, PrintWriter out) {

        System.out.println("[SERVER] Registrando jogador...");

        if (this.players.size() >= 10) {
            System.err.println("[SERVER] ERRO: Limite de jogadores atingido");
            out.printf("Limite de jogadores atingido");
        } else {
            this.players.add(player);
            System.out.println("[SERVER] Jogador registrado [" + players.size() + "/10]");
            out.printf("Jogador registrado [%s!]\n", player.getName());
        }

    }

    private void answer(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Player player = (Player) in.readObject();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            registry(player, out);

            out.printf("Servidor respondeu: Hello, %s!\n", player.getName());

        } catch (Exception e) {
            System.err.println("[SERVER] Erro no answer: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método answer(Socket)


}
