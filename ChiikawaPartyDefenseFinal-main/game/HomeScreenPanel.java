package game;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeScreenPanel extends JPanel {
    private Image background;
    private JButton playButton;

    public HomeScreenPanel(JFrame frame) {
        setLayout(null);

        // Load background image
        background = ImageLoader.load("HomePage.png");

        // Load play button image as icon
        ImageIcon playIcon = ImageLoader.loadIcon("TapToStart.png");

        playButton = new JButton(playIcon);
        playButton.setBounds(435, 280, 480, 188);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(playButton);

        // Handle button click
        playButton.addActionListener(e -> {
            frame.setContentPane(new HomePanel(frame));
            frame.revalidate();
        });

        // Optional cursor change (already handled above)
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
