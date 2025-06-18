package entities;

import game.GamePanel;
import util.ImageLoader;
import java.awt.*;

public class ConeheadZombie extends Zombie {
    // Animation constants
    private static final int FRAME_DELAY = 20;
    private static final int WALK_FRAMES = 2;
    private static final int CRY_FRAMES = 2;
    private static final int FRAME_WIDTH = 150;

    // Sprite sheets
    private final Image walkSheet;
    private final Image dyingSheet;

    // Animation state
    private int currentFrame = 0;
    private int frameTimer = 0;

    private enum State {
        WALK, DYING
    }

    private State currentState = State.WALK;

    public ConeheadZombie(int x, int y, GamePanel game) {
        super(x, y, game);
        this.health = 560;
        this.speed = 0.36;
        this.avoidMud = false;

        this.walkSheet = ImageLoader.load("MonsterBlue.png"); // 🎨 Add this
        this.dyingSheet = ImageLoader.load("MonsterDieSheet.png"); // 🎨 Add this
    }

    @Override
    public int getScoreValue() {
        return 2;
    }

    @Override
    public void update(java.util.List<Plant> plants) {
        super.update(plants);
        super.updateDying();

        if (isDying()) {
            currentState = State.DYING;
        }

        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            currentFrame++;

            switch (currentState) {
                case DYING -> currentFrame %= CRY_FRAMES;
                case WALK -> currentFrame %= WALK_FRAMES;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Image sheet = (currentState == State.DYING) ? dyingSheet : walkSheet;

        g.drawImage(
                sheet,
                (int) drawX, (int) drawY, (int) drawX + width, (int) drawY + height,
                currentFrame * FRAME_WIDTH, 0,
                (currentFrame + 1) * FRAME_WIDTH, height,
                null
        );
    }
}
