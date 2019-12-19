/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class AnalogueStickEvent extends iEvent {
    
    private float mHValue, mVValue;
    private int mPlayer;
    public AnalogueStickEvent(float _hValue, float _vValue, int _player)
    {
        mHValue = _hValue;
        mVValue = _vValue;
        mPlayer = _player;
    }
    
    public float getHValue()
    {
        return mHValue;
    }
    public float getVValue()
    {
        return mVValue;
    }

    @Override
    public String getName()
    {
        return getType() + mPlayer;
    }

    @Override
    public String getType()
    {
        return "AnalogueStickEvent";
    }
    
}
