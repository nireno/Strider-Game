/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author Who
 */
public class QuestionManager
{

	Random rand = new Random();
	Player player;
	Game game;
	Enemy enemy; //the enemy that is generating the question
	int auraWidth, auraHeight; /* The width and height of the enemy's "aura".
	The question is posed using these values. */

	int level = 0; /* A multiple of the difficulty that is added to the width and
	height of the generated question. Thus, higher difficulty enemies have 
	larger dimensions. */

	int enemySpriteX, enemySpriteY;
	int enemyWidth, enemyHeight;
	int playerSpriteX, playerSpriteY;
	int playerWidth, playerHeight;
	int numChoices = 3;
	int selected = 0; //Currently selected choice.
	int[] choices = new int[numChoices]; /* Stores 3 possible answers to the question. */

	int answer;
	Font QuestionFont = new Font("Arial", Font.BOLD, 14);
	FontMetrics metrics;

	public QuestionManager(Player player, Game game)
	{
		this.player = player;
		this.game = game;
		playerSpriteX = player.getSprite().getXPosn();
		playerSpriteY = player.getSprite().getYPosn();
		playerWidth = player.getSprite().getWidth();
		playerHeight = player.getSprite().getHeight();


		for (int i = 0; i < numChoices; i++)
		{
			choices[i] = 0;
		}
	}

	/* Generate a question for a given enemy. */
	public void generate(Enemy enemy)
	{
		this.enemy = enemy;

		if (enemy != null)
		{
			enemySpriteX = enemy.getSprite().getXPosn();
			enemySpriteY = enemy.getSprite().getYPosn();
			enemyWidth = enemy.getSprite().getWidth();
			enemyHeight = enemy.getSprite().getHeight();
			level = enemy.getDifficulty() * 5; /* Five times the difficulty will
			be added on to the generated dimensions */

			/* Set the width and heigth to some number between 1 and 10 (inclusive)
			 * then add the level to reflect the difficulty of the enemy in the
			 * question.
			 */
			auraWidth = 1 + rand.nextInt(10) + level;
			auraHeight = 1 + rand.nextInt(10) + level;

			answer = auraWidth * auraHeight; //store the correct answer

			int lowerLimit = 1 + level; //lowest possible dimension
			int upperLimit = 10 + level; //highest possible dimension

			/*
			 * The lowest possible answer in this difficulty is lowerLimit^2
			 * and the highest is upperLimit^2 so generate choices in this range.
			 */

			lowerLimit *= lowerLimit; //lowerLimit^2
			upperLimit *= upperLimit; //upperLimit^2

			for (int i = 0; i < numChoices; i++)
			{
				choices[i] = 1 + rand.nextInt(upperLimit);
				if (choices[i] < lowerLimit)
				{
					choices[i] = lowerLimit;
				}
				for (int j = 0; j < i; j++)
				{
					if (choices[j] == choices[i] || choices[j] == answer)
					{
						i--; //decrement i so that we repeat the process at the same index.
						break;
					}

				}
			}

			//Place the correct answer in a random position in the array.
			choices[rand.nextInt(numChoices)] = answer;
		}
	}

