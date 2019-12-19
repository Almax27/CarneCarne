/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import Entities.PlayerEntity;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
class PartialViewportCamera extends BodyCamera
{

    public PartialViewportCamera(Body _body, Rectangle _viewPort, boolean _topSplit, boolean _fourPlayerReady)
    {
        super(_body, _viewPort, _topSplit, _fourPlayerReady);
    }  
    @Override
    void transposeBody(iCamera _cameraB)
    {
        mBody = _cameraB.switchBody(mBody);
    }
    @Override
    Body switchBody(Body _body)
    {
        Body body = mBody;
        mBody = _body;
        ((PlayerEntity)mBody.getUserData()).setClip(mViewPort);
        return body;
    }
}
