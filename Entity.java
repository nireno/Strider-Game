package game;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Nitro
 */
public abstract class Entity {
    protected static int idCount = 0;
    protected int id;
    protected float xPos, yPos; //world position
    protected float xVel = 0;
    protected float yVel = 0;
    protected int width = 0;
    protected int height = 0;
	protected Game game;
    

	public Entity(float x, float y, Game game)
	{
		this.xPos = x;
		this.yPos = y;
		this.id = idCount++;
		this.width = 0;
		this.height = 0;
		this.game = game;
	}
    public Entity(float x, float y, int width, int height, Game game) {
        this.xPos = x;
        this.yPos = y;
        this.id = idCount++;
        this.width = width;
        this.height = height;
		this.game = game;
    }


    public Entity(float x, float y, float dx, float dy, int width, int height) {
        this.xPos = x;
        this.yPos = y;
        this.xVel = dx;
        this.yVel = dy;
        this.id = idCount++;
        this.width = width;
        this.height = height;
    }

	public abstract void update();

    public Rectangle2D.Double getMyRectangle()
    {
        return new Rectangle2D.Double(xPos,yPos, width, height);
    }

    public float getDx() {
        return xVel;
    }

    public float getDy() {
        return yVel;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return xPos;
    }

    public float getY() {
        return yPos;
    }

}
