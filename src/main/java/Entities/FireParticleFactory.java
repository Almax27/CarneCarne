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

/**
 *
 * @author alasdair
 */
class FireParticleFactory implements iEntityFactory
{

    public FireParticleFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        _parameters.put("ref", "ChilliSpit");
        iSkin skin = sSkinFactory.create("static", _parameters);
        Vec2 velocity = (Vec2)_parameters.get("velocity");
        PlayerEntity owner = (PlayerEntity)_parameters.get("owner");
        FireParticle entity = new FireParticle(skin, velocity, owner);
        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("FireParticleBody", _parameters));
        return entity;
    }
}
