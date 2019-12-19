/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.FireParticle;
import Level.Tile;
import World.sWorld;

/**
 *
 * @author alasdair
 */
public class TarFireEvent extends iEvent {

    Tile mTile;
    FireParticle mFireParticle;
    public TarFireEvent(Tile _tile, FireParticle _fireParticle)
    {
        mTile = _tile;
        mFireParticle = _fireParticle;
    }

    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "TarFireEvent";
    }
    
    @Override
    public boolean process()
    {
        mTile.setOnFire();
        sWorld.destroyBody(mFireParticle.getBody()); /// FIXME memory leak
        return true;
    }
}
