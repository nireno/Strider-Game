package game;

import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 *
 * @author Nitro
 */
public abstract class GameState {
//    private ArrayList<Entity> characters;
//    private BricksManager brickMan;
//    private RibbonsManager ribbonMan;
    protected Game game;
    protected BufferStrategy doublebuf;
    protected InputManager inputMan;
	protected boolean bIsEntering;	// If this is true then the player is now entering the state.
	protected boolean bIsLeaving;	// If this is true then the player is now leaving the state.
    public GameState(Game game) {
//        this.characters = characters;
//        this.brickMan = brickMan;
//        this.ribbonMan = ribbonMan;
        this.game = game;
        this.doublebuf = game.getDoublebuf();
        this.inputMan = game.getInputMan();
    }

    public abstract void draw();
    public abstract void update();

//    public void processKeyboard()
//    {
//
//    }
}
