/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.MapClickEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroSpitSection extends IntroSection implements iEventListener
{
    IntroSection mReturn;
    public IntroSpitSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, "XBoxTop", "XBoxTopSpit", 1.85f);
        sEvents.unblockEvent("MapClickEvent"+"Spit"+mPlayerNumber);
        sEvents.blockEvent("MapClickEvent"+"Hammer"+mPlayerNumber);
        sEvents.blockEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber);
        HashMap params = new HashMap();
        params.put("ref", "TutorialSpit");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("MapClickEvent"+"Spit"+mPlayerNumber, this);
        mReturn = this;
    }

    @Override
    void cleanup()
    {
        sEvents.unsubscribeToEvent("MapClickEvent"+"Spit"+mPlayerNumber, this);
    }
    @Override
    public IntroSection updateImpl()
    {
        return mReturn;
    }

    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }

    public boolean trigger(iEvent _event)
    {
        if (mReturn == this)
        {
            sEvents.unsubscribeToEvent("MapClickEvent"+"Spit"+mPlayerNumber, this);
            MapClickEvent event = (MapClickEvent)_event;
            mReturn = new IntroSwingSection(mPosition, mPlayerNumber);
        }
        return false;
    }
    
}
