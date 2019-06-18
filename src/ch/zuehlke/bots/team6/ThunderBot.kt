package ch.zuehlke.bots.team6

import robocode.AdvancedRobot
import robocode.ScannedRobotEvent
import java.awt.Color


class ThunderBot : AdvancedRobot() {
    override fun run() {
        beautifyRobot()

        while (true) {
            if (isInBorderArea()) {
                moveOval()
            } else {
                reverseMoveOval()
            }

        }
    }


    private fun isInBorderArea(): Boolean {
        return x < sentryBorderSize || x > battleFieldWidth - sentryBorderSize || y < sentryBorderSize || y > battleFieldHeight - sentryBorderSize
    }

    private fun moveOval() {
        setTurnLeft(90.0)
        setAhead(100.0)

        while (distanceRemaining > 0 && turnRemaining > 0) {
            execute()
        }

        setTurnRight(45.0)
        setAhead(150.0)

        while (distanceRemaining > 0 && turnRemaining > 0) {
            execute()
        }
    }


    private fun reverseMoveOval() {
        setTurnRight(90.0)
        setAhead(100.0)

        while (distanceRemaining > 0 && turnRemaining > 0) {
            execute()
        }

        setTurnLeft(45.0)
        setAhead(150.0)

        while (distanceRemaining > 0 && turnRemaining > 0) {
            execute()
        }
    }

    private fun beautifyRobot() {
        setBodyColor(Color(135, 45, 220))
        setGunColor(Color(70, 10, 125))
        setRadarColor(Color(220, 215, 230))
        setBulletColor(Color(190, 180, 30))
        setScanColor(Color(240, 210, 250))
    }

    override fun onScannedRobot(e: ScannedRobotEvent?) {
        e?.let {
            if (e.isSentryRobot) return
            // only fire if robot is closer than half a field length
            if (e.distance > Math.max(battleFieldHeight, battleFieldHeight) / 2) return
            else fire(5.0)
        }


    }

}