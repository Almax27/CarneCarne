/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Level.sLevel;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class BroccoliExplosionFactory implements iEntityFactory
{

    public Entity useFactory(HashMap _parameters)
    {
        Entity entity = new BroccoliExplosion(null, (Integer)_parameters.get("duration"));
        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("BroccoliExplosionBody", _parameters));
        
        Vec2 position = (Vec2)_parameters.get("position");
        grahamIsAFatCunt((int)position.x, (int)position.y);
        
        return entity;
    }
    
    private class DirectionSearch
    {
        protected int xPos, yPos, xDir, yDir;
        protected DirectionSearch prev, next;
        DirectionSearch(int _xPos, int _yPos, int _xDir, int _yDir)
        {
            xDir = _xDir;
            yDir = _yDir;
            xPos = _xPos + xDir;
            yPos = _yPos + yDir;
        }
        
        DirectionSearch setNext(DirectionSearch _next)
        {
            next = _next;
            next.prev = this;
            return next;
        }
        protected void progress()
        {
            xPos = xPos + xDir;
            yPos = yPos + yDir;
        }
        int step(int _health)
        {
            switch (sLevel.getTileGrid().get(xPos, yPos).getRootTile().getTileType())
            {
                case eEdible:
                case eSwingable:
                {
                    if (sLevel.getTileGrid().get(xPos, yPos).damageTile(false))
                    {
                        sLevel.getTileGrid().get(xPos, yPos).destroyFixture();
                        progress();
                    }
                    return _health-1;
                }
                case eEmpty:
                {
                    progress();
                    return _health-1;
                }
                default:
                {
                    return _health;
                }
            }
        }
    }
    private class DiagonalDirectionSearch extends DirectionSearch
    {
        boolean emittedSideways;
        DiagonalDirectionSearch(int _xPos, int _yPos, int _xDir, int _yDir)
        {
            super(_xPos, _yPos, _xDir, _yDir);
            emittedSideways = false;
        }
        @Override
        protected void progress()
        {
            if (!emittedSideways)
            {
                emittedSideways = true;
                
                DirectionSearch left = new DirectionSearch(xPos, yPos, xDir, 0);
                DirectionSearch right = new DirectionSearch(xPos, yPos, 0, yDir);

                prev.next = left;
                left.prev = prev;
                left.next = this;
                prev = left;

                next.prev = right;
                right.next = next;
                right.prev = this;
                next = right;
            }
            else
            {
                emittedSideways = false;
                super.progress();
            }
        }
        
    }
    private void grahamIsAFatCunt(int x, int y)
    {
        int health = 140;
        DirectionSearch last, search;
        last = search = new DirectionSearch(x,y, 0,1);
        search = search.setNext(new DirectionSearch(x,y, 1,0));
        search = search.setNext(new DirectionSearch(x,y, 0,-1));
        search = search.setNext(new DirectionSearch(x,y, -1,0));
        
        search = search.setNext(new DiagonalDirectionSearch(x,y, 1,1));
        search = search.setNext(new DiagonalDirectionSearch(x,y, 1,-1));
        search = search.setNext(new DiagonalDirectionSearch(x,y, -1,-1));
        search = search.setNext(new DiagonalDirectionSearch(x,y, -1,1));
        
        search.setNext(last);
        
        int prevHealth = health;
        last = null;
        while (health != 0)
        {
            health = search.step(health);
            if (prevHealth != health)
            {
                prevHealth = health;
                last = search;
            }
            else if (last == search)
            {
                break;
            }
            search = search.next;
        }
    }
}
