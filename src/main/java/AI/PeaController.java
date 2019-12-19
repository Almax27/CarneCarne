/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Level.sLevel;
import Level.sLevel.PathInfo;
import World.sWorld.BodyCategories;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author Graham
 */
public class PeaController extends iAIController
{
    private boolean mToggle;
    private float mSpeed;
    public PeaController(AIEntity _entity)
    {
        super(_entity);
        mToggle = true;
        mEntity.setMoveSpeed(0.9f);
        mSpeed = 0.175f;
    }
    public void update()
    {  
        ContactEdge contact = mEntity.getBody().getContactList();
        while (contact != null)
        {
            if(contact.contact.isTouching())
            {
                Fixture other = contact.contact.m_fixtureA;
                
                if (other.m_body == mEntity.getBody()) //if other is this body: swap
                    other = contact.contact.m_fixtureB;
                
                if (other.m_body.m_fixtureList.getFilterData().categoryBits == (1 << BodyCategories.ePlayer.ordinal()))
                {
                    if(other.m_body.getPosition().x < mEntity.getBody().getPosition().x) mToggle = false;
                    else mToggle = true;
                }
                else if (other.m_body.m_fixtureList.getFilterData().categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
                {
                    if(other.m_body.getPosition().x < mEntity.getBody().getPosition().x) mToggle = false;
                    else mToggle = true;
                }
            }
            contact = contact.next;
        }
        
        if(mToggle)
        {
            mEntity.walk(-mSpeed);
            if(sLevel.getPathInfo((int)(mEntity.getBody().getPosition().x), (int)(mEntity.getBody().getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = false;
            }
        }
        else if(!mToggle)
        {
            mEntity.walk(mSpeed);
            if(sLevel.getPathInfo((int)(mEntity.getBody().getPosition().x+1), (int)(mEntity.getBody().getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = true;
            }
        }
    }
}
