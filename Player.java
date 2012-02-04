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
public class Player extends Actor {
    private double cycleTime; // Length of an animation sequence.
    private int invrTime; //
    private int attackVal;//how much damage player will do if he hits something
	private int geoAttackVal = 50; // Damage dealt to enemy if geoTime question is answered correctly. 
	private EnemyManager enemyMgr;
    //track the size of the players movement to calculate ribbon motion.
    int currXPos;
    int lastXPos;
	
	/* This rectangle is drawn in front the player when attacking and used to 
	 * test collisions with the player's weapon.
	 */ 
	private Rectangle atkRect;
	private RibbonsManager ribbonsMgr;

	public Player(float x, float y, Game game) {
		super(x, y, game);
		enemyMgr = game.getEnemyMgr();
		ribbonsMgr = game.getRibsMan();
		currXPos = lastXPos = (int)xPos;
		xVel = 0;
        yVel = 0;
        state = "stopped";
        xAccel = 0.4f;
        yAccel = 15;
        dmgPushMag =5;
        maxXVel = 5;
        HP = 100;
        attackVal = 15;
        maxYVel = bricksMgr.getBrickHeight()-1;//set a max velocity to prevent bypassing collision detection
        cycleTime=1;
        invrTime = 0;
        sprite = new Sprite((int)game.getWidth()/2,(int)yPos,game.getWidth(),game.getHeight(),game.getImgLdr(),"striderStandR");
        sprite.setActive(true);
        colBox = sprite.getMyRectangle();
        colBox.grow(-sprite.getWidth()/6,0);
	}

    public void moveRight(){
        //check if you are in a state that allows movement
        if(state.matches("stopped")||state.matches("walking")){
           if(xVel<maxXVel)
               xVel+=xAccel;//*game.getDeltaTime();//apply x acceleration
           if(!isFacingRight||state.matches("stopped")){//check if the sprite needs to be changed
                if(bOnFloor)
                    sprite.setImage("striderR");
                else
                    sprite.setImage("striderJR");
                isFacingRight = true;
            }
            state = "walking";
        }
    }
	
    public void moveLeft(){
        //check if you are in a state that allows movement
        if(state.matches("stopped")||state.matches("walking")){
            if(xVel>-maxXVel)
                xVel+=-xAccel;//*game.getDeltaTime();//apply x acceleration
            if(isFacingRight||state.matches("stopped")){//check if the sprite needs to be changed
                if(bOnFloor)
                    sprite.setImage("striderR");
                else
                    sprite.setImage("striderJR");
                isFacingRight = false;
            }
            state = "walking";
        }
        
    }

   public void jump()
	{
		if (bOnFloor)
		{
			//set initial yVel to jumpSpeed
			yVel = -yAccel;
			//calculate yVel taking collsions into consideration so animation does not
			//look strange when sprite is in a tunnnel where it cannot jump
			////checkBrickBase works with a positive 3rd param so we have to change
			//yVel to positive.
			yVel = bricksMgr.checkBrickBase((int) xPos + (sprite.getWidth() / 2), (int) (yPos + yVel), (int) -yVel);
			yVel = -yVel;//change yVel back to -ve
			if (yVel < -5)//if there is at least some space to jump
			{
				bOnFloor = false;
				sprite.setImage("striderJR");
				game.getClipsLoader().play("plrjump", false);//play the jump sound
			} else
			{
				yVel = 0;//don't jump
			}
		}
	}

    public void attack(){
        
        if(state.matches("stopped")||state.matches("walking")){
           	if(bOnFloor)
				xVel = 0;
            game.getClipsLoader().play("attack",false);
            state = "attacking";
            
            sprite.setImage("striderAttack1R");
            sprite.playImgSeq(50, cycleTime);
            if(isFacingRight){
                atkRect = new Rectangle((int)(sprite.getXPosn()+sprite.getWidth()*0.75),(int)sprite.getYPosn(),sprite.getWidth()/3,sprite.getHeight());
            }
            else//TODO: do not create the rectangle everytime. just move it and check it.
                atkRect = new Rectangle((int)(sprite.getXPosn()-sprite.getWidth()/4),(int)sprite.getYPosn(),sprite.getWidth()/3,sprite.getHeight());
            atkRect.grow(0, -sprite.getHeight()/5);
			if(enemyMgr != null)
				enemyMgr.playerDamageEnemy(this);
//			for(int i = 0; i < game.geten)
//			if(game.getEnemy().getColBox().intersects(atkRect) && !game.getEnemy().getState().matches("dying"))
//                game.getEnemy().takeDamage(attackVal, this);
        }
    }

