/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Sound.MovingSoundAnchor;
import Sound.SoundScape;
import Sound.sSound;
import World.sWorld;
import java.util.HashMap;
import java.util.Random;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class Broccoli extends AIEntity
{
    private float mHopTimmer;
    private float mTimer;
    private float mIdleTick;
    private boolean mActive;
    private Vec2 mTargetPos;
    private enum brocState
    {
        eIdle,
        eIdleOut,
        eIdleIn,
        //Hopping
        eJump,
        eAir,
        eLand,
        //Attacking
        eThreaten,
        eSubmerge,
        eExplode
    };
    private brocState mState;
    
    public Broccoli(iSkin _skin) 
    {
        super(_skin);
        mJumpTimer = 0;
        mMoveSpeed = 1;
        mIdleTick = 0;
        mAnimSpeed = 0.75f;
        mTouchingTile = null;
        mCurrentAnimation = "broc_1_idle";
        mSkin.activateSubSkin("broc_1_idle", false, mAnimSpeed);
        mHopTimmer = 0;
        mTimer = 0;
        mState = brocState.eIdle;
        mActive = false;
    }
    
    @Override
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    @Override
    public void update()
    {
        
        if(!mActive)
        {
            if(!(mState == brocState.eIdle))
            {
                mSkin.deactivateSubSkin(mCurrentAnimation);
                
                if (mBody.getPosition().x > mTargetPos.x)
                    mCurrentAnimation = "broc_1_idlein";
                else
                    mCurrentAnimation = "broc_1_idlein_right";
                
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);         
                mState = brocState.eIdle;
                mIdleTick = -1;
            }
            else if(mState == brocState.eIdle)
            {
                // mSkin.stopAt("broc_1_idlein",3);
                 if(!mSkin.isAnimating("broc_1_idlein") && !mSkin.isAnimating("broc_1_idlein_right"))
                 {
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    mSkin.deactivateSubSkin("broc_1_idlein_right");
                    // mSkin.stopAt("broc_1_idle", 6);
                    if(mIdleTick < 0)
                    {
                        if(!mSkin.isAnimating("broc_1_idle") && !mSkin.isAnimating("broc_1_idle_right"))
                        {
                            mSkin.deactivateSubSkin("broc_1_idle");
                            mSkin.deactivateSubSkin("broc_1_idle_right");
                            mSkin.deactivateSubSkin(mCurrentAnimation);
                            
                            if (mBody.getPosition().x > mTargetPos.x)
                                mCurrentAnimation = "broc_1_idle";
                            else
                                mCurrentAnimation = "broc_1_idle_right";
                            
                            mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                            Random rand = new Random();
                            mIdleTick = 1+(rand.nextInt()%6);
                        }
                    }
                    else
                    {
                        mIdleTick -= 0.016f;
                    }
                }
            }
        }
        mActive = false;
        super.update();
    }
    public void move(float _speed)
    {
        switch(mState)
        {
            case eIdle:
            {
                mSkin.deactivateSubSkin("broc_1_idlein");
                mSkin.deactivateSubSkin("broc_1_idle");
                mSkin.deactivateSubSkin("broc_1_idlein_right");
                mSkin.deactivateSubSkin("broc_1_idle_right");
                mSkin.deactivateSubSkin(mCurrentAnimation);
                
                if (mBody.getPosition().x > mTargetPos.x)
                    mCurrentAnimation = "broc_1_idleout";
                else
                    mCurrentAnimation = "broc_1_idleout_right";
                //mCurrentAnimation = "broc_1_idleout";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eIdleOut;
                break;
            }
            case eIdleOut:
            {
                mSkin.stopAt("broc_1_idleout",3);
                mSkin.stopAt("broc_1_idleout_right",3);
                if(!mSkin.isAnimating("broc_1_idleout") && !mSkin.isAnimating("broc_1_idleout_right"))
                {
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    mSkin.deactivateSubSkin("broc_1_idleout");
                     mSkin.deactivateSubSkin("broc_1_idlein_right");
                    mSkin.deactivateSubSkin("broc_1_idleout_right");
                    mSkin.deactivateSubSkin(mCurrentAnimation);
                    
                    if (mBody.getPosition().x > mTargetPos.x)
                        mCurrentAnimation = "broc_1_jump";
                    else
                        mCurrentAnimation = "broc_1_jump_right";
                    
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eJump;
                }
                break;
            }
            case eJump:
            {
                mSkin.stopAt("broc_2_land", 4);
                mSkin.stopAt("broc_2_land_right", 4);

                if(!mSkin.isAnimating("broc_2_land") && !mSkin.isAnimating("broc_2_land_right"))
                {
                    mSkin.deactivateSubSkin("broc_2_land");
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    mSkin.deactivateSubSkin("broc_1_idleout");
                    mSkin.deactivateSubSkin(mCurrentAnimation);
                    
                    if (mBody.getPosition().x > mTargetPos.x)
                        mCurrentAnimation = "broc_2_jump";
                    else
                        mCurrentAnimation = "broc_2_jump_right";
                    
                    //mCurrentAnimation = "broc_2_jump";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eAir;
                }
                break;
            }
            case eAir:
            {
                mSkin.stopAt("broc_2_jump",3);
                mSkin.stopAt("broc_2_jump_right",3);
                if(!mSkin.isAnimating("broc_2_jump") && !mSkin.isAnimating("broc_2_jump_right"))
                {
                    jump();
                    stopJumping();
                    mBody.applyLinearImpulse(new Vec2(_speed,0), mBody.getWorldCenter());
                    mSkin.deactivateSubSkin("broc_2_jump");
                    mSkin.deactivateSubSkin("broc_2_jump_right");
                    mSkin.deactivateSubSkin(mCurrentAnimation);
                    
                    if (mBody.getPosition().x > mTargetPos.x)
                        mCurrentAnimation = "broc_2_air";
                    else
                        mCurrentAnimation = "broc_2_air_right";
               
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eLand;
                }
                break;
            }
            case eLand:
            {
                if(!isAirBorn())
                {
                    mSkin.stopAt("broc_2_air",4);
                    mSkin.stopAt("broc_2_air_right",4);
                    if(!mSkin.isAnimating("broc_2_air") && !mSkin.isAnimating("broc_2_air_right"))
                    {
                        mSkin.deactivateSubSkin("broc_2_air");
                        mSkin.deactivateSubSkin("broc_2_air_right");
                        mSkin.deactivateSubSkin(mCurrentAnimation);
                        
                        if (mBody.getPosition().x > mTargetPos.x)
                            mCurrentAnimation = "broc_2_land";
                        else
                            mCurrentAnimation = "broc_2_land_right";
                        
                        mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                        mState = brocState.eJump;
                    }
                }
                break;
            }
        }
        mActive = true;
    }
    public void attack()
    {
        switch(mState)
        { 
            case eLand:
            {
                if(!isAirBorn())
                {
                    mSkin.stopAt("broc_2_air",4);
                    if(!mSkin.isAnimating("broc_2_air"))
                    {
                        mSkin.deactivateSubSkin("broc_2_air");
                        mCurrentAnimation = "broc_3_start";
                        mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                        mState = brocState.eThreaten;
                    } 
                }
                break;
            }
            case eJump:
            {
                mSkin.stopAt("broc_2_land",5);
                if(!mSkin.isAnimating("broc_2_land"))
                {
                    mSkin.deactivateSubSkin("broc_2_land");
                    mCurrentAnimation = "broc_3_start";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eThreaten;
                }
                break;
            }
            case eAir:
            {
                mSkin.stopAt("broc_2_jump",4);
                if(!mSkin.isAnimating("broc_2_jump"))
                {
                    mSkin.deactivateSubSkin("broc_2_jump");
                    mCurrentAnimation = "broc_3_start";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eThreaten;
                }
                break;
            }
            case eIdle:
            {
                if(!mSkin.isAnimating("broc_2_land"))
                {
                    mSkin.deactivateSubSkin(mCurrentAnimation);
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    mSkin.deactivateSubSkin("broc_1_idle");
                    mCurrentAnimation = "broc_1_idleout";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eIdleOut;
                }
                break;
            }
            case eIdleOut:
            {
                mSkin.stopAt("broc_1_idleout",3);
                if(!mSkin.isAnimating("broc_1_idleout"))
                {
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    mSkin.deactivateSubSkin("broc_1_idleout");
                    mCurrentAnimation = "broc_3_start";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eThreaten;
                }
                break;
            }
            case eThreaten:
            {
                mSkin.stopAt("broc_3_start",2);
                if(!mSkin.isAnimating("broc_3_start"))
                {
                    mSkin.deactivateSubSkin("broc_3_start");
                    mCurrentAnimation = "broc_3_mid";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eSubmerge;
                    sParticleManager.createSystem("MelonSDamageParticle", 
                            sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                            , 1);
                }
                break;
            }
            case eSubmerge:
            {
                mSkin.stopAt("broc_3_mid",2);
                if(!mSkin.isAnimating("broc_3_mid"))
                {
                    mSkin.deactivateSubSkin("broc_3_mid");
                    mCurrentAnimation = "broc_3_end";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eExplode;

                    /*ParticleSysBase sys = sParticleManager.createSystem("broccoliExplode1", 
                    sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                    , 0.001f);*/
                    /*sParticleManager.createSystem("broccoliExplode2", 
                    sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                    , 0.001f);//*/
                    /*ParticleSysBase sys = sParticleManager.createSystem("broccoliExplode", 
                        sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                        , 1);*/
                    //sys.setWind(10);
                    //sys.setAngularOffset(180);
                }
                break;
            }
            case eExplode:
            {
                if(!mSkin.isAnimating("broc_3_end"))
                {
                    mSkin.deactivateSubSkin(mCurrentAnimation);
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    HashMap params = new HashMap();
                    params.put("position", mBody.getPosition().add(new Vec2(0.5f,0.5f)));
                    params.put("radius", 5.0f);
                    params.put("duration", 30);
                    sEntityFactory.create("BroccoliExplosion", params);
                    sSound.playPositional(SoundScape.Sound.eBroccoliExplode, mBody.getPosition());
                    
                    ParticleSysBase sys = sParticleManager.createSystem("BroccoliExplode1", 
                        sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                        , 0.001f);
                    sParticleManager.createSystem("BroccoliExplode2", 
                        sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                        , 0.001f);
                    kill(CauseOfDeath.eMundane, this);
                    /*ParticleSysBase sys = sParticleManager.createSystem("MelonSDamageParticle", 
                        sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                        , 1);*/
                                ;//*/
                    //sys.setWind(40);
                    //sys.setAngularOffset(270);
                }
                break;

            }
        }
        mActive = true;        
    }
    
    public void updateTargetPos(Vec2 _pos)
    {
        mTargetPos = _pos;
    }

    public void moveUp()
    {
        
    }
    public void moveDown()
    {
        
    }

    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }//*/
}
