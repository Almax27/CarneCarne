/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Graham
 */

public class SoundData 
{
    private boolean mLooping;
    private float mVolume;
    private float mPitch;
    private Vec2 mPosition;
    private Sound mSound;
    
    SoundData(String _soundFile) throws SlickException
    {
        mSound = new Sound(_soundFile);
        mPitch = 1.0f;
        mVolume = 0.5f;
        mPosition = new Vec2(0.0f, 0.0f);
    }
    
    public void setPosition(Vec2 _position){mPosition = _position;}
    public void setPitch(float _pitch){mPitch = _pitch;}
    public void setVolume(float _volume){mVolume = _volume;}
    public void setLooping(boolean _looping){mLooping = _looping;}
    public void setSound(Sound _sound){mSound = _sound;}
    
    public Vec2 getPosition(){return mPosition;}
    public float getPitch(){return mPitch;}
    public float getVolume(){return mVolume;}
    public boolean getLooping(){return mLooping;}
    public Sound getSound(){return mSound;}
    
}
