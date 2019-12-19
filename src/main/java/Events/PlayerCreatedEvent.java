/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.PlayerEntity;

/**
 *
 * @author A203946
 */
public class PlayerCreatedEvent extends iEvent {
    
    PlayerEntity mEntity;
    int mPlayerID;
    public PlayerCreatedEvent(PlayerEntity _entity, int _playerID)
    {
        mEntity = _entity;
        mPlayerID = _playerID;
    }
    public String getName()
    {
        return getType();
    }
    public PlayerEntity getPlayer()
    {
        return mEntity;
    }
    public String getType()
    {
        return "PlayerCreatedEvent";
    }
    public int getPlayerID()
    {
        return mPlayerID;
    }
}
