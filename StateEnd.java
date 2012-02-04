/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author nitro
 */
public class StateEnd extends GameState{

    private BufferedImage bI;
    public StateEnd(Game game)
    {
        super(game);
        bI = game.getImgLdr().getImage("gameover");
    }
    @Override
    public void draw() {
        Graphics2D g = (Graphics2D) doublebuf.getDrawGraphics();
        g.drawImage(bI, 0, 0, null);
        doublebuf.show();
        g.dispose();
    }

    @Override
    public void update() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
