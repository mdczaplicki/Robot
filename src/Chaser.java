import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class Chaser extends Robot
{
    @Override
    public void run()
    {
        turnRadarRight(360);
        while (true)
        {
            setBodyColor(Color.RED);
            ahead(50);
            setBodyColor(Color.BLUE);
            turnRadarLeft(180);
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e)
    {
        double angle = e.getBearing();
        turnRight(angle);
        ahead(80);
    }

    @Override
    public void onHitRobot(HitRobotEvent e)
    {
        back(50);
        turnGunRight(e.getBearing());
        fire(20);
    }
}
