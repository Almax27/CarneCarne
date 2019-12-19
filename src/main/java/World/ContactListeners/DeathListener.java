/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Entities.Entity;
import Entities.Entity.CauseOfDeath;
import Entities.FireParticle;
import Events.EntityDeathEvent;
import Events.sEvents;
import World.sWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class DeathListener implements iListener
{
    private CauseOfDeath mCauseOfDeath;
    public DeathListener(CauseOfDeath _causeOfDeath)
    {
        mCauseOfDeath = _causeOfDeath;
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal())||
                _contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEnemy.ordinal())||
                _contact.m_fixtureB.m_filter.categoryBits == (1 << sWorld.BodyCategories.eCarcass.ordinal()))
        {
            boolean kill = true;
            if (_contact.m_fixtureA.m_body.m_userData != null)
                kill = ((Entity)_contact.m_fixtureA.m_body.m_userData).killedOpponent((Entity)_contact.m_fixtureB.m_body.m_userData);
            if (kill)
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureB.m_body.m_userData), mCauseOfDeath, _contact.m_fixtureA));
            }
        }
        else
        {
            boolean kill = true;
            if (_contact.m_fixtureA.m_body.m_userData != null)
                kill = ((Entity)_contact.m_fixtureB.m_body.m_userData).killedOpponent((Entity)_contact.m_fixtureB.m_body.m_userData);
            if (kill)
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureA.m_body.m_userData), mCauseOfDeath, _contact.m_fixtureB));
            }
        }
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
    
}
