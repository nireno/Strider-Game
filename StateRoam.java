/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;

/**
 *
 * @author Nitro
 */
public class StateRoam extends GameState{
    private EnemyManager enemyMgr;
	private RibbonsManager ribsMan;
    private BricksManager bricksMan;
    private Player strider;
    //private TestEnemy testE;
    private boolean bDrawColBox;
	private HUD hud;
    private Font HUDFont;//TODO: remove these when the HUD class is completed
	private FontMetrics fMetric;
	int movesize = 5; //TODO: check if we can remove this.
        private AudioManager audioMgr;

	public StateRoam(Game game) {
        super(game);
        audioMgr = game.getAudioManager();
        audioMgr.Insert(new File("forest.wav"), "forest.wav");
        bIsEntering = true;
        ribsMan = game.getRibsMan();
        bricksMan = game.getBricksMan();
        strider = game.getPlayer();
		hud = game.getHUD();
		enemyMgr = game.getEnemyMgr();
//        testE = (TestEnemy)game.getEnemy();
        bDrawColBox = false;
		HUDFont = new Font("Arial", Font.BOLD, 14);
    }

    @Override
    public void draw() {
        Graphics2D g2 = (Graphics2D)doublebuf.getDrawGraphics();
        ribsMan.display(g2);
        bricksMan.display(g2);
//        testE.draw(g2);
		enemyMgr.draw(g2);
        strider.draw(g2);
		hud.draw(g2);
        g2.setColor(Color.white);
        if(bDrawColBox){
//            g2.draw(testE.getColBox());//for debug
			enemyMgr.drawDebug(g2);
            g2.draw(strider.getColBox());//for debug
            if(strider.getAtkRect()!=null)
            g2.draw(strider.getAtkRect());
        }
        
//        g2.setFont(HUDFont);
//		g2.getFontMetrics(HUDFont);
//        g2.drawString("HP: "+strider.getHP(), 15, 40);

        doublebuf.show();
        g2.dispose();
    }

    @Override
    public void update() {
       handleInput();
       if(bIsEntering)
       {
           audioMgr.playAudio("forest.wav", true);
           bIsEntering = false;
       }
       ribsMan.update();
       bricksMan.update();
       strider.update();
//       testE.update();
	   enemyMgr.update();
    }

    private void handleInput()
    {
        if(inputMan.isKeyDown(KeyEvent.VK_A))
        {
            strider.moveLeft();
        }
        else if(inputMan.isKeyDown(KeyEvent.VK_D))
        {
            strider.moveRight();
        }
        if(inputMan.isInitPressed(KeyEvent.VK_B)){
                bDrawColBox=!bDrawColBox;
        }
        if(inputMan.isInitPressed(KeyEvent.VK_W))
        {
            strider.jump();
        }
        if(inputMan.isInitPressed(KeyEvent.VK_SPACE)){
            strider.attack();
        }
		if(inputMan.isInitPressed(KeyEvent.VK_G))
		{
			if(enemiesInRange())
				game.setCurrentState("geotime");
		}
        if(inputMan.isInitPressed(KeyEvent.VK_M))
        {
            strider.setMaxXVel(8);
            strider.setXAccel(1.0f);
            strider.setAttackVal(400);
        }
		if(inputMan.isKeyDown(KeyEvent.VK_S))
		{
			game.setUPDATE_SPEED(game.getUPDATE_SPEED()+10);
		}
		else game.setUPDATE_SPEED(25);
    }

	private boolean enemiesInRange()
	{
		return (enemyMgr.getNumActive() > 0);
	}
}
