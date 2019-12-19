/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.CaveInTileGrid;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CaveInEvent extends iEvent
{
    CaveInTileGrid mCaveIn;
    int mSize;
    int mTimer = 0;
    public CaveInEvent(CaveInTileGrid _caveIn, int _size)
    {
        mCaveIn = _caveIn;
        mSize = _size * 60;
    }
    
    public float getScale(Vec2 _playerPosition)
    {
        return (mSize - mTimer)*0.1f;
    }

    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "CaveInEvent";
    }
    
    @Override
    public boolean process()
    {
        mTimer++;
        if (mTimer == mSize)
        {
            return true;
        }
        return false;
    }
    
}
