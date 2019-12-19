/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CheckPointZone extends AreaEvent
{
    int mCheckPointNumber;
    CheckPointZone mNextCheckPoint;
    public CheckPointZone(int _x, int _y, int _x2, int _y2, int _checkPointNumber, CheckPointZone _nextCheckPoint)
    {
        super(_x, _y, _x2, _y2);
        mCheckPointNumber = _checkPointNumber;
        mNextCheckPoint = _nextCheckPoint;
    }
    @Override
    public void enter(AIEntity _entity)
    {
        _entity.placeCheckPoint(this);
    }

    @Override
    public void leave(AIEntity _entity)
    {
        _entity.placeCheckPoint(this);
    }
    
    public Vec2 getPosition()
    {
        return new Vec2(x,y);
    }
    
    public int getCheckpointNumber()
    {
        return mCheckPointNumber;
    }
    
    public boolean incrementRaceTimer()
    {
        return true;
    }
    
    @Override
    public final void update()
    {
    }
    public void resetTimer()
    {
    }
    protected String getTimeString(int _raceTimer)
    {
        String timer = String.valueOf(_raceTimer/60.0f);
        if (timer.length() > 5)
        {
            timer = timer.substring(0, 5);
        }
        else while (timer.length() < 5)
        {
            timer = timer + "0";
        }
        return timer;
    }
    public void renderRaceState(int _raceTimer)
    {
       // sGraphicsManager.drawString("Run Forrest run! Time: " + getTimeString(_raceTimer) + " " + mCheckPointNumber + " checkpoints reached", 0f, 0);
    }

    public CheckPointZone getNext()
    {
        return mNextCheckPoint;
    }
}
