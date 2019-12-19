/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.BroccoliController;
import Graphics.Skins.CharacterSkin;
import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author Graham
 */
public class BroccoliFactory implements iEntityFactory {

    public BroccoliFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        HashMap animDef = new HashMap();
        
        ArrayList<CharacterSubSkin> subSkins = new ArrayList<CharacterSubSkin>();
        
        ///left 
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idle", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idleout", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idlein", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_jump", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_air", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_land", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_start", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_mid", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_end", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        //////right
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idle_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idleout_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1_idlein_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_jump_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_air_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2_land_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_start_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_mid_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3_end_right", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        

        
        animDef.put("subSkins", subSkins);
        iSkin skin = sSkinFactory.create("character", animDef);
        
        //left
        
        skin.setOffset("broc_1_idle", new Vec2(-46/2,-46));
        skin.setOffset("broc_1_idleout", new Vec2(-46/2,-46));
        skin.setOffset("broc_1_idlein", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_jump", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_air", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_land", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_start", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_mid", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_end", new Vec2(-46/2,-46));
        
        //right
        
        skin.setOffset("broc_1_idle_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_1_idleout_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_1_idlein_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_jump_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_air_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_2_land_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_start_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_mid_right", new Vec2(-46/2,-46));
        skin.setOffset("broc_3_end_right", new Vec2(-46/2,-46));
        
        
        
        AIEntity entity = new Broccoli(skin);
        HashMap parameters = new HashMap();
        parameters.put("position", position);
        parameters.put("aIEntity", entity);
        parameters.put("category", sWorld.BodyCategories.eEnemy);
        entity.mBody = sWorld.useFactory("CircleCharFactory",parameters);
        //entity.mBody.
        Fixture silly = entity.mBody.getFixtureList();
        entity.mBody.setFixedRotation(true);
        silly.setFriction(20);
        
        BroccoliController controller = new BroccoliController(entity);
        entity.mController = controller;
        return entity;
    }
    
}
