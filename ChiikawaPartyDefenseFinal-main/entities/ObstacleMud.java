package entities;

import util.ImageLoader;

import java.awt.*;

public class ObstacleMud extends Obstacle {
    private Image image;

    public ObstacleMud(int gridX, int gridY) {
        super(gridX, gridY);
        this.image = ImageLoader.load("mud.png");
    }

    @Override
    public int getWeight(Zombie z) {
        return z.avoidMud ? 5 : 1;
    }
    @Override
    public void draw(Graphics g, int pixelX, int pixelY) {
        g.drawImage(image, pixelX, pixelY, 100, 100, null);
    }
}
