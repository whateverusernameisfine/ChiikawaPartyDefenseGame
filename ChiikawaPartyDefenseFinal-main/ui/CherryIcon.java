package ui;

import game.*;
import java.awt.Point;


public class CherryIcon extends SidebarIcon {
    public CherryIcon(GamePanel panel) {
        super(panel,
                "UsagiIcon.png",
                "InactiveUsagi.png",
                710, 5,
                150); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceCherry(dropPoint);
    }
}
