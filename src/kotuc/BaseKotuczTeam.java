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
}
