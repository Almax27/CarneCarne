/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import Level.Tile;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class ZoomzoomListener implements iListener
{

    public void beginContact(Contact _contact) {
    }

    public void endContact(Contact _contact) {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
        Fixture sensor;
        Body other;
        if (_contact.getFixtureA().getFilterData().categoryBits == (1 << BodyCategories.eZoomzoom.ordinal()))
        {
            sensor = _contact.getFixtureA();
            other = _contact.getFixtureB().getBody();
        }
        else
        {
            sensor = _contact.getFixtureB();
            other = _contact.getFixtureA().getBody();
        }
        
        Tile tile = (Tile)sensor.getUserData();
        float mod = other.getMass()/2.5449f;
        other.applyLinearImpulse(new Vec2(tile.getBoostDirection().x*mod, tile.getBoostDirection().y*mod), other.getPosition());
        _contact.setEnabled(false);
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
}
