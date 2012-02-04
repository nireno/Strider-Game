package game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Kris
 */
public class Effects {
    


    public static BufferedImage colours(BufferedImage bI)
    {
       // BufferedImage bI = (BufferedImage)i;

        int[] pixels = new int[bI.getWidth() * bI.getHeight()];

        pixels = bI.getRGB(0,0,bI.getWidth(),bI.getHeight(), pixels, 0,bI.getWidth());

        int pInfo;
        int s,r,g,b;
        for(int j=0; j<pixels.length; j++)
        {
            pInfo = pixels[j];

            s = (pInfo >> 24)&255;
            r = (pInfo >> 16)&255;
            g = (pInfo>> 8)&255;
            b = (pInfo)&255;

            s=0;
  //          r=128;
    //        g=0;
           // b=0;
            g=255;
           // r=255;
            pInfo = b | (g<<8) | (r<< 16) | (s << 24) ;
            pixels[j] = pInfo;
        }
        bI.setRGB(0, 0, bI.getWidth(), bI.getHeight(), pixels, 0, bI.getWidth());

        return bI;
    };
	public static BufferedImage GrayScale(BufferedImage bI, float fraction)
	{
		// BufferedImage bI = (BufferedImage)i;
		BufferedImage newImage = new BufferedImage(bI.getWidth(), bI.getHeight(), bI.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.drawImage(bI, 0, 0, null);
		int[] pixels = new int[newImage.getWidth() * newImage.getHeight()];

		pixels = newImage.getRGB(0, 0, newImage.getWidth(), newImage.getHeight(), pixels, 0, newImage.getWidth());

		int pInfo;
		int s, r, g, b;
		for (int j = 0; j < pixels.length; j++)
		{
			pInfo = pixels[j];

			s = (pInfo >> 24) & 255;
			r = (pInfo >> 16) & 255;
			g = (pInfo >> 8) & 255;
			b = (pInfo) & 255;

			if(s != 0) //then the pixel is not transparent, so reduce coloring.
			{
				r=g=b = (r+g+b)/3;
			}
			pixels[j] = b | (g << 8) | (r << 16) | (s << 24);
		}

		newImage.setRGB(0, 0, newImage.getWidth(), newImage.getHeight(), pixels, 0, newImage.getWidth());

		return newImage;
	}

	;

   private static BufferedImage makeARGBImage(BufferedImage bI)
   {

       BufferedImage dest = new BufferedImage( bI.getWidth(), bI.getHeight(),
                                  BufferedImage.TYPE_INT_ARGB);  // alpha channel
        Graphics2D g2d = dest.createGraphics();

    // copy image
        //g2d.drawImage(bI, 0, 0,dest.getWidth(),dest.getHeight(), 0, 0,bI.getWidth(),bI.getHeight(), null);

        g2d.drawImage(bI, 0, 0, null);
        return dest;
   }


    public static BufferedImage scaleImage(BufferedImage bI, int w, int h)
    {
        BufferedImage dest = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);  // alpha channel
        Graphics2D g2d = dest.createGraphics();

        //copy image
        g2d.drawImage(bI, 0, 0, dest.getWidth(), dest.getHeight(), bI.getWidth(), 0, 0, bI.getHeight(), null);
        return dest;
    }

    public static BufferedImage flipImage(BufferedImage image, int flip)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage dest = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);  // alpha channel
        Graphics2D g2d = dest.createGraphics();

        //copy image
        switch(flip)
        {
            case 0: //No flip - draw default image.
                return image;

            case 1: //Flip image horizontally.
                g2d.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
                break;
            case 2:
                g2d.drawImage(image, 0, 0, width, height, 0, height, width, 0, null);
                break;
        }

        return dest;
    }

    public static BufferedImage fade(BufferedImage bI)
    {
       // BufferedImage bI = (BufferedImage)i;
        BufferedImage dest =  makeARGBImage(bI);

        int[] pixels = new int[dest.getWidth() * dest.getHeight()];

        pixels = dest.getRGB(0,0,dest.getWidth(),dest.getHeight(), pixels, 0,dest.getWidth());

        int pInfo;
        int s,r,g,b;
        for(int j=0; j<pixels.length; j++)
        {
            pInfo = pixels[j];

            s = (pInfo >> 24)&255;
            r = (pInfo >> 16)&255;
            g = (pInfo>> 8)&255;
            b = (pInfo)&255;

            if(s!=0)
            {
            s=255;
            }
//            r=128;
//            g=128;
           // b=0;
            //g=255;
           // r=255;
            pInfo = b | (g<<8) | (r<< 16) | (s << 24) ;
            pixels[j] = pInfo;
        }

        dest.setRGB(0, 0, bI.getWidth(), bI.getHeight(), pixels, 0, bI.getWidth());

        return dest;
        
    };

    public static BufferedImage roate(BufferedImage bI,double degrees)
    {

        BufferedImage dest = new BufferedImage( bI.getWidth(),bI.getHeight(),
                                  BufferedImage.TYPE_INT_ARGB);  // alpha channel
        Graphics2D g2d = dest.createGraphics();

        AffineTransform origAT = g2d.getTransform(); // save original transform

        // rotate the coord. system of the dest. image around its center
        AffineTransform rot = new AffineTransform();
        rot.rotate( Math.toRadians(degrees), bI.getWidth()/2, bI.getHeight()/2);
      //  rot.rotate( Math.toRadians(degrees));
        
        g2d.transform(rot);

        g2d.drawImage(bI, 0, 0, null);   // copy in the image
        

        g2d.setTransform(origAT);    // restore original transform
        g2d.dispose();

        return dest;


    }

}
