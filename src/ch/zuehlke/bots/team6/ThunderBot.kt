package ch.zuehlke.bots.team6

import ch.zuehlke.helpers.Helper.absbearing
import ch.zuehlke.helpers.Helper.normaliseBearing
import robocode.AdvancedRobot
import robocode.ScannedRobotEvent
import java.awt.Color


class ThunderBot : AdvancedRobot() {
    private var centerX = 0.0
    private var centerY = 0.0

    override fun run() {
        beautifyRobot()
        centerX = battleFieldWidth / 2
        centerY = battleFieldHeight / 2
        setMaxVelocity(2.0)

        while (true) {
            if (!isInBorderArea()) {
                moveOval()
            } else {
                print("move to center. location is $x, $y")
                goTo(centerX, centerY)
            }
        }
    }

    private fun isInBorderArea(): Boolean {
        return x < sentryBorderSize || x > battleFieldWidth - sentryBorderSize || y < sentryBorderSize || y > battleFieldHeight - sentryBorderSize
    }

    private fun moveOval() {
        setTurnLeft(90.0)
        setAhead(200.0)

        finishExecution()

        setTurnRight(45.0)
        setAhead(250.0)

        finishExecution()
    }

    private fun beautifyRobot() {
        setBodyColor(Color(135, 45, 220))
        setGunColor(Color(70, 10, 125))
        setRadarColor(Color(220, 215, 230))
        setBulletColor(Color(190, 180, 30))
        setScanColor(Color(240, 210, 250))
    }

    private fun goTo(x: Double, y: Double) {
        val dist = 20.0
        val angle = Math.toDegrees(absbearing(getX(), getY(), x, y))
        val r = turnTo(angle).toDouble()
        setAhead(dist * r)
        finishExecution()
    }

    private fun finishExecution() {
        while (distanceRemaining > 0 && turnRemaining > 0) {
            execute()
        }
    }

    private fun turnTo(angle: Double): Int {
        var ang: Double = normaliseBearing(heading - angle)
        val dir: Int
        when {
            ang > 90 -> {
                ang -= 180.0
                dir = -1
            }
            ang < -90 -> {
                ang += 180.0
                dir = -1
            }
            else -> dir = 1
        }
        setTurnLeft(ang)
        return dir
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