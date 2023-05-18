import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread implements Runnable {

  private final BufferedReader inputReader;

  public ClientThread(BufferedReader inputReader) {
    this.inputReader = inputReader;
  }

  @Override
  public void run() {
    try {
      String serverMessage;
      while ((serverMessage = inputReader.readLine()) != null) {
        System.out.println(serverMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
