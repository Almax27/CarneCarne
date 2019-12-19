/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.ContactListeners;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class FlipListener implements iListener {

    iListener mReaction;
    public FlipListener(iListener _reaction)
    {
        mReaction = _reaction;
    }

    public void beginContact(Contact _contact)
    {
        Fixture c = _contact.m_fixtureA;
        _contact.m_fixtureA = _contact.m_fixtureB;
        _contact.m_fixtureB = c;
        mReaction.beginContact(_contact);
    }

    public void endContact(Contact _contact)
    {
        Fixture c = _contact.m_fixtureA;
        _contact.m_fixtureA = _contact.m_fixtureB;
        _contact.m_fixtureB = c;
        mReaction.endContact(_contact);
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
        Fixture c = _contact.m_fixtureA;
        _contact.m_fixtureA = _contact.m_fixtureB;
        _contact.m_fixtureB = c;
        mReaction.preSolve(_contact, _manifold);
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        Fixture c = _contact.m_fixtureA;
        _contact.m_fixtureA = _contact.m_fixtureB;
        _contact.m_fixtureB = c;
        mReaction.postSolve(_contact, _impulse);
    }
    
}
