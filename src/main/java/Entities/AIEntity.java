/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import AI.iPathFinding.Command;
import Entities.AIEntityState.State;
import Events.AreaEvents.CheckPointZone;
import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Level.RootTile.AnimationType;
import Level.Tile;
import Level.sLevel.TileType;
import Sound.SoundScape;
import Sound.sSound;
import World.BreakableTongueAnchor;
import World.sWorld;
import java.util.Stack;
import javax.swing.JOptionPane;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
/**
 *
 * @author alasdair
 */
public class AIEntity extends Entity
{

    final static float root2 = (float) Math.sqrt(2);
    static int mContactParticleDelay = 10;
    protected iAIController mController;
    protected boolean mAllowRoll = false;
    protected int mJumpTimer;
    protected Tile mTouchingTile;
    protected Body mTouchingBody;
    protected Tile mLastTouchingTile;
    protected String mCurrentAnimation;
    protected float mAnimSpeed;
    protected Vec2 mFloorNormal;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected int mStunTimer;
    protected Command mCommand;
    protected AIEntityState mAIEntityState;
    protected int mContactParticleTimer = 0;

    public RevoluteJoint mJoint;
    
    /*public void changeState(State _newState)
    {
        mAIEntityState.changeState(_newState);
    }*/
    
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mAIEntityState = new AIEntityState(this);
        mJumpTimer = 0;
        mMoveSpeed = 1;
        mAnimSpeed = 1;
        mTouchingTile = null;
        mStunTimer = 0;
    }
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (getBody() != null)
        {
            sWorld.destroyBody(getBody());
            setBody(null);
            mController.destroy();
            mController = null;
            mAIEntityState.destroy();
            mAIEntityState = null;
        }
    }
    public void setController(iAIController _controller)
    {
        mController = _controller;
    }
    public iAIController getController()
    {
        return mController;
    }
    public void update()
    {
        mContactParticleTimer++;
        if (mStunTimer != 0)
        {
            mStunTimer--;
        }
        //dampen
        getBody().setAngularDamping(8);

        //count contacts
        int numContacts = 0;
        ContactEdge edgeCounter = getBody().m_contactList;
        while(edgeCounter != null)
        {
            numContacts++;
            edgeCounter = edgeCounter.next;
        }

        ContactEdge edge = getBody().m_contactList;
        int mTar = 0;
        int mIce = 0;
        int mJumpContacts = 0;
        mAllowRoll = false;
        if(mTouchingTile != null)
            mLastTouchingTile = mTouchingTile;
        mTouchingTile = null;

        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            boolean AtoB = true;
            if (other.m_body == getBody())
            {
                AtoB = false;
                other = edge.contact.m_fixtureB;
            }
            Vec2 collisionNorm = edge.contact.m_manifold.localNormal;
            float rot = other.getBody().getTransform().getAngle();
            collisionNorm.x = (float) (collisionNorm.x*Math.cos(rot) - collisionNorm.y*Math.sin(rot));
            collisionNorm.y = (float) (collisionNorm.x*Math.sin(rot) + collisionNorm.y*Math.cos(rot));
            if(AtoB == false)
            {
                collisionNorm.negateLocal();
            }
            collisionNorm.normalize();
            if (other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEnemy.ordinal())||
                other.m_filter.categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal()))
            {
                if(!other.isSensor() && edge.contact.isTouching())
                {
                    if(other.m_body.getPosition().sub(mBody.getPosition()).y > 0) //if below entity allow jump
                    {
                        mTouchingBody = other.m_body;
                        mTouchingTile = null;
                        mJumpContacts++;
                    }
                }
            }
            else
            {
                mTouchingBody = null;
            }
                

            //if (other.getUserData() != null)
            {

                if (other.getUserData() != null)
                {
                    TileType tileType = ((Tile)other.getUserData()).getTileType();
                    switch (tileType)
                    {
                        case eIce:
                            mIce++;
                            break;
                        case eTar:
                            if (((Tile)other.getUserData()).isOnFire())
                            {
                                kill(CauseOfDeath.eFire, other.getUserData());
                                return;
                            }
                            else
                            {
                                mTar++;
                            }
                            break;
                        case eSpikes:
                        {
                            if (edge.contact.isTouching())
                            {
                                if (Vec2.dot(collisionNorm, ((Tile)other.getUserData()).getRootTile().getSpikeNormal()) > 0.8f)
                                {
                                    kill(CauseOfDeath.eSpikes, other); 
                                    return;
                                }
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }

                if(collisionNorm.y > 1/root2) //up
                {
                    createContactParticle(collisionNorm);
                }
                else if(collisionNorm.y < - 0.9) //down
                {
                    if(edge.contact.isTouching())
                    {
                        if(((Tile)other.getUserData()) != null && !other.isSensor() )
                        {
                            mTouchingTile = ((Tile)other.getUserData());
                            if (!((Tile)other.getUserData()).getTileType().equals(TileType.eZoomzoom))
                                mJumpContacts++;
                            createContactParticle(collisionNorm);
                        }
                    }
                    mFloorNormal = collisionNorm.clone();
                }
                else if(collisionNorm.y < -0.15)// slopes -- (collisionNorm.y > 0.3) gives roof slopes
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        if(((Tile)other.getUserData()) != null)
                            mTouchingTile = ((Tile)other.getUserData());
                        mContactParticleDelay = 5;
                        createContactParticle(collisionNorm);
                        try
                        {
                            if (!((Tile)other.getUserData()).getTileType().equals(TileType.eZoomzoom))
                                mJumpContacts++; //allow jump on slopes
                        }
                        catch (NullPointerException e) // Some other shitty contact
                        {
                            
                        }
                        mAllowRoll = true;
                    }
                    mFloorNormal = collisionNorm.clone();
                }
                else
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        createContactParticle(collisionNorm);
                    }
                }
            }
            edge = edge.next;
        }
        mAIEntityState.update(mTar, mIce, mWaterHeight, mJumpContacts, getBody().getPosition().y + 1 - mWaterHeight);
        getBody().applyLinearImpulse(new Vec2(0,0.2f*getBody().getMass()), getBody().getWorldCenter());
        if (mJumpTimer != 0)
        {
            mJumpTimer--;
        }

        mController.update();
        subUpdate();
    }
    public void placeCheckPoint(CheckPointZone _checkPoint)
    {
    }
    protected void createContactParticle(Vec2 _dir)
    {
        createContactParticle(_dir, 60, 1.0f, 0.0f);
    }
    //this function will scale the effect linearly from the given speed by the given increment
    //increment is per unit of speed
    protected void createContactParticle(Vec2 _dir, float _minSpeed, float _startingScale, float _scaleInc)
    {
        if(mBody.getLinearVelocity().lengthSquared() > _minSpeed)
        {
            if(mContactParticleTimer > 10) //assumes 60fps
            {
                mContactParticleTimer = 0;
                ParticleSysBase sys = null;
                if (mTouchingTile == null)
                    sys = sParticleManager.createSystem("Cloud", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32)).add(_dir.mul(-32)), 1f);
                else
                    sys = sParticleManager.createSystem(mTouchingTile.getAnimationsName(AnimationType.eJump) + "Jump", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32)).add(_dir.mul(-32)), 1f);
                float angle = (float) Math.acos(Vec2.dot(_dir, new Vec2(0,-1)));
                if(_dir.x >= 0)
                {
                    try
                    {
                        sys.setAngularOffset(angle);
                    }
                    catch(NullPointerException _e)
                    {
                        _e.printStackTrace();
                    }
                }               
                else
                {
                    try
                    {
                        sys.setAngularOffset(angle);
                    }
                    catch(NullPointerException _e)
                    {
                        _e.printStackTrace();
                    }
                }
            }
        }
        //reset delay to default = 10
        mContactParticleDelay = 10;
    }
    protected void subUpdate()
    {
        if (mWaterHeight != 0)
        {
            buoyancy();
        }
    }
    protected void airControl(float _value)
    {
        getBody().applyLinearImpulse(new Vec2(0.8f*_value,0), getBody().getWorldCenter());
    }
    public void walk(float value)
    {
        mBody.m_fixtureList.m_friction = 5;
        switch (mAIEntityState.getState())
        {
            case eFalling:
            {
                mBody.m_fixtureList.m_friction = 1;
                // continue;
            }
            case eJumping:
            {
                airControl(value);
                break;
            }
            case eIdle: //fall through to standing (will break from state when moving)
            case eStanding:
            {
                if(mAllowRoll)
                {
                    mBody.m_fixtureList.m_friction = 20;
                    getBody().applyAngularImpulse(0.7f*value);
                }
                else
                {
                    getBody().applyLinearImpulse(new Vec2(1.1f*value,0),getBody().getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                getBody().applyLinearImpulse(new Vec2(0.5f*value,0),getBody().getWorldCenter());
                break;
            }
            case eIce:
            {
                Vec2 v = getBody().getLinearVelocity();
                if(value > 0 && v.x > 0)
                    getBody().applyLinearImpulse(new Vec2(0.3f*value,0),getBody().getWorldCenter());
                else if(value < 0 && v.x < 0)
                    getBody().applyLinearImpulse(new Vec2(0.3f*value,0),getBody().getWorldCenter());
                getBody().applyAngularImpulse(0.3f*value);
                break;
            }
            case eSwimming:
            {
                getBody().applyLinearImpulse(new Vec2(0.5f*value,0),getBody().getWorldCenter());
                getBody().applyAngularImpulse(0.5f*value);
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    public void walkLeft()
    {
        walk(-1f);
    }
    public void walkRight()
    {
        walk(1f);
    }
    public void jump()
    {
        jump(1);
    }
    public void jump(float scale)
    {
        float canJump = mAIEntityState.canJump(getBody().getLinearVelocity().y) * scale;
        if (canJump != 0.0f)
        {
            getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, canJump));
            mJumpTimer = mJumpReload;
            mAIEntityState.jump();
            if (!mAIEntityState.getState().equals(State.eSwimming))
            {
                try
                {
                    if(mTouchingTile != null)
                    {
                        sParticleManager.createSystem(mTouchingTile.getAnimationsName(AnimationType.eJump) + "Jump", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                        sSound.playPositional(SoundScape.Sound.ePlayerJump, mBody.getPosition(), mTouchingTile.getTileType());
                    }
                    else if(mTouchingBody != null)
                    {
                        getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, canJump * 0.8f));
                        mTouchingBody.applyLinearImpulse(new Vec2(0, -canJump *0.5f), mTouchingBody.getWorldCenter());
                        sParticleManager.createSystem("Cloud", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                        sSound.playPositional(SoundScape.Sound.ePlayerJump, mBody.getPosition(), TileType.eEmpty);
                    }
                    else if (mLastTouchingTile != null)
                    {
                        sParticleManager.createSystem(mLastTouchingTile.getAnimationsName(AnimationType.eJump) + "Jump", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                        sSound.playPositional(SoundScape.Sound.ePlayerJump, mBody.getPosition(), mLastTouchingTile.getTileType());
                    }       
                    else
                    {
                        sParticleManager.createSystem("Cloud", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                        sSound.playPositional(SoundScape.Sound.ePlayerJump, mBody.getPosition(), TileType.eEmpty);
                    }
                }
                catch (NullPointerException e)
                {
                    System.err.println("Null pointer rendering jump particle");
                }
            }
        }
    }
    public void stopJumping()
    {
        if(mAIEntityState.getState() == State.eJumping)
        {
            getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, getBody().getLinearVelocity().y * 0.3f));
            mAIEntityState.stopJumping();
        }        
    }
    
    public void stopIdle()
    {
        mAIEntityState.stopIdle();
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    
    public boolean isAirBorn()
    {
        switch(mAIEntityState.getState())
        {
            case eFalling:
            case eJumping:
                return true;
        }
        return false;
    }
    
    public boolean isDead()
    {
        return mAIEntityState.getState() == AIEntityState.State.eDead || mAIEntityState.getState() == AIEntityState.State.eRestartingRace;
    }
    public boolean isIdle()
    {
        return mAIEntityState.getState() == AIEntityState.State.eIdle;
    }
    
    public float setAnimation(String _animation)
    {
        if(!_animation.equals(mCurrentAnimation))
        {
            mSkin.deactivateSubSkin(mCurrentAnimation);
            mCurrentAnimation = _animation;
            return mSkin.activateSubSkin(_animation, false, mAnimSpeed);
        }
        else if(!mSkin.isAnimating(_animation))
        {
            mSkin.deactivateSubSkin(mCurrentAnimation);
            mCurrentAnimation = _animation;
            return mSkin.activateSubSkin(_animation, false, mAnimSpeed);
        }
        return 0;
    }
    private Stack<BreakableTongueAnchor> mAnchors = new Stack<BreakableTongueAnchor>();
    public void stun(Vec2 _direction)
    {
        mBody.applyLinearImpulse(_direction.mul(10.0f).mul(mBody.getMass()), getBody().getWorldCenter());
        breakTongueContacts();
    }
    void stun()
    {
        if (mStunTimer == 0)
        {
            mAIEntityState.stun();
            mStunTimer = 60 * 5;
        }
    }
    public void addAnchor(BreakableTongueAnchor _anchor)
    {
        mAnchors.add(_anchor);
    }
    public void removeAnchor(BreakableTongueAnchor _anchor)
    {
        mAnchors.remove(_anchor);
    }
    public void breakTongueContacts()
    {
        while (!mAnchors.isEmpty())
        {
            mAnchors.pop().breakContact();
        }
    }
    public boolean hasTongueContacts()
    {
        return !mAnchors.isEmpty();
    }

    public void crouch()
    {
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }

}