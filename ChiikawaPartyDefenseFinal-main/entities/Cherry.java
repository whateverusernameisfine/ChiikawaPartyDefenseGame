package entities;

import util.ImageLoader;

import java.awt.*;
import java.util.List;

public class Cherry extends Plant {
    private Image explosionSheet;
    private final int EXPLOSION_FRAMES = 3;
    private final int FRAME_WIDTH = 125; // width of one frame in explosion sheet
    private final int FRAME_DELAY = 15;

    private int currentFrame = 0;
    private int frameTimer = 0;
    private boolean damageApplied = false;

    public Cherry(int x, int y) {
        super(x, y);
        this.health = 9999; // Cannot be damaged
        this.cost = 150;
        this.explosionSheet = ImageLoader.load("UsagiExplosionSheet.png"); // must have 3 horizontal frames
    }

    @Override
    public void update() {
        // Handle animation timing
        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            currentFrame++;
        }
    }

    public void affectZombies(List<Zombie> zombies) {
        // Damage only once, during first frame
        if (!damageApplied && currentFrame == 2) {
            Rectangle explosionArea = new Rectangle(x - 80, y - 80, width + 160, height + 160);
            for (Zombie z : zombies) {
                if (z.getBounds().intersects(explosionArea) && !z.isDying()) {
                    z.takeDamage(9999); // or z.takeDamage(...);
                }
            }
            damageApplied = true;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (currentFrame < EXPLOSION_FRAMES) {
            g.drawImage(
                    explosionSheet,
                    x, y, x + width, y + height,
                    currentFrame * FRAME_WIDTH, 0,
                    (currentFrame + 1) * FRAME_WIDTH, height,
                    null
            );
        }
    }

    @Override
    public boolean isMarkedForRemoval() {
        return currentFrame >= EXPLOSION_FRAMES;
    }
}
