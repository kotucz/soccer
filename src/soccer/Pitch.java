package soccer;

import java.applet.AudioClip;
import java.awt.*;


/**
 * @author kotucz
 */
public class Pitch {

    /**
     * Creates a new instance of Pitch
     */
    public Pitch() {
    }

    public static final int LEFTX = 3;
    public static final int TOPY = 3;
    public static final int CENTX = 320;
    public static final int CENTY = 240;
    public static final int RIGHTX = 637;
    public static final int BOTTOMY = 477;

    public static final int WIDTH = 640;//getWidth();
    public static final int HEIGHT = 480;//getHeight();

    static int numPlayers = 5;
    static boolean ballType = true;

    static Team team1, team2;

    protected static Ball ball;

    static AudioClip kickClip, melodyClip, goalClip;

    static int delay = 100;

    public void newDuel(Team team1, Team team2) {

//		team1 = new Team();
//		team2 = new Team();

        team1.setSide(-1);
        team2.setSide(1);
        team1.setTeamColor(Color.RED);
//		team1.name = "RED Team";
//		team2.name = "GREEN Team";
        team2.setTeamColor(Color.GREEN);
        team1.opponents = team2.players;
        team2.opponents = team1.players;
        Team.pitch = this;

        ball = new Ball();
        ball.original = ball;

        if ("kotuc.AtomTeam".equals(team2.getClass().getName())) {
            // TODO
//            addMouseListener((MouseListener) team2);
        }

        startTime = System.currentTimeMillis();

        tick = 0;

//		new Timer().schedule(new RTTask(), 100);
//new Timer().schedule(new RTTask(), 100, 50);


//		bgClip.loop();


//		new Thread(team1).start();
//		new Thread(team2).start();
        System.out.println("started");
    }

//	java.util.List<Player> players = new LinkedList();

    public static int getScore(int side) {
        if (side == -1) {
            return team1.score;
        } else if (side == 1) {
            return team2.score;
        } else {
            return -1;
        }
    }

    Ball getBall() {
        return ball;
    }

    protected void doTeamMoves() {

        team1.ui0();
        team1.doMoves();

        ball.doHalfMove();

//		tick++;

        team2.ui0();
        team2.doMoves();

        ball.doHalfMove();

        tick++;

//		for (Player p1:players) {


//	for (Vert v2:verts) {
//	if (v1!=v2) v1.apply(v2);
//}
//}
    }

    protected void doTeamMovesWithRandom() {

        boolean swap = Math.random() < 0.5;

        if (swap) {
            team2.ui0();
//		tick++;
            team1.ui0();
        } else {
            team1.ui0();
//		tick++;
            team2.ui0();
        }

        team1.doMoves();
        ball.doHalfMove();
        team2.doMoves();
        ball.doHalfMove();

        tick++;

    }

    private Font font1 = new Font("Arial", Font.BOLD, 14);

    protected long startTime;

    protected long tick;

    void paintField(Graphics g) {
//		System.out.println("paintfield");        


        g.drawLine(320, 0, 320, 480);
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);


        g.setColor(Color.ORANGE);
//		g.fillRect(1, 1, width-2, height-2);
        g.drawRect(580, 160, 60, 160);
        g.drawRect(0, 160, 60, 160);


        team1.paint(g);
        team2.paint(g);
        ball.paint(g);

        g.setFont(font1);

        g.setColor(team1.getTeamColor());
        g.drawString("" + team1.score, 320 + team1.side * 100, 40);
        g.drawString("" + team1.name, 240 + team1.side * 200, 40);

        g.setColor(team2.getTeamColor());
        g.drawString("" + team2.score, 320 + team2.side * 100, 40);
        g.drawString("" + team2.name, 240 + team2.side * 200, 40);

        g.setColor(Color.CYAN);

        g.drawString("time:  " + (System.currentTimeMillis() - startTime) / 60000 + ":" + ((System.currentTimeMillis() - startTime) % 60000) / 100, 50, 60);
        g.drawString("tick:  " + tick, 50, 80);
        g.drawString("delay: " + delay, 50, 100);


    }

    private long benchstart;

    public long bench() {
        long b = benchstart;
        benchstart = System.currentTimeMillis();
        return benchstart - b;
    }

    static void playSound(AudioClip audioClip) {
        if (audioClip != null) {
            audioClip.play();
        }
    }


}


