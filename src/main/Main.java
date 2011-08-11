
package main;

import Events.sEvents;
import GUI.GUIManager;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Input.sInput;
import Sound.sSound;
import org.newdawn.slick.*;
import States.Game.StateGame;
import States.Menu.StateMenu;
import States.Splash.StateSplash;
import States.Title.StateTitle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame
{
    public Main()
    {
        super("CarneCarne!");
    }
    
    public static void main(String[] arguments)
    {        
        try
        {            
            AppGameContainer app = new AppGameContainer(new Main());
            app.setIcon("icon.png");
            app.setDisplayMode(1280, 800, false);
            app.setShowFPS(false);
            app.setVSync(true);
            //app.setTargetFrameRate(60);
            app.start();            
        }
        catch(SlickException e)
        {
            e.printStackTrace();
        }
    }
    

//    @Override
//    protected URL getThemeURL() {
//        //boolean does = ResourceLoader.resourceExists("data/ui/simple.xml");
//        URL magic = Thread.currentThread().getContextClassLoader().getResource("ui/simple.xml");
//        return magic;
//    }

    BasicGameState mSplashState, mTitleState, mGameState, mMenuState;
    
    @Override
    public void initStatesList(GameContainer _gc) throws SlickException {  
        //_gc.setDefaultFont(sFontLoader.createFont("default"));                
        sGraphicsManager.init((AppGameContainer)_gc);
        sInput.init(_gc); 
        sEvents.init();
        sSound.init();
        sParticleManager.warmUp();
        GUIManager.setDefaultContext(_gc);
        
        //Splash: state1
        mSplashState = new StateSplash();
        addState(mSplashState);
        //title: state2
        mTitleState = new StateTitle();
        addState(mTitleState);
        //game: state3
        mGameState = new StateGame();
        addState(mGameState); 
        //menu: state4
        mMenuState = new StateMenu();
        addState(mMenuState); 
        
        //FIXME: should start on splash
        //enterState(2, null, new BlobbyTransition(new Color(0,0,0)));
        //StateGame.setGameType(StateGame.GameType.eRace, "RaceReloaded");
        enterState(2, null, null);
        
    }
}
