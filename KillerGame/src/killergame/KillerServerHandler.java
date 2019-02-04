package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author miaad
 */
public class KillerServerHandler implements Runnable {

    private KillerGame game;
    private Socket socket;
    private String clientAddress;
    private boolean connected;
    private BufferedReader in;
    private PrintWriter out;
    private Controlled nave; // CAMBIAR - SOLO PRUEBA

    public String getClientAddress() {
        return clientAddress;
    }

    public KillerServerHandler(KillerGame game, Socket socket, String clientAddress) {
        this.game = game;
        this.socket = socket;
        this.clientAddress = clientAddress;
        this.connected = true;

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {

        }
    }

    public void sendSelfIp() {
        System.out.println("soy" + clientAddress);
        this.out.println(clientAddress);
    }

    private void processMessage() {
        try {
            String data = in.readLine();
            out.println("ok");
            System.out.println("client: " + data);
            processHeader(data);

        } catch (IOException e) {
            System.out.println("ERROR al leer datos");
            System.out.println("Connection lost with " + clientAddress);
            System.out.println(e);
            this.connected = false;
        }
    }

    private void processHeader(String data) {
        System.out.println("procesando header");
        //Cambiar en el mando y poner headers...
        String header = "up";
        if (data.length() >= 3) {
            header = data.substring(0, 3);
        }
        System.out.println(header);

        try {
            switch (header) {
                case "new":
                    createAutonomous(data.substring(4));
                    break;
                case "act":
                    if (this.nave == null) {
                        createControlled();
                    }
                    break;
                default:
                    if (this.nave != null) {
                        moveControlled(data);
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println("ERROR al procesar la cabecera");
            System.out.println("Connection lost with " + clientAddress);
            System.out.println(e);
            e.printStackTrace();
            this.connected = false;
        }

    }

    private void moveControlled(String data) {
        switch (data) {
            case "left":
                this.nave.setControls(0);
                break;
            case "right":
                this.nave.setControls(1);
                break;
            case "up":
                this.nave.setControls(2);
                break;
            case "down":
                this.nave.setControls(3);
                break;
        }
    }

    private void createControlled() {
        System.out.println("HANDLER: nave");
        this.nave = this.game.crearNave(100, 140, 0, 0, clientAddress);
    }

    private void createAutonomous(String data) {
        System.out.println(data);
        System.out.println("HANDLER: crearBola");
        String d[] = data.split("&");
        int y = Integer.parseInt(d[0]);
        int width = Integer.parseInt(d[1]);
        int height = Integer.parseInt(d[2]);
        int vX = Integer.parseInt(d[3]);
        int vY = Integer.parseInt(d[4]);
        System.out.println(y + "-" + width + "-" + height + "-" + vX + "-" + vY);
        this.game.crearBola(y, width, height, vX, vY);
    }

    private void moveObject(String data) {
        System.out.println("HANDLER: cambiar direccion");
        int vX = this.game.getBola().getvX();
        this.game.getBola().setvX(-vX);
    }

    private void sendControlledOut(String requestIP, Controlled c) {
        //repasar parametros
        // En lugar de Y calcular el porcentaje
        // Enviar desde el mando con un comando diferente, que no sea act, 
        //la nave ha de continuar, no ha de aparecer en medio... 
        //no hay que esperar a que se apriete el boton
        out.println("out=" + requestIP + "&" + c.getX() + "&" + c.getY() + "&" + c.getWidth() + "&" + c.getHeight());

    }

    @Override
    public void run() {
        while (connected) {
            processMessage();
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("ERROR al cerrar el socket.");
            System.out.println(clientAddress);
            System.out.println(e);
        }
    }

}
