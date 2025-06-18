package ui;

import game.*;
import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;

public abstract class SidebarIcon extends JComponent implements MouseListener, MouseMotionListener {
    protected Image activeIcon;
    protected Image inactiveIcon;
    protected Image currentIcon;
    protected boolean dragging = false;
    protected boolean isActive = true;

    protected Point offset;
    protected Point originalPosition;
    protected GamePanel gamePanel;
    protected int cost;

    public SidebarIcon(GamePanel panel, String activePath, String inactivePath, int x, int y, int cost) {
        this.gamePanel = panel;
        this.activeIcon = ImageLoader.load(activePath);
        this.inactiveIcon = ImageLoader.load(inactivePath);
        this.currentIcon = activeIcon;
        this.originalPosition = new Point(x, y);
        this.cost = cost;

        setBounds(x, y, 120, 120);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setActive(boolean value) {
        isActive = value;
        currentIcon = isActive ? activeIcon : inactiveIcon;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(currentIcon, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isActive) return;
        offset = e.getPoint();
        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!dragging || !isActive) return;
        int newX = getX() + e.getX() - offset.x;
        int newY = getY() + e.getY() - offset.y;
        setLocation(newX, newY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!dragging) return;
        dragging = false;
        Point drop = SwingUtilities.convertPoint(this, e.getPoint(), gamePanel);
        handleDrop(drop);
        setLocation(originalPosition); // reset back to sidebar
    }

    protected abstract void handleDrop(Point dropPoint);

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
