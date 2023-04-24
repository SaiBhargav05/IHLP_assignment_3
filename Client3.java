import java.io.*;
import java.net.*;
import java.util.*;

public class Client3 {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 4000;
    private static final String[] CARD_VALUES = {"ace","2","3","4","5","6","7","8","9","10","jack","queen","king"};
    private static String pick = "";
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("client3 Connected to server");

            Scanner scanner = new Scanner(System.in);
            for (int round = 1; round <= 26; round++) {
                String message = in.readLine();
                System.out.println(message);

                String pick = CARD_VALUES[new Random().nextInt(CARD_VALUES.length)];
                out.println(pick);
                System.out.println("client3 Picked " + pick);
            }
                
                
            
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
