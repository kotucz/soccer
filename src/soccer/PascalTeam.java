package soccer;

import java.util.Random;

/**
 * Team wrapper to be compatible with in pascal programed AIs.
 */
public abstract class PascalTeam extends Team {

    public static final Random RANDOM = new Random();
    public int MAX_HRACU;// = Pitch.numPlayers;

    public static final int V_MICE = 10;
    public static final int V_HRACE = 5;

    @Override
    public void setRules(Rules rules) {
        super.setRules(rules);
        MAX_HRACU = rules.getNumPlayers();
    }

    public final void ui() {
//		does nothing, since pascal is using	public abstract void ui(Player[] plays, Player[] opps, Ball ball, int strana) ;
//		calls pascal ui
        ui(getPascalPlayers(), getPascalOpponents(), getBall(), (getSide() > 0) ? 1 : 0);
    }

    public abstract void ui(Player[] plays, Player[] opps, Ball ball, int strana);

    public final double vzdalenost(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

    public final int random(int i) {
        return RANDOM.nextInt(i);
    }

    public final int round(double i) {
        return (int) Math.round(i);
    }

    public final void jdi_na(Player p, double x, double y) {
        if (p != null) p.goTo(x, y);
    }

    public final void kopni_mic(Player p, Ball b, double x, double y, double v) {
//		kickBall(x, y);
        if (p.distance(b) < Player.RANGE) b.kick(x - b.x, y - b.y, v);
    }

    /**
     * returns players field converted to pascal array system [1..MAX_HRACU]
     * the [0] field is null
     * <p/>
     * !not null since null pointer exception is thrown
     */
    private final Player[] getPascalPlayers() {
        Player[] pls = super.getPlayers();
        Player[] pps = new Player[pls.length + 1];
        pps[0] = new Player();
        System.arraycopy(pls, 0, pps, 1, pls.length);
        return pps;
    }

    /**
     * returns players field converted to pascal array system [1..MAX_HRACU]
     * the [0] field is null
     * <p/>
     * !not null since null pointer exception is thrown
     */
    private final Player[] getPascalOpponents() {
        Player[] ops = super.getOpponents();
        Player[] pops = new Player[ops.length + 1];
        pops[0] = new Player();
        for (int i = 0; i < ops.length; i++) {
            pops[i + 1] = ops[i];
        }
//		System.out.println("opps:"+pops);
        return pops;
    }

}
