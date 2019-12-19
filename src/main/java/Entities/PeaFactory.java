/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PeaController;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class PeaFactory implements iEntityFactory {

    public PeaFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        HashMap animDef = new HashMap();
        animDef.put("ref", "characters/pea");
        iSkin skin = sSkinFactory.create("static", animDef);
        AIEntity entity = new Pea(skin);
        HashMap parameters = new HashMap();
        parameters.put("position", position);
        parameters.put("aIEntity", entity);
        parameters.put("category", sWorld.BodyCategories.eEnemy);
        entity.setBody(sWorld.useFactory("CircleCharFactory",parameters));
        PeaController controller = new PeaController(entity);
        entity.mController = controller;
        return entity;
    }
    
}
