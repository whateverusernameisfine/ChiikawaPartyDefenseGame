package game;

import javax.swing.*;
import java.awt.*;
//
public class FadePanel extends JPanel {
    private float alpha = 0f;
    private Timer fadeTimer;

    public FadePanel() {
        setOpaque(false);
    }

    public void fadeIn(int durationMs, Runnable onComplete) {
        alpha = 0f;

        int interval = 30;
        int steps = durationMs / interval;
        float alphaStep = 1.0f / steps;

        fadeTimer = new Timer(interval, e -> {
            alpha += alphaStep;
            if (alpha >= 1f) {
                alpha = 1f;
                fadeTimer.stop();
                repaint();
                onComplete.run();
            }
            repaint();
        });

        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (alpha > 0f) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }
}
