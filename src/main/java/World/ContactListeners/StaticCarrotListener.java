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
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class StaticCarrotListener implements iListener
{

    public void beginContact(Contact _contact)
    {
        if (_contact.getFixtureA().getFilterData().categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
        {
            if (_contact.getFixtureA().getBody().getType().equals(BodyType.STATIC))
            {
                Body body = _contact.getFixtureB().getBody();
                float scalar = 02f;
                if (body.getPosition().x < _contact.getFixtureA().getBody().getPosition().x)
                {
                    body.applyLinearImpulse(new Vec2(-16, -15).mul(scalar), body.getWorldCenter());
                }
                else
                {
                    body.applyLinearImpulse(new Vec2(16, -15).mul(scalar), body.getWorldCenter());
                }
            }
        }
        else
        {
            if (_contact.getFixtureB().getBody().getType().equals(BodyType.STATIC))
            {
                Body body = _contact.getFixtureA().getBody();
                float scalar = 0.5f;
                if (body.getPosition().x < _contact.getFixtureB().getBody().getPosition().x)
                {
                    body.applyLinearImpulse(new Vec2(-16, -15).mul(scalar), body.getWorldCenter());
                }
                else
                {
                    body.applyLinearImpulse(new Vec2(16, -15).mul(scalar), body.getWorldCenter());
                }
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
