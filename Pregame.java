/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ImageIcon;

/**
 *
 * @author Nitro
 */
public class Pregame extends GameState{
    private Image menuImage;
    private ImagesLoader imgloader;
    private AudioManager AM;
    private boolean enter_beginning;
    private subState intro;
    private subState menu;
    private subState currState;

    public Pregame(Game game) {
        super(game);
        this.menuImage = game.getImgloader().getImage("menu");
        this.AM = game.getAudioManager();
        this.AM.Insert(new File("intro_Polymorphic.wav"), "intro_Polymorphic.wav");
        this.AM.Insert(new File("intro_strider.wav"), "intro_strider.wav");
        this.intro = new introState(game, this);
        this.menu = new menuState(game, this);
        this.currState = menu;
    }

    @Override
    public void draw() {
       this.currState.draw();
       /* Graphics2D g2 = (Graphics2D)doublebuf.getDrawGraphics();
        g2.drawImage(menuImage, null, 0, 0);
        doublebuf.show();
        g2.dispose();*/

    }

    @Override
    public void update() {

        this.currState.update();
        
    }

    private void handleInput() {
        this.currState.handleInput();
    /*    if(inputMan.isKeyDown(KeyEvent.VK_ENTER)){
            this.AM.stopAudio("intro_Strider.wav");
            game.setCurrentState("roam");
        }*/
    }


abstract class subState{
   protected Game game;
   protected Pregame pg;
   protected AudioManager AM;
   protected boolean finished;
   protected BufferStrategy DoubleBuffer;
   protected boolean runAlready;
   protected Audio runningClip;

   protected Graphics2D screen;
   protected Collection drawThings;

    public subState(Game g, Pregame pg){
        this.pg = pg;
        this.game = g;
        this.AM = g.getAudioManager();
        this.finished = false;
        this.runAlready = false;
        this.DoubleBuffer = g.getDoublebuf();
        this.drawThings = new ArrayList();

    }
   protected abstract void update();
   protected abstract void draw();
   protected abstract void handleInput();
}

class introState extends subState{
 private Rectangle2D bgwhite = new Rectangle2D.Double(0,0,game.getWidth(), game.getHeight());
 private Image polymorphic;
 private Image chronerio;
 private Image presentation;

  public introState(Game g, Pregame pg){
        super(g,pg);
        this.polymorphic = new ImageIcon("src/game/images/poly.png").getImage();
        this.chronerio = new ImageIcon("src/game/images/chronerio.png").getImage();
        this.presentation = new ImageIcon("src/game/images/achro.jpg").getImage();
    }
   protected void update(){
       System.out.println("In introState\n");
     if(this.runningClip != null){
      if(this.runningClip.getAudioStatus() == true){ //getAudioStatus returns true if clip is done
          this.pg.currState = this.pg.menu;
          return;
      }
     }
      if(this.runAlready != true){
         this.runningClip = AM.playAudio("intro_Polymorphic.wav", false);
          this.runAlready = true;
      }

   }
   protected void draw(){
       this.screen = (Graphics2D)this.DoubleBuffer.getDrawGraphics();
       this.screen.setColor(Color.white);
       this.screen.fill(bgwhite);
       this.screen.draw(bgwhite);
       this.screen.drawImage(polymorphic,20,20,200,200, null);
       this.screen.drawImage(chronerio,(game.getWidth() - 270),(game.getHeight() - 270), 330, 270, null);
       this.screen.drawImage(presentation,(game.getWidth()/2 - 150),(game.getHeight()/2 - 150), 300, 300, null);
       this.DoubleBuffer.show();
       this.screen.dispose();

   }
   protected void handleInput(){
       
   }

}

class menuState extends subState{
       protected Image menuImage;
       private Image striderImage;
       private Image info;
       private Image info2;


    public menuState(Game g, Pregame pg){
        super(g,pg);
       this.menuImage = new ImageIcon("src/game/images/mnu.jpg").getImage();
       this.striderImage = new ImageIcon("src/game/images/strider.png").getImage();
       this.info = new ImageIcon("src/game/images/text.png").getImage();
       this.info2 = new ImageIcon("src/game/images/text2.png").getImage();


    }
    protected void update(){
      if(this.runAlready != true){
         this.runningClip = AM.playAudio("intro_strider.wav", true);
          this.runAlready = true;
      }
        System.out.println("IN menuState\n");
        this.handleInput();
    }
    @Override
    protected void draw(){
        this.screen = (Graphics2D)this.DoubleBuffer.getDrawGraphics();
        this.screen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.screen.drawImage(menuImage,0,0,game.getWidth(),game.getHeight(),null);
        this.screen.drawImage(striderImage,(int)(game.getWidth()/2 - striderImage.getWidth(null)/2),70,striderImage.getWidth(null),striderImage.getHeight(null), null);
        this.screen.drawImage(info,200,330, info.getWidth(null), info.getHeight(null), null);
        this.screen.drawImage(info2, 115, 0, info2.getWidth(null), info.getHeight(null), null);
        this.DoubleBuffer.show();
        this.screen.dispose();

    }
    protected void handleInput(){
        if(inputMan.isKeyDown(KeyEvent.VK_ENTER)){
  
          this.AM.stopAudio("intro_strider.wav");

            game.setCurrentState("roam");
    
        }
    }

}
}

