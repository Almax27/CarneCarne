/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author alasdair
 */
public class MovingPlatform// extends Entity
{
    /*iPlatformController mController;
    private Vec2 mDimensions;
    private float mMoveSpeed;
    public MovingPlatform(iSkin _skin, iPlatformController _controller, Vec2 _dimensions)
    {
        super(_skin);
        mController = _controller;
        mController.setPlatform(this);
        mDimensions = _dimensions;
        mMoveSpeed = 1;
    }
    
    public Vec2 getDimensions()
    {
        return mDimensions;
    }
    
    public float getMoveSpeed()
    {
        return mMoveSpeed;
    }
    
    public void update()
    {
        mController.update();
    }
    public void render()
    {
        //Vec2 axis = sWorld.translateToWorld(new Vec2(mBody.getPosition().x,mBody.getPosition().y+));
        //sGraphicsManager.rotate(mBody.getAngle()*180.0f/(float)Math.PI);
        ///mBody.m_world.getBodyList().m_world.getBodyList().m_world.getBodyList().m_world
        //mSkin.
        Vec2 physPos = mBody.getPosition().clone();
        physPos.x -= (mDimensions.x/2)-0.5f; //FIXME: offset to compensate 0.5f bug (assumes width = 3)
        physPos.y -= (mDimensions.y/2)-0.5f;
        Vec2 pos = sWorld.translateToWorld(physPos);
        mSkin.render(pos.x,pos.y); 
        mSkin.setRotation(mBody.getAngle()*180.0f/(float)Math.PI);
    }*/
}
