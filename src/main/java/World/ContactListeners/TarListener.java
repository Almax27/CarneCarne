/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class TarListener implements iListener {

    public TarListener()
    {
    }


    public void beginContact(Contact _contact)
    {
        /*if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eTar.ordinal()))
        {
            if (((Tile)_contact.m_fixtureA.getUserData()).isOnFire())
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureB.m_body.m_userData)));
            }
            else
            {
                ((Entity)_contact.m_fixtureB.m_body.m_userData).tar();
                ((Entity)_contact.m_fixtureB.m_body.getUserData()).canJump();
            }
        }
        else
        {
            if (((Tile)_contact.m_fixtureB.getUserData()).isOnFire())
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureA.m_body.m_userData)));
            }
            else
            {
                ((Entity)_contact.m_fixtureA.m_body.m_userData).tar();  
                ((Entity)_contact.m_fixtureA.m_body.getUserData()).canJump();
            }
        }*/
    }

    public void endContact(Contact _contact)
    {
        /*if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eTar.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).untar();
            ((Entity)_contact.m_fixtureB.m_body.getUserData()).cantJump(); 
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).untar();   
            ((Entity)_contact.m_fixtureA.m_body.getUserData()).cantJump();        
        }*/
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
}
