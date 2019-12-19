/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class InRadiusEvent {
    
    InRadiusEvent(String _ID, Vec2 _position, float _radius)
    {
        mName = "InRadiusEvent";
        mRadius = _radius;
        mPosition.x = _position.x;
        mPosition.y = _position.y;
        mID = _ID;
    }
    
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
    
    public float getPosX(){return mPosition.x;}
    public float getPosY(){return mPosition.y;}
    public String getID(){return mID;}
    
    public String getName(){return mName + mID;}
    public String getType(){return mName;}
    
    private String mName;
    private Vec2 mPosition;
    private float mRadius;
    private String mID;
    
}
