package game;

/*
 *	
 ******************************************************************************************************************
 *	This class implements all the behaviour that a Sprite object should use
 *	
 *	All the characters/sprites used in this game inherit from this class
 *
 */
import java.awt.*;
import java.awt.image.*;


public class Sprite 
{
  // default dimensions when there is no image
  private static final int SIZE = 12;   

  // image-related
  private ImagesLoader imsLoader;
  private String imageName;
  private BufferedImage image;
  private int width, height;     // image dimensions

  private ImagesPlayer player;  // for playing a loop of images
  private boolean isLooping;

  private int pWidth, pHeight;   // panel dimensions

  private boolean isActive = false;      
  // a sprite is updated and drawn only when it is active

  // protected vars
  protected int locx, locy;        // location of sprite
  


 //--------------------------------------------------------------------------
//constructor method
  public Sprite(int x, int y, int panelWidth, int panelHeight, ImagesLoader imsLd, String name)
  { 
    locx = x; locy = y;
    pWidth = panelWidth; pHeight = panelHeight;
    imsLoader = imsLd;
    setImage(name);// the sprite's default image is 'name'
  } // end of Sprite()

 //--------------------------------------------------------------------------

  public void setImage(String name)
  // assign the name image to the sprite
  {
    imageName = name;
    image = imsLoader.getImage(imageName);
    if (image == null) {    // no image of that name was found
      System.out.println("No sprite image for " + imageName);
      width = SIZE;
      height = SIZE;
    }
    else {
      width = image.getWidth();
      height = image.getHeight();
    }
    // no image loop playing 
    player = null;
    isLooping = false;
  }  // end of setImage()
  
  public void setImage(String name, int pos)
  // assign the name image to the sprite
  {
    imageName = name;
    image = imsLoader.getImage(imageName,pos);
    if (image == null) {    // no image of that name was found
      System.out.println("No sprite image for " + imageName);
      width = SIZE;
      height = SIZE;
    }
    else {
      width = image.getWidth();
      height = image.getHeight();
    }
    // no image loop playing 
    player = null;
    isLooping = false;
  }  // end of setImage()
 //--------------------------------------------------------------------------

	public void setImage(BufferedImage image)
	{
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		//player = null;
		//isLooping = false;
	}

  public void loopImage(int animPeriod, double seqDuration)
  /* Switch on loop playing. The total time for the loop is
     seqDuration secs. The update interval (from the enclosing
     panel) is animPeriod ms. */
  {
    if (imsLoader.numImages(imageName) > 1) {
      player = null;   // to encourage garbage collection of previous player
      player = new ImagesPlayer(imageName, animPeriod, seqDuration,
                                       true, imsLoader);
      isLooping = true;
    }
    else
      System.out.println(imageName + " is not a sequence of images");
  }  // end of loopImage()
 //--------------------------------------------------------------------------
public void playImgSeq(int animPeriod, double seqDuration)
  /* Switch on loop playing. The total time for the loop is
     seqDuration secs. The update interval (from the enclosing
     panel) is animPeriod ms. */
  {
    if (imsLoader.numImages(imageName) > 1){
      player = null;   // to encourage garbage collection of previous player
      player = new ImagesPlayer(imageName, animPeriod, seqDuration,
                                       false, imsLoader);
    }
    else
      System.out.println(imageName + " is not a sequence of images");
  }  // end of loopImage()

  public void stopLooping()
  {
  	if (isLooping) {
      player.stop();
      isLooping = false;
    }
  }  // end of stopLooping()

 //--------------------------------------------------------------------------
  public boolean imgSeqEnded(){
      if(player!=null)
          return player.atSequenceEnd();
      return true;
  }
  public int getWidth()    // of the sprite's image
  {  return width;  }
 //--------------------------------------------------------------------------

  public int getHeight()   // of the sprite's image
  {  return height;  }
 //--------------------------------------------------------------------------

  public int getPWidth()   // of the enclosing panel
  {  return pWidth;  }
 //--------------------------------------------------------------------------

  public int getPHeight()  // of the enclosing panel
  {  return pHeight;  }
 //--------------------------------------------------------------------------

  public boolean isActive() 
  {  return isActive;  }
 //--------------------------------------------------------------------------

  public void setActive(boolean a) 
  {  isActive = a;  }
 //--------------------------------------------------------------------------

  public void setPosition(int x, int y)
  {  locx = x; locy = y;  }

  public void setXPos(int x)
  {locx = x;}
  
   public void setYPos(int y)
  {locy = y;}
 //--------------------------------------------------------------------------

  public void translate(int xDist, int yDist)
  {  locx += xDist;  locy += yDist;  }
 //--------------------------------------------------------------------------

  public int getXPosn()
  {  return locx;  }
 //--------------------------------------------------------------------------

  public int getYPosn()
  {  return locy;  }

 //--------------------------------------------------------------------------

   public Rectangle getMyRectangle()
  {  return  new Rectangle(locx, locy, width, height);  }

	public BufferedImage getImage()
	{
		return image;
	}

 //--------------------------------------------------------------------------

  public boolean getIsLooping()
  {
  	return isLooping;
  }
  public void updateSprite()
  // move the sprite
  {
    if (isActive()){      
      if (player != null)
        player.updateTick();  // update the player
    }
  } // end of updateSprite()

 //--------------------------------------------------------------------------


  public void drawSprite(Graphics g, int flip)
  {
    if (isActive()) {
      if (image == null) {   // the sprite has no image
        g.setColor(Color.yellow);   // draw a yellow circle instead
        g.fillOval(locx, locy, SIZE, SIZE);
        g.setColor(Color.black);
      }
      else {
        if (player!=null)
          image = player.getCurrentImage();

        g.drawImage(Effects.flipImage(image, flip), locx, locy, null);
      }
    }
  } // end of drawSprite()

}  // end of Sprite class
