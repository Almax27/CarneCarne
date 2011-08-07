/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import AI.iAIController;
import Events.AreaEvents.AreaEvent;
import Events.AreaEvents.CheckPointZone;
import Events.AreaEvents.sAreaEvents;
import GUI.Components.GraphicalComponent;
import GUI.Components.Text;
import GUI.GUIManager;
import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import GUI.HUD.Reticle;
import GUI.HUD.Revolver;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.sSkinFactory;
import Level.sLevel.TileType;
import Score.RaceScoreTracker;
import Score.ScoreTracker;
import Sound.SoundScape;
import Sound.sSound;
import States.Game.Tutorial.IntroSection;
import Utils.sFontLoader;
import World.sWorld;
import java.util.HashMap;
import java.util.HashSet;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.Joint;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity
{
    protected   String              mSkinType = "mexican";
    protected   String              mBodyType = "bdy";
    private     CheckPointZone      mOriginalSpawnPoint;
    private     CheckPointZone      mCheckPoint;
    private     Vec2                mCheckPointPosition;
    public      IntroSection        mIntroSection;
    private     Joint               mDeathJoint;
    private     Vec2                mDirection;
    private     int                 mTeam;
    public      Reticle             mReticle;
    protected   Revolver            mRevolver;
    private     GraphicalComponent  mHUDArrow;
    private     GraphicalComponent  mTooltipWindow = null;
    private     Text                mTooltipText = null;
    private     int                 mTooltipTimer = 0;
    private     int                 mTooltipLife = 180; //FIXME: Assumes 60fps
    private     GraphicalComponent  mHUDFootball;
    private     Rectangle           mViewPort;
    private     int                 mRaceTimer;
    private     Football            mFootball;
    private     int                 mDeaths;
    private     boolean             mWasIReallyKilled = true;
    public      ScoreTracker        mScoreTracker;
    private     Integer             mGUIManager;
    private int mStickShakeDisplayTimer;
    private iSkin mStickShakeDisplay;
    
    public PlayerEntity(iSkin _skin, CheckPointZone _spawnPoint)
    {
        super(_skin);
        //create mGUIManager
        mGUIManager = GUIManager.create();
        mScoreTracker = new RaceScoreTracker(mGUIManager);
        mOriginalSpawnPoint = mCheckPoint = _spawnPoint;
        if (mCheckPoint != null)
            mCheckPointPosition = mCheckPoint.getPosition();
        mDeaths = mRaceTimer = 0;
        mReticle = new Reticle(this);
        
        mRevolver = new Revolver("ui/revolver.png", new Vector2f(1520,890)); //+28+38
        GUIManager.use(mGUIManager).addRootComponent(mRevolver);
        mRevolver.setDimentionsToImage();
        mRevolver.setAlignment(false, false);
        //mRevolver.setLocalTranslation(new Vector2f(mRevolver.getImageWidth()*0.7f,mRevolver.getImageWidth()*0.65f));
        mRevolver.setLocalTranslation(mRevolver.getOffset());
        
        mHUDArrow = new GraphicalComponent(sGraphicsManager.getGUIContext(), new Vector2f(761,75), new Vector2f(0,0));
        GUIManager.use(mGUIManager).addRootComponent(mHUDArrow);
        mHUDArrow.setImage("ui/HUD/Arrow.png");
        mHUDArrow.setDimentionsToImage();
        //mHUDArrow.setMaintainRatio(true); 
        
        mHUDFootball = new GraphicalComponent(sGraphicsManager.getGUIContext(), new Vector2f(0,0), new Vector2f(0,0));
        GUIManager.use(mGUIManager).addRootComponent(mHUDFootball);
        mHUDFootball.setImage("assets/characters/football.png");
        mHUDFootball.setDimentionsToImage();
        
        mTooltipWindow = new GraphicalComponent(sGraphicsManager.getGUIContext(), new Vector2f(50,50), new Vector2f(0,0));
        GUIManager.use(mGUIManager).addRootComponent(mTooltipWindow);
        mTooltipWindow.setImage("ui/TooltipWindow.png");
        mTooltipWindow.setDimentionsToImage();
        mTooltipWindow.setIsVisible(false);
        //mTooltipWindow.setMaintainRatio(true);
        
        mTooltipText = new Text(sGraphicsManager.getGUIContext(), sFontLoader.createFont("Trading Post Bold", 42), "No Text Defined", new Vector2f(50,45), false);
        mTooltipText.setDimensions(new Vector2f(315,130));
        mTooltipWindow.addChild(mTooltipText);
        mTooltipText.setColor(Color.yellow);
        mTooltipText.setIsVisible(false);
        mTooltipText.setIsTextSizedToFit(true);
        mTooltipText.setIsWrapped(true);
        //mTooltipText.setMaintainRatio(true);
        
        mTeam = 0;
        mStickShakeDisplayTimer = 0;
    }
    
    public void destroy() /// FIXME more memory leaks to cleanup in here
    {
        /// Purposefully not destroying the body
        mController.destroy();
        GUIManager.destroy(mGUIManager);
    }
    public void setTeam(int _team)
    {
        mTeam = _team;
    }
    @Override
    public void setController(iAIController _controller)
    {
        super.setController(_controller);
        HashMap params = new HashMap();
        params.put("ref", "Player" + ((PlayerInputController)mController).mPlayer);
    }
    
    public void setClip(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
        GUIManager.use(mGUIManager).setDimensions(new Vector2f(_viewPort.getWidth(), _viewPort.getHeight()));
    }
    
    public CheckPointZone getCheckPoint()
    {
        return mCheckPoint;
    }
    @Override
    public boolean isGrabbable()
    {
        return ((PlayerInputController)mController).isGrabbable();
    }
    
    @Override
    protected float calculateArea()
    {
        return super.calculateArea() * 4.0f;
    }
    
    public void resetRace()
    {
        if (mDeathJoint != null)
        {
            sWorld.destroyMouseJoint(mDeathJoint);
            mDeathJoint = null;
            Fixture fixture = getBody().getFixtureList();
            while (fixture != null)
            {
                fixture.setSensor(false);
                fixture = fixture.getNext();
            }
            mAIEntityState.unkill();
        }
        mCheckPoint = mOriginalSpawnPoint;
        mCheckPointPosition = mCheckPoint.getPosition();
        mRaceTimer = 0;
        mWasIReallyKilled = false;
        kill(CauseOfDeath.eMundane, null);
        mWasIReallyKilled = true;
        mAIEntityState.restartingRace();
        mScoreTracker.raceEnded();
    }
    public void getToStartingZone()
    {
        mCheckPoint = mOriginalSpawnPoint;
    }
    @Override
    public void placeCheckPoint(CheckPointZone _checkPoint)
    {
        if (mDeathJoint == null)
        {
            if (mCheckPoint == mOriginalSpawnPoint)
            {
                if (_checkPoint.getCheckpointNumber() == mCheckPoint.getCheckpointNumber()+1)
                {
                    mCheckPoint = _checkPoint;
                    mCheckPointPosition = getBody().getPosition().clone();
                    //sEvents.triggerDelayedEvent(new ShowDirectionEvent(this));
                }
            }
            else if (_checkPoint.getCheckpointNumber() > mCheckPoint.getCheckpointNumber())
            {
                mCheckPoint = _checkPoint;
                mCheckPointPosition = getBody().getPosition().clone();
                sSound.play(SoundScape.Sound.eCheckPointHit, 0);
                //sEvents.triggerDelayedEvent(new ShowDirectionEvent(this));
            }
        }
    }
    /*public int getScore()
    {
        return mScore;
    }*/
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (_killer != this)
        {
            if (mDeathJoint == null)
            {
                if (mWasIReallyKilled)
                {
                    sSound.play(SoundScape.Sound.eCheckPointHit, 0, _causeOfDeath);
                    mDeaths++;
                    mScoreTracker.score(ScoreTracker.ScoreEvent.eDied);
                    HashMap params = new HashMap();
                    switch (_causeOfDeath)
                    {
                        case eSpikes:
                        //{
                            Fixture killer = (Fixture)_killer;
                            params.put("attachment", killer);
                            /// Purposefully not breaking
                            case eFire:
                            case eAcid:
                            {
                                params.put("characterType", "Carne");
                                try
                                {
                                    params.put("killer", ((Fixture)_killer).getBody().getUserData());
                                }
                                catch (ClassCastException e)
                                {
                                    params.put("killer", _killer);
                                }
                                params.put("causeOfDeath", _causeOfDeath);
                                params.put("position", mBody.getPosition());
                                params.put("rotation", mBody.getAngle());
                                params.put("linearVelocity", mBody.getLinearVelocity());
                                params.put("angularVelocity", mBody.getAngularVelocity());
                                sEntityFactory.create("Carcass", params);
                                break;
                            }
                        //}
                        default:
                        {
                            break;
                        }
                    }
                }
                Fixture fixture = getBody().getFixtureList();
                while (fixture != null)
                {
                    fixture.setSensor(true);
                    fixture = fixture.getNext();
                }
                mDeathJoint = sWorld.createMouseJoint(mCheckPointPosition, getBody());
                ((PlayerInputController)mController).kill();
            }
            mAIEntityState.kill();
            ((PlayerInputController)mController).kill();
        }
    }
    
    boolean compareFloat(float a, float b, float epsilon)
    {
        return (a < b + epsilon && a > b - epsilon);
    }
    HashSet<AreaEvent> checkpointSet = new HashSet<AreaEvent>();
    @Override
    protected void subUpdate()
    {
        if(++mTooltipTimer > mTooltipLife)
        {
            mTooltipWindow.setIsVisible(false);
            mTooltipText.setIsVisible(false);
        }
        //FIXME: crash: out of memory
//        if (mWaterHeight != 0)
//            sSound.play(SoundScape.Sound.ePlayerUnderwater, 0);
//        else
//            sSound.stop(SoundScape.Sound.ePlayerUnderwater, 0);
        if (mCheckPoint != null && mCheckPoint.incrementRaceTimer()) /// FIXME - put this null check in as a quick fix for IntroMode
        {
            mRaceTimer++;
        }
        mReticle.update();
        
        HashSet<AreaEvent> newCheckPoints = new HashSet<AreaEvent>();
        AreaEvent event = sAreaEvents.collidePoint(mBody.getPosition());
        if (event != null)
        try
        {
            if (!checkpointSet.contains(event))
            {
                event.enter(this);
            }
            newCheckPoints.add((CheckPointZone)event);
        }
        catch (ClassCastException e) /// Null pointer and class cast
        {
        }
        for (AreaEvent checkPoint: checkpointSet)
        {
            if (!newCheckPoints.contains((CheckPointZone)checkPoint))
            {
                checkPoint.leave(this);
            }
        }
        checkpointSet = newCheckPoints;
        if (mDeathJoint != null)
        {//when player is within half a tile of checkpoint destroy joint
            if (compareFloat(getBody().getPosition().x, mCheckPointPosition.x, 0.5f) && compareFloat(getBody().getPosition().y, mCheckPointPosition.y, 0.5f))
            {
                mBody.setLinearVelocity(new Vec2(0,0));
                sWorld.destroyMouseJoint(mDeathJoint);
                mDeathJoint = null;
                Fixture fixture = getBody().getFixtureList();
                while (fixture != null)
                {
                    fixture.setSensor(false);
                    fixture = fixture.getNext();
                }
                mAIEntityState.unkill();
            }
        }
        else
        {
            super.subUpdate();
        }
        mScoreTracker.update();
        GUIManager.use(mGUIManager).update(16); //assumes 60fps
    }

    @Override
    public void walk(float value) 
    {
        float weight = ((PlayerInputController)mController).getWeight();
        super.walk(value / weight);
    }

    @Override
    public void jump() {
        float weight = ((PlayerInputController)mController).getWeight();
            super.jump(1/weight);
    }
    
    
    
    @Override
    public void stun(Vec2 _direction)
    {
        super.stun(_direction);
        ((PlayerInputController)mController).hammerHit();
    }
    @Override
    void stun()
    {
        if (mStunTimer == 0)
        {
            super.stun();
            ((PlayerInputController)mController).stun();
            mSkin.activateSubSkin("pea_stun_large", true, 0.0f);
            sParticleManager.createSystem("KO", mBody.getPosition().mul(64.0f).add(new Vec2(32,32)), 1);
        }
    }

    @Override
    public void render()
    {
        mSkin.setRotation(mBodyType, getBody().getAngle()*(180/(float)Math.PI));
        super.render();
        //mPlayerNumberDisplay.render(root2, root2);
    }
    
    public void renderHUD()
    {
        if (sGraphicsManager.getClip() == mViewPort) //only render HUD when rendering this body's cam
        {
            if (mCheckPoint != null)
            {
                mCheckPoint.renderRaceState(mRaceTimer);
                float rotation = 0;
                if(mCheckPoint.getNext() != null)
                {
                    Vec2 direction = mCheckPoint.getNext().getPosition().sub(getBody().getPosition());
                    direction.normalize();
                    rotation = (float)Math.atan2(direction.y, direction.x);
                }
                else if (mFootball != null)
                {
                    Vec2 nativeScreenDim = new Vec2(1500,900);
                    Vec2 location = nativeScreenDim.clone();
                    Vec2 direction = mFootball.getBody().getPosition().sub(getBody().getPosition());
                    float length = direction.normalize();
                    Vec2 screenDimensions = sGraphicsManager.getScreenDimensions();
                    Vec2 scale = screenDimensions.clone();
                    scale.x = direction.x / location.x;
                    scale.y = direction.y / location.y;
                    float dimScale = screenDimensions.y/screenDimensions.x;
                    if (scale.x * scale.x > scale.y * scale.y)
                    {
                        if (scale.x > 0.0f)
                        {
                            
                        }
                        else
                        {
                            location.x = 0;
                        }
                        location.y = location.y * ((direction.y) + 0.5f);
                    }
                    else
                    {
                        if (scale.y > 0.0f)
                        {
                            
                        }
                        else
                        {
                            location.y = 0;
                        }
                        location.x = location.x * ((direction.x * dimScale) + 0.5f);
                    }
                    float locationLength = sWorld.translateToPhysics(location).sub(mBody.getPosition()).normalize();
                    if (locationLength > length)
                    {
                        mHUDFootball.setIsVisible(false);
                    }
                    else
                    {
                        mHUDFootball.setIsVisible(true);
                    }
                    mHUDFootball.setLocalTranslation(new Vector2f(location.x, location.y).add(mHUDFootball.getDimensions()));
                    rotation = (float)Math.atan2(direction.y, direction.x);
                }
                mHUDArrow.setLocalRotation(rotation*180.0f/(float)Math.PI);
                    
               // sGraphicsManager.drawString("You have died " + mDeaths + " times", 0f, 0.1f);
            }
            if (mIntroSection != null)
                mIntroSection.render();
            
            if (hasTongueContacts())
            {
                mStickShakeDisplayTimer++;
                if (mStickShakeDisplayTimer > 30)
                {
                    if (mStickShakeDisplay == null)
                    {
                        HashMap params = new HashMap();
                        params.put("ref", "ShakeYourStick");
                        params.put("width", 112);
                        params.put("height", 151);
                        //params.put("duration", 2);
                        mStickShakeDisplay = sSkinFactory.create("animated", params);
                        mStickShakeDisplay.setIsLooping(true);
                        mStickShakeDisplay.setSpeed(0.05f);
                    }
                    mStickShakeDisplay.render(0, 0);
                }
            }
            
            mReticle.render(); //always render ontop
            
            GUIManager.use(mGUIManager).render(false);
        }
    }
    
    public void changeBodyType(TileType _type)
    {
        mSkin.deactivateSubSkin(mBodyType);
        switch(_type)
        {
            case eEdible:
                mSkin.activateSubSkin("edi", false, 0.0f);
                mBodyType = "edi";
                break;
            case eMelonFlesh:
                mSkin.activateSubSkin("wtr", false, 0.0f);
                mBodyType = "wtr";
                break;
            case eBouncy:
                mSkin.activateSubSkin("jly", false, 0.0f);
                mBodyType = "jly";
                break;
            case eGum:
                mSkin.activateSubSkin("gum", false, 0.0f);
                mBodyType = "gum";
                break;
            case eChilli:
                mSkin.activateSubSkin("spi", false, 0.0f);
                mBodyType = "spi";
                break;
            default:
            case eTileTypesMax:
                mSkin.activateSubSkin("bdy", false, 0.0f);
                mBodyType = "bdy";
                break;
        }
        if(mRevolver != null)
            mRevolver.setAmmo(_type);
    }
    
    public void setDirection(Vec2 _dir)
    {
        mDirection = _dir;
    }

    public int getRaceTimer()
    {
        return mRaceTimer;
    }
    @Override
    protected void airControl(float _value)
    {
        float max = 8;
        if(_value >= 0)
        {
            if(getBody().getLinearVelocity().x < max)
            {
                getBody().applyLinearImpulse(new Vec2(0.3f*_value,0), getBody().getWorldCenter());
            }
        }
        else //if(_value < 0)
        {
            if(getBody().getLinearVelocity().x > -max)
            {
                getBody().applyLinearImpulse(new Vec2(0.3f*_value,0), getBody().getWorldCenter());
            }
        }
    }

    public void setFootball(Football _football)
    {
        mFootball = _football;
    }

    public void displayTooltip(String _text, String _type)
    {
        mTooltipTimer = 0;
        //change tooltipwindow based on type
        mTooltipText.setTextString(_text);
        mTooltipWindow.setIsVisible(true);
        mTooltipText.setIsVisible(true);
        //throw new UnsupportedOperationException("Tooltip text: " + _text);
    }
}