	public void draw(Graphics2D g2)
	{
		if (enemy != null)
		{
			metrics = g2.getFontMetrics(QuestionFont);
			int choiceBoxSize = metrics.stringWidth("999");
			String choiceStr;
			String instr = "Calculate the area of enemy's aura,select the correct answer and press spacebar to weaken enemy";
			int x, y;
			x = playerSpriteX + playerWidth;
			y = playerSpriteY;
			// (x,y) here, gives the top right coordinate of the player sprite.
			g2.setFont(QuestionFont);
			Rectangle2D stringRect;
			Rectangle choiceRect;
			//draw geotime instructions
			g2.setColor(new Color(0,148,192,200));
			g2.fillRect(0,0,game.getWidth(),50);
			g2.setColor(Color.white);
			g2.drawString(instr, (game.getWidth()-metrics.stringWidth(instr))/2, 45);
			
			for (int i = 0; i < numChoices; i++)
			{
				choiceStr = Integer.toString(choices[i]);
				choiceRect = new Rectangle(x, y + (i * choiceBoxSize), choiceBoxSize, choiceBoxSize);
				stringRect = metrics.getStringBounds(choiceStr, g2);
				stringRect.setRect(x, y - metrics.getAscent(), stringRect.getWidth(), stringRect.getHeight());
				if (selected == i)
				{
					g2.setPaint(new GradientPaint(0, (int) choiceRect.getY(), new Color(0, 20, 40), 0, (int) choiceRect.getMaxY(), new Color(0, 128, 255)));
				} else
				{
					g2.setPaint(new GradientPaint(0, (int) choiceRect.getY(), new Color(0, 128, 255), 0, (int) choiceRect.getMaxY(), new Color(0, 20, 40)));
				}
				g2.fill(choiceRect);
				g2.setColor(Color.white);
				g2.draw(choiceRect);

				if (i == selected)
				{
					g2.setColor(Color.green);
				} else
				{
					g2.setColor(Color.white);
				}
				//draw chioce string in the middle of the choice box
				g2.drawString(choiceStr, x + ((choiceBoxSize - metrics.stringWidth(choiceStr)) / 2), (int) choiceRect.getMaxY() - ((choiceBoxSize - metrics.getAscent()) / 2));
			}
			Rectangle geoRect = enemy.getSprite().getMyRectangle();
			g2.setColor(new Color(0,128,200,90));
			g2.fill(geoRect);
			g2.setColor(Color.red);
			g2.draw(geoRect);
			
			//TODO: clean this up
			stringRect = metrics.getStringBounds("99", g2);

			x = (int) (enemy.getSprite().getMyRectangle().getCenterX() - stringRect.getCenterX());
			y = enemySpriteY-5;

			stringRect.setRect(x, y - metrics.getAscent(), stringRect.getWidth(), stringRect.getHeight());
			stringRect = grow(stringRect, 3.0);

			g2.setColor(Color.red);
//			g2.fillArc((int) stringRect.getX(), (int) stringRect.getY(), (int) stringRect.getWidth(), (int) stringRect.getHeight(), 0, 360);
			g2.fillArc((int) stringRect.getX(), (int) stringRect.getY(), (int)stringRect.getWidth(), (int)stringRect.getWidth(), 0, 360);
			g2.setColor(Color.white);
			g2.drawString(Integer.toString(auraWidth), x, y);

			stringRect = metrics.getStringBounds(Integer.toString(auraHeight), g2);
			// Get (x,y) to start drawing the height
			x = enemySpriteX - (int) stringRect.getWidth();
			y = (int) (enemy.getSprite().getMyRectangle().getCenterY() - stringRect.getCenterY());

			stringRect.setRect(x, y - metrics.getAscent(), stringRect.getWidth(), stringRect.getHeight());
			stringRect = grow(stringRect, 3.0);

			g2.setColor(Color.red);
			g2.fillArc((int) stringRect.getX(), (int) stringRect.getY(), (int) stringRect.getWidth(), (int) stringRect.getHeight(), 0, 360);
			g2.setColor(Color.white);
			g2.drawString(Integer.toString(auraHeight), x, y);
		}
	}

	public void update()
	{
		playerSpriteX = player.getSprite().getXPosn();
		playerSpriteY = player.getSprite().getYPosn();
		if (enemy != null)
		{
			enemySpriteX = enemy.getSprite().getXPosn();
			enemySpriteY = enemy.getSprite().getYPosn();
		}
	}

	public void evaluate()
	{
		if (choices[selected] == answer)
		{
			//enemy.takeDamage(player.getGeoAttackVal(), player);
			enemy.setIsWeakened(true);
			game.getClipsLoader().play("rightChoice",false);
		}
		else
		{
			game.getClipsLoader().play("wrongChoice",false);
			player.takeDamage(5, enemy);
		}
	}
	//TODO: Rectangle2D is an abstract class. use Rectangle instead.

	public Rectangle2D grow(Rectangle2D rect, double amount)
	{
		rect.setRect(rect.getX() - amount, rect.getY() - amount, rect.getWidth() + amount * 2, rect.getHeight() + amount * 2);
		return rect;
	}

	public void setSelected(int selected)
	{
		if(selected <numChoices&& selected>=0)
		{
			this.selected = selected;
			game.getClipsLoader().play("select",false);
		}

	}

	public int getSelected()
	{
		return this.selected;
	}

	public int getNumChoices()
	{
		return numChoices;
	}
}
