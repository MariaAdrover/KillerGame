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
    //private KillerRule rules;
    private final Viewer viewer;
    private Autonomous bola;
    private ArrayList<VisibleObject> objects;

    public Viewer getViewer() {
        return viewer;
    }

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

        // Crear DFHDGFH
        //this.rules = new KillerRule();

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
        int crashed = 0; // no hay colision
        int action = 0; // no hacer nada

        if (obj instanceof Autonomous) {
            crashed = testColision((Autonomous) obj);
        }

        if (crashed != 0) {
            action = KillerRule.requestRule(obj, crashed);
        }
        
        if (action != 0) {
            // Hacer otro metodo
            switch (action) {
                case -1:
                    sendAutonomousOutspace((Autonomous)obj, this.clienteIzq);
                    break;
                case 1:
                    sendAutonomousOutspace((Autonomous)obj, this.clienteDerecha);
                    break;
            }
        }

        /*
        if (obj instanceof Autonomous) {
            Autonomous a = ((Autonomous) obj);
            if (((a.getX() + a.getWidth() > 1920) && a.getvX() > 0)) {
                sendAutonomousOutspace(a, this.clienteDerecha);
                this.rules.test(obj, 3);
            } else if (a.getX() < 0) {
                sendAutonomousOutspace(a, this.clienteIzq);
                this.rules.test(obj, 3);
            }
        }*/
    }

    private int testColision(Autonomous a) {
        // devuelve por que lado ha chocado
        // cambiar por Object o
        // para que devuelva con que OBJETO ha chocado, i NO con que lado
        // 0 --> no ha chocado
        // -1 --> ha chocado con lado izquierdo
        // -1 --> ha chocado con lado derecho
        int crashed = 0;
        if (((a.getX() + a.getWidth() > 1920) && a.getvX() > 0)) {
            crashed = 1;
        } else if (a.getX() < 0) {
            crashed = -1;
        }

        return crashed;
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

    private void createClients() {

        // Crear cliente
        this.clienteDerecha = new KillerClient("192.168.1.46", 8888);
        this.clienteIzq = new KillerClient("192.168.1.46", 8888);

    }

    public void startGame() {
        (new Thread(this.server)).start();
        createClients();
        (new Thread(this.viewer)).start();
        for (VisibleObject o : this.objects) {
            (new Thread((Alive) o)).start();
        }
        JFXPanel jfxPanel = new JFXPanel();
        KillerRadio.playAudio("8-bit-Arcade4.wav");
    }

    public static void main(String[] args) {
        KillerGame game = new KillerGame();
        game.startGame();
        game.setVisible(true);
    }
}
