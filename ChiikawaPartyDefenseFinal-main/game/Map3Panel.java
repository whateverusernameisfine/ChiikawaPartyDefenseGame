package game;

import entities.*;
import util.MapConfig;
import util.ImageLoader;
import java.awt.Image;
import java.awt.Point;

public class Map3Panel extends GamePanel {
    public Map3Panel(MapConfig config) {
        super(config);

        ObstacleRock r1 = new ObstacleRock(4, 1);
        ObstacleRock r3 = new ObstacleRock(4, 3);
        ObstacleMud  m1 = new ObstacleMud(7, 1);
        ObstacleMud  m2 = new ObstacleMud(4, 2);
        ObstacleMud  m3 = new ObstacleMud(7, 3);

        obstacles.add(r1);
        obstacles.add(r3);
        obstacles.add(m1);
        obstacles.add(m2);
        obstacles.add(m3);

        occupiedCells.add(new Point(getColumns()[r1.getGridX()], getRows()[r1.getGridY()]));
        occupiedCells.add(new Point(getColumns()[r3.getGridX()], getRows()[r3.getGridY()]));
        occupiedCells.add(new Point(getColumns()[m1.getGridX()], getRows()[m1.getGridY()]));
        occupiedCells.add(new Point(getColumns()[m2.getGridX()], getRows()[m2.getGridY()]));
        occupiedCells.add(new Point(getColumns()[m3.getGridX()], getRows()[m3.getGridY()]));
    }

    @Override
    protected Image loadBackgroundImage() {
        return ImageLoader.load("Map1.png");
    }
}

