/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import org.jbox2d.common.Vec2;

/**
 *
 * @author aaron
 */
//class public to package only
public interface iSkin {
    
    abstract void render(float _x, float _y);
    abstract void setAlwaysOnTop(boolean _isAlwaysOnTop);
    abstract void setDimentions(float _w, float _h);
    abstract void setAlpha(float _alpha);
    
    //Animation methods
    abstract void stop();
    abstract void stopAt(int _index); 
    abstract boolean isAnimating();
    abstract float getDuration();
    abstract void setRotation(float _radians);
    abstract void restart();
    abstract public void setIsLooping(boolean _isLooping);
    abstract public void setSpeed(float _speed);
    
    //character methods
    abstract void stop(String _subSkin);
    abstract void stopAt(String _subSkin, int _index); 
    abstract boolean isAnimating(String _subSkin);
    abstract void setRotation(String _animation, float _radians);
    abstract float activateSubSkin(String _animation, boolean _isLooping, float _speed);
    abstract void deactivateSubSkin(String _animation);
    abstract void setDimentions(String _animation, float _w, float _h);
    abstract void setOffset(String _animation, Vec2 _offset);
    abstract Vec2 getOffset(String _animation);
    abstract void setAlpha(String _animation, float _alpha);
}
