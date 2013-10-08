package kotuc;

import soccer.P;
import soccer.Pitch;
import soccer.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class EvolTeam2 extends BaseKotuczTeam {

    QualityPoint[] targets = new QualityPoint[30];

    QualityPoint[] locs = new QualityPoint[30];

    boolean logsenabled = false;

    WConfig[] configs = new WConfig[5];

    public EvolTeam2() {
        for (int i = 0; i < configs.length; i++) {
            configs[i] = new WConfig();
        }
    }


    public void ui() {

        bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB | BufferedImage.OPAQUE);

        g = bIm.getGraphics();

        if (coolgraphics) {
            g.setColor(getTeamColor());
        }

        plays = getPlayers();
        opps = getOpponents();
        ball = getBall();


        updateConfig();


        areaDivision();


        P bestt = ballAiming();
        kickBall(bestt.x, bestt.y);


        pgo = nearestGo();


        Player pbr = goalkeeper();

        for (Player p1 : plays) {
            if ((p1 != pgo) && (p1 != pbr)) {
                position(p1);
            }
        }

        if (coolgraphics) {
            g.setColor(Color.GRAY);
            g.drawLine((int) ball.x, (int) ball.y, (int) bestt.x, (int) bestt.y);

            for (Player p1 : plays) {
                g.drawLine((int) p1.x, (int) p1.y, (int) p1.cil_x, (int) p1.cil_y);
            }
        }

        g.setColor(Color.CYAN);
        g.drawString(generation + "." + curconfig + ". cuid:" + config.cuid + " " + config.homescore + "/" + config.hostscore + " (" + (int) (config.getFragrate() * 100) + "%) t:-" + (updateInterval - (System.currentTimeMillis() - lastUpdate)) / 100, 410, 60);
    }

    /**
     * neural where to kick the ball
     */
    P ballAiming() {

        for (int i = 0; i < targets.length; i++) {
            if (targets[i] == null) {
                targets[i] = new QualityPoint(new P(Math.random() * Pitch.WIDTH, Math.random() * Pitch.HEIGHT));
            }
        }

        QualityPoint best = new QualityPoint();

        for (QualityPoint target : targets) {

            target.quality = evaluateBallTarget(target.point);

            if (target.quality > best.quality) {
                best = target;
            }
        }

        if (coolgraphics) {
            for (QualityPoint target : targets) {
                try {
                    g.setColor(new Color((int) (Math.min((target.quality / (14)), 1) * 255), 0, 0));
                    g.fillOval((int) target.point.x - 10, (int) target.point.y - 10, 20, 20);
                    g.drawString("x" + Math.round(target.quality * 100), (int) target.point.x, (int) target.point.y);
                } catch (Exception e) {
                    println(e.getMessage() + "colorred:" + (int) ((target.quality / (6 + 3 + 3)) * 255));
                }
            }
        }

        for (int i = 0; i < targets.length; i++) {
            QualityPoint target = targets[i];

            if (target.quality < config.get(W.toogood)) {
                if ((target.quality < config.get(W.toobad)) || (Math.random() < config.get(W.randomizing))) {
                    // remove this point from future considerations
                    targets[i] = null;
                }
            }
        }

        return best.point;
    }

    private double evaluateBallTarget(P t1) {
        double qual = 0;
        {

            for (Player p1 : plays) {
                double diff = angle(p1, ball, t1);
                if (coolgraphics) {
                    g.setColor(Color.GREEN);
                    g.drawLine((int) ball.x, (int) ball.y, (int) p1.x, (int) p1.y);
                }
//				anywhere where a friend
                qual += config.get(W.teamcoop) * (1 - Math.min(diff, 1));//*100/o1.distance(ball);
            }


            for (Player o1 : opps) {
                double diff = angle(o1, ball, t1);
                if (coolgraphics) {
                    g.setColor(Color.RED);
                    g.drawLine((int) ball.x, (int) ball.y, (int) o1.x, (int) o1.y);
                }
//				anywhere where no enemy
                qual += config.get(W.careness) * Math.min(diff, 1);//*100/o1.distance(ball);

            }


//			depending on the distance to goal
            qual += config.get(W.agresivity) * Math.pow(Math.max(0, 1 - new P((1 - getSideSign()) * Pitch.WIDTH / 2, Pitch.HEIGHT / 2).distance(t1) / 500), config.get(W.agresivityexp));
        }
        return qual;
    }

    /**
     * where players should go
     */
    P position(Player p1) {

        for (int i = 0; i < locs.length; i++) {
            //create new random
            if (locs[i] == null) {
                locs[i] = new QualityPoint(new P(Math.random() * Pitch.WIDTH, Math.random() * Pitch.HEIGHT));
            }

        }

        QualityPoint best = new QualityPoint();

        for (QualityPoint location : locs) {

            location.quality = evaluateLocation(p1, location.point);

            if (location.quality > best.quality) {
                best = location;
            }
        }

        if (coolgraphics) {
            for (QualityPoint location : locs) {
                try {
                    g.setColor(new Color(0, 0, (int) (Math.min(location.quality / (14), 1) * 255)));
                    g.fillOval((int) location.point.x - 10, (int) location.point.y - 10, 20, 20);
                    g.drawString("x" + Math.round(location.quality * 100), (int) location.point.x, (int) location.point.y);
                } catch (Exception e) {
                    println(e.getMessage() + "colorblue:" + (int) (Math.min(location.quality / (6 + 1 + 2 + 1), 1) * 255));
                }
            }
        }

        for (int i = 0; i < locs.length; i++) {
            QualityPoint location = locs[i];
            if (location.quality < config.get(W.toogoodloc))
                if ((location.quality < config.get(W.toobadloc)) || (Math.random() < config.get(W.randomizingloc)))
                    locs[i] = null;
        }

        p1.goTo(best.point);

        return best.point;

    }

    private double evaluateLocation(Player p1, P location) {
        double qual = 0;

//			g.setColor(Color.RED);
        for (Player o1 : opps) {
            double diff = angle(o1, location, ball);
//				g.drawLine((int)ball.x, (int)ball.y, (int)o1.x, (int)o1.y);
//				anywhere where closer to ball than enemy
            qual += config.get(W.blocking) * diff / Math.PI;//*100/o1.distance(ball);

        }


        for (Player p2 : plays) {
            if (p2 == p1) continue;
            qual += config.get(W.repelency) * Math.pow(Math.min(p2.distance(location) / 500, 1), config.get(W.repelencyexp));
        }

        qual += config.get(W.locstability) * Math.max(0, 500 - p1.distance(location)) / 500;

//			depending on the distance to goal
        qual += config.get(W.agresivityloc) * Math.max(0, (1 - new P((1 - getSideSign()) * Pitch.WIDTH / 2, Pitch.HEIGHT / 2).distance(location) / 500));
        return qual;
    }


    BufferedImage bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB | BufferedImage.OPAQUE);

    public void paint(Graphics g) {

        if (coolgraphics) g.drawImage(bIm, 0, 0, null);

        super.paint(g);

        bIm = new BufferedImage(Pitch.WIDTH, Pitch.HEIGHT, BufferedImage.TYPE_INT_ARGB | BufferedImage.OPAQUE);
    }


    long updateInterval = 60 * 1000; // one minute

    long lastUpdate;

    int curconfig = -1;

    int lasthost, lasthome;

    int generation = 0;


    File logf;
    PrintWriter logpw;


    void updateConfig() {

        config.homescore = getPitch().getScore(getSideEnum()) - lasthome;
        config.hostscore = getPitch().getScore(getSideEnum().opponent()) - lasthost;

        if ((System.currentTimeMillis() - lastUpdate) < updateInterval) return;

        if (logsenabled) {
            if (logf == null) {
                try {
                    logf = new File("C:/Documents and Settings/PC/Dokumenty/Java Projects/soccer/logs/11032007/gen" + generation + ".log");
                    logpw = new PrintWriter(logf);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (config == null) config = configs[0];

        curconfig++;

        if (curconfig >= configs.length) {

//			do selections:

            println("all configs tested");

            for (int i = 0; i < configs.length; i++) {
                for (int j = i + 1; j < configs.length; j++) {
                    if (configs[i].getFragrate() < configs[j].getFragrate()) {
                        WConfig wc1 = configs[i];
                        configs[i] = configs[j];
                        configs[j] = wc1;
                    }
                }
            }


            try {
                if (logsenabled) {
                    logpw.println("" + new Date(System.currentTimeMillis()) + " generation " + generation);
                }

                for (int i = 0; i < configs.length; i++) {
                    println("" + i + ": " + configs[i].cuid + " " + configs[i].homescore + "/" + configs[i].hostscore + " (" + configs[i].getFragrate() + ")");
                    println(configs[i].toString());
                    if (logsenabled) {
                        logpw.println("" + i + ": " + configs[i].cuid + " " + configs[i].homescore + "/" + configs[i].hostscore + " (" + configs[i].getFragrate() + ")");
                        logpw.println(configs[i].toString());
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            generation++;

            if ((generation % 5) == 0) {
                logpw.close();
                logpw = null;
                logf = null;

            }

            double changerate = 0.1;

            // now rewarding winners
            configs[4].mutate(changerate);

            configs[3] = (WConfig) configs[2].clone();
            configs[3].mutate(changerate);

            configs[2].mutate(changerate);

            configs[1] = (WConfig) configs[1].clone();
            configs[1].mutate(changerate);

            configs[0] = configs[0];


            println("starting with new configs set");

            curconfig = 0;
        }


        config = configs[curconfig];

        println("SWITCHED TO : " + config.toString());

        lasthome = getPitch().getScore(getSideEnum());
        lasthost = getPitch().getScore(getSideEnum().opponent());

        lastUpdate = System.currentTimeMillis();
    }


    void areaDivision() {
        /**
         * 	area divVision
         */


        P pxy = new P();
        for (pxy.x = 30; pxy.x < 640; pxy.x += 30) {
            for (pxy.y = 30; pxy.y < 480; pxy.y += 30) {
                Player bp = null;
                double shor = 1000;
                for (Player p1 : plays) {
                    if (p1.distance(pxy) < shor) {
                        bp = p1;
                        shor = p1.distance(pxy);
                        if (coolgraphics) g.setColor(getTeamColor());
                    }
                }
                for (Player p2 : opps) {
                    if (p2.distance(pxy) < shor) {
                        bp = null;
                        break;
//						bp = p2;
//						shor = p2.distance(pxy);
//						g.setColor(Color.blue);
                    }
                }

                if (coolgraphics) if (bp != null) g.drawString("" + bp.n, (int) pxy.x - 5, (int) pxy.y - 5);
            }
        }
    }


}
