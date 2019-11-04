
/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import GUI.Components.Button;
import GUI.Components.Effects.AnimatedEffect;
import GUI.Components.GraphicalComponent;
import GUI.Components.ScrollableComponent;
import GUI.Components.TextField;
import GUI.Components.ToggleButton;
import GUI.GUIManager;
import Graphics.sGraphicsManager;
import Input.sInput;
import Sound.sSound;
import States.Game.StateGame;
import States.Game.sGameStateInfo;
import Utils.sFontLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author Aaron
 */
public class StateTitle extends BasicGameState 
{
   // @Override
    public int getID() {
       return 2;
    }
    
    enum MenuState
    {
        eCenter,
        eLeft,
        eRight
    }

    GameContainer cont = null;
    boolean inited = false;
    Integer mGUIManagerGlobal = null;
    Integer mCurrentGUIState = null;
    Integer mCurrentLeftGUIState = null;
    Integer mGUIManagerCenterMain = null;
    Integer mGUIManagerLeftTour = null;
    Integer mGUIManagerLeftAdventure = null;
    Integer mGUIManagerLeftRace = null;
    Integer mGUIManagerLeftFootball = null;
    Integer mGUIManagerLeftOptions = null;
    Integer mGUIManagerRight = null;
    
    
    @Override
    public void enter(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException 
    {
        System.gc();
        cont = _gc;
        if(!inited) //we do this here so if the state is not used it is not inited
        {
            inited = true;
            mGUIManagerGlobal = GUIManager.create(_gc);
            mGUIManagerCenterMain = GUIManager.create(_gc);
            mGUIManagerLeftTour = GUIManager.create(_gc);
            mGUIManagerLeftAdventure = GUIManager.create(_gc);
            mGUIManagerLeftRace = GUIManager.create(_gc);
            mGUIManagerLeftFootball = GUIManager.create(_gc);
            mGUIManagerLeftOptions = GUIManager.create(_gc);
            mGUIManagerRight = GUIManager.create(_gc);
            mCurrentGUIState = mGUIManagerCenterMain;
            mCurrentLeftGUIState = mGUIManagerLeftOptions;

            //initalise music
            //sSound.loadFile("menu1", "assets/music/Menu1.ogg");

            //initialise fonts
            mUIFont = sFontLoader.createFont("title",72);
            mUIFont.setPaddingAdvanceX(-2);
            mInputFont = sFontLoader.createFont("menu", 72, false, true);

            //initialise background & foreground
            mBackground = new GraphicalComponent(_gc);
            mBackground.setImage("ui/title/bg.png");
            mBackground.setDimentionsToImage();
            mForeground = new GraphicalComponent(_gc);
            mForeground.setImage("ui/title/fg.png");
            mForeground.setDimentionsToImage();

            //calculate scale relative to background and screen size
            Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
            mScale = screen.x/mBackground.getWidth();

            //initialise logo
            mLogo = new GraphicalComponent(_gc, new Vector2f(1690.5f,-470f), new Vector2f());
            mLogo.setImage("ui/title/logo.png");
            mLogo.setDimentionsToImage();

            //initialise animations
            mCarrotAnimation = new GraphicalComponent(_gc, new Vector2f(1200,10), new Vector2f(170, 140));
            mCarrotAnimation.mEffectLayer.pushEffect(new AnimatedEffect("ui/title/car_ss.png", 170, 140, 41));
            mBrocAnimation = new GraphicalComponent(_gc, new Vector2f(3453-60,10), new Vector2f(23, 39));
            mBrocAnimation.mEffectLayer.pushEffect(new AnimatedEffect("ui/title/broc_ss.png", 23, 39, 41));

            //initalise paralax
            Vector2f paralaxDim = new Vector2f(screen.x/mScale,400);
            Vector2f paralaxPos = new Vector2f(0,0);
            //FIXME: want this to be a child to root, need to implement scaling compoents in heirarchy
            mParalax0 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax0.setAbsScroll(3045,8);
           // mParalax0.setImage("ui/title/p0.png");
            mParalax1 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax1.setAbsScroll(1773,0);
           // mParalax1.setImage("ui/title/p1.png");
            mParalax2 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax2.setAbsScroll(129,86);
            //mParalax2.setImage("ui/title/p2.png");

            //add carrot and broc animations to the middle paralax layer
            //mParalax1.addChild(mCarrotAnimation);
            //mParalax1.addChild(mBrocAnimation);
            mParalax0.addChild(mLogo);
            
            //create screenshot stuff
            Vector2f shPos = new Vector2f(750,-350);
            mScreenShotWindow = new GraphicalComponent(cont, new Vector2f(shPos.x,shPos.y), new Vector2f());
            mScreenShotWindow.setImage("ui/title/screenShotWindow.png");
            mScreenShot = new GraphicalComponent(cont, new Vector2f(100,90), new Vector2f());
            
            mParalax0.addChild(mScreenShotWindow);
            mScreenShotWindow.addChild(mScreenShot);

            //initialise buttons
            Vector2f buttonDim = new Vector2f(200/mScale,50/mScale);
//            mNameField = new TextField(_gc, mInputFont, 0, 0, (int)buttonDim.x, (int)buttonDim.y);
//            mNameField.setMaxLength(15);
//            mNameField.setDefaultText("Name");
//            mNameField.setTextColor(new Color(0,0,0));
//            mNameField.setSelectedTextColor(new Color(187,139,44));
            
            mTourButton = new Button(_gc, new Vector2f(), buttonDim);
            mTourButton.addText(_gc, mUIFont, "TOur MOdE", true);
            mAdventureButton = new Button(_gc, new Vector2f(), buttonDim);
            mAdventureButton.addText(_gc, mUIFont, "AdvEnturE MOdE", true); //uppercase 'e' 's' because lowercase is crap
            mRaceButton = new Button(_gc, new Vector2f(), buttonDim);
            mRaceButton.addText(_gc, mUIFont, "RacE MOdE", true);
            mFootballButton = new Button(_gc, new Vector2f(), buttonDim);
            mFootballButton.addText(_gc, mUIFont, "FOOtball MOdE", true); // lowercase 'o' is crap
            mOptionsButton = new Button(_gc, new Vector2f(), buttonDim);
            mOptionsButton.addText(_gc, mUIFont, "OptiOnS", true); 
            mHighScoresButton = new Button(_gc, new Vector2f(), buttonDim);
            mHighScoresButton.addText(_gc, mUIFont, "HighScorES", true);
            mQuitButton = new Button(_gc, new Vector2f(), buttonDim);
            mQuitButton.addText(_gc, mUIFont, "Quit", true);     
            
            mParticlesToggle = new ToggleButton(_gc, new Vector2f(), buttonDim, true);
            mParticlesToggle.addText(_gc, mUIFont, "ParticlES On", true);
            mParticlesToggle.setToggleText("ParticlES On", "ParticlES Off");
            mParticlesToggle.setLocalTranslation(new Vector2f(400 - mParticlesToggle.getWidth()*0.5f,-300));
            
            mLightingToggle = new ToggleButton(_gc, new Vector2f(), buttonDim, true);
            mLightingToggle.addText(_gc, mUIFont, "Lighting On", true);
            mLightingToggle.setToggleText("Lighting On", "Lighting Off");
            mLightingToggle.setLocalTranslation(new Vector2f(400 - mLightingToggle.getWidth()*0.5f,-225));
            
            mVSyncToggle = new ToggleButton(_gc, new Vector2f(), buttonDim, true);
            mVSyncToggle.addText(_gc, mUIFont, "V Sync On", true);
            mVSyncToggle.setToggleText("V Sync On", "V Sync Off");
            mVSyncToggle.setLocalTranslation(new Vector2f(400 - mVSyncToggle.getWidth()*0.5f,-150));
            
            mPlayerCountButton = new Button(_gc, new Vector2f(), buttonDim);
            mPlayerCountButton.addText(_gc, mInputFont, "Player Count: " + Integer.toString(sGameStateInfo.mPlayerCount), true);
            mPlayerCountButton.setLocalTranslation(new Vector2f(400 - mPlayerCountButton.getWidth()*0.5f,-400));
            mPlayerCountButton.setIsVisible(false);
            
            mBack = new Button(_gc, new Vector2f(), buttonDim);
            mBack.addText(_gc, mInputFont, "BAck", true); //lowercase 'a' is crap
            mBack.setLocalTranslation(new Vector2f(400 - mBack.getWidth()*0.5f,-50));
            
            mParalax0.addChild(mParticlesToggle);
            mParalax0.addChild(mLightingToggle);
            mParalax0.addChild(mVSyncToggle);
            mParalax0.addChild(mPlayerCountButton);
            mParalax0.addChild(mBack);
            
            mLogo.addChild(mTourButton);
            mLogo.addChild(mAdventureButton);
            mLogo.addChild(mRaceButton);
            mLogo.addChild(mFootballButton);
            mLogo.addChild(mOptionsButton);
            mLogo.addChild(mHighScoresButton);
            mLogo.addChild(mQuitButton);
            
            //add to GUIManager in render order
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mBackground);
            
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax2);
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax1);
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax0);
            
