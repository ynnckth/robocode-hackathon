package ch.zuehlke.bots.team3;

import ch.zuehlke.helpers.Helper;
import robocode.AdvancedRobot;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RemoBot extends AdvancedRobot {

    public static final Color SHREDDER_PURPLE = new Color(102, 70, 127);
    public static final Color SHREDDER_GREY = new Color(156, 162, 197);

    private Field field;
    private Position targetPosition;


    @Override
    public void run() {
        initializeAppearance();
        initializeField();

        while (true) {

            // 1. scan
            // 2. choose target or move to corner
            // 3. calc angle for for shooting
            // 4. shoot

            moveToCorner();
            execute();
        }
    }

    private void moveToCorner() {
        if (targetPosition == null) {
            targetPosition = findClosestCorner();
        }

    }

    private Position findClosestCorner() {
        double marginX = getBattleFieldHeight() - getSentryBorderSize();
        double marginY = getBattleFieldWidth() - getSentryBorderSize();
        Position robo = new Position(getX(), getY());
        Position corner1 = new Position(marginX, marginY);
        Position corner2 = new Position(marginX, getBattleFieldWidth() - marginY);
        Position corner3 = new Position(getBattleFieldHeight() - marginX, marginY);
        Position corner4 = new Position(getBattleFieldHeight() - marginX, getBattleFieldWidth() - marginY);

        final double distanceToCorner1 = Helper.getDistance(robo.x, corner1.x, robo.y, corner1.y);
        final double distanceToCorner2 = Helper.getDistance(robo.x, corner2.x, robo.y, corner2.y);
        final double distanceToCorner3 = Helper.getDistance(robo.x, corner3.x, robo.y, corner3.y);
        final double distanceToCorner4 = Helper.getDistance(robo.x, corner4.x, robo.y, corner4.y);

        Map<Double, Position> positionToDist = new HashMap<Double, Position>();
        positionToDist.put(distanceToCorner1, corner1);
        positionToDist.put(distanceToCorner2, corner2);
        positionToDist.put(distanceToCorner3, corner3);
        positionToDist.put(distanceToCorner4, corner4);

        List<Double> distances = Arrays.asList(distanceToCorner1, distanceToCorner2, distanceToCorner3, distanceToCorner4);
        Double min = distances.stream().min(Comparator.comparing(Double::valueOf)).get();
        return positionToDist.get(min);
    }

    private void initializeAppearance() {
        setBodyColor(SHREDDER_PURPLE);
        setGunColor(SHREDDER_GREY);
        setRadarColor(Color.black);
        setScanColor(Color.yellow);
    }

    private void initializeField() {
        this.field = new Field(getBattleFieldHeight(), getBattleFieldWidth(), getSentryBorderSize());
    }
}
