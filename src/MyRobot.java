import robocode.*;
import robocode.Robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MyRobot extends Robot
{
    private Random generator = new Random();
    private int counter = 0;
    private double enemy_x = 0;
    private double enemy_y = 0;
    private int[] dim_factor;
    private int radar_angle = 180;
    private boolean radar_dir = true;
    private boolean[][] enemy_tab;
    private ATile aTiles[][];
    private Finish finish = new Finish(38, 38);
    private boolean[][] my_tab;
    private int dimension = 40;
    private int my_tx;
    private int my_ty;
    private boolean clutch = false;
    private int just_once = 0;

    private ArrayList<ATile> open_list = new ArrayList<>();
    private ArrayList<ATile> close_list = new ArrayList<>();
    private double path = 0;

    @Override
    public void run()
    {
        dim_factor = new int[2];

        //takes game width and height
        int field_x = (int)(getBattleFieldWidth() + 0.5d);
        int field_y = (int)(getBattleFieldHeight() + 0.5d);

        //counts a horizontal and vertical map factor
        dim_factor[0] = (int)(field_x/dimension + 0.5d);
        dim_factor[1] = (int)(field_y/dimension + 0.5d);
        enemy_tab = new boolean[dimension][dimension];
        my_tab = new boolean[dimension][dimension];

        //initial radar move, so that its range is from -90 to +90 deg.
        turnRadarLeft(radar_angle / 2);
        radar();

        //A start implementation
        aTiles = new ATile[dimension][dimension];
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                double fromStart = (double)Math.round(countDistance(3, 3, i, j)*100)/100;
                double toEnd = (double)Math.round(countDistance(i, j, finish.x, finish.y)*100)/100;
                boolean isAvailable = !enemy_tab[i][j];
                aTiles[i][j] = new ATile(fromStart, toEnd, isAvailable, null);
            }
        }

        meAsObstacle();
        open_list.add(aTiles[my_tx][my_ty]);

        while (open_list.size() > 0)
        {
            meAsObstacle();
            int minIndex = 0;
            double tmp = 1000;
            for (int index = 0; index < open_list.size(); index++)
            {
                if (open_list.get(index).f < tmp)
                {
                    tmp = open_list.get(index).f;
                    minIndex = index;
                }
            }
            close_list.add(open_list.get(minIndex));
            if (open_list.get(minIndex).h == 0)
            {
                out.println("Exit found!");
                break;
            }
            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    if (!aTiles[i][j].available || close_list.contains(aTiles[i][j]));
                    else if (!open_list.contains(aTiles[i][j]))
                    {
                        aTiles[i][j].parent = aTiles[my_tx][my_ty];
                        path += countDistance(my_tx, my_ty, i, j);
                        aTiles[i][j].g = path;
                        aTiles[i][j].f = aTiles[i][j].g + aTiles[i][j].h;
                        open_list.add(aTiles[i][j]);
                    }
                }
            }

            ahead(1);
            if (just_once < 100) radar();
            meAsObstacle();
            //if (counter == 10)
            //{
            //counter = 0;
            //out.println("X = " + (double)Math.round(getX() * 100)/100 + "|| Y = " + (double)Math.round(getY() * 100)/100);
            //}
            //counter++;
        }
    }

    @Override
    public void onHitWall(HitWallEvent event)
    {
        turnRight(this.generator.nextInt(150));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event)
    {
        double angle = event.getBearing();
        double dist = event.getDistance();
        enemy_x = (dist * Math.sin(Math.toRadians(getHeading() + angle)) + getX() - 20);
        enemy_y = (dist * Math.cos(Math.toRadians(getHeading() + angle)) + getY() - 20);
        enemyAsObstacle();
    }

    @Override
    public void onHitRobot(HitRobotEvent event)
    {
        back(40);
    }

    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                graphics2D.setColor(Color.green);
                graphics2D.drawRect(i * dim_factor[0], j * dim_factor[1], dim_factor[0], dim_factor[1]);
            }

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
            {
                graphics2D.setColor(Color.red);
                if (enemy_tab[i][j])
                    graphics2D.drawRect(i * dim_factor[0], j * dim_factor[1], dim_factor[0], dim_factor[1]);
                if (my_tab[i][j]) {
                    graphics2D.setColor(Color.blue);
                    graphics2D.drawRect(i * dim_factor[0], j * dim_factor[1], dim_factor[0], dim_factor[1]);
                }
            }

        // Target an enemy
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

    public void meAsObstacle()
    {
        my_tx = (int)(getX() + 0.5d) / dim_factor[0];
        my_ty = (int)(getY() + 0.5d) / dim_factor[1];
        my_tab = new boolean[dimension][dimension];
        my_tab[my_tx][my_ty] = true;

        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                try
                {
                    my_tab[my_tx + i][my_ty + j] = true;
                }
                catch (ArrayIndexOutOfBoundsException e){}
    }

    public void enemyAsObstacle()
    {
        // scans map 100 times, prevents changing map stats.
        if (just_once < 100)
        {
            int enemy_tx = (int) ((enemy_x / dim_factor[0]) + 0.5d);
            int enemy_ty = (int) ((enemy_y / dim_factor[1]) + 0.5d);
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
                    catch (ArrayIndexOutOfBoundsException e) {}
                }
            }
            just_once++;
        }
    }

    public boolean tabComparator()
    {
        for (int i = 0; i < dimension; i ++)
            for (int j = 0; j < dimension; j++)
                if (my_tab[i][j] && enemy_tab[i][j])
                    return true;
        return false;
    }

    public double countDistance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}