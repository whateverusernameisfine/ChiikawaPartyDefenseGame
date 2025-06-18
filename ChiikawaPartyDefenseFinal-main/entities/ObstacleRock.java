package entities;

import util.ImageLoader;
import java.awt.*;

public class ObstacleRock extends Obstacle {
    private Image image;

    public ObstacleRock(int gridX, int gridY) {
        super(gridX, gridY);
        this.image = ImageLoader.load("obstacle.png");
    }

    @Override
    public int getWeight(Zombie z) {
        return 9999; // Impassable
    }

    @Override
    public void draw(Graphics g, int pixelX, int pixelY) {
        g.drawImage(image, pixelX, pixelY, 100, 100, null);
    }
}

