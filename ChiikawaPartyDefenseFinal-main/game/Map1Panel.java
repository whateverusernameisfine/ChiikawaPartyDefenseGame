package game;

import entities.*;
import util.MapConfig;
import util.ImageLoader;
import java.awt.Image;
import java.awt.Point;

public class Map1Panel extends GamePanel {
    public Map1Panel(MapConfig config) {
        super(config);

        ObstacleMud m1 = new ObstacleMud(7, 0);
        ObstacleMud m2 = new ObstacleMud(7, 1);
        ObstacleMud m3 = new ObstacleMud(6, 4);
        ObstacleRock r1 = new ObstacleRock(8, 2);
        ObstacleRock r2 = new ObstacleRock(6, 3);

        obstacles.add(m1);
        obstacles.add(m2);
        obstacles.add(m3);
        obstacles.add(r1);
        obstacles.add(r2);

        occupiedCells.add(new Point(getColumns()[m1.getGridX()], getRows()[m1.getGridY()]));
        occupiedCells.add(new Point(getColumns()[m2.getGridX()], getRows()[m2.getGridY()]));
        occupiedCells.add(new Point(getColumns()[m3.getGridX()], getRows()[m3.getGridY()]));
        occupiedCells.add(new Point(getColumns()[r1.getGridX()], getRows()[r1.getGridY()]));
        occupiedCells.add(new Point(getColumns()[r2.getGridX()], getRows()[r2.getGridY()]));
    }

    @Override
    protected Image loadBackgroundImage() {
        return ImageLoader.load("Map1.png");
    }
}


