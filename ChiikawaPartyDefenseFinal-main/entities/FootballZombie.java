package entities;

import game.GamePanel;
import util.ImageLoader;
import java.awt.*;

public class FootballZombie extends Zombie {
    private static final int FRAME_DELAY = 20;
    private static final int WALK_FRAMES = 2;
    private static final int DYING_FRAMES = 2;
    private static final int FRAME_WIDTH = 150;

    private final Image walkSheet;
    private final Image dyingSheet;

    private int currentFrame = 0;
    private int frameTimer = 0;

    private enum State {
        WALK, DYING
    }

    private State currentState = State.WALK;

    public FootballZombie(int x, int y, GamePanel game) {
        super(x, y, game);
        this.health = 1100;
        this.speed = 1.5;
        this.avoidMud = true;
        this.walkSheet = ImageLoader.load("MonsterRed.png");
        this.dyingSheet = ImageLoader.load("MonsterDieSheet.png");
    }

    @Override
    public int getScoreValue() {
        return 3;
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
                case WALK -> currentFrame %= WALK_FRAMES;
                case DYING -> currentFrame %= DYING_FRAMES;
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
