/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;
import Graphics.sGraphicsManager;

/**
 *
 * @author alasdair
 */
public class RaceLostFakeZone extends CheckPointZone
{
    private PlayerEntity mLoser;
    private int mPosition;
    public RaceLostFakeZone(int _numCheckPoints, int _position)
    {
        super(-1,-1,-1,-1 , _numCheckPoints, null);
        mLoser = null;
        mPosition = _position;
    }
    private String placeString()
    {
        switch (mPosition)
        {
            case 0:
            {
                return "Zeroth";
            }
            case 1:
            {
                return "1st";
            }
            case 2:
            {
                return "2nd";
            }
            case 3:
            {
                return "3rd";
            }
            default:
            {
                return mPosition + "th";
            }
        }
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
       // sGraphicsManager.drawString("You lost. So hard. You got place " + placeString() + " in " + getTimeString(_raceTimer) + " seconds.", 0f, 0);
    }
    @Override
    public void enter(AIEntity _entity)
    {
    }
    @Override
    public boolean incrementRaceTimer()
    {
        return false;
    }
}