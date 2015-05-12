import robocode.Robot;
import robocode.ScannedRobotEvent;

import javax.xml.soap.Node;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Robot2 extends Robot {

    int just_once = 0;
    int dimension = 40;
    boolean[][] enemy_tab = new boolean[dimension][dimension];
    boolean[][] enemy_tab1 = new boolean[dimension/2][dimension/2];
    int factor;
    double enemy_x, enemy_y;


    Point start = new Point(1, 1);
    Point goal = new Point(18, 18);

    Field map[][] = new Field[19][19];
    ArrayList<Field> path;

    int counter = 0;
    boolean finish = false;

    @Override
    public void run()
    {
        factor = (int) getBattleFieldWidth() / dimension;
        int n = start.x;
        int m = start.y;

        while (true)
        {
            turnRadarLeft(10d);
            try
            {
                if (path.size() == 0) finish = true;
            }
            catch (NullPointerException ignored){}
            if (just_once == 100)
            {
                initializeAStar();
                aStar(start, goal);
                just_once++;
                counter = 0;
            }
            else if (just_once > 100 && counter == 20 && path.size() > 0)
            {
                moveTank(path.get(path.size()-1).getI() - n, path.get(path.size()-1).getJ() - m);
                n = path.get(path.size() - 1).getI();
                m = path.get(path.size() - 1).getJ();
                path.remove(path.size() - 1);
                counter = 0;
            }
            counter++;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e)
    {
        if (just_once < 100)
        {
            double angle = e.getBearing();
            double dist = e.getDistance();
            enemy_x = (dist * Math.sin(Math.toRadians(getHeading() + angle)) + getX());
            enemy_y = (dist * Math.cos(Math.toRadians(getHeading() + angle)) + getY());

            int enemy_i = (int)(enemy_x/factor + 0.5d);
            int enemy_j = (int)(enemy_y/factor + 0.5d);
            enemy_tab[enemy_i][enemy_j] = true;
            from40to20();

            just_once++;
        }
    }

    @Override
    public void onPaint(Graphics2D g)
    {

        for (int i = 0; i < dimension/2 - 1; i++)
        {
            for (int j = 0; j < dimension/2 - 1; j++)
            {
                g.setColor(Color.GREEN);
                g.drawRect(i * factor * 2 + factor, j * factor * 2 + factor, factor * 2, factor * 2);
            }
        }
        for (int i = 0; i < dimension/2 - 1; i++)
        {
            for (int j = 0; j < dimension/2 - 1; j++)
            {
                g.setColor(Color.RED);
                if (enemy_tab1[i][j]) g.drawRect(i * factor * 2 + factor, j * factor * 2 + factor, factor * 2, factor * 2);
            }
        }
        g.setColor(Color.WHITE);
        if (finish) g.drawString("I made it!!!", 18 * 2 * factor, 39.5f * factor);
    }

    void from40to20()
    {
        for (int i = 1; i < dimension - 3; i += 2)
        {
            for (int j = 1; j < dimension - 3; j += 2)
            {
                enemy_tab1[(int)i/2][(int)j/2] = enemy_tab[i][j] || enemy_tab[i+1][j+1] || enemy_tab[i+1][j] || enemy_tab[i][j+1];
            }
        }
    }

    void aStar(Point start, Point goal)
    {
        ArrayList<Field> closed = new ArrayList<>();
        ArrayList<Field> open = new ArrayList<>();
        map[start.x][start.y].setG(0d);
        map[start.x][start.y].setF();
        map[start.x][start.y].setParent(null);
        open.add(map[start.x][start.y]);

        while(!open.isEmpty())
        {
            double temp = 1000;
            Field Q = new Field();
            for (Field f : open)
            {
                if (f.getF() < temp)
                {
                    temp = f.getF();
                    Q = f;
                }
            }
            out.println("i = " + Q.getI() + " j = " + Q.getJ());
            open.remove(Q);
            closed.add(Q);
            if (Q.getI() == goal.x && Q.getJ() == goal.y)
            {
                out.println("Found goal.");
                break;
            }
            for (int i = Q.getI() - 1; i < Q.getI() + 2; i ++)
            {
                for (int j = Q.getJ() - 1; j < Q.getJ() + 2; j++)
                {
                    if (Math.abs(i - Q.getI()) == Math.abs(j - Q.getJ())) continue;
                    try {
                        if (!map[i][j].isAvailable() || closed.contains(map[i][j])) ;
                        else if (!open.contains(map[i][j])) {
                            map[i][j].setParent(Q);
                            map[i][j].setG(Q.getG() + countDistance(i, j, Q.getI(), Q.getJ()));
                            map[i][j].setF();
                            open.add(map[i][j]);
                        } else {
                            double newG = Q.getG() + countDistance(i, j, Q.getI(), Q.getJ());
                            if (newG < Q.getG()) {
                                open.remove(map[i][j]);
                                map[i][j].setParent(Q);
                                map[i][j].setG(newG);
                                map[i][j].setF();
                                open.add(map[i][j]);
                            }
                        }
                    }
                    catch (IndexOutOfBoundsException ignored){}
                }
            }
        }
        path = new ArrayList<>();
        Field temp = new Field();
        temp = map[18][18];
        path.add(temp);
        while (temp != map[0][0])
        {
            try
            {
                path.add(temp.getParent());
                temp = temp.getParent();
            }
            catch (NullPointerException e)
            {
                break;
            }
        }
        path.remove(path.size() - 1);
    }

    void initializeAStar()
    {
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                map[i][j] = new Field();
                map[i][j].setI(i);
                map[i][j].setJ(j);
                map[i][j].setAvailable(!enemy_tab1[i][j]);
                map[i][j].setH(countDistance(i, j, goal.x, goal.y));
            }
        }
    }

    double countDistance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    void moveTank(int i, int j)
    {
        int angle = (int)getHeading();
        int goal_angle = 0;
        /*if (i == 0)
        {
            if (j == 1) goal_angle = 0;
            else if (j == -1) goal_angle = 180;
        }
        else goal_angle = i * Math.abs(j - 3) * 45;*/
        if(i==0 && j==1) goal_angle = 0;
        else if(i==1 && j==0) goal_angle = 90;
        else if(i==0 && j==-1) goal_angle = 180;
        else if(i==-1 && j==0) goal_angle = -90;
        else if(i==-1 && j==1) goal_angle = -45;
        else if(i==1 && j==1) goal_angle = 45;
        else if(i==1 && j==-1) goal_angle = 135;
        else if(i==-1 && j==-1) goal_angle = -135;
        else goal_angle = 0;
        if (angle != goal_angle) turnRight(goal_angle - angle);
        if ((i != 0 || j != 0) && (i & j) == 0) ahead(factor*2);
        else if (i != 0 || j != 0) ahead(Math.sqrt(2 * Math.pow(factor*2, 2)));
    }
}
