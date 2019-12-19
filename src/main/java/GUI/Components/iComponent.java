

package GUI.Components;

import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.Iterator;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public abstract class iComponent extends AbstractComponent 
{
    protected iComponent(GUIContext _context) 
    {
        super(_context);
        super.setAcceptingInput(false);
    }
    protected iComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) 
    {
        super(_context);
        setDimensions(_dimensions);
        setLocalTranslation(_position);
        super.setAcceptingInput(false);
    }
    
    private String      mName = null; //could be used for searching a hieracrchy by name
    private iComponent  mParent;
    private ArrayList<iComponent> mChildren = new ArrayList<iComponent>();
    private Vector2f    mDimensions = new Vector2f(0,0);
    private boolean     mIsAlignUp = true;
    private boolean     mIsAlignLeft = true;
    private boolean     mMaintainRatio = false;
    private Vector2f    mLocalSizeScale = new Vector2f(1.0f,1.0f);
    private Vector2f    mLocalScale = new Vector2f(1.0f,1.0f);
    private float       mLocalRotation = 0.0f;
    private Vector2f    mLocalTranslation = new Vector2f(0,0);
    private Vector2f    mGlobalScale = new Vector2f(1.0f,1.0f);
    private float       mGlobalRotation = 0.0f;
    private Vector2f    mGlobalTranslation = new Vector2f(0,0);
    private Transform   mGlobalTransform = Transform.createTranslateTransform(0, 0);
    private boolean     mIsDestroyed = false;
    private boolean     mIsVisible = true;
    private Color       mColor = Color.white;
    private Shape       mShape = new Rectangle(0, 0, 1, 1);

    /*
     * -------primary functions-----------
     */
    public final boolean update(int _delta)
    {
        if(mParent == null)
        {
            updateInternal(_delta);
        }
        return false;
    }
    private boolean updateInternal(int _delta)
    {
        mGlobalScale = calcGlobalScale();
        mGlobalRotation = calcGlobalRotation();
        mGlobalTranslation = calcGlobalTranslation();
        mGlobalTransform = calcGlobalTransform();

        mShape = new Rectangle(0,0,getWidth(),getHeight());
        mShape = mShape.transform(getGlobalTransform());
        
        updateSelf(_delta);
        
        //update children
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.updateInternal(_delta);
        }

        return true;
    }
    protected abstract boolean updateSelf(int _delta);
    
    public final void render(GUIContext guic, Graphics grphcs) throws SlickException
    {
        render(guic, grphcs, false);
    }
    public final void render(GUIContext guic, Graphics grphcs, boolean _debug) throws SlickException
    {
        //render only if root
        if(mParent == null)
        {
            //start render chain
            renderInternal(guic, grphcs, new Vector2f(0,0));
            if(_debug)
                renderInternalDebug(grphcs);
        }
    }
    private void renderInternal(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException
    {      
        if(!mIsVisible)
            return;
        
        float rot = getLocalRotation();
        Vector2f scale = getLocalScale();
        Vector2f trans = getLocalTranslation().add(_globalPos);
        Vector2f center = new Vector2f(trans.x + (getWidth() * 0.5f), trans.y + (getHeight() * 0.5f));
        
        if(mMaintainRatio)
        {
            //FIXME: THIS IS THE MOST HORRIBLE CODE I'VE EVER WRITTEN
            float scalar = sGraphicsManager.getTrueScreenDimensions().x / 1680;
            float Sx = sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x;
            if(scalar > 1)
            {
                float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
                if(Sx < 1) // 1/4 SCREEN
                {
                    trans.x *= Sx * 1.18f;
                    center.x *= Sx * 1.18f;
                    trans.y *= Sy * 0.99f;
                    center.y *= Sy * 0.99f;
                    scale.y = scale.x = scalar * 0.8f;
                }
                else if(Sy < 1) //1/2 SCREEN
                {
                    trans.x *= (sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x) + 0.27;
                    trans.y *= (sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y) - 0.01;
                    scale.y = scale.x = scalar * 0.8f;   
                }
                
            }
            else if(scale.x != scale.y)
            {
                trans.x *= Sx;
                center.x *= Sx;
                float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
                trans.y *= Sy * 0.85f;
                center.y *= Sy * 0.85f;
                scale.y = scale.x = scalar;
            }
            else if(Sx != 1)
            {
                trans.x *= (sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x) - 0.045;
                trans.y *= (sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y) - 0.07;
                scale.y = scale.x = scalar;                
            }
        }
        else if(scale.x != scale.y && scale.x <= 1)
        {
            float scalar = sGraphicsManager.getScreenDimensions().y / 1050;
            float Sx = sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x;
            trans.x *= Sx * 1.3f;
            center.x *= Sx * 1.3f;
            float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
            trans.y *= Sy * 1.5f;
            center.y *= Sy * 1.5f;
            scale.y = scale.x = scalar * 1.5f;
        }
        
        grphcs.scale(scale.x, scale.y);
        grphcs.rotate(center.x, center.y, rot);
        {
            grphcs.setColor(mColor);
            grphcs.scale(mLocalSizeScale.x, mLocalSizeScale.y);
                renderSelf(guic, grphcs, trans);
            grphcs.scale(1/mLocalSizeScale.x, 1/mLocalSizeScale.y);

            //render children
            Iterator<iComponent> itr = getChildIterator();
            while(itr.hasNext())
            {
                iComponent child = itr.next();
                child.renderInternal(guic, grphcs, trans);
            }
        }
        grphcs.rotate(center.x, center.y, -rot);
        grphcs.scale(1/scale.x, 1/scale.y);
    }
    
    private final void renderInternalDebug(Graphics _graphics)
    {
        _graphics.setColor(Color.white);
        _graphics.scale(mLocalSizeScale.x, mLocalSizeScale.y);
            renderSelfDebug(_graphics) ;
        _graphics.scale(mLocalSizeScale.x, mLocalSizeScale.y);
        
        //render children
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.renderInternalDebug(_graphics);
        }
    }
    protected void renderSelfDebug(Graphics _graphics)
    {
        _graphics.draw(getShape());
    }
    protected void renderSelf(GUIContext guic, Graphics grphcs, final Vector2f _globalPos) throws SlickException
    {
        grphcs.fillRect(_globalPos.x, _globalPos.y, getWidth(), getHeight());
    }
    
    /*
     * ----------various getters and setters-------------
     */
    public final void setAlignment(boolean _isUp, boolean _isLeft)
    {
        mIsAlignUp = _isUp;
        mIsAlignLeft = _isLeft;
    }
    public final boolean getMaintainRatio(){return mMaintainRatio;}
    public final void setMaintainRatio(boolean _maintain) {mMaintainRatio = _maintain;}
    //returns shape translated in global space
    public final Shape getShape(){return mShape;}
    public final Transform getGlobalTransform(){return new Transform(mGlobalTransform);}
    private final Transform calcGlobalTransform()
    {
        float rot = getLocalRotation() / (float)(180.0f/Math.PI);
        Vector2f scale = getLocalScale();
        Vector2f trans = getLocalTranslation();
        Vector2f center = new Vector2f(trans.x + (getWidth() * 0.5f), trans.y + (getHeight() * 0.5f));          
        
        if(mMaintainRatio)
        {
            //FIXME: THIS IS THE MOST HORRIBLE CODE I'VE EVER WRITTEN
            float scalar = sGraphicsManager.getTrueScreenDimensions().x / 1680;
            float Sx = sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x;
            if(scalar > 1)
            {
                float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
                if(Sx < 1) // 1/4 SCREEN
                {
                    trans.x *= Sx * 1.18f;
                    center.x *= Sx * 1.18f;
                    trans.y *= Sy * 0.99f;
                    center.y *= Sy * 0.99f;
                    scale.y = scale.x = scalar * 0.8f;
                    trans.x *= 2.0f;
                    center.x *= 2.0f;
                }
                else if(Sy < 1) //1/2 SCREEN
                {
                    trans.x *= (sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x) + 0.27;
                    trans.y *= (sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y) - 0.01;
                    scale.y = scale.x = scalar * 0.8f;   
                }
                
            }
            else if(scale.x != scale.y)
            {
                trans.x *= Sx;
                center.x *= Sx;
                float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
                trans.y *= Sy * 0.85f;
                center.y *= Sy * 0.85f;
                scale.y = scale.x = scalar;
                trans.x *= 2.0f;
                center.x *= 2.0f;
            }
            else if(Sx != 1)
            {
                trans.x *= (sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x) - 0.045;
                trans.y *= (sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y) - 0.07;
                scale.y = scale.x = scalar;                
            }
        }
        else if(scale.x != scale.y && scale.x <= 1)
        {
            float scalar = sGraphicsManager.getScreenDimensions().y / 1050;
            float Sx = sGraphicsManager.getScreenDimensions().x / sGraphicsManager.getTrueScreenDimensions().x;
            trans.x *= Sx * 1.3f;
            center.x *= Sx * 1.3f;
            float Sy = sGraphicsManager.getScreenDimensions().y / sGraphicsManager.getTrueScreenDimensions().y;
            trans.y *= Sy * 1.5f;
            center.y *= Sy * 1.5f;
            scale.y = scale.x = scalar * 1.5f;
            trans.x *= 2.0f;
                center.x *= 2.0f;
        }
            
        Transform scaleT = Transform.createScaleTransform(scale.x, scale.y);
        Transform rotateT = Transform.createRotateTransform(rot, center.x, center.y);
        Transform translateT = Transform.createTranslateTransform(trans.x, trans.y);
        Transform transform = scaleT.concatenate(rotateT).concatenate(translateT);
        if(mParent != null)
        {
            transform = mParent.getGlobalTransform().concatenate(transform);
        }
        return transform;
    }
    public final Vector2f getGlobalScale(){return mGlobalScale.copy();}
    private final Vector2f calcGlobalScale()
    {
        Vector2f scale = getLocalScale();
        if(mParent != null)
        {
            scale.x *= mParent.getGlobalScale().x;
            scale.y *= mParent.getGlobalScale().y;
        }
        return scale;
    }
    public final float getGlobalRotation(){return mGlobalRotation;}
    private final float calcGlobalRotation()
    {
        float rotation = mLocalRotation;
        if(mParent != null)
        {
            rotation += mParent.getGlobalRotation();
        }
        return rotation;
    }
    public final Vector2f getGlobalTranslation(){return mGlobalTranslation.copy();}
    private final Vector2f calcGlobalTranslation()
    {
        Vector2f translation = mLocalTranslation.copy();
        if(mParent != null)
        {
            translation = translation.add(mParent.getGlobalTranslation());
        }
        return translation;
    }
    public final Vector2f getLocalSizeScale(){return mLocalSizeScale.copy();}
    public final void setLocalSizeScale(Vector2f _scaleFactor)
    {
        mLocalSizeScale.x = _scaleFactor.x;
        mLocalSizeScale.y = _scaleFactor.y;
    }
    public final Vector2f getLocalScale(){return mLocalScale.copy();}
    public final void setLocalScale(Vector2f _scaleFactor)
    {
        mLocalScale.x = _scaleFactor.x;
        mLocalScale.y = _scaleFactor.y;
    }
    public final float getLocalRotation(){return mLocalRotation;}
    public final void setLocalRotation(float _degrees)
    {
        mLocalRotation =_degrees;
    }
    //always returns translation in up/left alignment
    public final Vector2f getLocalTranslation()
    {
        Vector2f trans = new Vector2f(0,0);
        if(mIsAlignLeft)
            trans.x = mLocalTranslation.x;
        else
        {
            if(mParent != null)
                trans.x = mParent.getDimensions().x - mLocalTranslation.x;
            else
                trans.x = 1680.0f - mLocalTranslation.x;
        }
        if(mIsAlignUp)
            trans.y = mLocalTranslation.y;
        else
        {
            if(mParent != null)
                trans.y = mParent.getDimensions().y - mLocalTranslation.y;
            else
                trans.y = 1050.0f - mLocalTranslation.y;
        }
        return trans;
    }
    public final void setLocalTranslation(Vector2f _position)
    {
        mLocalTranslation.x = _position.x;
        mLocalTranslation.y = _position.y;
    }
    public final Vector2f getDimensions(){return mDimensions.copy();}
    public final void setDimensions(Vector2f _dimensions)
    {
        mDimensions.x = _dimensions.x;
        mDimensions.y = _dimensions.y;
    }
    public final boolean isRoot(){return mParent == null;}
    public final boolean setParent(iComponent _parent)
    {
        if(mParent == null)
        {
            mParent = _parent;
            return true;
        }
        else return false;
    }
    public final Color getColor() {return mColor;}
    public final void setColor(Color _color){mColor =  _color;}
    public final boolean isVisible(){return mIsVisible;}
    public final void setIsVisible(boolean _isVisible){mIsVisible = _isVisible;}
    
    /*
     * --------methods for child control :P-----------
     */
    //returns false if child already has a parent
    public final boolean addChild(iComponent _child)
    {            
        if(_child.setParent(this) && !_child.equals(this))
        {
            mChildren.add(_child);
            return true;
        }
        else return false;
    }
    public final void removeChild(iComponent _child)
    {
        mChildren.remove(_child);
    }
    public final Iterator<iComponent> getChildIterator()
    {
        return mChildren.listIterator();
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) 
    {
        super.setAcceptingInput(acceptingInput);
        for(iComponent c : mChildren)
        {
            c.setAcceptingInput(acceptingInput);
        }
    }
    
    
    /*
     * -------methods for destruction----------
     */
    public final boolean isDestroyed()
    {
        return mIsDestroyed;
    }
    public final void destroy()
    {
        if(!isRoot())
            mParent.removeChild(this);
        destroyInternal();
    }
    protected void destroyInternal()
    {
        //set destroyed flag
        mIsDestroyed = true;
        //destroy children
        Iterator<iComponent> itr = mChildren.listIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.destroyInternal();
        }
        //remove from parent
        mParent = null;
    }

    /*
     * ------------Overrides for AbstractComponent------------
     */
    @Override
    public void setLocation(int _x, int _y) {
        if(mLocalTranslation == null) //required due to call in parent's constructor
            mLocalTranslation = new Vector2f(0,0);
        setLocalTranslation(new Vector2f(_x,_y));
    }
    @Override
    public int getX() {
        return (int)mLocalTranslation.x;
    }
    @Override
    public int getY() {
        return (int)mLocalTranslation.y;
    }
    @Override
    public int getWidth() {
        return (int)mDimensions.x;
    }
    @Override
    public int getHeight() {
        return (int)mDimensions.y;
    }
}
