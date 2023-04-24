import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private static final String[] CARD_VALUES = {"ace","2","3","4","5","6","7","8","9","10","jack","queen","king"};
    private static final int NUM_CARDS = CARD_VALUES.length;

    private static List<String> cardList = new ArrayList<>(Arrays.asList(CARD_VALUES));
    private static Map<Socket, String> clientPicks = new HashMap<>();
    private static int numCardsPicked = 0;

    public static void main(String[] args) throws IOException {
        int portNumber = 4000;
        int round = 1;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started on port " + portNumber);

            while(numCardsPicked < 26) {
                Socket[] clientSockets = new Socket[3];
                for(int i=0; i<3; i++) {
                    clientSockets[i] = serverSocket.accept();
                    System.out.println("Client " + (i+1) + " connected: " + clientSockets[i]);
                }
                for(int j=1;j<28;j++)
                {
                startRound(round, clientSockets);
                round++;
            }
            }
        }
        catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(1);
        }
    }

    private static void startRound(int round, Socket[] clientSockets) throws IOException {
        String selectedCard = cardList.remove(new Random().nextInt(cardList.size()));
        System.out.println("Round " + round + ": The selected card is " + selectedCard);
        numCardsPicked++;

        // Map to keep track of client picks and scores
       Map<Socket, String> clientPicks = new HashMap<>();
        Map<Socket, Integer> clientScores = new HashMap<>();

        for(Socket clientSocket : clientSockets) {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("Round " + round + ": The selected card is " + selectedCard + ". Please pick a card.");
            String pick = in.readLine();
            clientPicks.put(clientSocket, pick);
            int score = getCardScore(pick);
            clientScores.put(clientSocket, score);
        }

        // Print out each client's score
        System.out.println("Round " + round + " scores:");
        int maxScore = -1;
        for(Socket clientSocket : clientSockets) {
            int score = clientScores.get(clientSocket);
            System.out.println("Client " + clientSocket + " score: " + score);
            if(score > maxScore) {
                maxScore = score;
            }
        }

        List<Socket> winners = new ArrayList<>();
        for(Socket clientSocket : clientSockets) {
            int score = clientScores.get(clientSocket);
            if(score == maxScore) {
                winners.add(clientSocket);
            }
        }
        
        String winnerMessage = "Round " + round + ": The winners are ";
        for(Socket clientSocket : clientSockets) {
        	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            if(winners.contains(clientSocket)) {
               out.println(winnerMessage + "you! Your pick was " + clientPicks.get(clientSocket));
            } else {
                out.println("you are not the winner");
            }
        }
    }
    private static int getCardScore(String cardValue) {
        switch(cardValue) {
            case "ace":
                return 1;
            case "jack":
                return 11;
            case "queen":
                return 12;
            case "king":
                return 13;
            default:
                return Integer.parseInt(cardValue);
        }
    }
}