	//This is called by an Actor when it hits the player
	@Override
    public void takeDamage(int damage, Actor damager){
        if(invrTime<=0&&!state.matches("takingDamage")&&!state.matches("dying")){
            game.getClipsLoader().play("hitplayer",false);
			invrTime = 2000;
			sprite.setImage("striderR");//TODO: change this to damage animation
			
			super.takeDamage(damage, damager);

			System.out.println("Player HP="+HP);
        }
    }
    
    public void die(){
        state = "dying";
        sprite.setActive(true);
		sprite.setImage("tombstone");
        System.out.println("dying");
    }

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
				if (state.matches("attacking"))//to fix collision problems with larger attack sprite
				{
					yVel = Math.min(bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() * 0.5)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel),
							bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() / 4)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel));
				} else
				{
					yVel = Math.min(bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() * 0.75)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel),
							bricksMgr.checkBrickTop((int) (xPos + (sprite.getWidth() * 0.25)), (int) (yPos + sprite.getHeight() + yVel), (int) yVel));
				}
				//if Actor hits the floor
				if (yVel == 0 && !bOnFloor)
				{
					if (xVel != 0)
					{
						state = "walking";
						sprite.setImage("striderR");
					} else
					{
						state = "stopped";
						sprite.setImage("striderStandR");
					}
					bOnFloor = true;
				}
			}
		}
	}//end processVertMotion


    public void update()
	{
		//need a better state handling system
		if (state.matches("stopped"))
		{
			if (!sprite.getIsLooping() && bOnFloor)
				sprite.loopImage(60, cycleTime);
		}
		else if (state.matches("walking"))
		{//if moveRight or moveLeft is being used
			if (!sprite.getIsLooping() && bOnFloor)
				sprite.loopImage((int) maxXVel * 12, cycleTime);
		}
		else if (state.matches("attacking"))
		{
			if (sprite.imgSeqEnded())
			{
				sprite.setImage("striderStandR");
				sprite.loopImage(60, cycleTime);
				state = "stopped";
			}
		}
		else if (state.matches("takingDamage"))
		{
			//if damage animation exists then check for end of animation to change state
		}
		//if player collides horizontally or is nearly stationary set xVel to zero
		//TODO: fix this 'if'...seems possible to be done better
		if (willHitBrick() || Math.abs(xVel) < xAccel && xVel!=0 && bOnFloor)
		{
			xVel = 0;
			sprite.setImage("striderStandR");
			sprite.loopImage(60, cycleTime);
			if(!state.matches("dying"))
				state = "stopped";
		}
		
		xDecelerate();

		if (invrTime > 0)
		{
			//make the player blink
			if (sprite.isActive())
				sprite.setActive(false);
			else
				sprite.setActive(true);
			//reduce invrTime by the amount of time that has elapsed since the last update
			invrTime -= game.getDeltaTime();
		} else
			sprite.setActive(true);

//		System.out.println("state: " + state); //TODO: remove this
		setXPos(xPos + xVel);//*game.getDeltaTime()/1000);
		processVertMotion();
		setYPos(yPos + yVel);//*game.getDeltaTime()/1000);
		sprite.setYPos((int) yPos + 2);//hack to make the strider image reach the floor
		sprite.updateSprite();

		//\/scroll the brick based on the player position in the world\/
		bricksMgr.setXMapHead(Math.round(sprite.getXPosn() - xPos));

		//\/Ribbon scrolling\/
		currXPos = Math.round(xPos);
		ribbonsMgr.setMoveSize(Math.abs(currXPos - lastXPos));
		if (xVel > 0)
			ribbonsMgr.moveLeft();
		else if (xVel < 0)
			ribbonsMgr.moveRight();
		lastXPos = currXPos;

	}

    //probably should not have a public setState
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
        //make sure the sprite position is updated with the player position
        //playerSprite.setXPos(Math.round(x));        
        //update the collision rectangle position
        colBox.setLocation((int)(sprite.getXPosn()+(sprite.getWidth()-colBox.getWidth())/2), (int)colBox.getY());
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

    public void setHP(int hp){
        HP=hp;
    }
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

	public Rectangle getAtkRect() {
		return atkRect;
	}

	public int getGeoAttackVal()
	{
		return geoAttackVal;
	}

    public void setMaxXVel(float maxXVel)
    {
        this.maxXVel = maxXVel;
    }

    public void setXAccel(float xAccel)
    {
        this.xAccel = xAccel;
    }


}
