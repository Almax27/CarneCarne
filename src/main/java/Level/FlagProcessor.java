/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import AI.SimplePlatformController;
import AI.StupidPlatformController;
import AI.iPlatformController;
import AI.sPathFinding;
import Entities.CaveIn;
import Entities.Entity;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.AreaEvents.CheckPointZone;
import Events.AreaEvents.GoalZone;
import Events.AreaEvents.PlayerSpawnZone;
import Events.AreaEvents.RaceEndZone;
import Events.AreaEvents.RaceStartZone;
import Events.AreaEvents.ToolTipZone;
import Events.FootballSpawnEvent;
import Events.GoalSpawnEvent;
import Events.TutorialSpawnEvent;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Level.Lighting.sLightsManager;
import Level.sLevel.TileType;
import Sound.sSound;
import States.Game.sGameStateInfo;
import World.sWorld;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Color;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class FlagProcessor
{
    private Vector<Vec2> playerPositions;
    private HashMap<String,AreaEvents> eventMap;
    private int mLayerIndex;
    AreaEvents areaEvents[][];

    enum AreaEvents
    {
        eNoEvent,
        eRaceStartZone,
        eRaceCheckPoint,
        eRaceEndZone,
        eFootballGoal,
        eTooltip,
        eAreaEventsMax
    }
    int lowestX, lowestY, highestX, highestY;
    private class CheckPoint
    {
        int x,y,x2,y2;
        //int mNumber;
        public CheckPoint(int _x, int _y, int _x2, int _y2, int _number)
        {
            x = _x;
            y = _y;
            x2 = _x2 + 1;
            y2 = _y2 + 1;
            //mNumber = _number;
        }
    }
    private class PlayerSpawnPoint extends CheckPoint
    {
        int mPlayerNumber;
        public PlayerSpawnPoint(int _x, int _y, int _x2, int _y2, int _playerNumber)
        {
            super (_x, _y, _x2, _y2, 0);
            mPlayerNumber = _playerNumber;
        }
    }
    private class RaceStartZoneParameters extends CheckPoint
    {
        public RaceStartZoneParameters(int _x, int _y, int _x2, int _y2)
        {
            super (_x, _y, _x2, _y2, 0);
        }
    }
    private class RaceCheckPointParameters extends CheckPoint
    {
        public RaceCheckPointParameters(int _x, int _y, int _x2, int _y2, int _number)
        {
            super (_x, _y, _x2, _y2, 0);
        }
    }
    private class RaceEndZoneParameters extends CheckPoint
    {
        public RaceEndZoneParameters(int _x, int _y, int _x2, int _y2)
        {
            super (_x, _y, _x2, _y2, 0);
        }
    }
    private Stack<PlayerSpawnPoint> spawnPoints;
    private Vector<RaceCheckPointParameters> checkPoints;
    private Vector<CheckPoint> goals;
    RaceStartZoneParameters raceStartZone;
    RaceEndZoneParameters raceEndZone;
    HashMap<String,StartBarrier> mBarriers;
    void cleanup()
    {
        
        for (StartBarrier barrier: mBarriers.values())
        {
            barrier.cleanup();
        }
    }
    FlagProcessor(TiledMap _tiledMap, int _levelLayerIndex, Body _levelBody, TileGrid _tileGrid)
    {
        mBarriers = new HashMap<String,StartBarrier>();
        raceStartZone = null;
        spawnPoints = new Stack<PlayerSpawnPoint>();
        checkPoints = new Vector<RaceCheckPointParameters>();
        goals = new Vector<CheckPoint>();
        lowestX = lowestY = Integer.MAX_VALUE;
        highestX = highestY = Integer.MIN_VALUE;
        int width = _tiledMap.getWidth();
        int height = _tiledMap.getHeight();
        mLayerIndex = _tiledMap.getLayerIndex("Flags");
        playerPositions = new Vector<Vec2>();
        int tutorialPlayers = 0;
        HashMap parameters = new HashMap();
        areaEvents = new AreaEvents[width][height];
        eventMap = new HashMap<String, AreaEvents>();
        eventMap.put("RaceStart", AreaEvents.eRaceStartZone);
        eventMap.put("RaceCheckPoint", AreaEvents.eRaceCheckPoint);
        eventMap.put("RaceEnd", AreaEvents.eRaceEndZone);
        eventMap.put("FootballGoal", AreaEvents.eFootballGoal);
        eventMap.put("Tooltip", AreaEvents.eTooltip);
        eventMap.put("None", AreaEvents.eNoEvent);
        for (int i = 0; i < width; i++)
        {
            for (int ii = 0; ii < height; ii++)
            {
                int id = _tiledMap.getTileId(i, ii, mLayerIndex);
                String spawn = _tiledMap.getTileProperty(id, "Spawn", "None");
                String eventString = _tiledMap.getTileProperty(id, "Event", "None");
                AreaEvents event = eventMap.get(eventString);
                if (!event.equals(AreaEvents.eNoEvent))
                {
                    processAreaEvents(_tiledMap, event, i,ii);
                    switch (event)
                    {
                        case eRaceStartZone:
                        {
                            //sEvents.addNewAreaEvent(new RaceStartZone(lowestX, lowestY, highestX, highestY, null));
                            if (raceStartZone == null)
                                raceStartZone = new RaceStartZoneParameters(lowestX, lowestY, highestX, highestY);
                            break;
                        }
                        case eRaceCheckPoint:
                        {
                            //sEvents.addNewAreaEvent(new RaceStartZone(lowestX, lowestY, highestX, highestY, null));
                            int checkPointNumber = new Integer(_tiledMap.getTileProperty(id, "Number", "Too many fat chicks error"));
                            if (checkPoints.size() <= checkPointNumber || checkPoints.get(checkPointNumber) == null)
                            {
                                RaceCheckPointParameters zone = new RaceCheckPointParameters(lowestX, lowestY, highestX, highestY, checkPointNumber);
                                if (checkPoints.size() <= checkPointNumber)
                                {
                                    checkPoints.setSize(checkPointNumber+1);
                                }
                                checkPoints.set(checkPointNumber, zone);
                            }
                            break;
                        }
                        case eRaceEndZone:
                        {
                            //sEvents.addNewAreaEvent(new RaceStartZone(lowestX, lowestY, highestX, highestY, null));
                            if (raceEndZone == null)
                                raceEndZone = new RaceEndZoneParameters(lowestX, lowestY, highestX, highestY);
                            break;
                        }
                        case eFootballGoal:
                        {
                            int goalNumber = new Integer(_tiledMap.getTileProperty(id, "Number", "Too many fat chicks error"));
                            if (goals.size() <= goalNumber || goals.get(goalNumber) == null)
                            {
                                CheckPoint zone = new CheckPoint(lowestX, lowestY, highestX, highestY, goalNumber);
                                if (goals.size() <= goalNumber)
                                {
                                    goals.setSize(goalNumber+1);
                                }
                                goals.set(goalNumber, zone);
                            }
                            break;
                        }
                        case eTooltip:
                        {
                            String string = _tiledMap.getTileProperty(id, "String", "None");
                            String tooltipType = _tiledMap.getTileProperty(id, "TooltipType", "None");
                            ToolTipZone zone = new ToolTipZone(lowestX, lowestY, highestX + 1, highestY + 1, string, tooltipType);
                            zone = null; /// Self registering
                            break;
                        }
                    }
                    lowestX = lowestY = Integer.MAX_VALUE;
                    highestX = highestY = Integer.MIN_VALUE;
                }
                if (!spawn.equals("None"))
                {
                    if (spawn.equals("Light"))
                    {
                        //Vec2 _position, Color _color, float _constantAttentuation, float _radius, float _quadraticAttentuation
                        float r = new Float(_tiledMap.getTileProperty(id, "R", "0"));
                        float g = new Float(_tiledMap.getTileProperty(id, "G", "0"));
                        float b = new Float(_tiledMap.getTileProperty(id, "B", "0"));
                        Color color = new Color(r,g,b);
                        float constAtt = new Float(_tiledMap.getTileProperty(id, "ConstAtt", "0"));
                        float radius = new Float(_tiledMap.getTileProperty(id, "Radius", "200"));
                        float quadAtt = new Float(_tiledMap.getTileProperty(id, "QuadAtt", "0"));
                        //create light source translated to world space
                        sLightsManager.createLightSource(new Vec2(i,ii), radius, color, constAtt, quadAtt); //FIXME: assumes 64x64 tiles
                        sParticleManager.createSystem("LightGround", new Vec2(i,ii).mul(64.0f).add(new Vec2(32,32)), -1);
                    }
                    else if (spawn.equals("Broccoli"))
                    {
                        parameters.put("position",new Vec2(i,ii));
                        sEntityFactory.create("Broccoli",parameters);
                    }
                    else if(spawn.equals("Carrot"))
                    {
                        parameters.put("position",new Vec2(i,ii));
                        sEntityFactory.create("Carrot",parameters);

                    }
                    else if(spawn.equals("Pea"))
                    {
                        parameters.put("position",new Vec2(i,ii));
                        sEntityFactory.create("Pea",parameters);
                    }
                    else if(spawn.equals("Football"))
                    {
                        parameters.put("position",new Vec2(i,ii));
                        Entity entity = sEntityFactory.create("Football",parameters);
                        sEvents.triggerEvent(new FootballSpawnEvent(entity));
                    }
                    else if (spawn.equals("Player"))
                    {
                        int player = playerPositions.size();
                        player = Integer.valueOf(_tiledMap.getTileProperty(id, "Number", String.valueOf(player)));
                        if (playerPositions.size() < player+1)
                            playerPositions.setSize(player+1);
                        playerPositions.set(player, new Vec2(i,ii));
                    }
                    else if (spawn.equals("SeeSaw"))
                    {
                        Vec2 dimensions = new Vec2(0,0);
                        dimensions.x = new Float(_tiledMap.getTileProperty(id, "Width", "192.0"));
                        dimensions.y = new Float(_tiledMap.getTileProperty(id, "Height", "64.0"));
                        dimensions = dimensions.mul(1.0f/64.0f);
                        parameters.put("dimensions",dimensions);
                        parameters.put("ref",_tiledMap.getTileProperty(id, "Image","Error, image not defined"));
                        parameters.put("position",new Vec2(i,ii));
                        sEntityFactory.create("SeeSaw", parameters);
                    }
                    else if (spawn.equals("StartBarrier"))
                    {
                        String barrierName = _tiledMap.getTileProperty(id, "BarrierName", "null");
                        if (barrierName.equals("null"))
                        {
                            throw new UnsupportedOperationException("Set the value of BarrierName"); /// Graham: You'll want to set it to "StartGate"
                        }
                        StartBarrier mBarrier;
                        if (!mBarriers.containsKey(barrierName))
                        {
                            mBarrier = new StartBarrier(barrierName);
                            mBarriers.put(barrierName, mBarrier);
                        }
                        else
                        {
                            mBarrier = mBarriers.get(barrierName);
                        }
                        mBarrier.addTile(i,ii,_tiledMap.getTileId(i, ii, mLayerIndex));
                    }
                    else if (spawn.equals("Tutorial"))
                    {
                        if(tutorialPlayers < sGameStateInfo.mPlayerCount)
                            sEvents.triggerEvent(new TutorialSpawnEvent(i,ii, tutorialPlayers++));
                    }
                    else if (spawn.equals("Platform"))
                    {                        
                        PlatformCaveInSearcher search = new PlatformCaveInSearcher(_tileGrid, _tiledMap, _levelLayerIndex, _levelBody);
                        search.destroy(i,ii, null, TileType.eEdible);
                        Body platformBody = search.getCreatedBody();
                        if (platformBody != null)
                        {
                            iPlatformController controller = null;
                            String platformType = _tiledMap.getTileProperty(id, "PlatformType", "Error, platform type not defined");
                            String speed = _tiledMap.getTileProperty(id, "Speed", "1");
                            String movement = _tiledMap.getTileProperty(id,"Movement", "Horizontal");
                            float speedf = Float.valueOf(speed);
                            if (platformType.equals("Stupid"))
                            {
                                controller = new StupidPlatformController();
                            }
                            else if(platformType.equals("Simple"))
                            {
                                controller = new SimplePlatformController(speedf, movement);
                            }
                            CaveIn cavein = (CaveIn)platformBody.getUserData();
                            cavein.setPlatformController(controller);
                            controller.setTileGrid(cavein);
                        }
                    }
                }
            }
        }
    }
    public void run()
    {
        for (StartBarrier mBarrier: mBarriers.values())
        {
            mBarrier.enable();
        }
        HashMap parameters = new HashMap();
        CheckPointZone startZone = null;
        if (raceEndZone != null)
        {
            CheckPointZone zone = new RaceEndZone(raceEndZone.x, raceEndZone.y, raceEndZone.x2, raceEndZone.y2, checkPoints.size()+1);
            for (int i = checkPoints.size()-1; i >= 0; i--)
            {
                RaceCheckPointParameters params = checkPoints.get(i);
                try
                {
                    zone = new CheckPointZone(params.x, params.y, params.x2, params.y2, i, zone);
                }
                catch (NullPointerException e)
                {
                    
                }
            }
            if (raceStartZone == null)
            {
                RaceCheckPointParameters params = checkPoints.get(0);
                startZone = zone = new CheckPointZone(params.x, params.y, params.x2, params.y2, 0, zone);
            }
            else
            {
                startZone = new RaceStartZone(raceStartZone.x, raceStartZone.y, raceStartZone.x2, raceStartZone.y2, zone, sGameStateInfo.mPlayerCount);
            }
        }
        for (int i = goals.size()-1; i >= 0; i--)
        {
            CheckPoint params = goals.get(i);
            GoalZone zone = new GoalZone(params.x, params.y, params.x2, params.y2, i);
            sEvents.triggerEvent(new GoalSpawnEvent(zone));
        }
        int players =  sGameStateInfo.mPlayerCount;
        //int players = sGameStateInfo.mPlayerCount;
        if(players == 3)
            sWorld.addPlayer(null); //add an extra view port so we have 2x2
        while (!playerPositions.isEmpty() && players > 0)
        {
            Vec2 position = playerPositions.remove(playerPositions.size()-1);
            parameters.put("position", position);
            parameters.put("playerNumber", --players);
            parameters.put("checkPoint", new PlayerSpawnZone((int)position.x, (int)position.y, (int)position.x+1, (int)position.y+1, startZone));
            PlayerEntity player = (PlayerEntity)sEntityFactory.create("Player",parameters);
            sPathFinding.addPlayer(player);
            sSound.addPlayer(player);
        }
    }
    private class Tile
    {
        int x, y;
        public Tile(int _x, int _y)
        {
            x = _x;
            y = _y;
        }
    }
    private void processAreaEvents(TiledMap _tiledMap, AreaEvents _event, int _x, int _y)
    {
        if (areaEvents[_x][_y] == null)
        {
            Stack<Tile> tiles = new Stack<Tile>();
            tiles.add(new Tile(_x, _y));
            check(_tiledMap, _event, _x, _y, tiles);
            while (!tiles.isEmpty())
            {
                Tile tile = tiles.pop();
                check(_tiledMap, _event, tile.x-1, tile.y, tiles);
                check(_tiledMap, _event, tile.x+1, tile.y, tiles);
                check(_tiledMap, _event, tile.x, tile.y-1, tiles);
                check(_tiledMap, _event, tile.x, tile.y+1, tiles);
            }
        }
    }
    private void check(TiledMap _tiledMap, AreaEvents _event, int _x, int _y, Stack<Tile> _tiles)
    {
        if (areaEvents[_x][_y] == null)
        {
            int id = _tiledMap.getTileId(_x, _y, mLayerIndex);
            AreaEvents event = eventMap.get(_tiledMap.getTileProperty(id, "Event", "None"));
            if (event.equals(_event))
            {
                areaEvents[_x][_y] = event;
                _tiles.add(new Tile(_x, _y));
                if (_x < lowestX)
                    lowestX = _x;
                if (_y < lowestY)
                    lowestY = _y;
                if (_x > highestX)
                    highestX = _x;
                if (_y > highestY)
                    highestY = _y;
            }
        }
    }
}
