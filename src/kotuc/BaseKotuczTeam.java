package kotuc;

import soccer.*;

import java.awt.*;

/**
 * @author Kotuc
 */
public abstract class BaseKotuczTeam extends Team {
    Graphics g;
    Player[] plays;
    Player[] opps;
    Ball ball;
    Player pgo;
    boolean coolgraphics = false;
    WConfig config = new WConfig();

    public BaseKotuczTeam() {
        coolgraphics = false;
    }

    public double angle(P a, P v, P b) {
        V v1 = new V();
        V v2 = new V();
        v1.sub(a, v);
        v2.sub(b, v);
        return v1.angle(v2);//Math.acos((v1.));
    }

    Player goalkeeper() {
        /**
         * 	nearest player to goal is goalkeeper go to goal
         *
         */

        Player pbr = null;

        P gomin = new P(320 + getSideSign() * 300, 240);

        double mindist = 1000;

        for (Player p1 : plays) {
            if (p1.distance(gomin) < mindist) {
                if (p1 != pgo) {
                    pbr = p1;
                    mindist = p1.distance(gomin);
                }
            }
        }

        if (pbr != null) pbr.goTo(gomin);

        return pbr;

    }

    /**
     * time nearest player go after ball in direct way
     * <p/>
     * using virtual ball simulation
     */
    Player nearestGo() {

        Player pgo = null;

        if (coolgraphics) {
            g.setColor(Color.GREEN);
        }

        Ball vball = (Ball) ball.clone();

        end1:
        for (int t = 0; t < 500; t++) {
            vball.doVMove();

            if (coolgraphics) g.drawLine((int) vball.x, (int) vball.y, (int) vball.x, (int) vball.y);

            for (Player p1 : plays) {
                if (p1.distance(vball) < (Player.MAX_SPEED * t)) {

                    pgo = p1;
                    pgo.goTo(vball);

                    break end1;
                }
            }

        }

        if (coolgraphics) {
            if (pgo != null) {
                g.drawLine((int) pgo.x, (int) pgo.y, (int) vball.x, (int) vball.y);
            }
        }

        return pgo;


    }

    static class QualityPoint {
        P point;
        double quality;

        public QualityPoint() {
        }

        public QualityPoint(P point) {
            this.point = point;
        }

    }

}
