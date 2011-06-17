/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Events.MouseMoveEvent;
import Entities.AIEntity;
import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Physics.sPhysics;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController implements iAIController, iEventListener{

    //protected to allow TongueStateMachine access
    protected AIEntity mEntity; 
    private TongueStateMachine mTongueState;
    private String mFaceDirAnim;
    private float mTongueAngle = 0;
    private Vec2 mCursorPos = new Vec2(0,0);
    public PlayerInputController(AIEntity _entity)
    {
        mEntity = _entity;
        mFaceDirAnim = "e";
        sEvents.subscribeToEvent("KeyDownEvent"+'w', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'a', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'s', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'d', this);
        sEvents.subscribeToEvent("MapClickEvent", this);
        sEvents.subscribeToEvent("MouseMoveEvent", this);
        mTongueState = new TongueStateMachine(this);
    }
    
    public void update()
    {
        mTongueState.tick(mEntity);
    }
    
    public boolean grabBlock(Vec2 _position)
    {
        return sPhysics.rayCastTiles(mEntity.mBody.getPosition(),_position);
    }
    public void spitBlock(Vec2 _position)
    {
        HashMap parameters = new HashMap();
        Vec2 direction = _position.sub( mEntity.mBody.getPosition());
        direction.normalize();;
        parameters.put("velocity", direction.mul(10.0f));
        parameters.put("position", mEntity.mBody.getPosition());
        sEntityFactory.create("SpatBlock", parameters); 
    }

    public void trigger(iEvent _event)
    {
        if (_event.getType().equals("KeyDownEvent"))
        {
            KeyDownEvent event = (KeyDownEvent)_event;
            switch (event.getKey())
            {
                case 'w':
                {
                    mEntity.jump();
                    break;
                }
                case 'a':
                {
                    mEntity.walkLeft();
                    break;
                }
                case 's':
                {
                    mEntity.crouch();
                    break;
                }
                case 'd':
                {
                    mEntity.walkRight();
                    break;
                }
            }
        }
        else if (_event.getType().equals("KeyUpEvent"))
        {
            KeyUpEvent event = (KeyUpEvent)_event;
            switch (event.getKey())
            {
                case 'w':
                {
                    break;
                }
                case 'a':
                {
                    break;
                }
                case 's':
                {
                    break;
                }
                case 'd':
                {
                    break;
                }
            }
        }
        else if (_event.getType().equals("MouseMoveEvent"))
        {
            MouseMoveEvent event = (MouseMoveEvent)_event;
            Vec2 direction = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
            direction.normalize();
            mTongueAngle = (float)Math.acos(Vec2.dot(new Vec2(0,-1), direction));
            if(direction.x < 0)
                mTongueAngle = (float) ((2*Math.PI) - mTongueAngle);
            float halfSeg = (float) (Math.PI/16.0f);
            //if statement splits left from right for efficiency
            //could further split into quadrents
            //each segment is the sum of half segments either side of each compass direction
            mEntity.mSkin.stopAnim(mFaceDirAnim);
            if(mTongueAngle < Math.PI)
            {
                if(mTongueAngle < halfSeg)
                    mFaceDirAnim = "n";
                else if(mTongueAngle >= halfSeg && mTongueAngle < 3*halfSeg)
                    mFaceDirAnim = "nne";
                else if(mTongueAngle >= 3*halfSeg && mTongueAngle < 5*halfSeg)
                    mFaceDirAnim = "ne";
                else if(mTongueAngle >= 5*halfSeg && mTongueAngle < 7*halfSeg)
                    mFaceDirAnim = "nee";
                else if(mTongueAngle >= 7*halfSeg && mTongueAngle < 9*halfSeg)
                    mFaceDirAnim = "e";
                else if(mTongueAngle >= 9*halfSeg && mTongueAngle < 11*halfSeg)
                    mFaceDirAnim = "see";
                else if(mTongueAngle >= 11*halfSeg && mTongueAngle < 13*halfSeg)
                    mFaceDirAnim = "se";
                else if(mTongueAngle >= 13*halfSeg && mTongueAngle < 15*halfSeg)
                    mFaceDirAnim = "sse";
                else if(mTongueAngle >= 15*halfSeg && mTongueAngle < 16*halfSeg)
                    mFaceDirAnim = "s";
            }
            else //angle < 0
            {
                if(mTongueAngle < 17*halfSeg)
                    mFaceDirAnim = "s";
                else if(mTongueAngle >= 17*halfSeg && mTongueAngle < 19*halfSeg)
                    mFaceDirAnim = "ssw";
                else if(mTongueAngle >= 19*halfSeg && mTongueAngle < 21*halfSeg)
                    mFaceDirAnim = "sw";
                else if(mTongueAngle >= 21*halfSeg && mTongueAngle < 23*halfSeg)
                    mFaceDirAnim = "sww";
                else if(mTongueAngle >= 23*halfSeg && mTongueAngle < 25*halfSeg)
                    mFaceDirAnim = "w";
                else if(mTongueAngle >= 25*halfSeg && mTongueAngle < 27*halfSeg)
                    mFaceDirAnim = "nww";
                else if(mTongueAngle >= 27*halfSeg && mTongueAngle < 29*halfSeg)
                    mFaceDirAnim = "nw";
                else if(mTongueAngle >= 29*halfSeg && mTongueAngle < 31*halfSeg)
                    mFaceDirAnim = "nnw";
                else if(mTongueAngle >= 31*halfSeg && mTongueAngle < 32*halfSeg)
                    mFaceDirAnim = "n";
            }
            mEntity.mSkin.startAnim(mFaceDirAnim, false, 0.0f);
        }
        else
        {
            MapClickEvent event = (MapClickEvent)_event;
            mCursorPos = event.getPosition();
            if (event.leftbutton())
            {
                mTongueState.leftClick(event.getPosition());
            }
            else
            {
                mTongueState.rightClick(event.getPosition());
            }
        }
    }
}