package entities;

import java.awt.*;

// =======================================
// ABSTRACT BASE CLASS: PLANT
// =======================================
public abstract class Plant {

    // =======================================
    // POSITION & SIZE
    // =======================================
    protected int x, y;
    protected int width = 125;
    protected int height = 110;

    // =======================================
    // PLANT ATTRIBUTES
    // =======================================
    protected int health;
    protected String name;
    protected int cost;

    // =======================================
    // DYING STATE
    // =======================================
    protected boolean isDying = false;
    protected int dyingTimer = 0;
    protected int dyingDuration = 95; // ~1 second if game runs at 30 FPS

    // =======================================
    // CONSTRUCTOR
    // =======================================
    public Plant(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // =======================================
    // GETTERS
    // =======================================
    public int getX() { return x; }

    public int getY() { return y; }

    public int getCost() { return cost; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() {
        return health > 0 && !isDying;
    }

    // =======================================
    // DAMAGE AND DYING LOGIC
    // =======================================
    public void takeDamage(int dmg) {
        if (!isDying) {
            health -= dmg;
        }
    }

    public boolean isDead() {
        return health <= 0 && !isDying;
    }

    public void startDying() {
        isDying = true;
        dyingTimer = 0;
    }

    public void updateDying() {
        if (isDying) dyingTimer++;
    }

    public boolean isMarkedForRemoval() {
        return isDying && dyingTimer >= dyingDuration;
    }

    // =======================================
    // ABSTRACT METHODS TO IMPLEMENT IN CHILDREN
    // =======================================
    public abstract void update();           // Per-frame behavior
    public abstract void draw(Graphics g);   // Drawing logic
}
