package ch.zuehlke.bots.team4;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class HappynessIsAWarmGun extends AdvancedRobot {
    public static final Color BODY_COLOR = new Color((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255));
    public static final Color SOUL_COLOR = new Color(197, 196, 6);

    @Override
    public void run() {
        List<Integer> scale = Arrays.asList(1,2,3,5,8);
        int count = 3;
        while(true) {
            setTurnRight(10000);
            //setMaxVelocity(5);
            setMaxVelocity(scale.get(count % 5));
            ahead(10000);
            setBodyColor(getRandomColor());
            setGunColor(getRandomColor());
            count++;
        }
    }

    private Color getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(3);
    }

    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -10 && e.getBearing() < 10) {
            fire(3);
        }
        if (e.isMyFault()) {
            back(100);
            turnRight(10);
        }
    }
}

