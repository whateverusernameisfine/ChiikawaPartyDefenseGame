package ui;

import game.*;
import java.awt.Point;


public class PeashooterIcon extends SidebarIcon {
    public PeashooterIcon(GamePanel panel) {
        super(panel,
                "ChiikawaIcon.png",
                "InactiveChiikawa.png",
                470, 5,
                100); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlacePeashooter(dropPoint);
    }
}
