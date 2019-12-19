/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class MovingSoundAnchor implements iSoundAnchor
{
    private Body mBody;
    private Vec2 mLocalPosition;
    public MovingSoundAnchor (Body _body, Vec2 _localPosition)
    {
        mBody = _body;
        mLocalPosition = _localPosition;
    }
    public Vec2 getPosition()
    {
        return mBody.getWorldPoint(mLocalPosition);
    }
    
}
