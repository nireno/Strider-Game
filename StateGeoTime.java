/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import javax.swing.GrayFilter;

/**
 *
 * @author Nitro
 */
public class StateGeoTime extends GameState
{
	private QuestionManager questionMgr;
	private RibbonsManager ribsMgr;
	private BricksManager bricksMgr;
	private Player strider;
	private EnemyManager enemyMgr;
//	private TestEnemy testE;
	private Enemy target;
	private int scale = 50; //Percentage of color removed from non-Actors
	BufferedImage bg; // Image to store the background for applying effects.
	Graphics2D bgGraphics;

	

	public StateGeoTime(Game game)
	{
		super(game);
		ribsMgr = game.getRibsMan();
		bricksMgr = game.getBricksMan();
		enemyMgr = game.getEnemyMgr();
		strider = game.getPlayer();
//		testE = (TestEnemy) game.getEnemy();
		questionMgr = new QuestionManager(strider, game);
		bIsEntering = true;
		bIsLeaving = false;
		bg = new BufferedImage(game.getWidth(),
							   game.getHeight(),
							   BufferedImage.TYPE_BYTE_GRAY);

		bgGraphics = bg.createGraphics();
		
	}

	@Override
	public void draw()
	{
		Graphics2D g = (Graphics2D) doublebuf.getDrawGraphics();
		ribsMgr.display(bgGraphics);
		bricksMgr.display(bgGraphics);
		g.drawImage(bg, 0, 0, null);
		strider.draw(g);
//		testE.draw(g);
		target.draw(g);
		questionMgr.draw(g);
		doublebuf.show();
		g.dispose();
	}

	@Override
	public void update()
	{
		if(bIsEntering)
		{
			target = getNearestEnemy();
			game.getClipsLoader().play("geotime",false);
			generateQuestion();
			bIsEntering = false;
		}
		
		questionMgr.update();
		handleInput();

		if(bIsLeaving)
		{
			//reset the state
			bIsEntering = true;
			bIsLeaving = false;
		}
//		questionMan.update();
//		if (bIsEntering || bIsLeaving)
//		{
//			ribsMan.update();
//			bricksMan.update();
//			strider.update();
//			testE.update();
//		}
		
	}

	// This method takes care of selecting an enemy and generating a question
	public void generateQuestion()
	{
		//we only have one enemy
		questionMgr.generate(target);
	}

	private Enemy getNearestEnemy()
	{
		Enemy nearest = enemyMgr.getEnemies().get(0); // Get first enemy in list
		float minDistance = getDistance(strider, nearest);
		for(int i = 1; i < enemyMgr.getNumActive(); i++)
		{
			Enemy currEnemy = enemyMgr.getEnemies().get(i);
			float currDistance = getDistance(strider, currEnemy);
			if(currDistance < minDistance)
			{
				minDistance = currDistance;
				nearest = currEnemy;
			}
		}
		return nearest;
	}

	private void handleInput()
	{
//		if(inputMan.isInitPressed(KeyEvent.VK_G))
//		{
//			//bIsLeaving = true;
//			game.setCurrentState("roam");
//		}
		if(inputMan.isInitPressed(KeyEvent.VK_S))
		{
			if(!(questionMgr.getSelected() == questionMgr.getNumChoices()-1))
			{
				questionMgr.setSelected(questionMgr.getSelected()+1);
			}
		}
		if(inputMan.isInitPressed(KeyEvent.VK_W))
		{
			if(!(questionMgr.getSelected() == 0))
			{
				questionMgr.setSelected(questionMgr.getSelected()-1);
			}
		}
		if(inputMan.isInitPressed(KeyEvent.VK_SPACE))
		{
			questionMgr.evaluate();
			bIsLeaving = true;
			game.setCurrentState("roam");
		}
	}
	private float getDistance(Actor a1, Actor a2)
	{
		return (float) Point.distance(a1.xPos, a1.yPos, a2.xPos, a2.yPos);
	}
}
