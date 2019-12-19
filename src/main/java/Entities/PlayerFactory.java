/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import Events.AreaEvents.CheckPointZone;
import Events.PlayerCreatedEvent;
import Events.sEvents;
import Graphics.Skins.CharacterSkin;
import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import org.jbox2d.common.Vec2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author alasdair
 */
public class PlayerFactory implements iEntityFactory {
    
    private static List<String> charSkinList = Arrays.asList("mexican", "english", "russian", "american");
    private static List<Vec2> charDimensionsList = Arrays.asList(new Vec2(130,115), new Vec2(113,107), new Vec2(80,90), new Vec2(81,87));
    private static List<Vec2> charOffsetList = Arrays.asList(new Vec2(-33,-36), new Vec2(-26,-28), new Vec2(-8,-14), new Vec2(-7,-16));
    
    boolean used;
    public PlayerFactory()
    {
        used = false;
    }
    public Entity useFactory(HashMap _parameters)
    {
        /// NOTE maybe put a event trigger in here
        Vec2 position = (Vec2)_parameters.get("position");
        CheckPointZone checkPoint = (CheckPointZone)_parameters.get("checkPoint");
        //if (!used)
        {
            used = true;
            int playerNum = (Integer)_parameters.get("playerNumber");
            
            //create a CharacterSubSkin for each layer of the character skin
            ArrayList<CharacterSubSkin> subSkins = new ArrayList<CharacterSubSkin>();
            //they must be in render order
            subSkins.add(new CharacterSkin.CharacterSubSkin("carne_fly", CharacterSubSkin.SubType.eAnimated, 198, 139, new Vec2(-70,-53)));
            ArrayList<String> bodies = new ArrayList(Arrays.asList("bdy","gum","edi","spi","wtr"));
            for(String body : bodies) 
            {
                subSkins.add(new CharacterSkin.CharacterSubSkin(body, CharacterSubSkin.SubType.eStatic, 64, 64, new Vec2(0,0)));
            }
            
            subSkins.add(new CharacterSkin.CharacterSubSkin("idle_burp2", CharacterSubSkin.SubType.eAnimated, 130, 115, new Vec2(-33,-36)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("idle_burp2_right", CharacterSubSkin.SubType.eAnimated, 130, 115, new Vec2(-33,-36)));
            
            Vec2 dim = charDimensionsList.get(playerNum);
            Vec2 offset = charOffsetList.get(playerNum);
            subSkins.add(new CharacterSkin.CharacterSubSkin(charSkinList.get(playerNum), CharacterSubSkin.SubType.e32Dir, (int)dim.x, (int)dim.y, offset));
            subSkins.add(new CharacterSkin.CharacterSubSkin(charSkinList.get(playerNum)+"Open", CharacterSubSkin.SubType.e32Dir, (int)dim.x, (int)dim.y, offset));
            
            
            
            subSkins.add(new CharacterSkin.CharacterSubSkin("pea_stun_large", CharacterSubSkin.SubType.eAnimated, 107, 39, new Vec2(-21.5f,0)));
            //draw tongue last
            subSkins.add(new CharacterSkin.CharacterSubSkin("tng", CharacterSubSkin.SubType.eStatic, 5, 5, new Vec2(32,32)));
            
            subSkins.add(new CharacterSkin.CharacterSubSkin("Player" + (playerNum+1), CharacterSubSkin.SubType.eStatic, 32, 32, new Vec2(0,-96), true));
            
            for (int i = 1; i <= 4; i++)
                subSkins.add(new CharacterSkin.CharacterSubSkin("finishedRaceAtPosition" + i, CharacterSubSkin.SubType.eStatic, 64, 64, new Vec2(-59,-124), true));
            
            
            HashMap params = new HashMap();
            
            params.put("subSkins", subSkins);
            
            iSkin skin = sSkinFactory.create("character", params);
            
            skin.activateSubSkin("bdy", false, 0);
            //skin.activateSubSkin("Player" + (playerNum+1), false, 0); //this is only activated for football (see FootballMode)

            PlayerEntity entity = new PlayerEntity(skin, checkPoint);
            entity.setPlayerNumber(playerNum+1); //we need to set the player number to activate it based on gamemode
            HashMap parameters = new HashMap();
            parameters.put("position", position);
            parameters.put("aIEntity", entity);
            parameters.put("category", sWorld.BodyCategories.ePlayer);
            entity.setBody(sWorld.useFactory("PlayerFactory",parameters));
            PlayerInputController controller = new PlayerInputController(entity, playerNum, charSkinList.get(playerNum));
            entity.setController(controller);
            sWorld.addPlayer(entity.getBody());
            sEvents.triggerEvent(new PlayerCreatedEvent(entity, playerNum));
            return entity;
        }
    }
}
