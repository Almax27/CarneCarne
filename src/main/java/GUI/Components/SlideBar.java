/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class SlideBar extends GraphicalComponent {
    public SlideBar(GUIContext _context, Vector2f _position, Vector2f _backgroundDimensions, Vector2f _sliderDimensions) {
        super(_context, _position, _backgroundDimensions);
        initInternal(_context, _sliderDimensions);
    }
    private void initInternal(GUIContext _context, Vector2f _sliderDimensions)//wraps construction time initialisation
    {
        //create slider
        mSlider = new DraggableComponent(_context, new Vector2f(0,0), _sliderDimensions);
        addChild(mSlider); //so transforms are automatiically applied from this component 
        mSlider.setColor(Color.yellow);
    }
    
    private DraggableComponent mSlider = null;
    private boolean mIsVertical = false;
    private int mBoundry = 0;
    private float mValue = 0.0f;
    
    public void init(boolean _isVertical, int _boundry)
    {
        //save 'settings'
        mIsVertical = _isVertical;
        mBoundry = _boundry;
        
        mSlider.setMoveInX(!_isVertical);
        mSlider.setMoveInY(_isVertical);
        if(!_isVertical)
        {
            //offset the slider so it is centered
            float offset = (getDimensions().y - mSlider.getDimensions().y) * 0.5f;
            mSlider.setLocalTranslation(new Vector2f(_boundry, offset));
            mSlider.setRangeX(new Vector2f(_boundry, getDimensions().x - _boundry - mSlider.getDimensions().x));
            mSlider.setRangeY(new Vector2f(offset, offset));
        }
        else
        {
            //offset the slider so it is centered
            float offset = (getDimensions().x - mSlider.getDimensions().x) * 0.5f;
            mSlider.setLocalTranslation(new Vector2f(offset, _boundry));
            mSlider.setRangeY(new Vector2f(_boundry, getDimensions().y - _boundry - mSlider.getDimensions().y));
            mSlider.setRangeX(new Vector2f(offset, offset));
        }
    }

    public void setImage(String _background, String _slider) {
        super.setImage(_background);
        mSlider.setImage(_slider);
    }
    
    public float getValue(){return mValue;}
    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {
        super.renderSelf(guic, grphcs, _globalPos);
    }

    @Override
    public boolean updateSelf(int _delta) 
    {
        //calc value
        Vector2f trans = mSlider.getLocalTranslation();
        if(!mIsVertical)
            mValue = trans.x / (getDimensions().x - mBoundry);
        else
            mValue = trans.y / (getDimensions().y - mBoundry);
        
        return super.updateSelf(_delta);
    }
    
    
    
}
