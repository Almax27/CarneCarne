/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Level.Tile;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
class Explosion extends Entity
{
    int mTimer;
    PlayerEntity mKiller;
    public Explosion(PlayerEntity _killer)
    {
        super(null);
        mTimer = 30;
        mKiller = _killer;
    }

    @Override
    public void update()
    {
        mBody.setLinearVelocity(new Vec2(0,0));
        Stack<Entity> killedEntities = new Stack<Entity>();
        for (ContactEdge edge = mBody.getContactList(); edge != null; edge = edge.next)
        {
            if (edge.other.getUserData() != null)
            {
                killedEntities.add((Entity)edge.other.getUserData());
            }
            else
            {
                Fixture fixture = edge.contact.getFixtureA();
                if (fixture.getBody() == mBody)
                    fixture = edge.contact.getFixtureB();
                Tile tile = (Tile)fixture.getUserData();
                tile.setOnFire();
                if (tile.getTileType().equals(TileType.eAcid)
                        || tile.getTileType().equals(TileType.eWater))
                {
                    kill(CauseOfDeath.eMundane, this);
                    return;
                }
            }
        }
        if (mTimer == 0)
        {
            sWorld.destroyBody(mBody);
        }
        mTimer--;
        while (!killedEntities.isEmpty())
        {
            killedEntities.pop().kill(CauseOfDeath.eFire, mKiller);
        }
        //((Entity)edge.other.getUserData()).kill(CauseOfDeath.eFire, mKiller);
    }
    
    @Override
    public void render()
    {
        
    }
    
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer) // I'm invincible!
    {
        if (_killer == this)
        {
            super.kill(_causeOfDeath, _killer);
        }
    }
    
}
