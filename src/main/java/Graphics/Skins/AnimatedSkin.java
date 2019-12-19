/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class AnimatedSkin implements iSkin{
    float mWidth, mHeight;
    Animation mAnim;
    boolean mAlwaysOnTop = false;
    
    //constructor public to graphics package only
    AnimatedSkin(String _spriteSheet, int _duration, int _tileWidth, int _tileHeight) throws SlickException
    {
        mAnim = new Animation(new SpriteSheet(_spriteSheet, _tileWidth, _tileHeight), _duration);
        mAnim.restart();
        mAnim.setLooping(false);
    }
    //constructor public to graphics package only
    AnimatedSkin(SpriteSheet _spriteSheet, int _duration) throws SlickException
    {
        mAnim = new Animation(_spriteSheet, _duration);
        mAnim.restart();
        mAnim.stop();
        mAnim.setLooping(false);
    }
    public void render(float _x, float _y)
    {
        if(mWidth == 0 && mHeight == 0)
            mAnim.draw(_x, _y);
        else
            mAnim.draw(_x, _y, mWidth, mHeight);
    }
    //internal graphics function for batch rendering
    void renderInUse(float _x, float _y)
    {
        //mAnimation.renderInUse(_x, _y);
    }
    //sets rotation of current frame
    public void setRotation(float _radians) {
        mAnim.getCurrentFrame().setRotation(_radians);
    }
    
    //animation control functions
    public void restart()
    {
        mAnim.restart();
    }
    public void setIsLooping(boolean _isLooping)
    {
        mAnim.setLooping(_isLooping);
    }
    public void setSpeed(float _speed)
    {
        mAnim.setSpeed(_speed);
    }
    
    public void setDimentions(float _w, float _h) {
        if(_w != 0)
            mWidth = _w;
        else
            mWidth = mAnim.getWidth();
        if(_h != 0)
            mHeight = _h;
        else
            mHeight = mAnim.getHeight();
    }
    
    public void setAlpha(float _alpha) {
        for(int i = 0; i < mAnim.getFrameCount(); i++)
        {
            mAnim.getImage(i).setAlpha(_alpha);
        }
    }
    
    public boolean isAnimating() {
        return !mAnim.isStopped();
    }

    public void deactivateSubSkin(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restart(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRotation(String _animation, float _radians) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getDuration() {
        return ((float)(mAnim.getDuration(0)*mAnim.getFrameCount()))/1000.0f;
    }
    
    public void stop() {
        mAnim.stop();
    }

    public void stopAt(int _index) {
        mAnim.stopAt(_index);
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
