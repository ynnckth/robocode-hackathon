package ch.zuehlke.bots.team2;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import static ch.zuehlke.helpers.Helper.absbearing;
import static ch.zuehlke.helpers.Helper.normaliseBearing;

public class YastBot extends AdvancedRobot {

    private double BATTLEFIELD_CENTER_X;
    private double BATTLEFIELD_CENTER_Y;

    @Override
    public void run() {
        BATTLEFIELD_CENTER_X = getBattleFieldWidth() / 2;
        BATTLEFIELD_CENTER_Y = getBattleFieldHeight() / 2;

        while (true) {
            // TODO: find center and drive towards it
            moveTowardsBattlefieldCenter();
            orbitBattlefieldCenter();

            execute();
        }
    }

    private void moveTowardsBattlefieldCenter() {
        System.out.println("Reached center: " + isWithinBattlefieldCenterRange());
        if (!isWithinBattlefieldCenterRange()) {
            moveTo(BATTLEFIELD_CENTER_X, BATTLEFIELD_CENTER_Y);
        }
    }

    private boolean isWithinBattlefieldCenterRange() {
        double tolerance = 20;
        return (getX() < BATTLEFIELD_CENTER_X - tolerance && getX() > BATTLEFIELD_CENTER_X + tolerance)
                && (getY() < BATTLEFIELD_CENTER_Y - tolerance && getY() > BATTLEFIELD_CENTER_Y + tolerance);
    }

    // TODO: drive in circles around center
    private void orbitBattlefieldCenter() {
        // if (isWithinBattlefieldCenterRange()) {
        turnGunRight(360);
        // }
    }

    private void moveTo(double x, double y) {
        double dist = 20;
        double angle = Math.toDegrees(absbearing(getX(), getY(), x, y));
        double r = turnTo(angle);
        setAhead(dist * r);
    }

    private int turnTo(double angle) {
        double ang;
        int dir;
        ang = normaliseBearing(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            dir = -1;
        } else if (ang < -90) {
            ang += 180;
            dir = -1;
        } else {
            dir = 1;
        }
        setTurnLeft(ang);
        return dir;
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
}
