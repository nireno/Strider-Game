package game;

import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Niren Orie
 **/
public class Game extends Thread
{
    private static final float GRAVITY = 1;
    private int UPDATE_SPEED = 25; // Corresponds to the number of milliseconds before an update
    private static final long PERIOD = 10000000;
    private static final int DELAYS_PER_YIELD = 16;
    private long deltaTime;//milliseconds elapsed since the last pass through game loop
    private BufferStrategy doublebuf;
    private HashMap<String, GameState> gamestates;
    private GameState currState;
    private boolean gameover = false;
    private Frame gamescreen;
    private static final String IMS_INFO = "imsInfo.txt";
    private static final String BRICKS_INFO = "stage1.txt";
    private static final String SNDS_FILE = "clipsInfo.txt";
    
    private ImagesLoader imgloader;
    private ClipsLoader clipsLoader;

    private BricksManager bricksMan;
    private RibbonsManager ribsMan;
    private int height;
    private int width;
    private InputManager inputMan;
    private Player strider;
	private HUD hud;
    private Kaiser testE;
	private EnemyManager enemyMgr;

    AudioManager AM = new AudioManager(new File("Intro.wav"), "Intro.wav");
    public Game(Frame frame)
    {
        gamescreen = frame;
        height = gamescreen.getFRAME_HEIGHT();
        width = gamescreen.getFRAME_WIDTH();

        imgloader = new ImagesLoader(IMS_INFO);
        clipsLoader = new ClipsLoader(SNDS_FILE);
        doublebuf = gamescreen.getBufferStrategy();
        gamestates = new HashMap<String, GameState>();
        
        bricksMan = new BricksManager(width, height, BRICKS_INFO, imgloader);
        ribsMan = new RibbonsManager(width, height, bricksMan.getMoveSize(), imgloader);
		enemyMgr = new EnemyManager();
        inputMan = new InputManager();
        strider = new Player(300,300,this);
		hud = new HUD(15,30,this);
        testE = new Kaiser(800,300,this,bricksMan, ribsMan);
		
		enemyMgr.add(testE);
		enemyMgr.add(new Eyeball(1400, 150, this, bricksMan, ribsMan));
		enemyMgr.add(new Kaiser(2697, 100, this, bricksMan, ribsMan));
		enemyMgr.add(new Rabit(2432, 300, this, bricksMan, ribsMan));
		enemyMgr.add(new Eyeball(2432, 200, this, bricksMan, ribsMan));
        enemyMgr.add(new Rabite(3100, 350, this, bricksMan, ribsMan));
        enemyMgr.add(new Rabite(3136, 300, this, bricksMan, ribsMan));
        enemyMgr.add(new Kaiser(4480, 300, this, bricksMan, ribsMan));
        enemyMgr.add(new Eyeball(4000, 150, this, bricksMan, ribsMan));
        enemyMgr.add(new Rabit(4480, 100, this, bricksMan, ribsMan));
        initGameStates();
        //AM.playAudio("Intro.wav", true);
    }

    @Override
	public void run()
	{
		long overTime = 0;
		long lastTime = System.currentTimeMillis();
		long lstTime = System.currentTimeMillis();

		while (!gameover)
		{

			long currentTime = System.currentTimeMillis() - lastTime;
			if (overTime != 0)
			{
				currentTime += overTime;
				overTime = 0;
			}

			if (currentTime >= UPDATE_SPEED)
			{
				deltaTime = System.currentTimeMillis() - lstTime;
				lstTime = System.currentTimeMillis();
				currState.update();
				currState.draw();
				overTime = currentTime - UPDATE_SPEED;
				lastTime = System.currentTimeMillis();
			}
		}
	}

    private void initGameStates()
    {
        this.gamestates.put("menu", new Pregame(this));
        this.gamestates.put("roam", new StateRoam(this));
		this.gamestates.put("geotime", new StateGeoTime(this));
        currState = gamestates.get("menu");
    }

//    public void throttleGameSpeed()
//    {
//        long beforeTime, afterTime, timeDiff, sleepTime;
//        long overSleepTime = 0L;
//        int delays = 0;
//        long excess = 0L;
//
//        long gameStartTime;
//        gameStartTime = System.nanoTime();
//        beforeTime = gameStartTime;
//        afterTime = System.nanoTime();
//
//        timeDiff = afterTime - beforeTime;
//        sleepTime = (PERIOD - timeDiff) - overSleepTime;
//
//        if (sleepTime > 0) {   // some time left in this cycle
//            try {
//              Thread.sleep(sleepTime/1000000L);  // nano -> ms
//            }
//            catch(InterruptedException ex){}
//            //overSleepTime = 0; // (J3DTimer.getValue() - afterTime) - sleepTime;
//            overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
//        }
//        else {    // sleepTime <= 0; the frame took longer than the period
//            excess -= sleepTime;  // store excess time value
//            overSleepTime = 0L;
//
//            if (++delays >= DELAYS_PER_YIELD) {
//              Thread.yield();   // give another thread a chance to run
//              delays = 0;
//            }
//        }
//
//          //beforeTime = 0; // J3DTimer.getValue();
//        beforeTime = System.nanoTime();
//    }

    public AudioManager getAudioManager(){
        return this.AM;
    }
    public void processKeyPress(KeyEvent e)
    {
        inputMan.setKeyDown(e.getKeyCode());
    }

    public void processKeyRelease(KeyEvent e)
    {
        inputMan.setKeyUp(e.getKeyCode());
    }

    public ImagesLoader getImgloader() {
        return imgloader;
    }

    public BufferStrategy getDoublebuf() {
        return doublebuf;
    }

    public BricksManager getBricksMan() {
        return bricksMan;
    }

    public RibbonsManager getRibsMan() {
        return ribsMan;
    }

    public InputManager getInputMan() {
        return inputMan;
    }

    public long getDeltaTime(){
        return deltaTime;
    }
    public int getWidth()
    {
        return gamescreen.getFRAME_WIDTH();
    }

    public int getHeight()
    {
        return gamescreen.getFRAME_HEIGHT();
    }

    public ImagesLoader getImgLdr()
    {
        return imgloader;
    }

    public float getGravity()
    {
        return GRAVITY;
    }

    public Player getPlayer()
    {
        return strider;
    }
	public HUD getHUD(){
		return hud;
	}

    public ClipsLoader getClipsLoader() {
        return clipsLoader;
    }
    
    public void setCurrentState(String name)
    {
        if(gamestates.containsKey(name)){
            this.currState = gamestates.get(name);

			currState.update();
			/* This update prevents the draw from drawing the data from the previous
			 * state.
			 */
        }
    }

	public void setUPDATE_SPEED(int UPDATE_SPEED) {
		this.UPDATE_SPEED = UPDATE_SPEED;
	}

	public int getUPDATE_SPEED() {
		return UPDATE_SPEED;
	}

	public EnemyManager getEnemyMgr()
	{
		return enemyMgr;
	}
}
