/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.CarrotController;
import Graphics.Skins.CharacterSkin;
import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
class CarrotFactory implements iEntityFactory {

    public CarrotFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        HashMap animDef = new HashMap();
        
        ArrayList<CharacterSubSkin> subSkins = new ArrayList<CharacterSubSkin>();
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_fly", CharacterSubSkin.SubType.eAnimated, 133, 80, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_fly_right", CharacterSubSkin.SubType.eAnimated, 133, 80, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_stu", CharacterSubSkin.SubType.eAnimated, 133, 79, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_stu_right", CharacterSubSkin.SubType.eAnimated, 133, 79, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_att", CharacterSubSkin.SubType.eAnimated, 132, 129, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_att_right", CharacterSubSkin.SubType.eAnimated, 132, 129, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("car_corpse", CharacterSubSkin.SubType.eStatic, 132, 129, new Vec2(-30f*2,-16.55f)));
        
        animDef.put("subSkins", subSkins);
        iSkin skin = sSkinFactory.create("character", animDef);
        skin.deactivateSubSkin("car_fly");
        skin.activateSubSkin("car_fly", true, 1);
        skin.setOffset("car_fly", new Vec2(-30f*2,-16.55f));
        skin.setOffset("car_stu", new Vec2(-35.5f, -14.55f));
        skin.setOffset("car_att", new Vec2(-34.5f,-64.40f));
         skin.setOffset("car_fly_right", new Vec2(-30f*2,-16.55f));
        skin.setOffset("car_stu_right", new Vec2(-35.5f, -14.55f));
        skin.setOffset("car_att_right", new Vec2(-34.5f,-64.40f));
        
        ///FIXME the x offset isn't perfect.
        AIEntity entity = new Carrot(skin);
        HashMap parameters = new HashMap();
        parameters.put("position", position);
        parameters.put("aIEntity", entity);
        parameters.put("category", sWorld.BodyCategories.eEnemy);
        entity.setBody(sWorld.useFactory("BoxCharFactory", parameters));
        
        CarrotController controller = new CarrotController(entity);
        entity.mController = controller;
        return entity;
    }
    
}
