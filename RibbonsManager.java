package game;


// RibbonsManager.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* RibbonsManager manages many ribbons (wraparound images 
   used for the game's background). 

   Ribbons 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the 
   sprite doesn't actually move, instead the ribbons move in
   the _opposite_direction (right or left).

*/

import java.awt.*;


public class RibbonsManager
{
  private String ribImages[] = {"bkgd1"};

  private double moveFactors[] = {0.4};  // applied to moveSize

  private Ribbon[] ribbons;
  private int numRibbons;
  private int moveSize;
     // standard distance for a ribbon to 'move' each tick


  public RibbonsManager(int w, int h, int brickMvSz, ImagesLoader imsLd)
  {
    moveSize = brickMvSz;
          // the basic move size is the same as the bricks ribbon

    numRibbons = ribImages.length;
    ribbons = new Ribbon[numRibbons];

    for (int i = 0; i < numRibbons; i++)
       ribbons[i] = new Ribbon(w, h, imsLd.getImage( ribImages[i] ),
						(int) (moveFactors[i]*moveSize) );
  }  // end of RibbonsManager()


  public void moveRight()
  { for (int i=0; i < numRibbons; i++)
      ribbons[i].moveRight();
  }

  public void moveLeft()
  { for (int i=0; i < numRibbons; i++)
      ribbons[i].moveLeft();
  }

  public void stayStill()
  { for (int i=0; i < numRibbons; i++)
      ribbons[i].stayStill();
  }


  public void update()
  { for (int i=0; i < numRibbons; i++)
      ribbons[i].update();
  }

  public void display(Graphics g)
  /* The display order is important.
     Display ribbons from the back to the front of the scene. */
  { for (int i=0; i < numRibbons; i++)
      ribbons[i].display(g);
  }

    public void setMoveSize(int moveSize) {
//        this.moveSize = moveSize;
        for(int i = 0; i < ribbons.length; i++)
            ribbons[i].setMoveSize((int)Math.round(moveFactors[i]*moveSize));
    }

} // end of RibbonsManager

