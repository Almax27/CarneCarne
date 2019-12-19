/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Entities.Entity;
import Entities.Entity.CauseOfDeath;
import Events.EntityDeathEvent;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import World.sWorld;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class HighImpulseListener implements iListener
{
    public HighImpulseListener()
    {
    }

    public void beginContact(Contact _contact)
    {
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
        Body bodyA = _contact.m_fixtureA.m_body;
        Body bodyB = _contact.m_fixtureB.m_body;
        Vec2 momentumExchanged = bodyA.getLinearVelocity().mul(bodyA.getMass()).sub(bodyB.getLinearVelocity().mul(bodyB.getMass()));
        float force = momentumExchanged.normalize();
        if (force > 10.0f)
        {
            if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.eCarcass.ordinal()))
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureB.m_body.getUserData(),CauseOfDeath.eImpact, _contact.m_fixtureA));
                //sParticleManager.createSystem("ParticleFire", sWorld.translateToWorld(_contact.m_fixtureB.m_body.getPosition()), 0.5f);
            }
            else 
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureA.m_body.getUserData(),CauseOfDeath.eImpact, _contact.m_fixtureB));    
                //sParticleManager.createSystem("ParticleFire", sWorld.translateToWorld(_contact.m_fixtureA.m_body.getPosition()), 0.5f);   
            }
        }
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        /*float impulse = _impulse.normalImpulses[0]; /// Only need to check one impulse because carne is a circle
        if (impulse > 50.0f)
        {
            if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.ePlayer.ordinal()))
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureB.m_body.getUserData()));
            }
            else 
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureA.m_body.getUserData()));       
            }
        }*/
    }
}