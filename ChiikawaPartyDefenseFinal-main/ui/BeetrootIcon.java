package ui;

import game.*;
import java.awt.Point;


public class BeetrootIcon extends SidebarIcon {
    public BeetrootIcon(GamePanel panel) {
        super(panel,
                "HachiwareIcon.png",
                "InactiveHachi.png",
                590, 5,
                150); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceBeetroot(dropPoint);
    }
}
