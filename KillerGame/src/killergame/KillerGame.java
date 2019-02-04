package killergame;

import java.awt.Container;
import java.util.ArrayList;
import javafx.embed.swing.JFXPanel;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class KillerGame extends JFrame {

    private final KillerServer server;
    private KillerClient clienteDerecha;
    private KillerClient clienteIzq;
    private final KillerRule rules;
    private final Viewer viewer;

    public Viewer getViewer() {
        return viewer;
    }
    private Autonomous bola;
    private ArrayList<VisibleObject> objects;

    public ArrayList<VisibleObject> getObjects() {
        return objects;
    }

    public KillerServer getServer() {
        return server;
    }

    public Autonomous getBola() {
        return bola;
    }

    public KillerGame() {
        this.setTitle("01001011.01000111");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(0, 0);
        this.objects = new ArrayList<>();

        // Crear server
        this.server = new KillerServer(this, 8888);

        // Crear cliente
        this.rules = new KillerRule();

        // Crear bola
        this.objects.add(new Autonomous(this, 1, 0, 400, 100, 140, 1, 2));
        this.viewer = new Viewer(this);
        Container c = this.getContentPane();
        c.add(viewer);

        //this.radio = new KillerRadio();
        this.setResizable(false);
        //this.setUndecorated(true);
        this.pack();
    }

    public Controlled crearNave(int width, int height, int vX, int vY, String pilot) {
        System.out.println("NAVE vaaaa");
        int x = (this.viewer.getWidth() / 2) - width / 2;
        int y = this.viewer.getHeight() - (height + 50);

        Controlled nave = new Controlled(this, 1, x, y, width, height, vX, vY, pilot);
        this.objects.add(nave);
        (new Thread(nave)).start();
        System.out.println("SIZEEEEEE: " + this.objects.size());

        return nave;
    }

    public void crearBola(int yPercent, int width, int height, int vX, int vY) {
        int x = 0;
        int y = (yPercent * this.viewer.getHeight()) / 100;
        System.out.println("yPercent = " + yPercent);
        System.out.println("y = " + y);
        if (vX < 0) {
            x = this.viewer.getWidth();
        }

        this.objects.add(new Autonomous(this, 1, x, y, width, height, vX, vY));
        (new Thread((Alive) this.objects.get(this.objects.size() - 1))).start();
        System.out.println("y = " + ((Autonomous) (this.objects.get(this.objects.size() - 1))).getY());
        System.out.println("SIZEEEEEE: " + this.objects.size());
    }

    public void test(VisibleObject obj) {
        // poner test para todos los objetos
        // para los controlled si se salen, sendControlledOutspace(obj, -1 si sale x la izq, o 1 si sale por la derecha)
        if (obj instanceof Autonomous) {
            Autonomous a = ((Autonomous) obj);
            // vaya kk cambiar
            // mirar colision, rules etc.
            // Todo eesto deberia hacerse desde las rules
            // Han de tener acceso a los clientes
            if (((a.getX() + a.getWidth() > 1920) && a.getvX() > 0)) {
                sendAutonomousOutspace(a, this.clienteDerecha);
                this.rules.test(obj, 3);
            } else if (a.getX() < 0) {
                sendAutonomousOutspace(a, this.clienteIzq);
                this.rules.test(obj, 3);
            }
        }
    }

    private void sendControlledOutspace(Controlled c, int server) {
        //Añadir handler al objeto
        String requestIP;
        if (server == -1) {
            requestIP = "ip del servidor de la izquierda";
        } else { // enviar 1
            requestIP = "ip del servidor de la derecha";
        }
        //c.getHandler().sendControlledOut(
        this.objects.remove(c);
    }

    private void sendAutonomousOutspace(Autonomous a, KillerClient client) {
        System.out.println("y = " + a.getY());
        int yPercent = (a.getY() * 100) / this.viewer.getHeight();
        System.out.println("percent= " + yPercent);
        client.sendData("new="
                + yPercent + "&"
                + a.getWidth() + "&"
                + a.getHeight() + "&"
                + a.getvX() + "&"
                + a.getvY());
        this.objects.remove(a);
    }

    private void createClients() {

        // Crear cliente
        this.clienteDerecha = new KillerClient("192.168.1.67", 8888);
        this.clienteIzq = new KillerClient("192.168.1.67", 8888);

    }

    public void startGame() {
        (new Thread(this.server)).start();
        (new Thread(this.viewer)).start();
        for (VisibleObject o : this.objects) {
            (new Thread((Alive) o)).start();
        }
        createClients();
        JFXPanel jfxPanel = new JFXPanel();
        KillerRadio.playAudio("8-bit-Arcade4.wav");
    }

    public static void main(String[] args) {
        KillerGame game = new KillerGame();
        game.startGame();
        game.setVisible(true);
    }
}
