package ch.zuehlke.bots.team1;

import ch.zuehlke.helpers.Helper;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.awt.*;
import java.util.Random;


public class AndresAggressor extends AdvancedRobot {

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
            battle();
        }
    }

    private void battle() {
        setNewColors();
        drive();
        scan();
        execute();
    }

    private void drive() {
        System.out.println("Walking...");
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
        System.out.println("Destination: " + String.valueOf(destination.x) + " : " + String.valueOf(destination.y));
        assert (destination.isInSentryRange());
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
        double a;
        int x = l.x;
        int y = l.y;
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
        if (!e.isSentryRobot()) {
            double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
            int scannedX = (int) (getX() + Math.sin(angle) * e.getDistance());
            int scannedY = (int) (getY() + Math.cos(angle) * e.getDistance());
            scannedLocation = new Location(scannedX, scannedY);
            double bearingFromGun = getBearingFromGun(e.getBearing());
            if (Math.abs(bearingFromGun) <= 3.0D) {
                this.turnRight(bearingFromGun);
                if (this.getGunHeat() == 0.0D) {
                    this.fire(getFirePower());
                }
            } else {
                this.turnGunRight(bearingFromGun);
            }

            if (bearingFromGun == 0.0D) {
                this.scan();
            }

            battle();
        }
    }

    private double getBearingFromGun(double bearing) {
        double absoluteBearing = this.getHeading() + bearing;
        return Utils.normalRelativeAngleDegrees(absoluteBearing - this.getGunHeading());
    }

    private double getFirePower() {
        return Math.min(3.0D * (getEnergy() / 100.0), this.getEnergy() - 0.1D);
    }


    public void onHitRobot(HitRobotEvent e) {
        this.turnGunRight(getBearingFromGun(e.getBearing()));
        this.fire(3.0);
        battle();
    }
}