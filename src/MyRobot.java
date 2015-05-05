import javafx.scene.shape.Circle;
import robocode.*;
import robocode.Robot;

import java.awt.*;
import java.awt.event.PaintEvent;
import java.util.Random;

public class MyRobot extends Robot
{
    private RobotStatus robotstatus;
    private Random generator = new Random();
    private int counter = 0;
    private double enemy_x = 0;
    private double enemy_y = 0;

    @Override
    public void run()
    {
        while (true)
        {
            enemy_x = robotstatus.getX() - 20;
            enemy_y = robotstatus.getY() - 20;
            if (counter == 10)
            {
                counter = 0;
                out.println("X = " + (double)Math.round(getX() * 100)/100 + "|| Y = " + (double)Math.round(getY() * 100)/100);
            }
            turnLeft(generator.nextInt(20) - 10);
            ahead(20);
            counter++;
        }
    }

    @Override
    public void onStatus(StatusEvent event)
    {
        this.robotstatus = event.getStatus();
    }

    @Override
    public void onHitWall(HitWallEvent event)
    {
        turnRight(generator.nextInt(150));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event)
    {
        double angle = event.getBearing();
        double dist = event.getDistance();
        enemy_x = (dist * Math.sin(Math.toRadians(robotstatus.getHeading() + angle)) + robotstatus.getX() - 20);
        enemy_y = (dist * Math.cos(Math.toRadians(robotstatus.getHeading() + angle)) + robotstatus.getY() - 20);
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
        Rectangle rect = new Rectangle();
        rect.setLocation((int) enemy_x, (int) enemy_y);
        rect.setSize(40, 40);

        graphics2D.draw(rect);
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.setColor(Color.RED);
    }
}
