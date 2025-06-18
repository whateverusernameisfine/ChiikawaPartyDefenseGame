package game;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import util.MapConfig;

public class GameFrame extends JFrame {
    private int totalScore = 0;
    private final Map<Integer, Integer> highScores = new HashMap<>();
    private static String currentUser = null;

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public GameFrame() {
        setTitle("Chiikawa Party Defense");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new LoginPanel(this)); // Show home screen first
        setVisible(true);
    }

    public void showVictoryPanel(int currentMap, int score) {
        setContentPane(new VictoryPanel(this, currentMap, score));
        revalidate();
        repaint();
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void restartCurrentMap() {
        Component current = getContentPane();
        if (current instanceof Map1Panel) {
            setContentPane(new Map1Panel(new MapConfig(1, new int[]{1, 2, 3})));
        } else if (current instanceof Map2Panel) {
            setContentPane(new Map2Panel(new MapConfig(2, new int[]{2, 4, 6})));
        } else if (current instanceof Map3Panel) {
            setContentPane(new Map3Panel(new MapConfig(3, new int[]{3, 5, 7})));
        }
        revalidate();
        repaint();
    }

    public void startMap(int mapId) {
        switch (mapId) {
            case 1 -> setContentPane(new Map1Panel(new MapConfig(1, new int[]{1,2,3})));
            case 2 -> setContentPane(new Map2Panel(new MapConfig(2, new int[]{2,4,6})));
            case 3 -> setContentPane(new Map3Panel(new MapConfig(3, new int[]{3,5,7})));
        }
        revalidate();
    }

    public void showGameOverPanel(int mapId) {
        setContentPane(new GameOverPanel(this, mapId));
        revalidate();
    }


    public void updateHighScore(int mapId, int newScore) {
        int previousHigh = highScores.getOrDefault(mapId, 0);
        if (newScore > previousHigh) {
            highScores.put(mapId, newScore);

            // Recalculate totalScore from all high scores
            totalScore = highScores.values().stream().mapToInt(Integer::intValue).sum();

            System.out.println("New high score for map " + mapId + ": " + newScore);
            System.out.println("Total score recalculated: " + totalScore);
            
            // Submit score to web database
            if (currentUser != null) {
                HttpClient.submitScore(currentUser, totalScore);
            }
        } else {
            System.out.println("Score " + newScore + " did not beat previous high: " + previousHigh);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
