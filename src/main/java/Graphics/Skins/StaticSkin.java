/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import Graphics.sGraphicsManager;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
public class StaticSkin implements iSkin
{
    float mWidth, mHeight;
    Image mImage;
    boolean mAlwaysOnTop = false;
    //constructor public to graphics package only
    public StaticSkin(String _image) throws SlickException
    {
        mImage = new Image(_image);
    }
    StaticSkin(Image _image) throws SlickException
    {
        mImage = _image;
    }
    public void render(float _x, float _y)
    {
        if(mAlwaysOnTop)
        {
            sGraphicsManager.addAlwaysOnTopSkin(this, new Vec2(_x,_y));
            return;
        }
        if(mWidth == 0 && mHeight == 0)
            mImage.draw(_x, _y);
        else
            mImage.draw(_x, _y, mWidth, mHeight);
    }
    public void setRotation(float _radians) {
        mImage.setRotation(_radians);
    }
    
    public void setDimentions(float _w, float _h) {
        if(_w != 0)
            mWidth = _w;
        else
            mWidth = mImage.getWidth();
        if(_h != 0)
            mHeight = _h;
        else
            mHeight = mImage.getHeight();
    }
    
    public boolean isAnimating() {
        return false;
    }

    public void restart() {
        assert(false);
    }

    public void deactivateSubSkin(String _animation) {
        assert(false);
    }

    public void restart(String _animation) {
        assert(false);
    }

    public void setRotation(String _animation, float _radians) {
        assert(false);
    }

    public void setIsLooping(boolean _isLooping) {
        assert(false);
    }

    public void setSpeed(float _speed) {
        assert(false);
    }

    public float getDuration() {
        return 0.0f;
    }
    
    public void setAlpha(float _alpha) {
        mImage.setAlpha(_alpha);
    }

    public float activateSubSkin(String _animation, boolean _isLooping, float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(String _animation, float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOffset(String _animation, Vec2 _offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vec2 getOffset(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAnimating(String _subSkin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAt(int _index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop(String _subSkin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAt(String _subSkin, int _index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlpha(String _animation, float _alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlwaysOnTop(boolean _isAlwaysOnTop) {
        mAlwaysOnTop = _isAlwaysOnTop;
    }

}
