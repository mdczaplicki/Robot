import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.awt.event.PaintEvent;
import java.util.Random;

public class MyRobot extends Robot
{
    Random generator = new Random();
    int counter = 0;

    @Override
    public void run()
    {
        while (true)
        {
            if (counter == 10)
            {
                counter = 0;
                out.println("X = " + (double)Math.round(getX() * 100)/100 + "|| Y = " + (double)Math.round(getY() * 100)/100);
            }
            turnLeft(generator.nextInt(40) - 20);
            ahead(20);
            counter++;
        }
    }

    @Override
    public void onHitWall(HitWallEvent event)
    {
        turnRight(generator.nextInt(150));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event)
    {
        out.println(event.getBearing());
        fire(50);
    }

    @Override
    public void onHitRobot(HitRobotEvent event)
    {
        back(40);
    }

    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        graphics2D.drawLine(100, 100, 500, 500);
        onPaint(graphics2D);
    }
}
