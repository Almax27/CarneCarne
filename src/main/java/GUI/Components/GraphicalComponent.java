/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */

public class GraphicalComponent extends iComponent{
    
    public GraphicalComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
    }
    public GraphicalComponent(GUIContext _context) {
        super(_context);
    }
    
    public ComponentEffectLayer mEffectLayer = new ComponentEffectLayer(this);
    protected Image mImage = null;
    
    @Override
    protected boolean updateSelf(int _delta) {
        mEffectLayer.update(_delta);
        return true;
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException {
        //render effects layer
        mEffectLayer.render((int)_globalPos.x, (int)_globalPos.y, getWidth(), getHeight());
        
        //render image - if none fall back on base rendering
        if(mImage != null)
            mImage.draw(_globalPos.x, _globalPos.y);
    }   
    
    public void setImage(String _ref)
    {
        if(_ref == null)
            mImage = null;
        else
        {
            try 
            {
                mImage = new Image(_ref);
                mImage.setFilter(Image.FILTER_LINEAR);
            } 
            catch (SlickException ex) {
                Logger.getLogger(GraphicalComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setDimentionsToImage()
    {
        if(mImage != null)
            setDimensions(new Vector2f(mImage.getWidth(), mImage.getHeight()));
    }
    
    public int getImageWidth()
    {
        if(mImage != null)
            return mImage.getWidth();
        else
            return this.getWidth();
    }
    public int getImageHeight()
    {
        if(mImage != null)
            return mImage.getHeight();
        else
            return this.getHeight();
    }
    
    public float getAlpha() 
    {
        if(mImage != null)
            return mImage.getAlpha();
        else
            return 1;
    }
    public void setAlpha(float _value)
    {
        if(mImage != null)
            mImage.setAlpha(_value);
    }
    
    @Override
    protected void destroyInternal()
    {
        try {
            mImage.destroy();
        } catch (SlickException ex) {
            Logger.getLogger(GraphicalComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroyInternal();
        
    }
}
