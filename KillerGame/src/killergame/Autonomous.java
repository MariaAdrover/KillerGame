package killergame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Autonomous extends Alive {

    private BufferedImage[] img;
    private BufferedImage frame;
    private long riiing;

    public Autonomous(KillerGame killer, int state, int x, int y, int width, int height, int vX, int vY) {
        super(killer, state, x, y, width, height, vX, vY);
        this.img = new BufferedImage[2];
        loadImg();
        this.frame = img[0];
        riiing = System.currentTimeMillis();
    }

    private void loadImg() {
        try {
            this.img[0] = ImageIO.read(new File("nave3.png"));
            this.img[1] = ImageIO.read(new File("nave4.png"));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private void changeFrame() {
        if (this.frame == this.img[0]) {
            this.frame = this.img[1];
        } else {
            this.frame = this.img[0];
        }
        riiing = System.currentTimeMillis();
    }

    @Override
    public void run() {
        test();
        while (this.getState() > 0) {
            long nextFrameTime = System.currentTimeMillis() + 5;
            while ((System.currentTimeMillis() < nextFrameTime)) {
                //cambiar preguntar fotogramas
            }

            if (System.currentTimeMillis() - riiing > 450) {
                changeFrame();
            }

            move();
            test();
        }
    }

    @Override
    public void move() {
        if ((this.getY()) < 0 || (this.getY() + this.getHeight()) > this.getGame().getViewer().getHeight()) {
            // REBOTAR
            // REVISAR!!!! cambiar 1000 por variable o metodo segiun dimensiones pantalla
            // Se puede pasar a KillerRules
            this.setvY(-this.getvY());
        }

        int newX = this.getX() + this.getvX();
        this.setX(newX);
        int newY = this.getY() + this.getvY();
        this.setY(newY);
    }

    @Override
    public void render(Graphics g) {
        if (this.getState() != 0) {
            g.drawImage(this.frame, this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
        }
    }
}
