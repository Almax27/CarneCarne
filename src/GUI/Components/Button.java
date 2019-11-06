/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public class Button extends GraphicalComponent{
    public Button(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
        _context.getInput().addListener(this);
        setColor(mDefaultColor);
    }
    public Button(GUIContext _context) {
        super(_context);
    }
    
    enum ButtonState
    {
        eHoverOver,
        eSelected,
        eDefault   
    }
    
    Text mText = null;
    Image mDefaultImage = null;
    Color mDefaultColor = new Color(0,0,0);
    Image mMouseOverImage = null;
    Color mMouseOverColor = new Color(187,139,44);
    Image mSelectedImage = null;
    Color mSelectedColor = new Color(210,187,21);
    ButtonState mState = ButtonState.eDefault;
    Runnable mSelectedCallback = null;    
    Runnable mOnHoverCallback = null; 
    
    public void setButtonStateColors(Color _default, Color _hover, Color _selected)
    {
        if(_default != null)
            mDefaultColor = _default;
        if(_hover != null)
            mMouseOverColor = _hover;
        if(_selected != null)
            mSelectedColor = _selected;
        updateButtonPropeties();
    }
    public void setButtonStateImages(Image _default, Image _hover, Image _selected)
    {
        if(_default != null)
            mDefaultImage = _default;
        if(_hover != null)
            mMouseOverImage = _hover;
        if(_selected != null)
            mSelectedImage = _selected;
        updateButtonPropeties();
    }
    public void setSelectedCallback(Runnable _callback)
    {
        mSelectedCallback = _callback;
    }
    public void setOnHoverCallback(Runnable _callback)
    {
        mOnHoverCallback = _callback;
    }

    protected void changeState(ButtonState _newState)
    {
        //do not process if already in _newState
        if(mState == _newState)
            return;
        switch(_newState)
        {
            case eDefault:
            {
                break;
            }
            case eHoverOver:
            {
                if(mOnHoverCallback != null)
                {
                    mOnHoverCallback.run();
                }
                break;
            }
            case eSelected:
            {
                if(mSelectedCallback != null)
                {
                    mSelectedCallback.run();
                }
                break;
            }
        }
        mState = _newState;
        updateButtonPropeties();
    }
    
    private void updateButtonPropeties()
    {
        switch(mState)
        {
            case eSelected:
            {
                //set relative image and color
                if(mSelectedImage != null)
                    mImage = mSelectedImage;
                else
                    setColor(mSelectedColor);
                break;
            }
            case eHoverOver:
            {
                //set relative image and color
                if(mMouseOverImage != null)
                    mImage = mMouseOverImage;
                else
                    setColor(mMouseOverColor);
                break;
            }
            case eDefault:
            {
                //set relative image and color
                if(mDefaultImage != null)
                    mImage = mDefaultImage;
                else
                    setColor(mDefaultColor);
                break;
            }
        } 
        if(mText != null)
            mText.setColor(getColor());
    }
    public void reset()
    {
        changeState(ButtonState.eDefault);
    }
    public void hoverOver()
    {
        changeState(ButtonState.eHoverOver);
    }
    public void select()
    {
        changeState(ButtonState.eSelected);
    }
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) 
    {
        if(isAcceptingInput())
        {
            if(mState != ButtonState.eSelected)
            {
                Shape myshape = getShape();
                if(myshape.contains(newx, newy))
                {
                    if(mState != ButtonState.eSelected)
                        changeState(ButtonState.eHoverOver);  
                }
                else 
                    changeState(ButtonState.eDefault);
            }
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) 
    {
        if(isAcceptingInput())
        {
            Shape myshape = getShape();
            if(myshape.contains(newx, newy))
            {
                //do nothing
            }
            else 
                changeState(ButtonState.eDefault);
        }
    }
    

    @Override
    public void mousePressed(int button, int x, int y) 
    {
        if(isAcceptingInput())
        {
            switch(button)
            {
                case Input.MOUSE_LEFT_BUTTON: 
                {
                    Shape myshape = getShape();
                    if(myshape.contains(x, y))
                        changeState(ButtonState.eSelected);
                    else
                        changeState(ButtonState.eDefault);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(int button, int x, int y) 
    {
        if(isAcceptingInput())
        {
            switch(button)
            {
                case Input.MOUSE_LEFT_BUTTON: 
                {
                    Shape myshape = getShape();
                    if(myshape.contains(x, y))
                        changeState(ButtonState.eHoverOver);
                    else
                        changeState(ButtonState.eDefault);
                }
            }
        }
    }

    public void addText(GUIContext _context, UnicodeFont _font, String _str)
    {
         addText(_context, _font, _str, false);
    }
    public void addText(GUIContext _context, UnicodeFont _font, String _str, boolean _sizeToText)
    {
        if(mText != null)
        {
            removeChild(mText);
            mText.destroy();
            mText = null;
        }
        Vector2f pos = new Vector2f(    (getWidth() - _font.getWidth(_str)) * 0.5f,
                                        (getHeight() - _font.getHeight(_str)) * 0.5f);
        mText = new Text(_context, _font, _str, pos, true);
        addChild(mText);
        mText.setColor(getColor());
        
        if(_sizeToText)
            setDimensionsToText();
    }
    
    public void setDimensionsToText()
    {
        setDimensions(mText.getDimensions());
        mText.setLocalTranslation(new Vector2f(0,0));
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) 
    {
        changeState(ButtonState.eDefault);
        super.setAcceptingInput(acceptingInput);
    }
    
    
    
    
    
   
    
    
    
}
