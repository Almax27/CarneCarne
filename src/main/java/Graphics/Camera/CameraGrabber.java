/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CameraGrabber
{
    private Vec2 mTargetPosition;
    float mStranthScale;
    int mTimer;
    public CameraGrabber(Vec2 _targetOffset)
    {
        mTargetPosition = _targetOffset;
        mTargetPosition = new Vec2(80,40);
        mStranthScale = 0.0f;
        mTimer = 0;
    }
    public CameraGrabber update()
    {
        mTimer++;
        int scale = mTimer;
        if (scale > 500)
            scale = 1000 - scale;
        mStranthScale = ((float)scale)/500.0f;
        if (mTimer > 1000)
            return null;
        return this;
    }
    public Vec2 getOffset(Vec2 _position)
    {
        Vec2 direction = mTargetPosition.sub(_position);
        float length = direction.normalize();
        return direction.mul(mStranthScale*length);
    }
}
