package entities;

import util.ImageLoader;
import java.awt.*;

public class Pea extends Bullet {
    public Pea(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        Image img = ImageLoader.load("ChiikawaBullet.png");
        g.drawImage(img, x, y, 100, 100, null);
    }
}
