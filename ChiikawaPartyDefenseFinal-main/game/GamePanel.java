package game;

import entities.*;
import ui.*;
import util.ImageLoader;
import util.MapConfig;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// =======================================
// CLASS DEFINITION
// =======================================
public abstract class GamePanel extends JPanel implements ActionListener, MouseListener {

    // =======================================
    // GAME STATE VARIABLES
    // =======================================
    private boolean gameOver = false;
    private boolean showWaveBanner = false;
    private long gameStartTime = System.currentTimeMillis();
    private Random random = new Random();
    private Timer timer;
    private boolean gameWon = false;

    private int score = 0;

    private int sunSpawnTimer = 0;
    private int zombieSpawnTimer = 0;
    private int waveBannerTimer = 0;
    private boolean finalWaveBannerShown = false;
    private boolean finalWaveStarted = false;
    private boolean waitingForMowersToFinish = false;

    private static final int ZOMBIE_SPAWN_INTERVAL = 400;

    // =======================================
    // IMAGE RESOURCES
    // =======================================
    protected Image background;
    private Image counterBox;
    private Image waveBannerImage;

    // =======================================
    // UI COMPONENTS
    // =======================================
    private FadePanel fadePanel;
    private Shovel shovel;
    private SunCounter sunCounter;
    private SunflowerIcon sunflowerIcon;
    private PeashooterIcon peaShooterIcon;
//  private WalnutIcon walnutIcon;
    private BeetrootIcon beetrootIcon;
    private CherryIcon cherryIcon;


    // =======================================
    // ENTITY LISTS
    // =======================================
    protected Set<Point> occupiedCells;
    private java.util.List<Sun> suns;
    private List<Plant> plants;
    private List<Bullet> bullets;
    private List<Zombie> zombies;
    private List<Lawnmower> mowers;
    protected final List<Obstacle> obstacles = new ArrayList<>();

    // =======================================
    // GRID CONFIGURATION
    // =======================================
    private final int[] columns = {284, 391, 496, 600, 705, 820, 927, 1030, 1136, 1243};
    private final int[] rows = {170, 272, 377, 481, 583};
    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 100;

    private MapConfig activeMapConfig;

    private int currentWave = 0;
    private int zombiesSpawnedThisWave = 0;
    private int zombiesKilledThisWave = 0;
    private int totalZombiesThisWave = 0;

    private JButton settingsButton;
    private JPanel pauseMenuPanel;
    private boolean isPaused = false;

    // =======================================
    // CONSTRUCTOR: SETUP GAME PANEL
    // =======================================

    public GamePanel(MapConfig config) {
        setLayout(null);
        setFocusable(true);
        addMouseListener(this);

        this.activeMapConfig = config;

        // Load images
        background = loadBackgroundImage();
        counterBox = ImageLoader.load("Counter.png");
        waveBannerImage = ImageLoader.load("banner.png");

        // Add fade overlay
        fadePanel = new FadePanel();
        fadePanel.setBounds(0, 0, 1920, 1080);
        fadePanel.setOpaque(false); // ✅ This is important
        add(fadePanel);
        setComponentZOrder(fadePanel, 0); // put on top


        // Initialize and add sidebar icons
        sunflowerIcon = new SunflowerIcon(this); add(sunflowerIcon);
        peaShooterIcon = new PeashooterIcon(this); add(peaShooterIcon);
//      walnutIcon = new WalnutIcon(this); add(walnutIcon);
        beetrootIcon = new BeetrootIcon(this); add(beetrootIcon);
        cherryIcon = new CherryIcon(this); add(cherryIcon);


        // Initialize and add counters
        sunCounter = new SunCounter();
        sunCounter.setBounds(200, 25, 50, 50);

// ✅ Completely transparent and clean
        add(sunCounter);


        // Create game objects
        shovel = new Shovel(); add(shovel);
        suns = new ArrayList<>();
        plants = new ArrayList<>();
        bullets = new ArrayList<>();
        zombies = new ArrayList<>();
        mowers = new ArrayList<>();
        occupiedCells = new HashSet<>();

        for (int y : rows) {
            mowers.add(new Lawnmower(220, y));
        }

        // === Settings Button ===
        settingsButton = new JButton(new ImageIcon(ImageLoader.load("Setting.png")));
        settingsButton.setBounds(1250, 50, 60, 60);
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        settingsButton.setToolTipText("Pause / Settings");

        settingsButton.addActionListener(e -> togglePauseMenu());

        add(settingsButton);

        // Start game loop
        timer = new Timer(30, this);
        timer.start();
    }

    // =======================================
    // RENDERING: DRAW BACKGROUND & ENTITIES
    // =======================================

