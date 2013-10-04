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

    static AudioClip kickClip, melodyClip, goalClip;

    int numPlayers = 5;
    boolean ballType = true;

    Team team1, team2;

    protected Ball ball;

    public void newDuel(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;

//		team1 = new Team();
//		team2 = new Team();

        team1.init(this);
        team2.init(this);
        team1.setSide(-1);
        team2.setSide(1);
        team1.setTeamColor(Color.RED);
//		team1.name = "RED Team";
//		team2.name = "GREEN Team";
        team2.setTeamColor(Color.GREEN);
        team1.opponents = team2.players;
        team2.opponents = team1.players;

        ball = new Ball(this);
        ball.original = ball;

        if ("kotuc.AtomTeam".equals(team2.getClass().getName())) {
            // TODO
//            addMouseListener((MouseListener) team2);
        }


//		new Timer().schedule(new RTTask(), 100);
//new Timer().schedule(new RTTask(), 100, 50);


//		bgClip.loop();


//		new Thread(team1).start();
//		new Thread(team2).start();
        System.out.println("started");
    }

//	java.util.List<Player> players = new LinkedList();

    public int getScore(int side) {
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
            team1.ui0();
        } else {
            team1.ui0();
            team2.ui0();
        }

        team1.doMoves();
        ball.doHalfMove();
        team2.doMoves();
        ball.doHalfMove();

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


