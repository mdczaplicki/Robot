/**
 * Created by Marek on 2015-05-27.
 */
import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.Droid;

public class TeamDroid extends TeamRobot implements Droid
{
    public void run()
    {
        out.println("MyFirstDroid ready.");
        while (true)
        {
            ahead(50);
            back(50);
        }
    }

    public void onMessageReceived(MessageEvent e)
    {
        if (e.getMessage() instanceof Point)
        {
            Point p = (Point) e.getMessage();
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            fire(200);
        }
        else if (e.getMessage() instanceof RobotColors)
        {
            RobotColors c = (RobotColors) e.getMessage();

            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
}