    protected abstract Image loadBackgroundImage();  // Each map must define its own background

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(score), 210, 125);  // Adjust x and y to center text

        for (Lawnmower mower : mowers) mower.draw(g);

        for (Obstacle obs : obstacles) {
            int px = columns[obs.getGridX()];
            int py = rows[obs.getGridY()];
            obs.draw(g, px, py);
        }

        for (Plant plant : plants) plant.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        for (Zombie z : zombies) z.draw(g);
        for (Sun sun : suns) sun.draw(g);

        if (!gameOver && totalZombiesThisWave > 0) {
            int barX = 280, barY = 140, barWidth = 300, barHeight = 20;
            double progress = (double) zombiesKilledThisWave / totalZombiesThisWave;
            int fillWidth = (int)(barWidth * progress);

            g.setColor(Color.GRAY);
            g.fillRect(barX, barY, barWidth, barHeight);

            g.setColor(progress >= 1.0 ? Color.ORANGE : Color.GREEN);
            g.fillRect(barX, barY, fillWidth, barHeight);

            g.setColor(Color.WHITE);
            g.drawRect(barX, barY, barWidth, barHeight);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Wave " + (currentWave + 1) + "/" + activeMapConfig.totalWaves, barX, barY - 5);
        }

        if (showWaveBanner && waveBannerImage != null) {
            int imgWidth = 800, imgHeight = 53;
            int x = (getWidth() - imgWidth) / 2, y = 200;
            g.drawImage(waveBannerImage, x, y, imgWidth, imgHeight, this);

            waveBannerTimer--;
            if (waveBannerTimer <= 0) showWaveBanner = false;
        }
    }

    // =======================================
    // GAME LOOP (called every 30ms)
    // =======================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // -- Sun spawning
        sunSpawnTimer++;
        if (sunSpawnTimer == 50 || sunSpawnTimer >= 1000) {
            suns.add(new Sun(random.nextInt(700) + 200, 0, false));
            sunSpawnTimer = 51;
        }

        for (Sun sun : suns) sun.update();

        // -- Plant behavior
        for (Plant plant : plants) {
            plant.update();
            if (plant instanceof Peashooter shooter) shooter.update(bullets, zombies);
            else if (plant instanceof Beetroot beet) beet.update(bullets, zombies);
            else if (plant instanceof Sunflower sf) sf.update(suns);
        }

        // -- Check for zombies crossing the baseline
        for (Zombie z : zombies) {
            if (z.getX() <= 220) {
                Lawnmower mower = getMowerInRow(z.getY());
                if (mower != null && !mower.isUsed() && !mower.isActive()) mower.activate();
                else if (mower == null || mower.isUsed()) {
                    triggerGameOver(); break;
                }
            }
        }

        // -- Zombie stage progression
        zombieSpawnTimer++;
        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - gameStartTime) / 1000;

        // ==== Wave Progression ====
        int totalWaves = activeMapConfig.totalWaves;

// 1️⃣ Normal wave progression (before final)
        if (currentWave < totalWaves - 1) {
            int zombiesThisWave = activeMapConfig.zombiesPerWave[currentWave];
            totalZombiesThisWave = zombiesThisWave;

            if (zombieSpawnTimer >= ZOMBIE_SPAWN_INTERVAL && zombiesSpawnedThisWave < zombiesThisWave) {
                spawnZombieForCurrentMap();
                zombiesSpawnedThisWave++;
                zombieSpawnTimer = 0;
            }

            if (zombiesKilledThisWave >= zombiesThisWave) {
                currentWave++;
                zombiesSpawnedThisWave = 0;
                zombiesKilledThisWave = 0;
            }
        }

// 2️⃣ Before final wave: show banner ONCE
        else if (currentWave == totalWaves - 1 && !finalWaveBannerShown) {
            showWaveBanner = true;
            waveBannerTimer = 90;
            finalWaveBannerShown = true;
        }

// 3️⃣ Final wave begins AFTER banner fades
        else if (finalWaveBannerShown && !finalWaveStarted && waveBannerTimer <= 0) {
            finalWaveStarted = true;
        }

