/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Shader;

import java.util.Random;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;

/**
 *
 * @author a203945
 */
public class LightSource 
{
    public LightSource(Vec2 _position, Color _color, float _constantAttentuation, float _radius, float _quadraticAttentuation)
    {
        mPosition = _position;
        mColor = _color;
        mConstantAttentuation = _constantAttentuation;
        mRadius = _radius;
        mQuadraticAttentuation = _quadraticAttentuation;
    }
    
    public LightSource clone()
    {
        return new LightSource(mPosition.clone(), mColor, mConstantAttentuation, mRadius, mQuadraticAttentuation);
    }
    
    private Vec2 mPosition = null;
    private Color mColor = Color.white;
    float mConstantAttentuation = 0.5f;
    private float mRadius = 0.0f;
    float mQuadraticAttentuation = 0.0f;
    private float mTick = 0.95f;
    private Random rand = new Random();
    
    public void update(int _delta)
    {
        if(rand.nextBoolean())
            mTick += (rand.nextFloat() * 0.01f) / (mRadius / 100);
        else
            mTick -= (rand.nextFloat() * 0.01f) / (mRadius / 100);
        mTick = Math.max(1.0f, mTick);
        mTick = Math.min(1.2f, mTick);
    }
    
    public Vec2 getPosition() {return mPosition.clone();}
    public float getRadius() {return mRadius;}
    public Color getColor() {return mColor;}
    public float getTick() {return mTick;}
    
    public void setPosition(float _x, float _y)
    {
        mPosition.x = _x;
        mPosition.y = _y;
    }
    public void setRadius(float _r) {mRadius = _r;}
}
