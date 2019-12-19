/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.RootTile.AnimationType;
import Level.sLevel.TileType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class FakeTile extends Tile
{
    Body mBody;
    TileType mTileType;
    public FakeTile(Body _body, TileType _tileType)
    {
        super(-1, null, null, -1, -1);
        mBody = _body;
        mTileType = _tileType;
    }
    
    @Override
    public TileType getTileType()
    {
        return mTileType;
    }
    @Override
    public Vec2 getWorldPosition()
    {
        return mBody.getPosition();
    }
    @Override
    public Vec2 getLocalPosition()
    {
        return mBody.getPosition();
    }
    @Override
    public void destroyFixture()
    {
        //((Entity)mBody.getUserData()).kill();
    }
    @Override
    public boolean damageTile(boolean _particles)
    {
        return true;
    }
}
