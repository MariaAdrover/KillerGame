package killergame;

public class Alive extends VisibleObject implements Runnable, Movable {

    private int vX;
    private int vY;

    public int getvX() {
        return vX;
    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public int getvY() {
        return vY;
    }

    public void setvY(int vY) {
        this.vY = vY;
    }

    public Alive(KillerGame killer, int state, int x, int y, int width, int height, int vX, int vY) {
        super(killer, state, x, y, width, height);
        this.vX = vX;
        this.vY = vY;
    }

    @Override
    public void run() {
        
    }

    @Override
    public void move() {

    }
}
