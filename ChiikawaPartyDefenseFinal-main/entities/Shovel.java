package entities;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Shovel extends JLabel {
    private boolean active = false;
    private Image normalIcon;
    private Image cursorIcon;

    public Shovel() {
        normalIcon = ImageLoader.load("shovel.png");
        cursorIcon = ImageLoader.load("shovel.png");

        setIcon(new ImageIcon(normalIcon));
        setBounds(1125, 45, 70, 75); // Adjust position
        setCursor(Cursor.getDefaultCursor());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle();
            }
        });
    }

    public void toggle() {
        active = !active;

        if (active) {
            Cursor shovelCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorIcon, new Point(0, 0), "ShovelCursor");
            SwingUtilities.getWindowAncestor(this).setCursor(shovelCursor);
            System.out.println("🧹 Shovel mode ON");
        } else {
            SwingUtilities.getWindowAncestor(this).setCursor(Cursor.getDefaultCursor());
            System.out.println("🧹 Shovel mode OFF");
        }
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
        SwingUtilities.getWindowAncestor(this).setCursor(Cursor.getDefaultCursor());
    }
}
