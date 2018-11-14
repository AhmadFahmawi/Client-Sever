package ServerSide;

import Model.ClientNode;
import Model.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    private static final int NUMBER_OF_CLIENTS = 5;
    private static DataInputStream[] inputStreams;
    private static DataOutputStream[] outputStreams;
    private static Graph graph;
    private static int[][] neighborsMatrix;
    private static ClientNode[] clientNodes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Server Connected on port " + PORT);
        outputStreams = new DataOutputStream[NUMBER_OF_CLIENTS];
        inputStreams = new DataInputStream[NUMBER_OF_CLIENTS];
        graph = new Graph(NUMBER_OF_CLIENTS);
        neighborsMatrix = new int[5][5];
        clientNodes = new ClientNode[NUMBER_OF_CLIENTS];

        try {
            serverSocket = new ServerSocket(PORT);
            {
                clientSockets = new Socket[NUMBER_OF_CLIENTS];
                initializeClients();
                {
                    buildNeighborsMatrix();
                    buildNeighbourRelations();
                    Graph.printGraph(graph);

                    changeColors();
                }
                closeConnections();
            }
            serverSocket.close();
            System.out.println("Everything is cool, and the port " + PORT + " will be released in a few misc");
            System.out.println("Solved by Ahmad fahmawi");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void buildNeighbourRelations() {
        for (int clientCounter = 0; clientCounter < NUMBER_OF_CLIENTS; clientCounter++) {
            for (int neighborCounter = 0; neighborCounter < NUMBER_OF_CLIENTS; neighborCounter++) {
                if (neighborsMatrix[clientCounter][neighborCounter] == 1) {
                    Graph.addEdge(graph, clientNodes[clientCounter], clientNodes[neighborCounter]);
                }
            }
        }
    }

    private static void buildNeighborsMatrix() {
        /*
        please note that, since client one is neighbor with client two,
        implestle client two is neighbor with client one
         */
        //neighhbors with client 1
        neighborsMatrix[0][1] = 1;
        neighborsMatrix[0][2] = 1;
        neighborsMatrix[0][3] = 1;

        //neighhbors with client 2
        neighborsMatrix[1][2] = 1;
        neighborsMatrix[1][4] = 1;

        //neighhbors with client 3
        neighborsMatrix[2][3] = 1;
        neighborsMatrix[2][4] = 1;

        //neighhbors with client 4
    }

    private static void closeConnections() throws IOException {
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
            clientNodes[i] = new ClientNode(i);
        }
    }

    private static void changeColors() throws IOException {
        boolean flag = false;
        ArrayList<Integer> coloredClient = new ArrayList();

        while (!flag) {
            for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
                int colorIndex = inputStreams[i].readInt();

                while (!coloredClient.contains(i)) {

                    if (Graph.coloring(graph, clientNodes[i], Color.values()[colorIndex])) {
                        outputStreams[i].writeUTF("Done");
                        outputStreams[i].writeBoolean(true);
                        coloredClient.add(i);

                    } else {
                        outputStreams[i].writeUTF("invaled Color, please wait for the next trail");
                        outputStreams[i].writeBoolean(false);
                        colorIndex = inputStreams[i].readInt();
                    }
                    System.out.println("client" + (i + 1) + " request color: " + Color.values()[colorIndex]);
                }
            }

            System.out.println("==================================================");
            Graph.printGraph(graph);
            flag = isColored();

        }

    }

    private static boolean isColored() {
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            if (clientNodes[i].getColor().equals(Color.notSet)) {
                return false;
            }
        }
        return true;

    }

}
