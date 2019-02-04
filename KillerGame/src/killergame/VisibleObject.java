package killergame ;

import java.awt.Graphics;

public class VisibleObject implements Renderizable, Testable {
    private KillerGame game;
    private int state;

    public KillerGame getGame() {
        return game;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    private int x;
    private int y;
    private int width;
    private int height;
    
    public VisibleObject(KillerGame killer, int state, int x, int y, int width, int height) {
        this.game = killer;
        this.state = state;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;                
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
 
    public VisibleObject(){
        super();
    }

    @Override
    public void render(Graphics g) {
        
    }

    @Override
    public void test() {
        game.test(this);
    }
}
