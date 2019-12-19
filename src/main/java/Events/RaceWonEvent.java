/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */
public class RaceWonEvent extends iEvent
{
    PlayerEntity mEntity;
    int mTime;
    public RaceWonEvent(PlayerEntity _entity)
    {
        mEntity = _entity;
        mTime = mEntity.getRaceTimer();
    }
    @Override
    public String getName()
    {
        return getType();
    }
    
    public PlayerEntity getPlayer()
    {
        return mEntity;
    }

    @Override
    public String getType()
    {
        return "RaceWonEvent";
    }
    
    public int getTime()
    {
        return mTime;
    }
    @Override
    public boolean process()
    {
        sEvents.triggerEvent(new GenericStringEvent("BarrierCloseEvent", "StartGate"));
        return true;
    }
}
