/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;
import java.lang.String;

/**
 *
 * @author Graham
 */

public class InAreaEvent extends iEvent
{
    InAreaEvent(String _ID, Vec2 _position, Vec2 _dimension)
    {
        mName = "InAreaEvent";
        mWidth = _dimension.x;
        mHeight = _dimension.y;
        mPosition.x = _position.x;
        mPosition.y = _position.y;
        mID = _ID;
    }
    
    public String getName(){return mName + mID;}
    public String getType(){return mName;}
    
    public void setPosition(Vec2 _position)
    {
        mPosition.x = _position.x;
        mPosition.y = _position.y;
    }
    public void setPosition(float _x, float _y)
    {
        mPosition.x = _x;
        mPosition.y = _y;
    }
    public void setDimensions(Vec2 _dimensions)
    {
        mWidth = _dimensions.x;
        mHeight = _dimensions.y;
        
    }
    
    public float getPosX(){return mPosition.x;}
    public float getPosY(){return mPosition.y;}
    public String getID(){return mID;}
    
    private String mName;
    private Vec2 mPosition;
    private float mWidth;
    private float mHeight;
    private String mID;
    
}
