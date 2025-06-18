package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Beetroot extends Plant {
    private static final int SHOOT_INTERVAL_FRAMES = 210; // 2 seconds
    private static final int FRAME_DELAY = 10;

    private boolean hasTarget = false;
    private boolean inAttackCycle = false;
    private int bulletCooldown = 0;

    private int currentFrame = 0;
    private int frameTimer = 0;

    private Image idleSheet;
    private Image attackSheet;
    private Image crySheet;

    private final int IDLE_FRAMES = 2;
    private final int ATTACK_FRAMES = 2;
    private final int CRY_FRAMES = 5;

    private final int IDLE_FRAME_WIDTH = 125;
    private final int ATTACK_FRAME_WIDTH = 125;
    private final int CRY_FRAME_WIDTH = 125;

    private enum State {
        IDLE, ATTACK, DYING
    }

    private State currentState = State.IDLE;

    public Beetroot(int x, int y) {
        super(x, y);
        this.health = 300;
        this.name = "Hachiware";
        this.cost = 100;
        this.dyingDuration = 60;

        idleSheet = ImageLoader.load("HachiwareIdleSheet.png");
        attackSheet = ImageLoader.load("HachiwareAttackSheet.png");
        crySheet = ImageLoader.load("HachiwareCrySheet.png");
    }

    public void update(List<Bullet> bullets, List<Zombie> zombies) {
        updateDying();

        if (isDying) {
            currentState = State.DYING;
        } else {
            hasTarget = false;
            for (Zombie z : zombies) {
                if (!z.isDead() && Math.abs(z.getY() - this.y) <= 20 && z.getX() > this.x) {
                    hasTarget = true;
                    break;
                }
            }

            if (bulletCooldown > 0) {
                bulletCooldown--;
            }

            if (currentState == State.IDLE && hasTarget && bulletCooldown <= 0) {
                currentState = State.ATTACK;
                inAttackCycle = true;
                currentFrame = 0;
                frameTimer = 0;
            }
        }

        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            currentFrame++;

            switch (currentState) {
                case DYING:
                    currentFrame %= CRY_FRAMES;
                    break;
                case ATTACK:
                    if (currentFrame >= ATTACK_FRAMES) {
                        bullets.add(new Beet(x + 40, y));
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
