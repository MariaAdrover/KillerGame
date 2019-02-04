package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Viewer extends Canvas implements Runnable {

    private BufferedImage espacio;
    private BufferedImage frame;
    private int width;
    private int height;
    private Graphics frameGraphics;
    private final KillerGame game;

    public Viewer(KillerGame game) {
        super();
        this.game = game;
        this.calculateSize();
        this.createBackground();
        this.createFrameImage();
        this.setSize(this.width, this.height);
        this.setBackground(Color.blue);
        this.setVisible(true);
    }

    private void createBackground() {
        try {
            this.espacio = ImageIO.read(new File("espacio.jpg"));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private void createFrameImage() {
        this.frame = new BufferedImage(this.width, this.height, TYPE_3BYTE_BGR);
        this.frameGraphics = this.frame.getGraphics();
    }

    private void calculateSize() {
        /*
        this.width = 1920;
        this.height = 1000;*/
        // java - get screen size using the Toolkit class
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = screenSize.width;
        this.height = screenSize.height - ((screenSize.height * 7) / 100);
    }

    private void pintarFrame() {
        frameGraphics.fillRect(0, 0, this.width, this.height);
        frameGraphics.drawImage(espacio, 0, 0, this.width, this.height, this);
        frameGraphics.setColor(Color.white);
        int a = 0;//QUITAR
        for (KillerServerHandler h : game.getServer().getClients()) {
           frameGraphics.drawString(h.getClientAddress(), 1800, 900 + a);
           a+=15;
        }

        for (VisibleObject o : (game.getObjects())) {
            o.render(frameGraphics);
        }

        this.getGraphics().drawImage(frame, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        //g.drawImage(frame, 0, 0, this);
    }

    @Override
    public void run() {
        while (true) {
            this.pintarFrame();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {

            }
        }
    }
}
