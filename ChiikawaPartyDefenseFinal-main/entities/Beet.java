package entities;

import java.awt.*;
import util.ImageLoader;

public class Beet extends Bullet {
    public Beet(int x, int y) {
        super(x, y);
        this.speed = 3; // slightly slower but deals more damage
        this.damage = 80; // stronger
    }

    @Override
    public void draw(Graphics g) {
        Image img = ImageLoader.load("HachiwareBullet.png");
        g.drawImage(img, x, y, 104, 104, null);
    }
}
