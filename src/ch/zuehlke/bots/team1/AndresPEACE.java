package ch.zuehlke.bots.team1;

import ch.zuehlke.helpers.Helper;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.Random;


public class AndresPEACE extends AdvancedRobot {

    private Location scannedLocation = null;
    private Location destination = null;

    private static int averageMovesUntilDestinationChanges = 10;

    class Location {
        int x;
        int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private boolean isInSentryRange() {
            if (x < getSentryBorderSize() || y < getSentryBorderSize()) {
                return true;
            }
            return x > (int) getBattleFieldWidth() - getSentryBorderSize() || y > (int) getBattleFieldHeight() - getSentryBorderSize();
        }
    }


    public void run() {
        setRadarColor(Color.black);
        setScanColor(Color.yellow);

        while (true) {
            battlePeacefully();
        }
    }

    private void battlePeacefully() {
        setNewColors();
        drive();
        scan();
        execute();
    }

    private void drive() {
        if (isFirstDestinationCalculation()) {
            setNewRandomLocation();
            goTo(destination);
            return;
        }

        if (!shouldChooseNewDestination()) {
            goTo(destination);
            return;
        }

        if (shouldMoveTowardsScannedRobot()) {
            destination = scannedLocation;
        } else {
            setNewRandomLocation();
        }

        goTo(destination);
    }

    private void setNewRandomLocation() {
        int destinationX = Math.abs(rand.nextInt()) % ((int) getBattleFieldWidth() - (getSentryBorderSize() * 2));
        destinationX += getSentryBorderSize();
        int destinationY = Math.abs(rand.nextInt()) % ((int) getBattleFieldHeight() - (getSentryBorderSize() * 2));
        destinationY += getSentryBorderSize();
        destination = new Location(destinationX, destinationY);
    }


    private boolean shouldMoveTowardsScannedRobot() {
        if (scannedLocation == null || scannedLocation.isInSentryRange()) {
            return false;
        }
        return rand.nextInt() % 2 == 0;
    }

    private boolean shouldChooseNewDestination() {
        if (isCloseToDestination()) {
            return true;
        }
        return rand.nextInt() % averageMovesUntilDestinationChanges == 0;
    }

    private boolean isCloseToDestination() {
        return Helper.getDistance(destination.x, destination.y, getX(), getY()) < 2.0;
    }

    private boolean isFirstDestinationCalculation() {
        return destination == null;
    }

    //Move towards an x and y coordinate

    void goTo(Location l) {
        int x = l.x;
        int y = l.y;
        double a;
        setTurnRightRadians(Math.tan(
                a = Math.atan2(x -= (int) getX(), y -= (int) getY())
                        - getHeadingRadians()));
        setAhead(Math.hypot(x, y) * Math.cos(a));
    }

    private static Random rand = new Random();

    private void setNewColors() {
        setBodyColor(new Color(rand.nextInt() % 24));
        setGunColor(new Color(rand.nextInt() % 24));
        setBulletColor(new Color(rand.nextInt() % 24));
    }

    /**
     * onScannedRobot: Fire hard!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        setNewRandomLocation();
        goTo(destination);
    }

    public void onHitRobot(HitRobotEvent e) {
        setNewRandomLocation();
        goTo(destination);
    }
}