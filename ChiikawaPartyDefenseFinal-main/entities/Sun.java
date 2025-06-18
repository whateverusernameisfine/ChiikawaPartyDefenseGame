package entities;

import util.ImageLoader;
import java.awt.*;

public class Sun {
    private int x, y;
    private int size = 60;
    private Image image;
    private boolean stationary;

    public Sun(int x, int y, boolean stationary) {
        this.x = x;
        this.y = y;
        this.stationary = stationary;
        image = ImageLoader.load("candy.png");
    }

    public void update() {
        if (!stationary && y < 630) {  // Stop at y = 500 (adjust to fit your layout)
            y += 2;
        }
    }


    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }

    public int getY() {
        return y;
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + size && my >= y && my <= y + size;
    }
}