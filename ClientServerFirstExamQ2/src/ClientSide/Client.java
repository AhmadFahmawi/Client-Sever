package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmad Fahmawi
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {

            Socket clientSocket = new Socket(ServerSide.Server.ADDRESS, ServerSide.Server.PORT);
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            {

                int attempts = doIt(inputStream, outputStream);
                System.out.println("Finaly i got my color in " + attempts + " attempts");
            }
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static int doIt(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {

        Random r = new Random();

        int attempts = 0;
        boolean flag = false;
        while (!flag) {

            int randomColorIndex = r.nextInt((3 - 1) + 1);
            outputStream.writeInt(randomColorIndex);
            System.out.println("need color " + randomColorIndex);
            String respons = inputStream.readUTF();
            System.out.println(respons);
            flag = inputStream.readBoolean();
            attempts++;
        }
        return attempts;
    }

}
