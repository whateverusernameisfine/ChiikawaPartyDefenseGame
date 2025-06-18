package entities;

import util.ImageLoader;
import java.awt.*;

public class Walnut extends Plant {
    public Walnut(int x, int y) {
        super(x, y);
        this.health = 4000;
        this.name = "Walnut";
        this.cost = 25;
    }

    @Override
    public void update() {
        updateDying();
    }



    @Override
    public void draw(Graphics g) {
        Image img;

        if (isDying) {
            if (dyingTimer < dyingDuration) {
                img = ImageLoader.load("walnut_dead.gif");
                g.drawImage(img, x, y, width, height, null);
            } else {
                return;
            }
        } else if (health > 2000) {
            img = ImageLoader.load("walnut_full_life.gif");
        } else if (health > 0) {
            img = ImageLoader.load("walnut_half_life.gif");
        } else {
            return;
        }
        g.drawImage(img, x, y, width, height, null);
    }
}
