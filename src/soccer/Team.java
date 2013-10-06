package soccer;

import java.awt.*;

/**
 * @author kotucz
 */
public abstract class Team {

    private Player[] players;

    private Player[] opponents;

    private String name;

    private Color teamColor = Color.CYAN;

    protected int side = 0;

    int score;

    private Pitch pitch;

    private Rules rules;

    void init(Pitch pitch, Player[] players, Player[] opponents) {
        this.pitch = pitch;
        this.players = players;
        this.opponents = opponents;
        setRules(pitch.rules);
        name = getClass().getName();

    }

    void setSide(int side) {
        this.side = side;
    }

    /** Creates a new instance of Team */
 /*   public Team(int s) {
        
    	this();
    	
    	this.side = s;
        if (side>0) teamColor = Color.RED;
        else teamColor = Color.GREEN;
        
        
    }*/
    
/*    public void run() {
        
        println(this +": redy");
        
        try {
        
                Thread.sleep(5000);
        } catch (InterruptedException ex) {
               ex.printStackTrace();
        }

        
        while (true) {
            
                        
            
                ui0();
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    */

    /**
     * @return the players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the side
     */
    public int getSide() {
        return side;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    protected void doMoves() {


        for (Player player : players) {
            player.doMove();
        }
//       for (Player p1:plays) {
///           System.out.println("1");
        //          p1.doMove();
//       }
    }

    public Ball getBall() {
//        return (Ball)pitch.getBall().clone();
        return pitch.ball;
    }

    final void ui0() {
        try {
            ui();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void ui();

    public void kickBall(double bx, double by) {
        for (Player p1 : players) {
            if (p1.distance(pitch.getBall()) < Player.RANGE) pitch.getBall().kick(bx - getBall().x, by - getBall().y);
        }
    }

    protected void paint(Graphics g) {

        for (Player p1 : players) {
            p1.paint(g);
        }

    }

    public void println(String text) {
        System.out.println(name + ": " + text);
    }

    /**
     * @param teamColor the teamColor to set
     */
    public void setTeamColor(Color teamColor) {
        this.teamColor = teamColor;
    }

    public void setTeamColor(int teamColor) {
        try {
            this.teamColor = new Color(teamColor);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the opponents
     */
    public Player[] getOpponents() {
        return opponents;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    public Pitch getPitch() {
        return pitch;
    }

    public String getName() {
        return name;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
}
