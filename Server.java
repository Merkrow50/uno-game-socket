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

    private Game game;

    private ObjectOutputStream out;

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

                this.out = new ObjectOutputStream(socket.getOutputStream());

                answer(socket);

                play(socket);

            }
        } catch (Exception e) {
            System.err.println("[SERVER] Erro no start: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método start()

    private void play(Socket socket) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String start = in.readLine();

        if (start.equals("iniciar")) {
            playRounds(socket);
            declareWinner();
        }
    }

    public void playRounds(Socket socket) throws IOException {
        game = new Game(players, socket);
        while (game.roundsPlayed <= 10 && !game.isGameOver()) {
            // Display the round number
            System.out.println("ROUND " + game.roundsPlayed);

            // Display each player's hand
            displayHands();

            // Play individual round
            game.start(game);


            // Increment roundsPlayed counter
            game.roundsPlayed++;
        }
    }




    // Displays each player's current hand
    public void displayHands() {

        final String[] displayedHands = {""};

        players.forEach(x -> {
            displayedHands[0] = displayedHands[0].concat(x.displayHand());
        });

       PrintWriter message = new PrintWriter(out, true);
       message.println(displayedHands[0]);
    }

    // Declare a winner
    public void declareWinner() {
        for (int i = 0; i < this.players.size(); i++){

            if (players.get(i).getHandSize() == 0){
                System.out.println(players.get(i).getName().toUpperCase() + " WINS!");
            }
        }
    }



    private void registry(Player player) throws IOException {

        System.out.println("[SERVER] Registrando jogador...");

        if (this.players.size() >= 10) {
            System.err.println("[SERVER] ERRO: Limite de jogadores atingido");
        } else {
            player.setId(String.valueOf(players.size() + 1));
            this.players.add(player);
            System.out.println("[SERVER] Jogador registrado [" + players.size() + "/10] " + player.getId() + ":" + player.getName());
            this.out.writeObject(player);
        }

    }

    private void answer(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Player player = (Player) in.readObject();
            registry(player);

        } catch (Exception e) {
            System.err.println("[SERVER] Erro no answer: " + e.getMessage());
            e.printStackTrace();
        }
    } // fim do método answer(Socket)


}
