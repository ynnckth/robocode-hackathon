package ch.zuehlke.bots.team1;

import ch.zuehlke.helpers.Helper;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.awt.*;
import java.util.Random;


public class AndresBot extends AdvancedRobot {

    private Location scannedLocation = null;
    private Location destination = null;

    private static int averageMovesUntilDestinationChanges = 5;

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
        // Set colors
        setRadarColor(Color.black);
        setScanColor(Color.yellow);

        // Loop forever
        while (true) {
            battle();
        }
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
        assert(destination.isInSentryRange());
    }



    private boolean shouldMoveTowardsScannedRobot() {
        if (scannedLocation == null || scannedLocation.isInSentryRange()) {
            return false;
        }
        return rand.nextInt() % 2 == 0;
    }

    private boolean shouldChooseNewDestination() {
        return rand.nextInt() % averageMovesUntilDestinationChanges == 0;
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


    //Turns the shortest angle possible to come to a heading, then returns the direction the
    //the bot needs to move in.

    private void battle() {
        setNewColors();
        drive();
        scan();
        execute();
    }

    private static Random rand = new Random();
    private void setNewColors() {
        setBodyColor(new Color(rand.nextInt()%24));
        setGunColor(new Color(rand.nextInt()%24));
        setBulletColor(new Color(rand.nextInt()%24));
    }

    /**
     * onScannedRobot: Fire hard!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        if (!e.isSentryRobot()) {
            double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
            int scannedX = (int)(getX() + Math.sin(angle) * e.getDistance());
            int scannedY = (int)(getY() + Math.cos(angle) * e.getDistance());
            scannedLocation = new Location(scannedX, scannedY);
            double absoluteBearing = this.getHeading() + e.getBearing();
            double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - this.getGunHeading());
            if (Math.abs(bearingFromGun) <= 3.0D) {
                this.turnGunRight(bearingFromGun);
                if (this.getGunHeat() == 0.0D) {
                    this.fire(Math.min(3.0D - Math.abs(bearingFromGun), this.getEnergy() - 0.1D));
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

    /**
     * onHitRobot:  If it's our fault, we'll stop turning and moving,
     * so we need to turn again to keep spinning.
     */
    public void onHitRobot(HitRobotEvent e) {

        this.turnRight(e.getBearing());
        if (e.getEnergy() > 16.0D) {
            this.fire(3.0D);
        } else if (e.getEnergy() > 10.0D) {
            this.fire(2.0D);
        } else if (e.getEnergy() > 4.0D) {
            this.fire(1.0D);
        } else if (e.getEnergy() > 2.0D) {
            this.fire(0.5D);
        } else if (e.getEnergy() > 0.4D) {
            this.fire(0.1D);
        }

        if ((rand.nextInt() % 3) == 0) battle();
    }


}