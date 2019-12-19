/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.sEntityFactory;
import Events.GenericEvent;
import Events.KeyDownEvent;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.GUIManager;
import GUI.HUD.sHud;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.sSkinFactory;
import Graphics.Sprites.sSpriteFactory;
import Graphics.sGraphicsManager;
import Input.sInput;
import Sound.sSound;
import States.Game.Adventure.AdventureMode;
import States.Game.FootballMode.FootballMode;
import States.Game.RaceMode.RaceMode;
import States.Game.Tutorial.IntroMode;
import States.Menu.StateMenu;
import States.StateChanger;
import World.sWorld;
import java.util.LinkedList;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

/**
 *
 * @author a203945
 */
public class StateGame extends BasicGameState implements iEventListener {

    public enum GameType
    {
        eAdventure,
        eRace,
        eFootball,
        eTutorial,
        eGameTypesMax
    }
    public static GameType getGameType()
    {
        return gameType;
    }
    private static GameType gameType = GameType.eGameTypesMax;
    private static Queue<String> levelProgression;
    private int mGUIRef;
    private static iGameMode mGameMode; 
    StateChanger mChangeToMenu;
    static private int mPlayers;
    static public Vec2 mMousePos = new Vec2(0,0);
    static boolean mInited = false;
    static long mEffectiveFPSTimer = 0;
    static int mEffectiveFPS = 0;
    static boolean mReEnterSelf = false;
    
    public static int getEffectiveFPS() {return mEffectiveFPS;}
    
    public StateGame()
    {
        levelProgression = new LinkedList<String>();
        levelProgression.add("NoLevel");
    }
    boolean die = false;//FIXME: DELETE
    public boolean trigger(iEvent _event) {
        //on player creation subscribe to their input
        if(_event.getType().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent) _event;
            sEvents.subscribeToEvent("KeyDownEvent"+"Q"+event.getPlayerID(), this);
        }
        else if(_event.getType().equals("KeyDownEvent"))
        {
            KeyDownEvent event = (KeyDownEvent) _event;
            if(event.getKey() == 'Q')
            {
                sEvents.triggerEvent(new GenericEvent("GamePaused"));
                StateMenu.setPlayerInControl(event.getPlayer());
                mChangeToMenu.setTransitions(new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));
                mChangeToMenu.run();
                sEvents.blockListener(this);
            }
        }
        return true;
    }
    
    public int getID()
    {
        return 3;
    }
    private float mDeltaTimeRemainder;
    public void update(GameContainer _gc, StateBasedGame _sbg, int _delta) throws SlickException 
    {       
        int fixedStep = 16;
        if(_delta > 2000)
            _delta = fixedStep;
        
        mDeltaTimeRemainder += _delta;
        
        while(mDeltaTimeRemainder > fixedStep)
        {
            mDeltaTimeRemainder -= fixedStep;
            _delta = fixedStep;
            
            if(mReEnterSelf)
            {
                mReEnterSelf = false;
                _sbg.enterState(getID(),null, new VerticalSplitTransition());
            }
            if(die) _gc.exit();

            //update sounds
            sSound.poll(_delta);

            //update input
            sInput.update(_delta);

            //update game
            if(mGameMode != null)
                mGameMode = mGameMode.update(_gc.getGraphics(), _delta);

            if(mGameMode == null) //this is to catch requests for exiting to title by the game mode
                _sbg.enterState(2,new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));

            //update particles
            sParticleManager.update(_delta);

            //update GUI
            GUIManager.get().update(_delta);
        }

    }
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException
    {
        if(!mInited) //need to catch this as transition uses render call before enter() is called
        {
            mInited = true;
            subInit();
        }
        Vec2 s = sGraphicsManager.getScreenDimensions();
        if(mGameMode!=null)
            mGameMode.render(_grphcs);
        
        //cleanup texture data
        //screen.flushPixelData();
        GUIManager.get().render(false);
        
        sGraphicsManager.renderDebugInfo();

    }
    @Override
    //callback for when the game enters this state
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {        
        System.gc();
        //set GUI to this
        GUIManager.set(mGUIRef);
        container.setMouseGrabbed(true);
        //sSound.playAsMusic("level1", true);
        sEvents.unblockListener(this);
        super.enter(container, game);
    }
    
    @Override
    //callback for when the game leaves this state
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        
        sEvents.blockListener(this);
        container.setMouseGrabbed(false);
        sSound.stop("level1");
        super.leave(container, game);
    }
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException
    {
        //initialise GUI
        mGUIRef = GUIManager.create(_gc);
        GUIManager.set(mGUIRef) ;
        
        //initialise sound
        sSound.loadFile("jump", "assets/sfx/fart_4.ogg");
        sSound.loadFile("tongueFire", "assets/sfx/tongueFire.ogg");
        
        //initialise game
        sHud.init();
        sEntityFactory.init();
        sSkinFactory.init();
        sSpriteFactory.init();
        sWorld.init();
        
        //mGameMode = new FootballMode();
        //mGameMode = new RaceMode();
        //mGameMode = new IntroMode();
        //mGameMode = new AdventureMode();

        //subscribe to events (must be done before further initialisation)  
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        
        //sLevel.loadLevel();
        
        //create state changers
        mChangeToMenu = new StateChanger(4, null, new BlobbyTransition(Color.black), _sbg);
    }

    private void subInit()
    {
        for(int i = 0; i < mPlayers; i++)
            sEvents.unsubscribeToEvent("KeyDownEvent"+"Q"+mPlayers, this);
        if(mGameMode != null)
            mGameMode.cleanup();
        switch(gameType)
        {
            case eAdventure:
            {
                mGameMode = new AdventureMode(levelProgression);
                break;
            }
            case eFootball:
            {
                mGameMode = new FootballMode(levelProgression.peek());
                break;
            }
            case eRace:
            {
                //mGameMode = new IntroMode(levelRef);
                mGameMode = new RaceMode(levelProgression);
                break;
            }    
            case eTutorial:
            {
                mGameMode = new IntroMode(levelProgression);
                break;
            }
        }
    }
    public static void setGameType(GameType _type, Queue<String> _levelProgression)
    {
        mInited = false;
        gameType = _type;
        levelProgression = _levelProgression;
    }
    public static void setGameType(GameType _type, String _singleLevel)
    {
        mInited = false;
        gameType = _type;
        levelProgression = new LinkedList<String>();
        levelProgression.add(_singleLevel);
    }
    public static void resetCurrentMode()
    {
        mInited = false;
    }
    public static void reEnterSelf()
    {
        mReEnterSelf = true;
    }
}
