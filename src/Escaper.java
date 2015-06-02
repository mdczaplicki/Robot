import robocode.HitRobotEvent;
import robocode.Robot;

import java.awt.*;
import java.util.Random;

public class Escaper extends Robot
{
    double wallMargin = 100d;
    Random generator = new Random();

    double bfWidth, bfHeight;

    @Override
    public void run()
    {
        setBodyColor(Color.BLACK);
        bfWidth = getBattleFieldWidth();
        bfHeight = getBattleFieldHeight();

        while (true)
        {
            if (getX() < wallMargin || getX() > bfWidth - wallMargin || getY() < wallMargin || getY() > bfHeight - wallMargin)
            {
                boolean n = generator.nextBoolean();
                if (n)
                    turnLeft(generator.nextDouble() * 90 + 90);
                else
                    turnLeft(-(generator.nextDouble() * 90 + 90));
                ahead(200);
            }
            ahead(50);
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e)
    {
        boolean n = generator.nextBoolean();
        if (n)
            turnLeft(generator.nextDouble() * 90 + 90);
        else
            turnLeft(-(generator.nextDouble() * 90 + 90));
        ahead(20);
    }

    /*@Override
    public void onHitWall(HitWallEvent e)
    {
        boolean n = generator.nextBoolean();
        if (n)
            turnLeft(generator.nextDouble() * 90 + 90);
        else
            turnLeft(-(generator.nextDouble() * 90 + 90));
        ahead(20);
    }*/
}
