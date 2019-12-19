/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Entities.Entity;
import Entities.Entity.CauseOfDeath;
import Events.EntityDeathEvent;
import Events.sEvents;
import Level.Tile;
import World.sWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class SpikeListener implements iListener
{
    Vec2 mSpikeDirection[];
    public SpikeListener()
    {
        mSpikeDirection = new Vec2[4];
        mSpikeDirection[0] = new Vec2(0,-1);
        mSpikeDirection[1] = new Vec2(1,0);
        mSpikeDirection[2] = new Vec2(0,1);
        mSpikeDirection[3] = new Vec2(-1,0);
    }
    public void beginContact(Contact _contact)
    {
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        /*if (_contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal())||
                _contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEnemy.ordinal())||
                _contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.eCarcass.ordinal()))
        {
            Tile tile = (Tile)_contact.m_fixtureA.getUserData();
            int spikeAngle = tile.getRootTile().getSlopeType();
            sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureB.m_body.m_userData), CauseOfDeath.eSpikes, _contact.m_fixtureA));
        }
        else
        {
            Tile tile = (Tile)_contact.m_fixtureA.getUserData();
            int spikeAngle = tile.getRootTile().getSlopeType();
            sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureA.m_body.m_userData), CauseOfDeath.eSpikes, _contact.m_fixtureB));
        }*/
    }
}