// 4️⃣ Spawning final wave zombies
        if (finalWaveStarted && currentWave == totalWaves - 1) {
            int zombiesThisWave = activeMapConfig.zombiesPerWave[currentWave];
            totalZombiesThisWave = zombiesThisWave;

            if (zombieSpawnTimer >= ZOMBIE_SPAWN_INTERVAL && zombiesSpawnedThisWave < zombiesThisWave) {
                spawnZombieForCurrentMap();
                zombiesSpawnedThisWave++;
                zombieSpawnTimer = 0;
            }

            if (zombiesKilledThisWave >= zombiesThisWave) {
                // All waves done, let zombies clear → handled below
            }
        }

        // -- Bullet collision with zombies
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            Rectangle bBox = bullet.getBounds();
            for (Zombie zombie : zombies) {
                if (bBox.intersects(zombie.getBounds())) {
                    zombie.takeDamage(bullet.getDamage());
                    bulletsToRemove.add(bullet);
                    System.out.println("Bullet hit! Zombie health: " + zombie.getHealth());
                    break;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // -- Update zombies
        for (Zombie z : zombies) {
            if (z.isDead() && !z.isDying()) {
                z.startDying();
            } else {
                z.update(plants);
            }
            z.updateDying();
        }

        zombies.removeIf(z -> {
            if (z.isMarkedForRemoval()) {
                zombiesKilledThisWave++;
                // 🧠 Add score based on zombie type
                if (z instanceof NormalZombie) {
                    score += 10;
                } else if (z instanceof ConeheadZombie) {
                    score += 20;
                } else if (z instanceof FootballZombie) {
                    score += 30;
                }
                System.out.println("✅ Zombie defeated! Current score: " + score);
                return true;
            }
            return false;
        });

        boolean allZombiesCleared = zombies.stream().noneMatch(z -> !z.isMarkedForRemoval() && !z.isDying());
        boolean allFinalZombiesSpawned = (zombiesSpawnedThisWave >= totalZombiesThisWave);
        boolean isFinalWave = (currentWave == activeMapConfig.totalWaves - 1);

        if (finalWaveStarted && isFinalWave && allFinalZombiesSpawned && allZombiesCleared && !gameWon) {
            boolean allMowersDone = mowers.stream().allMatch(m -> !m.isActive());
            if (allMowersDone) {
                triggerVictoryWithFade();
            } else {
                waitingForMowersToFinish = true;
            }
        }

// 🕓 Continue checking mower status on every frame
        if (waitingForMowersToFinish && !gameWon) {
            boolean allMowersDone = mowers.stream().allMatch(m -> !m.isActive());
            boolean allZombiesStillGone = zombies.stream().noneMatch(z -> !z.isMarkedForRemoval() && !z.isDying());

            if (allMowersDone && allZombiesStillGone) {
                triggerVictoryWithFade();
                waitingForMowersToFinish = false;
            }
        }

        // -- Update plant deaths
        Iterator<Plant> plantIterator = plants.iterator();
        while (plantIterator.hasNext()) {
            Plant p = plantIterator.next();
            if (p.isDead() && !p.isMarkedForRemoval()) {
                p.startDying();
                score = Math.max(0, score - 20);  // Prevent negative score
                System.out.println("❌ Plant died → Score -20. Current Score: " + score);
            }
        }
        plants.removeIf(Plant::isMarkedForRemoval);


        for (Plant plant : plants) {
            if (plant instanceof Cherry cherry) {
                cherry.affectZombies(zombies);
            }
        }


        // -- Update projectiles
        for (Bullet b : bullets) b.update();

        // -- Lawnmower logic
        for (Lawnmower mower : mowers) mower.update(zombies);

        // -- Icon activation thresholds
        sunflowerIcon.setActive(sunCounter.getValue() >= 50);
        peaShooterIcon.setActive(sunCounter.getValue() >= 100);
//      walnutIcon.setActive(sunCounter.getValue() >= 25);
        beetrootIcon.setActive(sunCounter.getValue() >= 150);
        cherryIcon.setActive(sunCounter.getValue() >= 150);

        repaint();
    }

    private void triggerVictoryWithFade() {
        gameWon = true;

        fadePanel.fadeIn(1000, () -> {
            long unusedCount = mowers.stream().filter(m -> !m.wasTriggered()).count();
            int unusedMowerBonus = (int) (unusedCount * 100);
            score += unusedMowerBonus;
            System.out.println("✅ Victory! + " + unusedMowerBonus + " points from " + unusedCount + " unused mowers. Total score: " + score);

            SwingUtilities.invokeLater(() -> {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof GameFrame gameFrame) {
                    gameFrame.updateHighScore(activeMapConfig.mapId, score);
                    gameFrame.showVictoryPanel(activeMapConfig.mapId, score);
                }
            });
            timer.stop();
        });
    }


    // =======================================
    // SPAWN A SINGLE ZOMBIE AT RANDOM ROW
    // =======================================
    private void spawnZombieForCurrentMap() {
        int mapId = activeMapConfig.mapId;
        int wave = currentWave;
        int totalWaves = activeMapConfig.totalWaves;
        boolean isFinalWave = (wave == totalWaves - 1);

        int row = rows[random.nextInt(rows.length)];
        int x = getWidth();

        Zombie z;

        if (mapId == 1) {
            // Map 1: only normal, conehead in final
            if (isFinalWave) {
                z = random.nextBoolean() ? new NormalZombie(x, row, this) : new ConeheadZombie(x, row, this);
            } else {
                z = new NormalZombie(x, row, this);
            }

        } else if (mapId == 2) {
            // Map 2: normal + frequent conehead, 1 football in final wave
            if (isFinalWave && zombiesSpawnedThisWave == 0) {
                z = new FootballZombie(x, row, this); // spawn only one
            } else {
                z = (random.nextInt(3) == 0)
                        ? new ConeheadZombie(x, row, this)
                        : new NormalZombie(x, row, this);
            }

        } else if (mapId == 3) {
            // Map 3: less normal at early wave, more conehead, final wave = 5 football per row + many others
            if (isFinalWave && zombiesSpawnedThisWave < 5 * rows.length) {
                int rowIdx = zombiesSpawnedThisWave % rows.length;
                row = rows[rowIdx];
                z = new FootballZombie(x, row, this);
            } else if (isFinalWave) {
                z = (random.nextBoolean())
                        ? new ConeheadZombie(x, row, this)
                        : new NormalZombie(x, row, this);
            } else {
                z = (zombiesSpawnedThisWave < 2)
                        ? new NormalZombie(x, row, this)
                        : new ConeheadZombie(x, row, this);
            }

        } else {
            // Fallback/default
            z = new NormalZombie(x, row, this);
        }

        zombies.add(z);
    }


    // =======================================
    // CHECK FOR LAWNMOWER IN SAME ROW
    // =======================================
    private Lawnmower getMowerInRow(int y) {
        for (Lawnmower mower : mowers) {
            if (Math.abs(mower.getY() - y) <= 20) {
                return mower;
            }
        }
        return null;
    }

    // =======================================
    // TRIGGER GAME OVER SEQUENCE
    // =======================================
    private void triggerGameOver() {
        if (gameOver) return;
        gameOver = true;
        timer.stop();

        fadePanel.fadeIn(1000, () -> {
            // Submit current score even on game over
            String currentUser = GameFrame.getCurrentUser();
            if (currentUser != null && score > 0) {
                HttpClient.submitScore(currentUser, score);
            }
            
            SwingUtilities.invokeLater(() -> {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof GameFrame gameFrame) {
                    gameFrame.showGameOverPanel(activeMapConfig.mapId);
                }
            });
            timer.stop();
        });
    }


    // =======================================
    // MOUSE CLICK HANDLING
    // =======================================
    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        Point cell = snapToGrid(mx, my);

        System.out.println("Mouse clicked at: (" + mx + ", " + my + ")");
        Iterator<Sun> iterator = suns.iterator();
        while (iterator.hasNext()) {
            Sun sun = iterator.next();
            if (sun.contains(mx, my)) {
                iterator.remove();
                sunCounter.addSun(50);
                break;
            }
        }

        if (shovel.isActive()) {
            removePlantAt(cell);
            shovel.deactivate(); // Optional: auto-deactivate
            return;
        }
    }

    // =======================================
    // GRID ALIGNMENT LOGIC
    // =======================================
    private Point snapToGrid(int x, int y) {
        for (int rowY : rows) {
            for (int colX : columns) {
                if (x >= colX && x <= colX + CELL_WIDTH && y >= rowY && y <= rowY + CELL_HEIGHT) {
                    return new Point(colX, rowY);
                }
            }
        }
        return null;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public int[] getColumns() {
        return columns;
    }

    public int[] getRows() {
        return rows;
    }


    // =======================================
    // REMOVE PLANT AND REFUND SUN
    // =======================================
    private void removePlantAt(Point cell) {
        Iterator<Plant> iterator = plants.iterator();

        while (iterator.hasNext()) {
            Plant plant = iterator.next();
            if (plant.getX() == cell.x && plant.getY() == cell.y) {
                iterator.remove();
                occupiedCells.remove(cell);

                int refund = plant.getCost() / 2;
                sunCounter.addSun(refund);
                System.out.println("Removed plant at: " + cell + " → Refunded: " + refund + " sun");
                break;
            }
        }
    }

    // =======================================
    // PLANT PLACEMENT METHODS
    // =======================================
    public void tryPlaceSunflower(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(50)) {
            plants.add(new Sunflower(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place sunflower at " + p);
        }
    }

    public void tryPlacePeashooter(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(100)) {
            plants.add(new Peashooter(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place peashooter at " + p);
        }
    }

//    public void tryPlaceWalnut(Point p) {
//        Point cell = snapToGrid(p.x, p.y);
//        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(25)) {
//            plants.add(new Walnut(cell.x, cell.y));
//            occupiedCells.add(cell);
//        } else {
//            System.out.println("Failed to place walnut at " + p);
//        }
//    }

    public void tryPlaceBeetroot(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(125)) {
            plants.add(new Beetroot(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place beetroot at " + p);
        }
    }

    public void tryPlaceCherry(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(150)) {
            plants.add(new Cherry(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place cherry at " + p);
        }
    }

    // =======================================
    // HELPER: CHECK IF GRID CELL IS TAKEN
    // =======================================
    private boolean isPlantOccupied(Point cell) {
        for (Plant plant : plants) {
            if (plant.getX() == cell.x && plant.getY() == cell.y - 30) {
                return true;
            }
        }

        for (Obstacle obs : obstacles) {
            int obsX = columns[obs.getGridX()];
            int obsY = rows[obs.getGridY()];
            if (cell.x == obsX && cell.y == obsY) {
                return true;
            }
        }
        return false;
    }

    public int[][] buildCostGrid(Zombie zombie) {
        int cols = columns.length;
        int rowsCount = rows.length;
        int[][] costGrid = new int[rowsCount][cols];

        for (int y = 0; y < rowsCount; y++) {
            for (int x = 0; x < cols; x++) {
                costGrid[y][x] = 1;
            }
        }

        for (Obstacle obs : obstacles) {
            int gx = obs.getGridX();
            int gy = obs.getGridY();
            if (gx >= 0 && gx < cols && gy >= 0 && gy < rowsCount) {
                costGrid[gy][gx] = obs.getWeight(zombie); // ✅ Use zombie-based weight
            }
        }

        return costGrid;
    }


    public Point gridToPixel(int gridX, int gridY) {
        return new Point(columns[gridX], rows[gridY]);
    }

    public Point pixelToGrid(int x, int y) {
        int col = -1, row = -1;

        for (int i = 0; i < columns.length; i++) {
            if (x >= columns[i]) col = i;
        }

        for (int j = 0; j < rows.length; j++) {
            if (y >= rows[j]) row = j;
        }

        if (col >= 0 && col < columns.length && row >= 0 && row < rows.length) {
            return new Point(col, row);
        } else {
            return null; // out of bounds
        }
    }

    private void togglePauseMenu() {
        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
            showPauseMenu();
        } else {
            hidePauseMenu();
            timer.start();
        }
    }


    private void showPauseMenu() {
        pauseMenuPanel = new JPanel(null); // null layout
        pauseMenuPanel.setBounds(500, 200, 400, 400);
        pauseMenuPanel.setOpaque(false); // Allows transparency
        pauseMenuPanel.setBackground(new Color(0, 0, 0, 0)); // Optional: fully transparent color


        // === Continue Button ===
        JButton continueButton = new JButton(new ImageIcon(ImageLoader.load("continue_button.png")));
        continueButton.setBounds(100, 20, 205, 100);
        configureButton(continueButton, "Continue playing", e -> togglePauseMenu());

        // === Restart Button ===
        JButton restartButton = new JButton(new ImageIcon(ImageLoader.load("restart_button.png")));
        restartButton.setBounds(100, 120, 205, 100);
        configureButton(restartButton, "Restart game", e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof GameFrame gf) {
                gf.restartCurrentMap();
            }
        });

        // === Home Button ===
        JButton homeButton = new JButton(new ImageIcon(ImageLoader.load("back_button2.png")));
        homeButton.setBounds(100, 220, 205, 100);
        configureButton(homeButton, "Back to Home", e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof GameFrame gf) {
                gf.setContentPane(new MapSelectPanel(gf));
                gf.revalidate();
            }
        });

        pauseMenuPanel.add(continueButton);
        pauseMenuPanel.add(restartButton);
        pauseMenuPanel.add(homeButton);
        add(pauseMenuPanel);
        setComponentZOrder(pauseMenuPanel, 0);
        pauseMenuPanel.repaint();
    }

    private void configureButton(JButton button, String tooltip, ActionListener listener) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.addActionListener(listener);
    }

    private void hidePauseMenu() {
        if (pauseMenuPanel != null) {
            remove(pauseMenuPanel);
            pauseMenuPanel = null;
            revalidate();
            repaint();
        }
    }


    // =======================================
    // UNUSED MOUSE EVENTS
    // =======================================
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // (End of class)
}