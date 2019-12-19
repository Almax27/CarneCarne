/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.Tile;

/**
 *
 * @author alasdair
 */
public class DynamicTileDestructionEvent extends iEvent
{
    Tile mTile;
    public DynamicTileDestructionEvent(Tile _tile)
    {
        mTile = _tile;
    }
    
    @Override
    public boolean process()
    {
        if (mTile.getFixture() != null)
            mTile.destroyFixture();
        return true;
    }

    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "DynamicTileDestructionEvent";
    }
}
