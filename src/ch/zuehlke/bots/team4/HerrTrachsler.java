package ch.zuehlke.bots.team4;
import robocode.*;

import java.util.concurrent.ThreadLocalRandom;

public class HerrTrachsler extends Robot {
    public void run() {
        while (true) {
            int scale = ThreadLocalRandom.current().nextInt(1, 3);
            ahead(100 * scale);
            turnGunRight(360);
            back(100*scale);
            turnGunRight(360);
        }
    }


    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(45);
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        turnLeft(45);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        turnLeft(45);
    }
}
