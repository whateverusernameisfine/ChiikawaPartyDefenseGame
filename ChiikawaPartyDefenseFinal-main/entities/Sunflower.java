package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Sunflower extends Plant {
    private static final int SUN_INTERVAL_FRAMES = 800;
    private static final int FRAME_DELAY = 10;

    private int sunTimer = 0;
    private int currentFrame = 0;
    private int frameTimer = 0;

    // Animation states
    private enum State {
        IDLE, PRODUCE, DYING
    }

    private State currentState = State.IDLE;

    // Animation sheets
    private final Image idleSheet = ImageLoader.load("MomongaIdleSheet.png");
    private final Image produceSheet = ImageLoader.load("MomongaProduceSheet.png");
    private final Image crySheet = ImageLoader.load("MomongaCrySheet.png");

    // Frame setup
    private final int IDLE_FRAMES = 2;
    private final int PRODUCE_FRAMES = 5;
    private final int CRY_FRAMES = 2;

    private final int FRAME_WIDTH = 125; // assuming all sheets are same width per frame

    public Sunflower(int x, int y) {
        super(x, y);
        this.health = 300;
        this.name = "Sunflower";
        this.cost = 50;
    }

    public void update(List<Sun> suns) {
        updateDying();

        if (isDying) {
            currentState = State.DYING;
        } else {
            sunTimer++;
            if (currentState == State.IDLE && sunTimer >= SUN_INTERVAL_FRAMES) {
                currentState = State.PRODUCE;
                currentFrame = 0;
                frameTimer = 0;
            }

            if (currentState == State.PRODUCE && currentFrame == PRODUCE_FRAMES - 1 && frameTimer == 0) {
                suns.add(new Sun(x + 20, y + 10, true));
                sunTimer = 0;
                currentState = State.IDLE;
                currentFrame = 0;
            }
        }

        // Animate
        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            currentFrame++;

            switch (currentState) {
                case DYING -> currentFrame %= CRY_FRAMES;
                case PRODUCE -> {
                    if (currentFrame >= PRODUCE_FRAMES) {
                        currentFrame = PRODUCE_FRAMES - 1; // stay on last until sun is spawned
                    }
                }
                case IDLE -> currentFrame %= IDLE_FRAMES;
            }
        }
    }

    @Override
    public void update() {
        updateDying();
    }

    @Override
    public void draw(Graphics g) {
        Image sheet;
        int frameCount;

        switch (currentState) {
            case DYING -> sheet = crySheet;
            case PRODUCE -> sheet = produceSheet;
            case IDLE -> sheet = idleSheet;
            default -> sheet = idleSheet;
        }

        g.drawImage(
                sheet,
                x, y, x + width, y + height,
                currentFrame * FRAME_WIDTH, 0,
                (currentFrame + 1) * FRAME_WIDTH, height,
                null
        );
    }

    public boolean isDead() {
        return health <= 0 && !isDying;
    }
}
