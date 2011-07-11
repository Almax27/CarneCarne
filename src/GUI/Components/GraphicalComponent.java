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
    
    public iComponentEffectLayer mEffectLayer = new iComponentEffectLayer(this);
    protected Image mImage = null;
    
    @Override
    protected boolean updateSelf(int _delta) {
        mEffectLayer.update(_delta);
        return true;
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException {
        //render effects layer
        mEffectLayer.render((int)_globalPos.x, (int)_globalPos.y);
        
        //render image - if none fall back on base rendering
        if(mImage != null)
            mImage.draw(_globalPos.x, _globalPos.y);
    }   
    
    public void setImage(String _ref)
    {
        try 
        {
            mImage = new Image(_ref);
        } 
        catch (SlickException ex) {
            Logger.getLogger(GraphicalComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setDimentionsToImage()
    {
        setDimensions(new Vector2f(mImage.getWidth(), mImage.getHeight()));
    }
    
    public int getImageWidth(){return mImage.getWidth();}
    public int getImageHeight(){return mImage.getHeight();}
}
