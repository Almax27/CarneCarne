/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class CarcassFactory implements iEntityFactory
{
    String mTextures[];
    Vec2 mOffsets[];
    public CarcassFactory()
    {
        mTextures = new String[Entity.CauseOfDeath.eCauseOfDeathMax.ordinal()];
        mOffsets = new Vec2[Entity.CauseOfDeath.eCauseOfDeathMax.ordinal()];
        mTextures[Entity.CauseOfDeath.eSpikes.ordinal()] = "characters/spikes";
        mTextures[Entity.CauseOfDeath.eFire.ordinal()] = "characters/fire";
        mTextures[Entity.CauseOfDeath.eAcid.ordinal()] = "characters/acid";

        Vec2 noOffset = new Vec2(0,0);
        mOffsets[Entity.CauseOfDeath.eSpikes.ordinal()] = noOffset;
        mOffsets[Entity.CauseOfDeath.eFire.ordinal()] = noOffset;
        mOffsets[Entity.CauseOfDeath.eAcid.ordinal()] = noOffset;
    }
    public Entity useFactory(HashMap _parameters)
    {
        int cause = ((Entity.CauseOfDeath)_parameters.get("causeOfDeath")).ordinal();
        _parameters.put("ref", mTextures[cause] + (String)_parameters.get("characterType"));
        iSkin skin = sSkinFactory.create("static", _parameters);
        Entity entity = new Carcass(skin, _parameters.get("killer"),mOffsets[cause]);

        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("CarcassBody", _parameters));

        return entity;
    }
}
