package entities;

import util.ImageLoader;

import java.awt.*;

public abstract class Obstacle {
    protected int gridX;
    protected int gridY;

    public Obstacle(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public abstract int getWeight(Zombie z); // movement cost
    public abstract void draw(Graphics g, int pixelX, int pixelY);
}
