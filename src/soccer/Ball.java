package soccer;

import java.awt.*;

/**
 * @author kotucz
 */
public class Ball extends P {

    public final int MAX_SPEED;

    protected V v = new V();

    protected V vhalf = new V();

    public double vx, vy, v_x, v_y;

    Ball original;

    private Pitch pitch;
    private Rules rules;

    public Ball() {
        // TODO pitch not initialized so hope it will be not used - only as data object
        MAX_SPEED = 10;
    }

    /**
     * Creates a new instance of Ball
     */
    public Ball(Pitch pitch, Rules rules) {
        this.pitch = pitch;
        this.rules = rules;
        reset();
        MAX_SPEED = rules.getBallMaxSpeed();
    }

    void reset() {
        this.x = Pitch.WIDTH / 2;
        this.y = Pitch.HEIGHT / 2;
    }

    void kick(double vx, double vy) {
        kick(vx, vy, MAX_SPEED);
    }

    void kick(double vx, double vy, double speed) {

        this.v.x = vx;
        this.v.y = vy;

        double vl = v.length();

        speed = Math.min(speed, MAX_SPEED);

        if (vl > speed) {
            if (isOriginal()) {
                Pitch.playSound(Pitch.kickClip);
            }
            v.scale(speed / (vl));
        }

        this.vx = this.v_x = v.x;
        this.vy = this.v_y = v.y;

        vhalf = new V(v);
        vhalf.scale(0.5);

    }

    protected void paint(Graphics g) {

        int r = 3;

        g.setColor(Color.WHITE);
        g.fillOval((int) x - r, (int) y - r, 2 * r, 2 * r);

    }

    void doHalfMove() {
        if (rules.isStickyBallType()) {
            stickyHalfMove();
        } else {
            javaHalfMove();
        }
    }

    /**
     * Emulation of Java ball rules. Ball will bounce when reach field boundary.
     */
    private void javaHalfMove() {
        add(vhalf);

        if ((x < 0) || (x > Pitch.WIDTH)) {
            if ((y > 160) && (y < 320)) {

                if (isOriginal()) {

                    System.out.println("GOOOOL");
                    if (x > 320) {
                        pitch.team1.score++;
                    } else {
                        pitch.team2.score++;
                    }

                    Pitch.playSound(Pitch.goalClip);
                }

            }


            kick(-vx, vy);

//				v.x*=-1;
//vhalf.x*=-1;

        }

        if ((y < 0) || (y > Pitch.HEIGHT)) {
            kick(vx, -vy);
//				v.y*=-1;
//				vhalf.y*=-1;
        }
    }

    /**
     * Emulation of Pascal ball rules. Ball will stick when reach field boundary.
     */
    private void stickyHalfMove() {
        add(vhalf);

        if ((y < Pitch.TOPY) || (y > Pitch.BOTTOMY)) {
            kick(0, 0);
        }

        if ((x < Pitch.LEFTX) || (x > Pitch.RIGHTX)) {
            if ((y > 160) && (y < 320)) {

                if (isOriginal()) {
                    System.out.println("GOOOOL");
                    if (x > Pitch.CENTX) {
                        pitch.team1.score++;
                    } else {
                        pitch.team2.score++;
                    }
                    Pitch.playSound(Pitch.goalClip);
                }

                reset();

            }

            kick(0, 0);

        }

        x = Math.min(Math.max(Pitch.LEFTX, x), Pitch.RIGHTX);
        y = Math.min(Math.max(Pitch.TOPY, y), Pitch.BOTTOMY);
    }


    public void doVMove() {
        if (isOriginal()) {
            System.err.println("Illegal use doVMove on original Ball");
            return;
        }
        doHalfMove();
        doHalfMove();
    }

    /**
     * @return the velocity
     */
    public V getV() {
        return new V(v);
    }

    /**
     * @return the position
     */
    public P getP() {
        return new P(x, y);
    }

    boolean isOriginal() {
        return (original == this);
    }

    @Override
    public Object clone() {
        Ball b = new Ball(pitch, rules);
        b.x = x;
        b.y = y;
        b.vx = b.v_x = v.x;
        b.vy = b.v_y = v.y;
        b.v = new V(v);
        b.vhalf = new V(vhalf);
        return b;
    }

}
