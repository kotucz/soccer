package soccer;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kotuc
 */
public class SoccerPainter {

    public static final SimpleDateFormat RINTIME_FORMAT = new SimpleDateFormat("mm:ss:SSS");
    private Font font1 = new Font("Arial", Font.BOLD, 14);

    protected long startTime;

    protected long tick;

    int delay = 100;

    final Pitch pitch;

    public SoccerPainter(Pitch pitch) {
        this.pitch = pitch;
    }

    void newDuel() {
        startTime = System.currentTimeMillis();

        tick = 0;
    }

    void paintField(Graphics g) {
//		System.out.println("paintfield");


        g.drawLine(320, 0, 320, 480);
        g.drawRect(1, 1, Pitch.WIDTH - 2, Pitch.HEIGHT - 2);


        g.setColor(Color.ORANGE);
//		g.fillRect(1, 1, width-2, height-2);
        g.drawRect(580, 160, 60, 160);
        g.drawRect(0, 160, 60, 160);

        Team team1 = pitch.team1;
        Team team2 = pitch.team2;
        Ball ball = pitch.ball;

        team1.paint(g);
        team2.paint(g);
        ball.paint(g);

        g.setFont(font1);

        g.setColor(team1.getTeamColor());
        g.drawString("" + team1.getScore(), 320 + team1.side * 100, 40);
        g.drawString("" + team1.getName(), 240 + team1.side * 200, 40);

        g.setColor(team2.getTeamColor());
        g.drawString("" + team2.getScore(), 320 + team2.side * 100, 40);
        g.drawString("" + team2.getName(), 240 + team2.side * 200, 40);

        g.setColor(Color.CYAN);

        long runTime = System.currentTimeMillis() - startTime;

        g.drawString("time:  " + RINTIME_FORMAT.format(new Date(runTime)), 50, 60);
//        g.drawString("time:  " + runTime / 60000 + ":" + (runTime % 60000) / 100, 50, 60);
        g.drawString("tick:  " + tick, 50, 80);
        g.drawString("delay: " + delay, 50, 100);

    }

}
