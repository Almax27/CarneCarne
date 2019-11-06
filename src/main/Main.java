
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
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.ResourceLoader;

public class Main extends StateBasedGame
{
    public Main()
    {
        super("CarneCarne!");
    }
    
    public static void main(String[] arguments) throws IOException
    {   
        List<String> argsList = Arrays.asList(arguments);

        Path currentRelativePath = Paths.get("");
        String appRoot = currentRelativePath.toAbsolutePath().toString();
        
        FileSystemLocation loc = new FileSystemLocation(new File(appRoot + "/data"));
        ResourceLoader.addResourceLocation(loc);
        
        if(!argsList.contains("nologfile"))
        {
            try
            {
                String logFilePath = appRoot + "/DebugOut.log";
                System.out.println("Creating log file: " + logFilePath);

                File outputFile = new File(logFilePath);
                if(outputFile.exists())
                    outputFile.delete();

                outputFile.createNewFile();
                
                PrintStream fileStream = new PrintStream(outputFile);
                System.setOut(fileStream);
                System.setErr(fileStream);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Starting Game");
        
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
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Closing Game");
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
