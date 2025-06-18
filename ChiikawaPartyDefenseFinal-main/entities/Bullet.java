package entities;

import java.awt.*;

public abstract class Bullet {
    protected int x, y;
    protected int speed = 5;
    protected int damage = 30; // default

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += speed;
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }

    public int getDamage() {
        return damage;
    }
}
