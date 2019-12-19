/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
class PlayerTongueAnchor extends MovingBodyTongueAnchor {

    public PlayerTongueAnchor(Fixture mLastHit, Vec2 vec2)
    {
        super(mLastHit, vec2);
    }
    int mTimer = 0;
    @Override
    public boolean hasContact()
    {
        if (mFixture.getBody() == null)
            return false;
        if (mTimer++ > 120)
        {
            //return false;
        }
        return true;
    }
}
