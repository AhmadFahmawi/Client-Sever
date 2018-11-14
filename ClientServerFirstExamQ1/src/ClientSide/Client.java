package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    public static void main(String[] args) {
        try {

            Socket clientSocket = new Socket(ServerSide.Server.ADDRESS, ServerSide.Server.PORT);
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            {
                boolean flag = false;
                int counter = 0;
                do {
                    System.out.println(counter + " : get a number: " + inputStream.readInt());
                    flag = inputStream.readBoolean();
                } while (!flag && ++counter < 10);

                System.out.println(inputStream.readUTF());

                if (counter < 10 && flag) {
                    outputStream.writeUTF("Happy to meet you.");
                    System.out.println(inputStream.readUTF());
                }
            }
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
