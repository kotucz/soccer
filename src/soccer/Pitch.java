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

    Team team1, team2;

    protected Ball ball;

    Rules rules = new Rules();

    public void newDuel(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;

//		team1 = new Team();
//		team2 = new Team();

        final Player[] players1 = createPlayers(team1, Side.LEFT);
        final Player[] players2 = createPlayers(team2, Side.RIGHT);

        team1.init(this, players1, players2);
        team2.init(this, players2, players1);
        team1.setSide(Side.LEFT);
        team2.setSide(Side.RIGHT);
        team1.setTeamColor(Color.RED);
//		team1.name = "RED Team";
//		team2.name = "GREEN Team";
        team2.setTeamColor(Color.GREEN);

        ball = new Ball(this, rules);
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

    private Player[] createPlayers(Team team, Side side) {
        Player[] players = new Player[rules.getNumPlayers()];

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1, team);
        }

        int sign = side.getSign();

        for (Player p1 : players) {
            p1.x = p1.dx = 320 + sign * 100;
            p1.y = p1.dy = 100 + p1.n * 40;
        }
        // goal keeper
        players[0].x = players[0].dx = 320 + sign * 300;
        players[0].y = players[0].dy = 240;

        return players;
    }

//	java.util.List<Player> players = new LinkedList();

    public int getScore(Side side) {
        switch (side) {
            case LEFT:
                return team1.getScore();
            case RIGHT:
                return team2.getScore();
            default:
                throw new IllegalArgumentException("" + side);
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


