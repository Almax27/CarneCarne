/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Entities.Entity;
import Level.Tile;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class WaterListener implements iListener {

    public WaterListener() {
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eWater.ordinal()) ||
                _contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eAcid.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).submerge(((Tile)_contact.m_fixtureA.m_userData).getWaterHeight());
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).submerge(((Tile)_contact.m_fixtureB.m_userData).getWaterHeight());        
        }
    }

    public void endContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eWater.ordinal()) ||
                _contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eAcid.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).unsubmerge();
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).unsubmerge();          
        }
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
    
}
