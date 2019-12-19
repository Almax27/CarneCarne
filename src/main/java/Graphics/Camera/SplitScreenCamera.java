/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
class SplitScreenCamera extends iCamera
{
    iCamera mCameraA, mCameraB;
    iCamera mActiveCamera;
    Rectangle viewPortA, viewPortB;
    boolean lastSplit;
    boolean mTopSplit;
    boolean mFourPlayerReady;
    public SplitScreenCamera(Body _bodyA, Body _bodyB, Rectangle _viewPort, boolean _topSplit, boolean _fourPlayerReady)
    {
        super(_viewPort);
        mFourPlayerReady = _fourPlayerReady;
        lastSplit = false;
        mTopSplit = _topSplit;
        split();
        mCameraA = new PartialViewportCamera(_bodyA, viewPortA, !mTopSplit, !mFourPlayerReady);
        mCameraB = new PartialViewportCamera(_bodyB, viewPortB, !mTopSplit, !mFourPlayerReady);
        mActiveCamera = mCameraA;
    }

    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        return mActiveCamera.translateToWorld(_physicsSpace);
    }

    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        return mActiveCamera.translateToPhysics(_worldSpace);
    }

    public Vec2 getPixelTranslation()
    {
        return mActiveCamera.getPixelTranslation();
    }
    
    public void render(Graphics _graphics)
    {
        mActiveCamera = mCameraA;
        mActiveCamera.render(_graphics);
        mActiveCamera = mCameraB;
        mActiveCamera.render(_graphics);
    }
    
    @Override
    public iCamera addPlayer(Body _body)
    {
        if (lastSplit)
        {
            mCameraA = mCameraA.addPlayer(_body);
        }
        else
        {
            mCameraB = mCameraB.addPlayer(_body);
            if (!mFourPlayerReady)
            {
                mCameraA.transposeBody(mCameraB);
                mFourPlayerReady = true;
            }
        }
        lastSplit = !lastSplit;
        return this;
    }
    @Override
    void transposeBody(iCamera _cameraB)
    {
        mCameraB.transposeBody(_cameraB);
    }
    @Override
    Body switchBody(Body _body)
    {
        return mCameraA.switchBody(_body);
    }
    public void update(Graphics _graphics)
    {
        mCameraA.update(_graphics);
        mCameraB.update(_graphics);
    }
    @Override
    public void resize(Rectangle _viewPort)
    {
        super.resize(_viewPort);
        split();

        mCameraA.resize(viewPortA);
        mCameraB.resize(viewPortB);
    }
    public void split()
    {
        int x = (int)mViewPort.getX();
        int y = (int)mViewPort.getY();
        int width = (int)mViewPort.getWidth();
        int height = (int)mViewPort.getHeight();
        int x2 = x;
        int y2 = y;
        if (mTopSplit)
        {
            height /= 2;
            y2 += height;
        }
        else
        {
            width /= 2;
            x2 += width;
        }
        viewPortA = new Rectangle(x,y,width, height);
        viewPortB = new Rectangle(x2,y2,width, height);        
    }
}
