package ch.zuehlke.bots.team10;

import robocode.*;
import robocode.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static ch.zuehlke.helpers.Helper.absbearing;
import static ch.zuehlke.helpers.Helper.normaliseBearing;

public class SteffisRobot extends AdvancedRobot {

    static Stack<Object> nextMoves;
    static Map<String, ScannedRobotEvent> enemyRobots = new HashMap<>();


    double battleFieldHeight = getBattleFieldHeight();
    double battleFieldWidth = getBattleFieldWidth();
    int sentryBorderSize = getSentryBorderSize();

    private class Move {
        double gun;
        double radar;
        double heading;
        double acceleration;
        double velocity;
        double distance;
        double gunHeat;
    }

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        while(true) {
            setTurnRadarRight(Double.POSITIVE_INFINITY);


            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        String name = e.getName();
        double n_energy = e.getEnergy();
        double n_velocity = e.getVelocity();
        double n_bearing = e.getBearing();
        double n_heading = e.getHeading();
        double n_distance = e.getDistance();

        fire(1);
        enemyRobots.put(name, e);
    }


    public boolean firedBullet(double previousLifePower, double currentLifePower) {
        double lifePowerDrop = previousLifePower-currentLifePower;
        return (lifePowerDrop >= 0.1 && lifePowerDrop <= 3.0);
    }


  //  This method should be called every round
    private void goTo(int x, int y) {
        double a;
        setTurnRightRadians(Math.tan(
                a = Math.atan2(x -= (int) getX(), y -= (int) getY())
                        - getHeadingRadians()));
        setAhead(Math.hypot(x, y) * Math.cos(a));
    }

    // over longer turn periods
    private void go(double x, double y) {
        /* Calculate the difference bettwen the current position and the target position. */
        x = x - getX();
        y = y - getY();

        /* Calculate the angle relative to the current heading. */
        double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());

        /*
         * Apply a tangent to the turn this is a cheap way of achieving back to front turn angle as tangents period is PI.
         * The output is very close to doing it correctly under most inputs. Applying the arctan will reverse the function
         * back into a normal value, correcting the value. The arctan is not needed if code size is required, the error from
         * tangent evening out over multiple turns.
         */
        setTurnRightRadians(Math.atan(Math.tan(goAngle)));

        /*
         * The cosine call reduces the amount moved more the more perpendicular it is to the desired angle of travel. The
         * hypot is a quick way of calculating the distance to move as it calculates the length of the given coordinates
         * from 0.
         */
        setAhead(Math.cos(goAngle) * Math.hypot(x, y));
    }

    int turnTo(double angle) {
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
}
