/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import java.util.Iterator;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class ScrollableComponent extends GraphicalComponent
{
    public ScrollableComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
    }
    
    private boolean isAutoScrollPaused = true;
    private Vector2f mAutoScroll = new Vector2f(0,0);
    private Vector2f mTarget = new Vector2f(0,0);
    private Vector2f mScroll = new Vector2f(0,0);
    private Vector2f mOldScroll = new Vector2f(0,0);
    private Vector2f mAbsMaxScroll = new Vector2f(0,0);

    @Override
    protected boolean updateSelf(int _delta) {
        if(false == isAutoScrollPaused)
        {
            float timeStep = ((float)_delta)/1000.0f; //convert to seconds
            
            //if target not reached in X
            if(mScroll.x > mTarget.x ) //X greater than
                mScroll.x -= mAutoScroll.x * timeStep;
            else if(mScroll.x < mTarget.x) //X lesser than
                mScroll.x += mAutoScroll.x * timeStep;
            else autoScrollStop(true);
            
            //if target not reached in Y
            if(mScroll.y > mTarget.y) //Y greater than
                mScroll.y -= mAutoScroll.y * timeStep;
            else if(mScroll.y < mTarget.y) //Y lesser than
                mScroll.y += mAutoScroll.y * timeStep;
            else autoScrollStop(false);
            
            //clamp if within an autoscroll step
            if( mScroll.x > mTarget.x - mAutoScroll.x * timeStep && 
                mScroll.x < mTarget.x + mAutoScroll.x * timeStep)
                mScroll.x = mTarget.x;
            if( mScroll.y > mTarget.y - mAutoScroll.y * timeStep && 
                mScroll.y < mTarget.y + mAutoScroll.y * timeStep)
                mScroll.y = mTarget.y;
            
            forceToBounds(mScroll);
            
            if(mAutoScroll.x == 0 && mAutoScroll.y == 0)
                isAutoScrollPaused = true;
        }
        Vector2f deltaScroll = mScroll.copy().sub(mOldScroll);
        Vector2f offset = new Vector2f(-deltaScroll.x * mAbsMaxScroll.x, deltaScroll.y * mAbsMaxScroll.y);
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            
            Vector2f newPos = child.getLocalTranslation().add(offset);
            child.setLocalTranslation(newPos);
        }
        mOldScroll.set(mScroll);
        return super.updateSelf(_delta);
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {        
        Vector2f offset = new Vector2f(-mScroll.x * mAbsMaxScroll.x, mScroll.y * mAbsMaxScroll.y);
        grphcs.setWorldClip(_globalPos.x, _globalPos.y, getWidth(), getHeight());
        grphcs.translate(offset.x, offset.y);
            super.renderSelf(guic, grphcs, _globalPos);
        grphcs.translate(-offset.x, -offset.y);
        grphcs.clearWorldClip();
    }
    //need to override this method to scroll debug too

    @Override
    protected void renderSelfDebug(Graphics _graphics) 
    {
        super.renderSelfDebug(_graphics);
    }
    

    @Override
    public void setImage(String _ref) {
        super.setImage(_ref);
        //we set the Absolute max scroll distance 
        //to the difference between the scroll box and component
        //and ensure it's not negative
        float absScrollX = Math.max(0,mImage.getWidth() - getWidth());
        float absScrollY = Math.max(0,mImage.getHeight() - getHeight());
        mAbsMaxScroll = new Vector2f(absScrollX, absScrollY);
    }
    
    public void setAbsScroll(float x, float y)
    {
        float absScrollX = Math.max(0,x);
        float absScrollY = Math.max(0,y);
        mAbsMaxScroll = new Vector2f(absScrollX, absScrollY);
    }
    
    //relative (0-1)
    public final void scrollHorizontalBy(float _dx)
    {
        mScroll.x += _dx;
        mScroll.x = forceToBounds(mScroll.x);
    }
    //relative (0-1)
    public final void scrollVerticalBy(float _dy)
    {
        mScroll.y += _dy;
        mScroll.y = forceToBounds(mScroll.y);
    }
    //relative (0-1)
    public final void setScrollHorizontal(float _x)
    {
        mScroll.x = _x;
        mScroll.x = forceToBounds(mScroll.x);
    }
    //relative (0-1)
    public final void setScrollVertical(float _y)
    {
        mScroll.y = _y;
        mScroll.y = forceToBounds(mScroll.y);
    }
    //scroll to relative point (0-1)
    public final void scrollTo(Vector2f _relativePoint, Vector2f _ratePerSec)
    {
        forceToBounds(_relativePoint);
        forceToBounds(_ratePerSec);
        mTarget.set(_relativePoint);
        mAutoScroll.set(_ratePerSec);
        isAutoScrollPaused = false;
    }
    public final void autoScrollStop(boolean _stopHorizontal)
    {
        if(_stopHorizontal)
            mAutoScroll.x = 0;
        else
            mAutoScroll.y = 0;
    }
    public final void autoScrollPause(boolean _isPaused)
    {
        isAutoScrollPaused = _isPaused;
    }
    private void forceToBounds(Vector2f _value)
    {
        _value.x = forceToBounds(_value.x);
        _value.y = forceToBounds(_value.y);
    }
    private float forceToBounds(float _value)
    {
        float temp = Math.max(0,_value);
        return Math.min(1,temp);
    }
    
}
