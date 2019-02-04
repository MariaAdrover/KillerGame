package killergame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class KillerServer implements Runnable {
    private KillerGame game;
    private ArrayList<KillerServerHandler> clients;
    private ServerSocket serverSocket;

    public KillerServer(KillerGame game, int port) {
        this.game = game;
        this.clients = new ArrayList<>();
        
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<KillerServerHandler> getClients() {
        return clients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for a client...");
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                KillerServerHandler cliente = new KillerServerHandler(game, clientSocket, clientAddress);
                System.out.println("ACCEPTED");
                System.out.println(clientAddress);
                this.clients.add(cliente);
                Thread t = new Thread(cliente);
                t.start();
                System.out.println("Accepted client request ip:" + clientAddress + "\n");
                System.out.println("---");
                System.out.println("soy");
                cliente.sendSelfIp();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
