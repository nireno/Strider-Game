package game;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Niren Orie
 */

/*Frame provides a game screen with double buffering enabled.*/
public class Frame extends JFrame{

    private int FRAME_WIDTH = 800, FRAME_HEIGHT = 450; //Panel height and with
    private Game game;

   public Frame () {
       setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
//      setLocation(160, 120);
      setTitle ("PathFinder");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //setUndecorated(true);
      setVisible(true);
      //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
      createBufferStrategy(2);

      addKeyListener(new KeyAdapter()
      {
            @Override
          public void keyPressed(KeyEvent e)
          {
              game.processKeyPress(e);
          }

            @Override
          public void keyReleased(KeyEvent e)
          {
              game.processKeyRelease(e);
          }
      });
      
      game = new Game(this);
      game.start();
   }

    public int getFRAME_HEIGHT() {
        return FRAME_HEIGHT;
    }

    public int getFRAME_WIDTH() {
        return FRAME_WIDTH;
    }
	//Testing mercurial
}
