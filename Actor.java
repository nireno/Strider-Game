/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Nitro
 *
 * This is the parent class for all controllable entities in the game (controlled
 * by the player or AI).
 */
public abstract class Actor extends Entity{
	// Access to the BricksManager is needed for testing brick collisions.
	// All movement of Actors needs to be in relation to the brick map.
	protected BricksManager bricksMgr;

	protected Sprite sprite; // Visible representation of the actor.
	protected Rectangle colBox; //Bounding box used to test for collisions.
	protected float xAccel = 0; 
	protected float yAccel = 0; 
	protected float dmgPushMag = 0;
	protected float maxYVel;//maximum vertical velocity.Should not be more than the height of a brick
	protected float maxXVel;//maximum horizontal velocity.Should not be more than the width of a brick
	
	protected boolean isFacingRight = true;
	protected boolean bOnFloor = false;
	protected boolean isDead = false;

	// Default to one Hit Point. 
	// Less than 1 HP kills the Actor (calls the die() method).
	protected int HP = 1;
	protected int MaxHP = 100;

	//The state variable holds the current status of the Actor: eg. stopped,
	//walking, attacking.
	//A state restricts certain capabilities of the actor. For example,
	//the Actor may not be able to move while in the attacking state.
	protected String state;


	public Actor(float x, float y, Game game) {
		super(x, y, game);
		bricksMgr = game.getBricksMan();
		maxYVel = bricksMgr.getBrickHeight();
		maxXVel = bricksMgr.getBrickWidth();
		bOnFloor = false;
	}

	public void draw(Graphics dbg){
        if(isFacingRight)
            sprite.drawSprite(dbg, 0);
        else
            sprite.drawSprite(dbg, 1); //flip image horizontally
    }

	//
	public void takeDamage(int damage, Actor damager)
	{
		if (!state.matches("dying"))
		{
			state = "takingDamage";
			Sprite damagerSprite = damager.getSprite();
			// Find the length of the vector joining the center of both sprites
			float xVect = (sprite.getXPosn() + sprite.getWidth() / 2)
					- (damagerSprite.getXPosn() + damagerSprite.getWidth() / 2);
			float yVect = (sprite.getYPosn() + sprite.getHeight() / 2)
					- (damagerSprite.getYPosn() + damagerSprite.getHeight() / 2);
			float vectorLen = (float) Math.sqrt(xVect * xVect + yVect * yVect);
			xVel = dmgPushMag * (xVect / vectorLen);
			yVel = dmgPushMag * (yVect / vectorLen);
			HP -= damage;
			if (HP <= 0)
			{
				HP = 0;
				die();
			}
		}
	}

	//this method checks to see if a specific point on the Actor sprite is in a brick in the world
	//TODO: fix this to work like checkBrickTop or redo the entire world collisions using rectangle intersections
	public boolean willHitBrick(){

//		if(isFacingRight)
//		{
//			
//		}
//		else
//		{
//			
//		}
		if (xVel==0) // can't hit anything if not moving
			return false;
		int xTest;// for testing the new x- position
		xTest = Math.round(xPos + xVel);
		int xColPt;
		if(xVel>0)//if the Actor is moving right check a x point on the right of sprite
			xColPt= xTest + (int)(sprite.getWidth()*0.8);
		else//else check a point on the left of the sprite
			xColPt= xTest + (int)(sprite.getWidth()*0.2);

		//generate 3 y collision points to test
		int yColPt1 = (int)(yPos + sprite.getHeight()*0.99);
		int yColPt2 = (int)(yPos + sprite.getHeight()*0.5);
		int yColPt3 = (int)(yPos + sprite.getHeight()*0.01);

		//test the collison points
		return bricksMgr.insideBrick(xColPt,yColPt1)||bricksMgr.insideBrick(xColPt,yColPt2)||bricksMgr.insideBrick(xColPt,yColPt3);
    }  // end of willHitBrick()


	/* This method should determine how forces such as gravity should affect the
	 * actor's vertical motion. It should also ensure that the actor does not
	 * rise/fall through a brick.
	 */
	protected void processVertMotion()
	{
		//apply gravity
		yVel += game.getGravity();//*game.getDeltaTime();
		if (yVel > maxYVel)
		{
			yVel = maxYVel;
		}
		if (yVel < 0)//if sprite is moving up. TODO: check multiple collision points like when falling
		{
			//determine if yVel needs to be reduced to avoid entering a brick from below
			yVel = bricksMgr.checkBrickBase((int) xPos + (sprite.getWidth() / 2), (int) (yPos + yVel), (int) -yVel);//make yVel +ve for the 3rd param.
			yVel = -yVel;//checkBrickBase works with a positive 3rd param so we have to change it back to -ve here.
		} else
		{
			if (yVel > 0)//if falling
			{
				//determine if yVel needs to be increased to avoid entering the floor
				yVel = Math.min(bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() * 0.75)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel),
						bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() * 0.25)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel));
				//if Actor hits the floor
				if (yVel == 0 && !bOnFloor)
				{
					if (xVel != 0)
					{
						state = "walking";
						//set walking sprite here
					} else
					{
						state = "stopped";
						//set stopped sprite here
					}
					bOnFloor = true;
				}
			}
		}
	}//end processVertMotion;

	protected void xDecelerate()
	{
		//apply deceleration
		if (xVel < 0)
		{
			if (bOnFloor)
			{
				xVel += xAccel / 2;
			} else
			{
				xVel += xAccel / 6;//lose less speed in air
			}
		} else
		{
			if (xVel > 0)
			{
				if (bOnFloor)
				{
					xVel -= xAccel / 2;
				} else
				{
					xVel -= xAccel / 6;
				}
			}
		}
	}
	protected abstract void die();

	// Accessors ---------------------------------------------------------------

	public Sprite getSprite() {
		return sprite;
	}

	public boolean getIsDead()
	{
		return isDead;
	}

	//--------------------------------------------------------------------------

}