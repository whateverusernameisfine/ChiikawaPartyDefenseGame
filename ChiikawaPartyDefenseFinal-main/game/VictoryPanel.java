package game;

import util.ImageLoader;
import util.MapConfig;

import javax.swing.*;
import java.awt.*;

public class VictoryPanel extends JPanel {
    private Image backgroundImage;
    private final JButton backButton;
    private JButton nextMapButton;
    private final int score;

    public VictoryPanel(GameFrame frame, int currentMap, int score) {
        setLayout(null);
        this.score = score;

        // ✅ Load background image using ImageLoader
        backgroundImage = ImageLoader.load("BackgroundChar.png");

        // === Back Button ===
        backButton = new JButton(new ImageIcon(ImageLoader.load("back_button.png")));
        backButton.setBounds(500, 310, 154, 103);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(backButton);
        backButton.addActionListener(e -> {
            frame.setContentPane(new MapSelectPanel(frame));
            frame.revalidate();
        });

        // === Next Map Button ===
        if (currentMap < 3) {
            nextMapButton = new JButton(new ImageIcon(ImageLoader.load("next_button.png")));
            nextMapButton.setBounds(680, 300, 240, 135);
            nextMapButton.setBorderPainted(false);
            nextMapButton.setContentAreaFilled(false);
            nextMapButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final int nextMapId = currentMap + 1;

            nextMapButton.addActionListener(e -> {
                if (nextMapId == 2) {
                    frame.setContentPane(new Map2Panel(new MapConfig(2, new int[]{2, 4, 6})));
                } else if (nextMapId == 3) {
                    frame.setContentPane(new Map3Panel(new MapConfig(3, new int[]{3, 5, 7})));
                }
                frame.revalidate();
            });

            add(nextMapButton);
        }

        // ✅ Score Title (using ImageLoader)
        ImageIcon victoryIcon = new ImageIcon(ImageLoader.load("titleBlue.png"));
        JLabel scoreBox = new JLabel(victoryIcon);
        scoreBox.setBounds(300, 30, victoryIcon.getIconWidth(), victoryIcon.getIconHeight());
        scoreBox.setLayout(new GridBagLayout());

        JLabel scoreLabel = new JLabel("Victory!!!");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 60));
        scoreLabel.setForeground(Color.PINK);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreBox.add(scoreLabel);
        add(scoreBox);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        g.setFont(new Font("Arial", Font.BOLD, 64));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 500, 250);
    }
}
