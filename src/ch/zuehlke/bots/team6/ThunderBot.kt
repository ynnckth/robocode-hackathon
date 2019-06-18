package ch.zuehlke.bots.team6

import ch.zuehlke.helpers.Helper.absbearing
import ch.zuehlke.helpers.Helper.normaliseBearing
import robocode.AdvancedRobot
import robocode.HitByBulletEvent
import robocode.HitRobotEvent
import robocode.ScannedRobotEvent
import java.awt.Color


class ThunderBot : AdvancedRobot() {
    private var centerX = 0.0
    private var centerY = 0.0

    override fun run() {
        beautifyRobot()
        centerX = battleFieldWidth / 2
        centerY = battleFieldHeight / 2

        while (true) {
            if (!isInBorderArea()) {
                println("move oval. location is $x, $y")
                moveOval()
            } else {
                println("move out of border. location is $x, $y")
                goTo(centerX, centerY)
            }
        }
    }

    private fun isInBorderArea(): Boolean {
        return x < sentryBorderSize || x > battleFieldWidth - sentryBorderSize || y < sentryBorderSize || y > battleFieldHeight - sentryBorderSize
    }

    private fun moveOval() {
        setTurnLeft(90.0)
        setAhead(100.0)
        execute()
        finishExecution()
    }

    private fun beautifyRobot() {
        setBodyColor(Color(135, 45, 220))
        setGunColor(Color(70, 10, 125))
        setRadarColor(Color(220, 215, 230))
        setBulletColor(Color(190, 180, 30))
        setScanColor(Color(240, 210, 250))
    }

    private fun goTo(currentX: Double, currentY: Double) {
        while (x != currentX && y != currentY) {
            val dist = 20.0
            val angle = Math.toDegrees(absbearing(x, y, currentX, currentY))
            val r = turnTo(angle).toDouble()
            setAhead(dist * r)
            execute()
        }
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
        println("Gotcha!. location is $x, $y")
        e?.let {
            if (e.isSentryRobot) return
            // only fire if robot is closer than half a field length
            if (e.distance > Math.max(battleFieldHeight, battleFieldHeight) / 2) return
            else fire(5.0)
        }
    }

    override fun onHitByBullet(e: HitByBulletEvent?) {
        println("evasive maneuver. location is $x, $y")
        e?.let {
            turnTo(e.bearing - 45)
            ahead(25.0)
        }
    }

    override fun onHitRobot(e: HitRobotEvent?) {
        e?.let {
            turnTo(e.bearing)
            fire(5.0)
            back(50.0)
        }
    }

}