/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class ToggleButton extends Button 
{
    public ToggleButton(GUIContext _context, Vector2f _position, Vector2f _dimensions, boolean _initialState) {
        super(_context, _position, _dimensions);
        mToggleState = _initialState;
    }
    public ToggleButton(GUIContext _context, boolean _initialState) {
        super(_context);
        mToggleState = _initialState;
    }
    
    boolean mToggleState = true;
    String mOnText = "";
    String mOffText = "";
    Runnable mOnCallback = null;
    Runnable mOffCallback = null;
    
    public boolean getState()
    {
        return mToggleState;
    }
    
    public void setToggleText(String _on, String _off)
    {
        mOnText = _on;
        mOffText = _off;
    }
    
    public void setOnCallback(Runnable _run)
    {
        mOnCallback = _run;
    }
    
    public void setOffCallback(Runnable _run)
    {
        mOffCallback = _run;
    }
    
    private void flip()
    {
        mToggleState = !mToggleState;
        if(mToggleState)
        {
            mSelectedCallback = mOnCallback;
            mText.setTextString(mOnText);
        }
        else
        {
            mSelectedCallback = mOffCallback;
            mText.setTextString(mOffText);
        }
    }

    @Override
    protected void changeState(ButtonState _newState) 
    {
        //do not process if already in _newState
        if(mState == _newState)
            return;
        
        switch(_newState)
        {
            case eSelected:
            {
                flip();
            }
        }
        super.changeState(_newState);
    } 
}
