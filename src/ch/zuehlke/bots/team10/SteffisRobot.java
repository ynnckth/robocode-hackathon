package ch.zuehlke.bots.team10;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static ch.zuehlke.helpers.Helper.normaliseBearing;

public class SteffisRobot extends AdvancedRobot {

    static Stack<Move> nextMoves;
    static Map<String, ScannedRobotEvent> enemyRobots = new HashMap<>();

    double battleFieldHeight;
    double battleFieldWidth;
    int sentryBorderSize;

    int x_coord;
    int y_coord;

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
        battleFieldHeight = getBattleFieldHeight();
        battleFieldWidth = getBattleFieldWidth();
        sentryBorderSize = getSentryBorderSize();
        while(true) {
            x_coord = (int) getX();
            y_coord = (int) getY();

            setTurnRadarRight(Double.POSITIVE_INFINITY);
            moveDistanceIfNotInBorder(50, 50);
            turnGunRight(Utils.normalRelativeAngleDegrees(90));
            moveDistanceIfNotInBorder(-50, -50);
            turnGunRight(Utils.normalRelativeAngleDegrees(90));
            execute();
        }
    }

    private void backIfNotInBorder(int i) {
        back(i);
    }

    private boolean moveDistanceIfNotInBorder(int x, int y) {
        if(isInBorder(x_coord+x, y_coord+y)){
            return false;
        }
        else {
            goTo(x_coord+x, y_coord+y);
            return true;
        }
    }

    private boolean isInBorder(double x, double y){
        return (x > sentryBorderSize && x < battleFieldWidth-sentryBorderSize) && (y > sentryBorderSize && y < battleFieldHeight-sentryBorderSize);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(e.isSentryRobot()){
            return;
        }
        String name = e.getName();
        double n_energy = e.getEnergy();
        double n_velocity = e.getVelocity();
        double n_bearing = e.getBearing();
        double n_heading = e.getHeading();
        double n_distance = e.getDistance();

        if(firedBullet(enemyRobots.get(name).getEnergy(), n_energy)){
            moveRandomly();
        }

        if(n_distance < battleFieldWidth / 3 && getEnergy() > 10) {
            turnTo(e.getBearing());
            fire(3);
        }
        enemyRobots.put(name, e);
        System.out.println("Scanned enemy: " + name + " with energy " + n_energy + ", heading "
                + n_heading + ", bearing " + n_bearing + ", distance " + n_distance + ", velocity " + n_velocity);
    }

    public void onBulletHit(BulletHitEvent e) {
        moveRandomly();
    }

    private void moveRandomly() {
        int rand_x = (int) (10 * Math.random());
        int rand_y = (int) (10 * Math.random());
        if(!isInBorder(x_coord + rand_x, y_coord + rand_y)) {
            go(x_coord + rand_x, y_coord);
        }
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
