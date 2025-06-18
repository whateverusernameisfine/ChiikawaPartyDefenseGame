package game;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    private final JButton retryButton;
    private final JButton homeButton;
    private final Image backgroundImage;

    public GameOverPanel(GameFrame frame, int mapId) {
        setLayout(null);
        setBackground(Color.BLACK);  // fallback background

        // ✅ Use ImageLoader for compatibility with .jar
        backgroundImage = ImageLoader.load("BackgroundChar.png");

        // === Retry Button ===
        retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Arial", Font.BOLD, 20));
        retryButton.setBounds(550, 320, 200, 50);
        retryButton.setFocusPainted(false);
        retryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(retryButton);

        retryButton.addActionListener(e -> {
            frame.startMap(mapId); // You need to implement this method in GameFrame
        });

        // === Home Button ===
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        homeButton.setBounds(550, 390, 200, 50);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(homeButton);

        homeButton.addActionListener(e -> {
            frame.setContentPane(new HomePanel(frame));
            frame.revalidate();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
