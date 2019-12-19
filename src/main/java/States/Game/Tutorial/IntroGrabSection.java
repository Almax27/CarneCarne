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
class IntroGrabSection extends IntroSection implements iEventListener
{
    public IntroGrabSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, "XBoxTop", "XBoxTopTongue", 1.85f);
        sEvents.unblockEvent("MapClickEvent"+"Tongue"+mPlayerNumber);
        sEvents.unblockEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.unblockEvent("MapClickReleaseEvent"+"Tongue"+mPlayerNumber);
        sEvents.unblockEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.subscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
        HashMap params = new HashMap();
        params.put("ref", "TutorialEat");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    void cleanup()
    {
        sEvents.unsubscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
    }
    @Override
    public IntroSection updateImpl()
    {
        Vec2 tile = mPosition.add(new Vec2(4,0));
        if (sLevel.getPathInfo((int)tile.x,(int)tile.y).equals(sLevel.PathInfo.eAir))
        {
            sEvents.unsubscribeToEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber, this);
            return new IntroSmashSection(mPosition, mPlayerNumber);
        }
        return this;
    }
    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }

    public boolean trigger(iEvent _event)
    {
        sEvents.triggerEvent(new GenericStringEvent("MapClickEvent", "Tongue"+mPlayerNumber));
        return true;
    }
    
}
