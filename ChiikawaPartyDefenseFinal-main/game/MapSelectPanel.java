package game;

import util.MapConfig;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapSelectPanel extends JPanel {
    private Image backImage;
    private Image backgroundImage;

    public MapSelectPanel(JFrame frame) {
        setLayout(null);
        backgroundImage = ImageLoader.load("Background.png");

        // === Back Button ===
        backImage = ImageLoader.load("back_button.png");
        JButton backButton = new JButton(new ImageIcon(backImage));
        backButton.setBounds(1150, 600, 154, 103);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        add(backButton);

        backButton.addActionListener(e -> {
            frame.setContentPane(new HomePanel(frame));
            frame.revalidate();
        });

        // === Map 1 Button ===
        JLabel map1Button = new JLabel(new ImageIcon(ImageLoader.load("map1_button.png")));
        map1Button.setBounds(120, 200, 307, 307);
        map1Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map1Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(1, new int[]{1, 2, 3}); // Easy
                frame.setContentPane(new Map1Panel(config));
                frame.revalidate();
            }
        });
        add(map1Button);

        // === Map 2 Button ===
        JLabel map2Button = new JLabel(new ImageIcon(ImageLoader.load("map2_button.png")));
        map2Button.setBounds(520, 200, 307, 307);
        map2Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map2Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(2, new int[]{2, 4, 6}); // Medium
                frame.setContentPane(new Map2Panel(config));
                frame.revalidate();
            }
        });
        add(map2Button);

        // === Map 3 Button ===
        JLabel map3Button = new JLabel(new ImageIcon(ImageLoader.load("map3_button.png")));
        map3Button.setBounds(920, 200, 307, 307);
        map3Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map3Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(3, new int[]{3, 5, 7}); // Hard
                frame.setContentPane(new Map3Panel(config));
                frame.revalidate();
            }
        });
        add(map3Button);

        // ✅ Use ImageLoader for score box too
        ImageIcon scoreBoxIcon = ImageLoader.loadIcon("titleBlue.png");

        JLabel scoreBox = new JLabel(scoreBoxIcon);
        scoreBox.setBounds(300, 30, scoreBoxIcon.getIconWidth(), scoreBoxIcon.getIconHeight());
        scoreBox.setLayout(new GridBagLayout());

        JLabel scoreLabel = new JLabel("Total Score: " + ((GameFrame) frame).getTotalScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scoreLabel.setForeground(Color.BLACK);
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

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("Choose a Map", 430, 100);
    }
}
