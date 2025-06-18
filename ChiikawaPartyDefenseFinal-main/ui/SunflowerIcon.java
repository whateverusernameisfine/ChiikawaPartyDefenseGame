package ui;

import game.*;
import java.awt.Point;

public class SunflowerIcon extends SidebarIcon {
    public SunflowerIcon(GamePanel panel) {
        super(panel,
                "MomongaIcon.png",
                "InactiveMomonga.png",
                350, 5,
                50); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceSunflower(dropPoint);
    }
}


