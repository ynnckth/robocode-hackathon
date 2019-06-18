package ch.zuehlke.bots.team5;

import ch.zuehlke.helpers.Helper;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class RaphaelBot extends AdvancedRobot {

    private double height;
    private double width;

    public void run() {
        height = getBattleFieldHeight();
        width = getBattleFieldWidth();
        int borderSize = getSentryBorderSize();
        double reducedFieldXMax = width - borderSize;
        double reducedFieldYMax = height - borderSize;

        setBodyColor(new Color(0, 200, 100));
        setGunColor(new Color(0, 150, 50));
        setRadarColor(new Color(0, 100, 100));
        setBulletColor(new Color(0, 200, 0));
        setScanColor(new Color(255, 200, 200));

        while (true) {


            if (!isInBorderArea(reducedFieldXMax, reducedFieldYMax, borderSize)) {
                turnTo(makeStraight((int) getHeading()));
                ahead(80);
                //turnRight(20);
            } else {
                goTo(width / 2, height / 2, 110);
                turnRight(100);
                //boolean wasMovingForward = goTo(height / 2, width / 2, width / 8);
                //if (!wasMovingForward) {
                //    turnLeft(20);
                //}
            }

            //ahead(100);
            //turnGunRight(360);
            //back(100);
            //turnGunRight(360);
        }
    }

    private static int makeStraight(int currentAngle) {
        return (currentAngle / 90) * 90;
    }

    /**
     * Move towards an x and y coordinate
     */
    private boolean goTo(double x, double y, double dist) {
        double angle = Math.toDegrees(Helper.absbearing(getX(), getY(), x, y));
        boolean forward = turnTo(angle);
        ahead(dist * (forward ? 1 : -1));
        return forward;
    }


    /**
     * Turns the shortest angle possible to come to a heading, then returns the direction the
     * the bot needs to move in.
     */
    private boolean turnTo(double angle) {
        double ang;
        boolean forward;
        ang = Helper.normaliseBearing(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            forward = false;
        } else if (ang < -90) {
            ang += 180;
            forward = false;
        } else {
            forward = true;
        }
        turnLeft(ang);
        return forward;
    }


    private double turnGunTo(double angle) {
        double ang;
        boolean forward;
        ang = Helper.normaliseBearing(getGunHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            forward = false;
        } else if (ang < -90) {
            ang += 180;
            forward = false;
        } else {
            forward = true;
        }
        return ang;
    }

    private boolean isInBorderArea(double reducedFieldXMax, double reducedFieldYMax, int borderSize) {

        double posX = getX();
        double posY = getY();

        return (posX < borderSize || posX > reducedFieldXMax) || (posY < borderSize || posY > reducedFieldYMax);
    }


    private boolean isTired() {
        double currentEnergy = getEnergy();
        return (currentEnergy < 20);
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        double dist = e.getDistance();
        if (dist > height / 2) {
            return;
        }

        //turnTo(e.getBearing());
        if (dist < 300) {
            if (!isTired()) {
                turnGunTo(e.getBearing());
                fire(1);
            }
        }
    }
}