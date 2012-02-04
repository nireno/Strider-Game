/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

/**
 *
 * @author Who
 */
import java.awt.*;
import java.awt.image.BufferedImage;
public abstract class Enemy extends Actor{
    protected double cycleTime;
    protected long takeDmgTime;//used to know how long to leave the enemy in the takingDamage state
	protected int takeDmgMultiplier;
    protected int attackVal;//how much damage enemy will do if it attacks something
	protected int difficulty; // Used by the QuestionManager when generating dimensions.
	protected boolean isWeakened; // True if the geotime question for the enemy is correctly answered.

	public Enemy(float xPos, float yPos, Game game) {
		super(xPos, yPos, game);
		xVel = 0;
		yVel = 0;
		state = "stopped";
		xAccel = 0.4f;
		maxXVel = 4;
		dmgPushMag = 5;
		HP = 100;
		attackVal = 5;
		maxYVel = bricksMgr.getBrickHeight() - 1;//set a max velocity to prevent bypassing collision detection
		cycleTime = 1;
		takeDmgTime = 200;
		takeDmgMultiplier = 1;
		sprite = null;
		colBox = new Rectangle((int) xPos, (int) yPos, 10, 10);
	}


	public void initHP(int hp){
    /* When initializing the enemy, it is important that this method be used as
	 * it is also responsible for determining the difficulty of the foe.
	 */
		HP = hp;
		if(hp < 100) { difficulty = 0; }
		else if( hp < 200) { difficulty = 1; }
		else difficulty = 2;
    }

	@Override
	public void takeDamage(int damage, Actor damager) {
		game.getClipsLoader().play("hitenemy",false);
		super.takeDamage(damage * takeDmgMultiplier, damager);
	}


    public void setState(String state){
        this.state=state;
    }
    public String getState(){
        return state;
    }

    public Rectangle getColBox(){
        return colBox;
    }
    public void setXPos(float x){
        xPos=x;
        sprite.setXPos(Math.round((xPos+bricksMgr.getXMapHead())));
        //update the collision rectangle position.May need to move this to subclasses if collision box is not to be centered
        colBox.setLocation((int)(sprite.getXPosn()+(sprite.getWidth()-colBox.getWidth())/2), (int)colBox.getY());
        //System.out.println("xpos: "+xPos+"xmh: "+bricksMgr.getXMapHead()+"spritex: "+enemySprite.getXPosn());
    }
    public float getXPos(){
        return xPos;
    }

    public void setYPos(float y){
        yPos=y;
        //make sure the sprite position is updated with the player position
        sprite.setYPos(Math.round(y));
        //update the collision rectangle position
        colBox.setLocation((int)colBox.getX(),(int)(sprite.getYPosn()+(sprite.getHeight()-colBox.getHeight())/2));
    }
    public float getYPos(){
        return yPos;
    }

    public void setTermVel(float maxv){
        maxYVel = maxv;
    }
    public float getTermVel(){
        return maxYVel;
    }
    public void setXVel(float xvel){
        xVel = xvel;
    }
    public float getXVel(){
        return xVel;
    }
    public void setYVel(float yvel){
        yVel = yvel;
    }
    public float getYVel(){
        return yVel;
    }

	public void setHP(int hp) { HP = hp; }

	public int getHP(){
        return HP;
    }
    public int getAttackVal(){
        return attackVal;
    }
    public void setAttackVal(int atkVal){
        attackVal = atkVal;
    }
    public boolean getOnFloor(){
		return bOnFloor;
    }

	public void setDifficulty(int difficulty)
	{
		if(difficulty>=0&&difficulty<3)
			this.difficulty = difficulty;
	}


	public int getDifficulty()
	{
		return difficulty;
	}

	public int getTakeDmgMultiplier()
	{
		return takeDmgMultiplier;
	}

	public void setTakeDmgMultiplier(int takeDmgMultiplier)
	{
		this.takeDmgMultiplier = takeDmgMultiplier;
	}


	public void setIsWeakened(boolean isWeakened)
	{
		this.isWeakened = isWeakened;
		setDifficulty(difficulty+1);//make calculations harder everytime geotime is used on an enemy
	}

	@Override
	public void draw(Graphics dbg)
	{
		super.draw(dbg);
		if(isWeakened)
		{
			//TODO: fix this hack to get grayscale sprite image
			//Sprite.drawSprite gets an image from the image player and draws immediately after
			//there seems to be no way to modify that image before it is drawn so we are
			//drawing a new greayscale image over the original image
			BufferedImage bI = Effects.GrayScale(sprite.getImage(),0);
			if(!isFacingRight)
				bI = Effects.flipImage(bI, 1);
			dbg.drawImage(bI, sprite.getXPosn(), sprite.getYPosn(), null);
		}
	}

	
}
