package soccer;

/**
 * @author Kotuc
 */
public class Rules {

    int numPlayers = 5;

    boolean ballType = true;

    static final int BALL_MAX_SPEED = 10;

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getBallMaxSpeed() {
        return BALL_MAX_SPEED;
    }

    public boolean isStickyBallType() {
        return ballType;
    }
}
