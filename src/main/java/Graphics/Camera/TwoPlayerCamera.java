/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import Graphics.Camera.BodyCamera;
import Graphics.sGraphicsManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
class TwoPlayerCamera extends BodyCamera {

    Body mBodyB;
    public TwoPlayerCamera(Body _bodyA, Body _bodyB, Rectangle _viewPort, boolean _topSplit)
    {
        super(_bodyA, _viewPort, _topSplit, false);
        mBodyB = _bodyB;
    }

    protected void calculatePosition()
    {
        mPosition = mBody.getPosition().add(mBodyB.getPosition()).mul(0.5f);
        Vec2 s = sGraphicsManager.getScreenDimensions();
        mTranslation = new Vec2((s.x/2)/64.0f,(s.y/2)/64.0f);
        if (mPosition.x < mTranslation.x)
        {
            mTranslation.x -= ((mTranslation.x)-mPosition.x);
        }
        if (mPosition.y < mTranslation.y)
        {
            mTranslation.y -= ((mTranslation.y)-mPosition.y);
        }        
        mTranslation = mTranslation.mul(64.0f);
    }
    
}
