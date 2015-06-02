/**
 * Created by Marek on 2015-05-27.
 */
import robocode.*;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Leader extends TeamRobot {

    Random generator = new Random();
    @Override
    public void run()
    {
        RobotColors c = new RobotColors();
        c.bodyColor = Color.YELLOW;
        c.bulletColor = Color.RED;
        c.gunColor = Color.RED;
        c.radarColor = Color.RED;
        c.scanColor = Color.RED;

        setBodyColor(Color.RED);
        setGunColor(Color.RED);
        setRadarColor(Color.RED);
        setScanColor(Color.RED);
        try
        {
            broadcastMessage(c);
        } catch (IOException ignored) {}

        while (true)
        {
            radar();
            ahead(100);
            turnLeft(generator.nextInt(90) - 45d);
        }
    }
    @Override
    public void onHitWall(HitWallEvent e)
    {
        back(200);
        turnRight(90d);
    }

    @Override
    public void onHitRobot(HitRobotEvent e)
    {
        back(200);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e)
    {
        if (isTeammate(e.getName()))
        {
            return;
        }
        double enemyBearing = this.getHeading() + e.getBearing();
        double enemy_x = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
        double enemy_y = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

        double dx = enemy_x - this.getX();
        double dy = enemy_y - this.getY();
        double theta = Math.toDegrees(Math.atan2(dx, dy));

        turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
        fire(200);
        try
        {
            broadcastMessage(new Point(enemy_x, enemy_y));
        }
        catch (IOException ex)
        {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }

    private void radar()
    {
        turnRadarLeft(360d);
    }
}
