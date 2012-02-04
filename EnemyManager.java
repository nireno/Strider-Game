/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Nitro
 */
public class EnemyManager {

	ArrayList<Enemy> enemies;
	int numActive;

	public EnemyManager()
	{
		this.enemies = new ArrayList<Enemy>();
	}

	public void add(Enemy enemy)
	{
		if(enemy != null)
			enemies.add(enemy);
	}

	public void draw(Graphics2D g2)
	{
		for(int i = 0; i < numActive; i++)
		{
			enemies.get(i).draw(g2);
		}
	}

	public void drawDebug(Graphics2D g2)
	{
		this.draw(g2);
		for(int i = 0; i < numActive; i++)
		{
			g2.draw(enemies.get(i).getColBox());
		}
	}
	public void update()
	{
		numActive = 0;
		for(int i = 0; i < enemies.size(); i++)
		{
			
			Enemy currEnemy = enemies.get(i);
			currEnemy.update();
			//remove dead enemies
			if(currEnemy.getIsDead())
			{
				enemies.remove(i);
				i--; // Since remove will shift all elements down by one.
				break;
			}

			if(currEnemy.getSprite().isActive())
			{
				
				//swap with the first inactive enemy
				Enemy temp = enemies.get(numActive);
				enemies.set(numActive, currEnemy);
				enemies.set(i, temp);
				numActive++;
			}
		}
	} // End Update

	public void playerDamageEnemy(Player player)
	{
		Rectangle atkRect =  player.getAtkRect();
		int damage = player.getAttackVal();
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).getColBox().intersects(atkRect) && !enemies.get(i).getState().matches("dying"))
			{
				enemies.get(i).takeDamage(damage, player);
			}
		}
	}

	public int getNumActive()
	{
		return numActive;
	}

	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}

	
	
}
