/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Who
 */
public class HUD {
	private Font HUDFont;//Font used for text in the HUD
	private FontMetrics fontMtr;//used for checking the pixel size of text being drawn
	private int xPos, yPos;//upper left corner postion of HUD on the screen
	private Game game;

	public HUD(int xPos, int yPos, Game game){
		this.xPos = xPos;
		this.yPos = yPos;
		this.game = game;
		HUDFont = new Font("Arial", Font.BOLD, 14);
	}

	public void draw(Graphics2D g)
	{
		fontMtr =g.getFontMetrics(HUDFont);
		String HPLabel = "HP";
		int HPBarHeight = 20;
		int HPBarWidth = 150;
		Player player = game.getPlayer();
		int HPBarXOffset = fontMtr.stringWidth(HPLabel);
		Rectangle hudBgRect = new Rectangle(0,0,game.getWidth(),80);
		g.setColor(new Color(0,0,0,150));
		g.fill(hudBgRect);
		//Draw HP Label
		g.setColor(Color.white);
		g.setFont(HUDFont);
		g.drawString(HPLabel, xPos, yPos+fontMtr.getHeight());
		//Draw translucent background for health bar
		g.setPaint(new Color(255,255,255,150));
		g.fillRect(xPos+HPBarXOffset, yPos, HPBarWidth, 20);
		//set the gradient fill colors for the health bar based on the players HP
		if(player.getHP()<34)
			g.setPaint(new GradientPaint(0, yPos, new Color(255,0,0), 0, yPos+HPBarHeight, new Color(40,0,0)));
		else if(player.getHP()<67)
			g.setPaint(new GradientPaint(0, yPos, new Color(255,255,0), 0, yPos+HPBarHeight, new Color(40,40,0)));
		else
			g.setPaint(new GradientPaint(0, yPos, new Color(0,255,0), 0, yPos+HPBarHeight, new Color(0,40,0)));
		//Draw the player health bar
		g.fillRect(xPos+HPBarXOffset, yPos, (int)(HPBarWidth*((float)player.getHP()/100)), 20);
		//draw border around health bar
		g.setStroke(new BasicStroke(2));
		g.setColor(new Color(128,128,128));
		g.drawRect(xPos+HPBarXOffset, yPos, HPBarWidth, HPBarHeight);
		g.setColor(Color.white);
		g.drawString("Press G to enter Geotime mode!",xPos , yPos+HPBarHeight+fontMtr.getHeight());
	}

	public void setXPos(int x)
	{
		xPos = x;
	}
	public int getXPos()
	{
		return xPos;
	}
	public void setYPos(int y){
        yPos=y;
     }
    public float getYPos(){
        return yPos;
    }
	public void setFont(Font f)
	{
		HUDFont = f;
	}

}
