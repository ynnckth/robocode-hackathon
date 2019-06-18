package ch.zuehlke.bots.team2;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


import java.awt.*;

import static ch.zuehlke.helpers.Helper.absbearing;
import static ch.zuehlke.helpers.Helper.normaliseBearing;
import static ch.zuehlke.helpers.Helper.getRandomIntInclusiveInRange;

public class YastBot extends AdvancedRobot {

    private double BATTLEFIELD_CENTER_X;
    private double BATTLEFIELD_CENTER_Y;

    @Override
    public void run() {
        BATTLEFIELD_CENTER_X = getBattleFieldWidth() / 2;
        BATTLEFIELD_CENTER_Y = getBattleFieldHeight() / 2;

        setBodyColor(Color.BLACK);
        setGunColor(Color.BLACK);
        setRadarColor(Color.BLACK);

        while (true) {
            moveTowardsBattlefieldCenter();
            doRandomStuff();

            execute();
        }
    }

    private void moveTowardsBattlefieldCenter() {
        if (!isWithinBattlefieldCenterRange()) {
            moveTo(BATTLEFIELD_CENTER_X, BATTLEFIELD_CENTER_Y);
        }
    }

    private void doRandomStuff() {
        if (isWithinBattlefieldCenterRange()) {
            setAhead(100);
            turnGunRight(getRandomIntInclusiveInRange(-60, 60));
            if (getRandomIntInclusiveInRange(0, 100) > 50) {
                turnRight(90);
            } else {
                turnLeft(90);
            }
        }
    }

    private boolean isWithinBattlefieldCenterRange() {
        double tolerance = 100;
        return (getX() < BATTLEFIELD_CENTER_X + tolerance && getX() > BATTLEFIELD_CENTER_X - tolerance)
                && (getY() < BATTLEFIELD_CENTER_Y + tolerance && getY() > BATTLEFIELD_CENTER_Y - tolerance);
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
        if (e.getDistance() < 300 && getGunHeat() < 1) {
            fire(1);
        }
    }
}
