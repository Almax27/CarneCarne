/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author G203947
 */
public class Carrot  extends AIEntity
{
    //private float mMoveSpeed;

    public Carrot(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 6;
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {      
        //mBody.setLinearVelocity(new Vec2(0,0));
        mController.update();
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f/mBody.m_mass), new Vec2(0,0));
        //subUpdate();    
    } 
}