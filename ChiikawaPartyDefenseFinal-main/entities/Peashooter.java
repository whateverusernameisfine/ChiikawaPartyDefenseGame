package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Peashooter extends Plant {
    private static final int SHOOT_INTERVAL_FRAMES = 90; // 2 seconds if 60 FPS
    private static final int FRAME_DELAY = 10;

    private boolean hasTarget = false;
    private boolean inAttackCycle = false;
    private int bulletCooldown = 0;

    // Animation
    private int currentFrame = 0;
    private int frameTimer = 0;

    private Image idleSheet;
    private Image attackSheet;
    private Image crySheet;

    private final int IDLE_FRAMES = 2;
    private final int ATTACK_FRAMES = 2;
    private final int CRY_FRAMES = 6; // Updated from 4 to 6

    private final int IDLE_FRAME_WIDTH = 125;
    private final int ATTACK_FRAME_WIDTH = 125;
    private final int CRY_FRAME_WIDTH = 125;

    private enum State {
        IDLE, ATTACK, DYING
    }

    private State currentState = State.IDLE;

    public Peashooter(int x, int y) {
        super(x, y);
        this.health = 300;
        this.name = "Peashooter";
        this.cost = 100;

        idleSheet = ImageLoader.load("ChiikawaIdleSheet.png");
        attackSheet = ImageLoader.load("ChiikawaAttackSheet.png");
        crySheet = ImageLoader.load("ChiikawaCrySheet.png");
    }

    public void update(List<Bullet> bullets, List<Zombie> zombies) {
        updateDying();

        if (isDying) {
            currentState = State.DYING;
        } else {
            // Check for zombies in row
            hasTarget = false;
            for (Zombie z : zombies) {
                if (!z.isDead() && Math.abs(z.getY() - this.y) <= 20 && z.getX() > this.x) {
                    hasTarget = true;
                    break;
                }
            }

            // Update cooldown
            if (bulletCooldown > 0) {
                bulletCooldown--;
            }

            // Trigger attack if target found and not cooling down
            if (currentState == State.IDLE && hasTarget && bulletCooldown <= 0) {
                currentState = State.ATTACK;
                inAttackCycle = true;
                currentFrame = 0;
                frameTimer = 0;
            }
        }

        // ✅ Animate based on current state (always runs)
        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            currentFrame++;

            switch (currentState) {
                case DYING:
                    currentFrame %= CRY_FRAMES;  // Now cycles through 6 frames
                    break;

                case ATTACK:
                    if (currentFrame >= ATTACK_FRAMES) {
                        bullets.add(new Pea(x + 40, y + 20)); // Fire bullet
                        bulletCooldown = SHOOT_INTERVAL_FRAMES;
                        currentState = State.IDLE;
                        currentFrame = 0;
                        inAttackCycle = false;
                    }
                    break;

                case IDLE:
                    currentFrame %= IDLE_FRAMES;
                    break;
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
        int frameWidth;

        switch (currentState) {
            case DYING:
                sheet = crySheet;
                frameWidth = CRY_FRAME_WIDTH;
                break;
            case ATTACK:
                sheet = attackSheet;
                frameWidth = ATTACK_FRAME_WIDTH;
                break;
            case IDLE:
            default:
                sheet = idleSheet;
                frameWidth = IDLE_FRAME_WIDTH;
                break;
        }

        g.drawImage(
                sheet,
                x, y, x + width, y + height,
                currentFrame * frameWidth, 0,
                (currentFrame + 1) * frameWidth, height,
                null
        );
    }
}
