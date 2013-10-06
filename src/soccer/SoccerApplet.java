package soccer;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * @author kotucz
 */
public class SoccerApplet extends Applet implements MouseListener, KeyListener, Runnable {

    final Pitch pitch = new Pitch();
    final SoccerPainter painter = new SoccerPainter(pitch);

    static final String[] teamNames = loadTeamNames();

    //	static int playerVers = 0;
    static final int maxTick = 4000;

    static String[] loadTeamNames() {
        return new String[]{
                "soccer.Duck",
                "kotuc.UI_Kotuc",
                "kotuc.GTeam",
                "kotuc.EvolTeam2",
                "kotuc.AtomTeam",
                "petr.UI_Petr",
                "koste.UI_Koste",
                "kotuc.FinalTeam",
                "kotuc.VizTeam"
        };
    }

    int team1index = 5;

    int team2index = 7;

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init() {
        // TODO start asynchronous download of heavy resources

        this.addMouseListener(this);
        this.addKeyListener(this);

        setSize(Pitch.WIDTH, Pitch.HEIGHT);

        try {
            Pitch.kickClip = getAudioClip(getClass().getResource("/soccer/data/punch.wav"));
            Pitch.goalClip = getAudioClip(getClass().getResource("/soccer/data/ding.wav"));
//			melodyClip = getAudioClip(getClass().getResource("/soccer/data/melody.wav"));

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        newDuel(team1index, team2index);

    }

    // TODO overwrite start(), stop() and destroy() methods

    Thread thread;

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }

    }


    private BufferedImage imageBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    public void paint(Graphics g) {

        g.drawImage(imageBuffer, 0, 0, null);

    }


    public void flushGraphics() {

        BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        painter.paintField(bi.getGraphics());
        imageBuffer = bi;
        repaint();

    }

    public void run() {

        long lastflush = 0;

        int j = 0;
        while (true) {

            if (thread != Thread.currentThread()) {
                break;
            }

            if (painter.tick == maxTick) {
                break;
            }

            try {

                long t = System.currentTimeMillis();

                pitch.doTeamMovesWithRandom();

                painter.tick++;

//				System.out.println("benchmoves: "+bench());

//				if ((j%1)==0) {
                flushGraphics();
//				}

/*				if ((System.currentTimeMillis()-lastflush)>100)	{
                    flushGraphics();
					lastflush = System.currentTimeMillis();
				}
*/

                t = painter.delay - (System.currentTimeMillis() - t);

                if (t > 0)
                    try {
                        Thread.sleep(t);
                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

//				if ((j%10)==0) System.out.println("bench10paint: "+bench());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            j++;
        }
    }
//	}


    public void update(Graphics g) {
        paint(g);
    }


//	enum Action {
//	NEW_VERT,
//SELECT_VERT,
//NO
//}

//	public Action action = Action.NEW_VERT;

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }


    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        //   	pascal.TransForm.main(null);
        if (e.getButton() == MouseEvent.BUTTON3) {
            showUpSets();
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
//		System.out.println(""+e);
        if ('-' == e.getKeyChar()) {
            if (painter.delay > 1) {
                painter.delay /= 2;
            }
        }
        if ('+' == e.getKeyChar()) {
            painter.delay *= 2;
        }
    }


    public void keyTyped(KeyEvent e) {

    }


    SettingsFrame setsFrame;

    public void showUpSets() {
        if (setsFrame != null) {
            setsFrame.setVisible(false);
        }
        setsFrame = new SettingsFrame();
        setsFrame.setVisible(true);
    }

    class SettingsFrame extends JFrame {

        SettingsFrame() {
            reset();
        }

        Choice team1chooser;
        Choice team2chooser;
        Checkbox ballcheckbox;
        TextField numplayerstextfield;

        public void reset() {
            setLayout(new FlowLayout());

            team1chooser = new Choice();
            team2chooser = new Choice();

            for (String tn : teamNames) {
                team1chooser.add(tn);
                team2chooser.add(tn);
            }

            team1chooser.select(team1index);
            team2chooser.select(team2index);

            Button startButton = new Button("Start");

            startButton.addActionListener(new ActionListener() {

                /* (non-Javadoc)
                 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                 */
                public void actionPerformed(ActionEvent arg0) {
                    pitch.rules.ballType = ballcheckbox.getState();
                    pitch.rules.numPlayers = Integer.valueOf(numplayerstextfield.getText());
                    newDuel(team1chooser.getSelectedIndex(), team2chooser.getSelectedIndex());
                }

            });

            ballcheckbox = new Checkbox("pascal ball", pitch.rules.ballType);
            numplayerstextfield = new TextField("" + pitch.rules.numPlayers, 2);

            add(team1chooser);
            add(team2chooser);

            add(startButton);

            add(numplayerstextfield);

            add(ballcheckbox);

            setSize(200, 150);
        }
    }

    public void newDuel(int id1, int id2) {
        team1index = id1;
        team2index = id2;
        newDuel(teamNames[team1index], teamNames[team2index]);
    }

    public void newDuel(String team1classname, String team2classname) {
        try {
            Team team1 = (Team) Class.forName(team1classname).newInstance();
            Team team2 = (Team) Class.forName(team2classname).newInstance();
            pitch.newDuel(team1, team2);
            painter.newDuel();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
