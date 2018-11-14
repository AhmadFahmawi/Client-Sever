package ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmad Fahmawi
 */
public class Server {

    public static final int PORT = 2222;
    public static final String ADDRESS = "localhost";
    private static ServerSocket serverSocket;
    private static Socket[] clientSockets;
    private static final int NUMBER_OF_CLIENTS = 4;
    private static DataInputStream[] inputStreams;
    private static DataOutputStream[] outputStreams;
    private static ArrayList<Integer> partnersExchangeDone;
    private static int randomNumbers[];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Server Connected on port " + PORT);
        outputStreams = new DataOutputStream[NUMBER_OF_CLIENTS];
        inputStreams = new DataInputStream[NUMBER_OF_CLIENTS];
        randomNumbers = new int[NUMBER_OF_CLIENTS];
        partnersExchangeDone = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(PORT);
            {
                clientSockets = new Socket[NUMBER_OF_CLIENTS];
                initializeClients();
                {//build the problem solution...
                    for (int trail = 0; trail < 10; trail++) {
                        sendRandom();
                        cheakPartners();
                        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
                            if (!partnersExchangeDone.contains(i)) {
                                outputStreams[i].writeBoolean(false);
                            }
                        }
                    }
                    sendGoodPartners();
                }
                closeAllConnections();
            }
            serverSocket.close();
            System.out.println("Everything is cool, and the port " + PORT + " will be released in a few misc");
            System.out.println("Solved by Ahmad fahmawi");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void closeAllConnections() throws IOException {
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            clientSockets[i].close();
            inputStreams[i].close();
            outputStreams[i].close();
        }
    }

    private static void initializeClients() throws IOException {
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            clientSockets[i] = serverSocket.accept();
            inputStreams[i] = new DataInputStream(clientSockets[i].getInputStream());
            outputStreams[i] = new DataOutputStream(clientSockets[i].getOutputStream());
        }
    }

    private static void cheakPartners() throws IOException {
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            for (int j = i + 1; j < NUMBER_OF_CLIENTS; j++) {
                if (i == j) {
                    continue;
                }
                if (randomNumbers[i] == randomNumbers[j]
                        && !partnersExchangeDone.contains(i) && !partnersExchangeDone.contains(j)) {
                    partnersExchangeDone.add(i);
                    partnersExchangeDone.add(j);
                    outputStreams[i].writeBoolean(true);
                    outputStreams[j].writeBoolean(true);
                    partnersMessageExchange(i, j);
                    break;
                }

            }

        }
    }

    private static void sendRandom() throws IOException {
        Random r = new Random();
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            if (partnersExchangeDone.contains(i)) {
                continue;
            }
            int randomNumber = r.nextInt((10 - 1) + 1) + 1;
            outputStreams[i].writeInt(randomNumber);
            randomNumbers[i] = randomNumber;
        }
    }

    private static void partnersMessageExchange(int i, int j) throws IOException {
        outputStreams[i].writeUTF("Hi Client " + (i + 1)
                + " you are now is a partner with Client " + (j + 1)
                + System.getProperty("line.separator")
                + "Please say something to your partner");
        outputStreams[j].writeUTF("Hi Client " + (j + 1)
                + " you are now is a partner with Client " + (i + 1)
                + System.getProperty("line.separator")
                + "Please say something to your partner");

        String message1 = inputStreams[i].readUTF();
        String message2 = inputStreams[j].readUTF();
        outputStreams[i].writeUTF("Message from your partnrt"
                + System.getProperty("line.separator")
                + message1
        );
        outputStreams[j].writeUTF("Message from your partnrt"
                + System.getProperty("line.separator")
                + message2
        );

    }

    private static void sendGoodPartners() throws IOException {

        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            if (!partnersExchangeDone.contains(i)) {
                outputStreams[i].writeUTF("good luck");
            }
        }
    }

}
