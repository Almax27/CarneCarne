/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.GenericStringEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.sSkinFactory;
import Level.sLevel;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroSwingSection extends IntroSection implements iEventListener
{
    int mTimer = 0;
    IntroSection mSection;
    boolean swingEventHit = false;
    public IntroSwingSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, "XBoxTop", "XBoxTopTongue", 1.85f);
        Vec2 tile = mPosition.add(new Vec2(2,-3));
        sEvents.unblockEvent("MapClickEvent"+"Tongue"+mPlayerNumber);
        sEvents.unblockEvent("MapClickReleaseEvent"+"Tongue"+mPlayerNumber);
        sEvents.unblockEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.unblockEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.subscribeToEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber, this);
        sEvents.subscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
        sLevel.placeTile((int)tile.x,(int)tile.y, 17);
        HashMap params = new HashMap();
        params.put("ref", "TutorialSwing");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("PlayerSwingEvent" + mPlayerNumber, this);
        mSection = this;
    }

    @Override
    void cleanup()
    {
        sEvents.unsubscribeToEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber, this);
        sEvents.unsubscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
    }
    @Override
    public IntroSection updateImpl()
    {
        return mSection;
    }

    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("MapClickEvent"))
        {
            sEvents.triggerEvent(new GenericStringEvent("MapClickEvent", "Tongue"+mPlayerNumber));
            return true;
        }
        else if (_event.getName().equals("MapClickReleaseEvent"))
        {
            sEvents.triggerEvent(new GenericStringEvent("MapClickReleaseEvent", "Tongue"+mPlayerNumber));
            return true;
        }
        else
        {
            if (mSection == this && !swingEventHit)
            {
                sEvents.unsubscribeToEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber, this);
                sEvents.unsubscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
                sEvents.unsubscribeToEvent("PlayerSwingEvent" + mPlayerNumber, this);
                swingEventHit = true;
                mSection = new IntroEndSection(mPosition, mPlayerNumber);
            }
            return false;
        }
    }
    
}