//            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mNameField);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mTourButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mAdventureButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mRaceButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mFootballButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mOptionsButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mHighScoresButton);
            GUIManager.use(mGUIManagerCenterMain).addSelectable(mQuitButton);
            
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mForeground);
            
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mParticlesToggle);
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mLightingToggle);
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mVSyncToggle);
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mBack);
            
            GUIManager.use(mGUIManagerLeftTour).addSelectable(mBack);
            GUIManager.use(mGUIManagerLeftAdventure).addSelectable(mBack);
            GUIManager.use(mGUIManagerLeftRace).addSelectable(mBack);
            GUIManager.use(mGUIManagerLeftFootball).addSelectable(mBack);
            
            GUIManager.use(mGUIManagerLeftTour).addSelectable(mPlayerCountButton);
            GUIManager.use(mGUIManagerLeftRace).addSelectable(mPlayerCountButton);
            GUIManager.use(mGUIManagerLeftFootball).addSelectable(mPlayerCountButton);
            
            calcUI();

            mTourButton.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftTour).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mScreenShotWindow.setIsVisible(false);
                    mScreenShot.setIsVisible(false);
                    mCurrentLeftGUIState = mGUIManagerLeftTour;
                }
            });
            mAdventureButton.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftAdventure).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mScreenShotWindow.setIsVisible(true);
                    mScreenShot.setIsVisible(true);
                    mCurrentLeftGUIState = mGUIManagerLeftAdventure;
                }
            });
            mRaceButton.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftRace).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mScreenShotWindow.setIsVisible(true);
                    mScreenShot.setIsVisible(true);
                    mCurrentLeftGUIState = mGUIManagerLeftRace;
                }
            });
            mFootballButton.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftFootball).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mScreenShotWindow.setIsVisible(true);
                    mScreenShot.setIsVisible(true);
                    mCurrentLeftGUIState = mGUIManagerLeftFootball;
                }
            });
            mOptionsButton.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftOptions).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mScreenShotWindow.setIsVisible(false);
                    mScreenShot.setIsVisible(false);
                    mCurrentLeftGUIState = mGUIManagerLeftOptions;
                }
            });
            mHighScoresButton.setSelectedCallback(new Runnable() {
                public void run() {
                    //mState = MenuState.eRight; //FIXME
                }
            });
            mQuitButton.setSelectedCallback(new Runnable() {
                public void run() {
                    _gc.exit();
                }
            });
            
            mParticlesToggle.setOnCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowParticles(true);}});
            mParticlesToggle.setOffCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowParticles(false);}});
            
            mLightingToggle.setOnCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowShaders(true);}});
            mLightingToggle.setOffCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowShaders(false);}});
            
            mVSyncToggle.setOnCallback(new Runnable() { public void run() {
                    _gc.setVSync(true);}});
            mVSyncToggle.setOffCallback(new Runnable() { public void run() {
                    _gc.setVSync(false);}});
            
            mPlayerCountButton.setSelectedCallback(new Runnable() { public void run() 
            {
                sGameStateInfo.mPlayerCount++;
                if(sGameStateInfo.mPlayerCount > 4)
                    sGameStateInfo.mPlayerCount = 1;
                mPlayerCountButton.addText(_gc, mInputFont, "Player Count: " + Integer.toString(sGameStateInfo.mPlayerCount), true);  
            }});
            
            mBack.setSelectedCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eCenter;
                }
            });
            mBack.setOnHoverCallback(new Runnable() {public void run() {
                    mScreenShot.setImage(null);
                }});
            
            initLevelLists(_sbg);
        }
        
        GUIManager.use(mCurrentGUIState).setAcceptingInput(true);
        GUIManager.use(mGUIManagerCenterMain).gotoFirstSelectable();
        //container.setMouseCursor("ui/title/mouse.png", 0, 62); //FIXME: break in fullscreen
        //sSound.playAsMusic("menu1", true);
        super.enter(_gc, _sbg);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        container.setDefaultMouseCursor();
        //sSound.stop("menu1");
        GUIManager.use(mCurrentGUIState).setAcceptingInput(false);
        sInput.update(16);
    }
    
    
    GraphicalComponent mBackground = null, mForeground = null;
    GraphicalComponent mLogo = null;
    ScrollableComponent mParalax0 = null;
    ScrollableComponent mParalax1 = null;
    ScrollableComponent mParalax2 = null;
    GraphicalComponent mCarrotAnimation = null;
    GraphicalComponent mBrocAnimation = null;
    //TextField mNameField = null;
    GraphicalComponent mScreenShotWindow = null;
    GraphicalComponent mScreenShot = null;
    Button mTourButton = null;
    Button mAdventureButton = null;
    Button mRaceButton = null;
    Button mFootballButton = null;
    Button mOptionsButton = null;
    Button mHighScoresButton = null;
    Button mQuitButton = null;
    Button mPlayerCountButton = null;
    ToggleButton mParticlesToggle = null;
    ToggleButton mLightingToggle = null;
    ToggleButton mVSyncToggle = null;
    ToggleButton mFullScreenButton = null;
    Button mBack = null;
    float mScale = 1.0f;
    float mOffset = 0.0f;
    MenuState mState = MenuState.eCenter;
    UnicodeFont mUIFont = null;
    UnicodeFont mInputFont = null;
    
    ArrayList<Button> mAdventureLevels = new ArrayList<Button>();
    ArrayList<Button> mRaceLevels = new ArrayList<Button>();
    ArrayList<Button> mFootballLevels = new ArrayList<Button>();

    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        
        
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        if(mGUIManagerGlobal != null)
            GUIManager.use(mGUIManagerGlobal).render(false);
        if(mGUIManagerCenterMain != null)
            GUIManager.use(mGUIManagerCenterMain).render(false);
        if(mGUIManagerRight != null)
            GUIManager.use(mGUIManagerRight).render(false);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException 
    {
        sSound.poll(_i);
        sInput.update(_i);
        Input input = _gc.getInput();
        if(input.isKeyDown(Input.KEY_F11))
            sGraphicsManager.toggleFullscreen();  
        
        updateMenuState();
        
        if(mGUIManagerGlobal != null)
        {
            GUIManager.use(mGUIManagerGlobal).update(_i);
        }
        if(mCurrentGUIState != null)
        {
            GUIManager.use(mCurrentGUIState).update(_i);
        }
    }
    private void changeGUIState(int _newState)
    {
        if(mCurrentGUIState == _newState)
            return;
        
        GUIManager.use(mCurrentGUIState).setAcceptingInput(false);
        GUIManager.use(_newState).setAcceptingInput(true);
        mCurrentGUIState = _newState;
    }
    protected void updateMenuState()
    {
        float scrollTime = 0.75f;
        switch(mState)
        {
            case eLeft:
            {
                changeGUIState(mCurrentLeftGUIState);
                mParalax0.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                break;
            }
            case eCenter:
            {
                changeGUIState(mGUIManagerCenterMain);
                mParalax0.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                break;
            }
            case eRight:
            {
                changeGUIState(mGUIManagerRight);
                mParalax0.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                break;
            }
        }
    }
    //calculates the positions relative to the background
    protected void calcUI()
    {
        //calc scale and offset values
        Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
        //scale by x to fit but keep aspect ratio using offset
        mScale = screen.x/mBackground.getWidth();
        mOffset = (screen.y - mBackground.getHeight()*mScale) * 0.5f;
        Vector2f vOffset = new Vector2f(0, mOffset);
        
        mBackground.setLocalTranslation(vOffset);
        mForeground.setLocalTranslation(vOffset);
        
        //Vector2f buttonPos = new Vector2f(mBackground.getWidth() * 0.6f, mOffset + (mBackground.getHeight() * 0.25f));
        Vector2f buttonPos = new Vector2f(1000,120);
        int buttonOffset = (int)75;
        
        //mNameField.setLocalTranslation(new Vector2f(buttonPos.x - mNameField.getWidth()*0.5f, buttonPos.y));
        
        mTourButton.setLocalTranslation(new Vector2f(buttonPos.x - mTourButton.getWidth()*0.5f, buttonPos.y + 0*buttonOffset + 10)); //constant to provide extra offset for namefield
        mTourButton.setDimensionsToText();
        
        mAdventureButton.setLocalTranslation(new Vector2f(buttonPos.x - mAdventureButton.getWidth()*0.5f, buttonPos.y + buttonOffset + 10)); //constant to provide extra offset for namefield
        mAdventureButton.setDimensionsToText();
        
        mRaceButton.setLocalTranslation(new Vector2f(buttonPos.x - mRaceButton.getWidth()*0.5f, buttonPos.y + 2*buttonOffset));
        mRaceButton.setDimensionsToText();
        
        mFootballButton.setLocalTranslation(new Vector2f(buttonPos.x - mFootballButton.getWidth()*0.5f, buttonPos.y + 3*buttonOffset - 10));
        mFootballButton.setDimensionsToText();
        
        mOptionsButton.setLocalTranslation(new Vector2f(buttonPos.x - mOptionsButton.getWidth()*0.5f, buttonPos.y + 4*buttonOffset - 20));
        mOptionsButton.setDimensionsToText();
        
        mHighScoresButton.setLocalTranslation(new Vector2f(buttonPos.x - mHighScoresButton.getWidth()*0.5f, buttonPos.y + 5*buttonOffset - 30)); //FIXME: booooooooooodge
        mHighScoresButton.setDimensionsToText();
        
        mQuitButton.setLocalTranslation(new Vector2f(buttonPos.x - mQuitButton.getWidth()*0.5f, buttonPos.y + 6*buttonOffset - 40)); //FIXME: booooooooooodge
        mQuitButton.setDimensionsToText();
        
        float paralaxOffset = -34.0f; //tweak
        mParalax0.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax0.getImageHeight()) + paralaxOffset));
        mParalax1.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax1.getImageHeight()) + paralaxOffset));
        mParalax2.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax2.getImageHeight()) + paralaxOffset));
    }
    
    private void initLevelLists(final StateBasedGame _sbg)
    {
        List<String> tourModeRef = Arrays.asList("Adventure tour", "Race tour");
        final List<String> singlePlayerLevelProgression = Arrays.asList("Ripples","Nom");
        final List<String> raceLevelProgression = Arrays.asList("tutorial","Race Reloaded","Ice Race", "Big Loop");
        final StateGame.GameType tourModeGameTypes[] = new StateGame.GameType[2];
        tourModeGameTypes[0] = StateGame.GameType.eAdventure;
        tourModeGameTypes[1] = StateGame.GameType.eTutorial;
        final String tourModeLevelProgressions[][] = new String[2][];
        tourModeLevelProgressions[0] = (String[])singlePlayerLevelProgression.toArray();
        tourModeLevelProgressions[1] = (String[])raceLevelProgression.toArray();
        
        List<String> adventureLevelRefs = Arrays.asList("Ripples","Nom");
        List<String> raceLevelRefs = Arrays.asList("Race Reloaded","Ice Race", "Big Loop");
        List<String> footballLevelRefs = Arrays.asList("The Match");
        
        UnicodeFont levelListFont = sFontLoader.createFont("title", 52);
        int step = 0;
        for(String ref : tourModeRef)
        {
            //create button
            Button button = new Button(cont, new Vector2f(), new Vector2f());
            button.addText(cont, levelListFont, ref.toUpperCase(), true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            button.setLocalTranslation(new Vector2f(400 - button.getWidth()*0.5f, -300 + (60*step)));
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftTour).addSelectable(button);
            mAdventureLevels.add(button);
            //set up call back to load level
            final Queue<String> levelProgression = new LinkedList<String>();
            levelProgression.addAll(Arrays.asList(tourModeLevelProgressions[step]));
            final StateGame.GameType gameType = tourModeGameTypes[step];
            button.setSelectedCallback(new StartLevel("Level1") {public void run()
            {
                    if(gameType == StateGame.GameType.eAdventure) 
                        sGameStateInfo.mPlayerCount = 1;
                    StateGame.setGameType(gameType, levelProgression);
                    _sbg.enterState(3, new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));
                }});
            step++;
        }
        step = 0;
        for(String ref : adventureLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(), new Vector2f());
            button.addText(cont, levelListFont, ref.toUpperCase(), true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            button.setLocalTranslation(new Vector2f(400 - button.getWidth()*0.5f, -300 + (60*step)));
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftAdventure).addSelectable(button);
            mAdventureLevels.add(button);
            //set up call back to load level
            button.setSelectedCallback(new StartLevel(ref) {public void run() {
                    sGameStateInfo.mPlayerCount = 1;
                    StateGame.setGameType(StateGame.GameType.eAdventure, mRef);
                    _sbg.enterState(3, new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));
                }});
            button.setOnHoverCallback(new StartLevel(ref) {public void run() {
                    mScreenShot.setImage("ui/title/screenShots/"+mRef+".png");
                }});
            step++;
        }
        step = 0;
        for(String ref : raceLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(), new Vector2f());
            button.addText(cont, levelListFont, ref.toUpperCase(), true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            button.setLocalTranslation(new Vector2f(400 - button.getWidth()*0.5f, -300 + (60*step)));
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftRace).addSelectable(button);
            mRaceLevels.add(button);
            //set up call back to load level
            button.setSelectedCallback(new StartLevel(ref) {public void run() {
                    StateGame.setGameType(StateGame.GameType.eRace, mRef);
                    _sbg.enterState(3, new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));
                }});
            button.setOnHoverCallback(new StartLevel(ref) {public void run() {
                    mScreenShot.setImage("ui/title/screenShots/"+mRef+".png");
                }});
            step++;
        }
        step = 0;
        for(String ref : footballLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(), new Vector2f());
            button.addText(cont, levelListFont, ref.toUpperCase(), true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            button.setLocalTranslation(new Vector2f(400 - button.getWidth()*0.5f, -300 + (60*step)));
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftFootball).addSelectable(button);
            mFootballLevels.add(button);
            //set up call back to load level
            button.setSelectedCallback(new StartLevel(ref) {public void run() {
                    StateGame.setGameType(StateGame.GameType.eFootball, mRef);
                    _sbg.enterState(3, new FadeOutTransition(Color.black,100), new FadeInTransition(Color.black,100));
                }});
            button.setOnHoverCallback(new StartLevel(ref) {public void run() {
                    mScreenShot.setImage("ui/title/screenShots/"+mRef+".png");
                }});
            step++;
        }
    }
    
    
    
}

abstract class StartLevel implements Runnable
{
    StartLevel(String _ref)
    {
        mRef = _ref;
    }
    String mRef = "NoLevel";

    abstract public void run();
}