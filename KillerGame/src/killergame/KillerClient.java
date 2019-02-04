package killergame ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class KillerClient {
    private String id;
    private BufferedReader in;
    private PrintWriter out;
 
    public KillerClient(String ip, int port){
        System.out.println("constructor cliente");
        try {
            System.out.println("Client request connection to " + ip + " in PORT " + port);
            Socket socket = new Socket(ip, port);
            // Get I/O streams from the socket 
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            //this.id = in.readLine();
            //System.out.println("nuevo cliente. ID = " + this.id);
            //clientSocket.close();
        } catch (IOException e) {
            System.out.println("ERROR creating client connection");
            System.out.println(e);
        }
    }
    
    public void sendData(String data) {
        out.println(data);
    }
}
