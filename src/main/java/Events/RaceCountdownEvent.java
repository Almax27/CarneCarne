/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Graphics.Sprites.sSpriteFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class RaceCountdownEvent extends iEvent
{
    int mTimer;
    public RaceCountdownEvent()
    {
        HashMap params = new HashMap();
        params.put("pos", new Vec2(0,0));
        params.put("ref", "CountdownGo");
        sSpriteFactory.create("simple", params, true);
        mTimer = 0;
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "RaceCountdownEvent";
    }
    
    @Override
    public boolean process()
    {
        mTimer++;
        if (mTimer == 60)
        {
            sEvents.triggerEvent(new RaceStartEvent());
            return false;
        }
        return true;
    }
    
}
