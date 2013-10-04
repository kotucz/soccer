/*
 * Main.java
 *
 * Created on 1. b�ezen 2007, 21:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package soccer;

/**
 * @author PC
 */
public class Main {

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("soccer");
        SoccerApplet soccerApplet = new SoccerApplet();
        soccerApplet.init();
        frame.add(soccerApplet);
        frame.setSize(640, 480);
        frame.setVisible(true);
        soccerApplet.start();
    }

}
