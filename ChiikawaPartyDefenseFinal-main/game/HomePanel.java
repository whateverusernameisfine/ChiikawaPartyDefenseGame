package game;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePanel extends JPanel {
    private Image backgroundImage;
    private JButton mapButton;
    private JButton galleryButton;

    public HomePanel(JFrame frame) {
        setLayout(null);

        // ✅ Load background using ImageLoader (from resources)
        backgroundImage = ImageLoader.load("BackgroundChar.png");

        // ✅ Map Button using image from resources
        mapButton = createImageButton("button.png", "Choose Map", 550, 180, 240, 135);
        mapButton.addActionListener(e -> {
            frame.setContentPane(new MapSelectPanel(frame));
            frame.revalidate();
        });
        add(mapButton);

//        // ✅ Gallery Button using image from resources
//        galleryButton = createImageButton("button.png", "Gallery", 550, 320, 240, 135);
//        galleryButton.addActionListener(e -> {
//            frame.setContentPane(new GalleryPanel(frame));
//            frame.revalidate();
//        });
//        add(galleryButton);

        // ✅ Load score box using resource
        ImageIcon scoreBoxIcon = ImageLoader.loadIcon("titleBlue.png");
        JLabel scoreBox = new JLabel(scoreBoxIcon);
        scoreBox.setBounds(300, 30, scoreBoxIcon.getIconWidth(), scoreBoxIcon.getIconHeight());
        scoreBox.setLayout(new GridBagLayout()); // center the score label

        // Score label text
        JLabel scoreLabel = new JLabel("Total Score: " + ((GameFrame) frame).getTotalScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scoreBox.add(scoreLabel);
        add(scoreBox);
    }

    private JButton createImageButton(String imageName, String text, int x, int y, int width, int height) {
        ImageIcon icon = ImageLoader.loadIcon(imageName);
        JButton button = new JButton(text, icon);
        button.setBounds(x, y, width, height);

        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.BLACK);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
