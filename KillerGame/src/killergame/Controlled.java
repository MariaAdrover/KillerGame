package killergame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Controlled extends Alive {

    private BufferedImage[] img;
    private BufferedImage frame;
    private long riiing;
    private String pilot;
    private boolean[] controls;
    //pONER CONTADOR, 5s AL SER CREADO PARA EVITAR COLISION

    public Controlled(KillerGame killer, int state, int x, int y, int width, int height, int vX, int vY, String pilot) {
        super(killer, state, x, y, width, height, vX, vY);
        this.pilot = pilot;
        this.img = new BufferedImage[2];
        loadImg();
        this.frame = img[0];
        this.riiing = System.currentTimeMillis();
        this.controls = new boolean[4];
    }

    public void setControls(int direction) {
        this.controls[direction] = !this.controls[direction];
    }

    private void loadImg() {
        try {
            this.img[0] = ImageIO.read(new File("nave1.png"));
            this.img[1] = ImageIO.read(new File("nave2.png"));
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
        setVelocity();
        setPosition();
    }

    private void setVelocity() {
        if (controls[0] && !controls[1]) {
            this.setvX(-1);
        } else if (controls[1] && !controls[0]) {
            this.setvX(1);
        } else {
            this.setvX(0);
        }

        if (controls[2] && !controls[3]) {
            this.setvY(-1);
        } else if (controls[3] && !controls[2]) {
            this.setvY(1);
        } else {
            this.setvY(0);
        }
    }

    private void setPosition() {
        this.setX((this.getX()) + this.getvX());
        this.setY((this.getY()) + this.getvY());

        // NO TRASPASAR ARRIBA NI ABAJO
        // REVISAR!!!! cambiar 1000 por variable o metodo segiun dimensiones pantalla
        // Se puede pasar a KillerRules
        if (this.getY() < 0) {
            this.setY(0);
        } else if (this.getY() + this.getHeight() > 1000) {
            this.setY(860);
        }
    }

    @Override
    public void render(Graphics g) {
        if (this.getState() != 0) {
            g.drawImage(this.frame, this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
        }
    }
}
