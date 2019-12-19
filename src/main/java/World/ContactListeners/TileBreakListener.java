/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Events.DynamicTileDestructionEvent;
import Events.sEvents;
import Level.Tile;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class TileBreakListener implements iListener
{
    int mKillerCategory;
    public TileBreakListener(int _killerCategory)
    {
        mKillerCategory = _killerCategory;
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.getFixtureA().getFilterData().categoryBits == mKillerCategory)
        {
            sEvents.triggerDelayedEvent(new DynamicTileDestructionEvent((Tile)_contact.getFixtureB().getUserData()));
        }
        else
        {
            sEvents.triggerDelayedEvent(new DynamicTileDestructionEvent((Tile)_contact.getFixtureA().getUserData()));
        }
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
    
}
