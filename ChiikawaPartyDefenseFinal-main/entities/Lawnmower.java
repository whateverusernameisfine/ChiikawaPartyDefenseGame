package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Lawnmower {
    private int x, y;
    private int width = 60, height = 60;
    private boolean active = false;
    private boolean used = false;
    private boolean triggered = false; // NEW: tracks if mower was activated (used for scoring)
    private int speed = 10;

    public Lawnmower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(List<Zombie> zombies) {
        if (active) {
            x += speed;

            for (Zombie z : zombies) {
                if (!z.isDying && Math.abs(z.getY() - y) <= 20 && z.getBounds().intersects(getBounds())) {
                    z.startDying();
                }
            }

            if (x > 1345) {
                used = true;
                active = false;
            }
        }
    }


    public void activate() {
        if (!used && !active) {
            active = true;
            triggered = true;
        }
    }

    public boolean wasTriggered() {
        return triggered;
    }

    public boolean isUsed() {
        return used;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        Image img = ImageLoader.load("lawn_mower.gif");
        g.drawImage(img, x, y, width, height, null);
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }
}
