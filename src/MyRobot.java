import robocode.*;
import robocode.Robot;

import java.awt.*;
import java.util.Random;

public class MyRobot extends Robot
{
    private RobotStatus robotstatus;
    private Random generator = new Random();
    private int counter = 0;
    private double enemy_x = 0;
    private double enemy_y = 0;
    private int enemy_tx = 0;
    private int enemy_ty = 0;
    private int field_x;
    private int field_y;
    private int[] dim_factor;
    private int radar_angle = 180;
    private boolean radar_dir = true;
    private boolean[][] enemy_tab;
    private int dimension = 40;

    @Override
    public void run()
    {
        dim_factor = new int[2];
        field_x = (int)getBattleFieldWidth();
        field_y = (int)getBattleFieldHeight();
        dim_factor[0] = field_x/dimension;
        dim_factor[1] = field_y/dimension;
        enemy_tab = new boolean[dimension][dimension];
        turnRadarLeft(radar_angle/2);

        while (true)
        {
            radar();
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

        enemy_tx = (int)(enemy_x/dim_factor[0]);
        enemy_ty = (int)(enemy_y/dim_factor[1]);
        enemy_tab[enemy_tx][enemy_ty] = true;
        //fire(50);
        for (int i = -1; i < 3; i++)
        {
            for (int j = -1; j < 3; j++)
            {
                try
                {
                    enemy_tab[enemy_tx + i][enemy_ty + j] = true;
                }
                catch (IndexOutOfBoundsException e)
                {

                }
            }
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        back(40);
    }

    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                graphics2D.setColor(Color.green);
                graphics2D.drawRect(i * dim_factor[0], j * dim_factor[1], dim_factor[0], dim_factor[1]);
            }
        }
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                graphics2D.setColor(Color.red);
                if (enemy_tab[i][j]) graphics2D.drawRect(i * dim_factor[0], j * dim_factor[1], dim_factor[0], dim_factor[1]);
            }
        }
        /*Rectangle rect = new Rectangle();
        rect.setLocation((int) enemy_x, (int) enemy_y);
        rect.setSize(40, 40);

        graphics2D.draw(rect);
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.setColor(Color.RED);*/
    }

    public void radar()
    {
        if (radar_dir) turnRadarRight(radar_angle);
        else turnRadarLeft(radar_angle);
        radar_dir = !radar_dir;
    }
}
