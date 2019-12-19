/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

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
public class SelfSensorListener implements iListener
{
    
    public void beginContact(Contact _contact) {
    }

    public void endContact(Contact _contact) {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
        Body sensor, other;
        if (_contact.getFixtureA().getFilterData().categoryBits == (1 << BodyCategories.eSelfSensor.ordinal()))
        {
            sensor = _contact.getFixtureA().getBody();
            other = _contact.getFixtureB().getBody();
        }
        else
        {
            sensor = _contact.getFixtureB().getBody();
            other = _contact.getFixtureA().getBody();
        }
        
        Vec2 direction = other.getWorldCenter().sub(sensor.getWorldCenter());
        float length = direction.normalize();
        float scalar = sensor.getFixtureList().getShape().m_radius - length;
        other.applyLinearImpulse(direction.mul(scalar), other.getPosition());
        _contact.setEnabled(false);
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
}
