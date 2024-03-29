/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

/**
 *
 * @author Who
 */
public class Rabite extends Enemy {
private boolean landed;
	public Rabite(float xPos, float yPos, Game game, BricksManager bricksMgr, RibbonsManager ribbonsMgr)
	{
		super(xPos, yPos, game);
		sprite = new Sprite((int) xPos, (int) yPos, game.getWidth(), game.getHeight(), game.getImgLdr(), "rabite");
		sprite.setActive(true);
		colBox = sprite.getMyRectangle();
		colBox.grow(-sprite.getWidth() / 4, -sprite.getHeight() / 6);//resize to match the sprite
		maxXVel = 1;
		xVel = maxXVel;
		initHP(20);
		state = "roaming";
		dmgPushMag = 3;
		attackVal = 5;
	}

	public void die()
	{
		//sprite.setActive(false);
		state = "dying";
		xVel = yVel = 0;
		isDead = true;
	}

	public void update()
	{
		sprite.updateSprite();
		if (!sprite.getIsLooping())
			sprite.loopImage(60 , 1);
		//turn around when hits a brick
		if (willHitBrick())
		{
			xVel *= -1;
			if (state.matches("takingDamage"))
			{
				xVel = 0;
			}
			else
			{
				if (isFacingRight)
				{
					isFacingRight = false;
				} else
				{
					isFacingRight = true;
				}
			}
		}
//        if(!state.matches("takingDamage"))
		setXPos(xPos + xVel);//*game.getDeltaTime()/1000);
//        else
		if (takeDmgTime <= 0 && !state.matches("dying"))
		{
			state = "roaming";
			if (isFacingRight)
			{
				xVel = maxXVel;
			} else
			{
				xVel = -maxXVel;
			}
		} else
		{
			takeDmgTime -= game.getDeltaTime();
		}
		if (sprite.getXPosn() < -sprite.getWidth() || sprite.getXPosn() > game.getWidth())
		{
			sprite.setActive(false);
		} else
		{
			if (!state.matches("dying"))
			{
				sprite.setActive(true);
			}
		}
		processVertMotion();
		setYPos(yPos + yVel);
		//if enemy collides with the player then damage the player
		if (colBox.intersects(game.getPlayer().getColBox()) && !state.matches("dying"))
		{
			game.getPlayer().takeDamage(attackVal, this);
		}
	}

	@Override
	public void takeDamage(int damage, Actor damager)
	{
		super.takeDamage(damage, damager);
//		yVel = 0;//remove the y component of the damage push vector generated by takeDamage
		takeDmgTime = 500;

	}
	@Override
	public void setIsWeakened(boolean isWeak)
	{
		super.setIsWeakened(isWeak);
		if(isWeak)
		{
			takeDmgMultiplier = 2;
			maxXVel /=2;
			cycleTime*=2;//slow down animation to match new xVel
			attackVal/=2;
		}
		else
		{
			//TODO: need to create default value variables to reset xVel etc if not weak;\
		}
	}
}
