/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.Tile;
import Level.sLevel;
import Level.sLevel.TileType;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class GumLandEvent extends iEvent
{
    Body mBody;
    Vec2 mNormal;
    int mRootId;
    Tile mTile;
    public GumLandEvent(Vec2 _normal, Body _body, int _rootId, Tile _tile)
    {
        mBody = _body;
        mRootId = _rootId;
        mNormal = _normal.clone();
        mTile = _tile;
    }
    public String getName()
    {
        return "GumLandEvent";
    }

    public String getType()
    {
        return getName();
    }
    
    @Override
    public boolean process()
    {
        if(mTile != null && mBody.getFixtureList() != null)
        {
            int xPos = (int)mTile.getLocalPosition().x;//Math.round(mBody.getPosition().x);
            int yPos = (int)mTile.getLocalPosition().y;//Math.round(mBody.getPosition().y);
            boolean wasDestroyed = false;
            if(mNormal.x == 1)
            {
                wasDestroyed = place(xPos+1, yPos);
            }
            else if(mNormal.x == -1)
            {
                wasDestroyed = place(xPos-1, yPos);
            }
            else if(mNormal.y == 1)
            {
                wasDestroyed = place(xPos, yPos+1);
            }
            else if(mNormal.y == -1)
            {
                wasDestroyed = place(xPos, yPos-1);
            }     
            else if(mNormal.x > 0) //test for corner collisions that give non-axis-aligned normals
            {
                if(Math.abs(mNormal.x) > Math.abs(mNormal.y)) //if x out weighs y, do in direction of x
                    wasDestroyed = place(xPos+1, yPos);
                else if(mNormal.y > 0) //else determine top or bottom
                    wasDestroyed = place(xPos, yPos+1);
                else if(mNormal.y > 0)
                    wasDestroyed = place(xPos, yPos-1);
            }
            else if(mNormal.x < 0)
            {
                if(Math.abs(mNormal.x) > Math.abs(mNormal.y)) //if x out weighs y, do in direction of x
                    wasDestroyed = place(xPos-1, yPos);
                else if(mNormal.y > 0) //else determine top or bottom
                    wasDestroyed = place(xPos, yPos+1);
                else if(mNormal.y > 0)
                    wasDestroyed = place(xPos, yPos-1);
            }
            else wasDestroyed = false;
            if (!wasDestroyed)
            {
                Vec2 position = sWorld.getLastGumEaten();
                //sLevel.placeTile((int)position.x, (int)position.y, mRootId);
                sLevel.getTileGrid().addRegrowingTile((int)position.x, (int)position.y, mRootId);
            }
            sWorld.destroyBody(mBody);
            mTile = null;
        }
        return false;
    }
    
    private boolean place(int _x, int _y)
    {
        if (sLevel.getTileGrid().get(_x, _y).getRootTile().getTileType().equals(TileType.eEmpty))
        {
            sLevel.getTileGrid().placeTile(_x, _y, mRootId);
            return true;
        }
        else return false;
    }
    
}
