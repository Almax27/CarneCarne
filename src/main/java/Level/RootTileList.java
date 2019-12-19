/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.MelonSkinTile.SkinDirection;
import Level.RootTile.AnimationType;
import Level.Tile.Direction;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class RootTileList {
    
    private static ArrayList<RootTile> mRootTiles;
    
    public RootTileList(TiledMap _tiledMap)
    {
        HashMap<String, sLevel.TileType> typeMap = new HashMap<String, sLevel.TileType>();
        typeMap.put("Ice", sLevel.TileType.eIce);
        typeMap.put("Acid", sLevel.TileType.eAcid);
        typeMap.put("Swim", sLevel.TileType.eWater);
        typeMap.put("Edible", sLevel.TileType.eEdible);
        typeMap.put("Swingable", sLevel.TileType.eSwingable);
        typeMap.put("NonEdible", sLevel.TileType.eIndestructible);
        typeMap.put("Bouncy", sLevel.TileType.eBouncy);
        typeMap.put("Gum", sLevel.TileType.eGum);
        typeMap.put("Tar", sLevel.TileType.eTar);
        typeMap.put("Chilli", sLevel.TileType.eChilli);
        typeMap.put("MelonFlesh", sLevel.TileType.eMelonFlesh);
        typeMap.put("MelonSkin", sLevel.TileType.eMelonSkin);
        HashMap<String, SkinDirection> directionMap = new HashMap<String, SkinDirection>();
        directionMap.put("Right", SkinDirection.eRight);
        directionMap.put("Down", SkinDirection.eDown);
        directionMap.put("Left", SkinDirection.eLeft);
        directionMap.put("Up", SkinDirection.eUp);
        directionMap.put("DownLeft", SkinDirection.eDownLeft);
        directionMap.put("DownRight", SkinDirection.eDownRight);
        directionMap.put("UpLeft", SkinDirection.eUpLeft);
        directionMap.put("UpRight", SkinDirection.eUpRight);
        
        HashMap<String, Direction> fromDirection = new HashMap<String, Direction>();
        fromDirection.put("Left", Direction.eFromRight);
        fromDirection.put("Up", Direction.eFromDown);
        fromDirection.put("Right", Direction.eFromLeft);
        fromDirection.put("Down", Direction.eFromUp);
        int idsSize = 0;
        for (int i = 0; i < _tiledMap.getTileSetCount(); i++)
        {
            idsSize += _tiledMap.getTileSet(i).lastGID - _tiledMap.getTileSet(i).firstGID;
        }
        String zoomZoomName = _tiledMap.getMapProperty("ZoomZoomName", "Meat");
        mRootTiles = new ArrayList<RootTile>();
        mRootTiles.add(new EmptyTile());
        for (int i = 1; i < idsSize;)
        {
            String shape = _tiledMap.getTileProperty(i, "Type", "None");
            String typeString = _tiledMap.getTileProperty(i, "Material", "Edible");
            int maxHealth = new Integer(_tiledMap.getTileProperty(i, "MaxHealth", "1")).intValue();
            String defaultAnimationsName = _tiledMap.getTileProperty(i, "Animationsname", "Default");
            String animationsNames[] = new String[RootTile.AnimationType.eAnimationsMax.ordinal()];
            animationsNames[AnimationType.eFireHit.ordinal()] = _tiledMap.getTileProperty(i, "FireHitAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eDamage.ordinal()] = _tiledMap.getTileProperty(i, "DamageAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eSpawn.ordinal()] = _tiledMap.getTileProperty(i, "SpawnAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eSpit.ordinal()] = _tiledMap.getTileProperty(i, "SpitAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eJump.ordinal()] = _tiledMap.getTileProperty(i, "JumpAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eNom.ordinal()] = _tiledMap.getTileProperty(i, "NomAnimation", defaultAnimationsName);
            boolean regrows = new Boolean(_tiledMap.getTileProperty(i,"Regrows","true")).booleanValue();
            boolean anchor = Boolean.valueOf(_tiledMap.getTileProperty(i, "Anchor", "true")).booleanValue();
            boolean isFlammable = Boolean.valueOf(_tiledMap.getTileProperty(i, "Flammable", "false")).booleanValue();
            sLevel.TileType type = typeMap.get(typeString);
            int assertion = type.ordinal();
            //assertion = 0xB00B135;
            if (!shape.equals("None"))
            {
                if (shape.equals("Block"))
                {
                    int size = new Integer(_tiledMap.getTileProperty(i, "Size", "1")).intValue();
                    for (int ii = 0; ii < size; ii++)
                    {
                        RootTile tile = new BlockTile(i, type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                        RootTile everBurning = null;
                        for (int rootId = i + 16; i < rootId; i++)
                        {
                            boolean isEverBurning = Boolean.parseBoolean(_tiledMap.getTileProperty(i, "Everburning", "false"));
                            if (isFlammable && i == rootId-1) /// FIXME don't know why this is needed, tile property isn't working
                                isEverBurning = true;
                            if (isEverBurning)
                            {
                                if (everBurning == null)
                                {
                                    everBurning = new BlockTile(rootId-16, type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                                    everBurning.setEverburning(true);
                                }
                                mRootTiles.add(everBurning);
                            }
                            else mRootTiles.add(tile);
                        }
                        int health = maxHealth;
                        while (maxHealth > 1)
                        {
                            maxHealth--;
                            RootTile tile2 = new BlockTile(i-16, type, animationsNames, regrows, anchor, false, maxHealth);
                            tile.setNext(tile2);
                            tile2.setNext(tile);
                            tile = tile2;
                        }
                        maxHealth = health;
                    }
                }
                else if (shape.equals("Spikes"))
                {
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        mRootTiles.add(new SpikeTile(i,ii));
                        i++;
                    }
                }
                else if (shape.equals("Slope"))
                {
                    RootTile tile = new RightDownSlope(i,type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                    tile.mNext = tile;
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(tile); 
                    
                    tile = new LeftDownSlope(i,type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                    tile.mNext = tile;
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(tile); 
                    
                    tile = new LeftUpSlope(i,type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                    tile.mNext = tile;
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(tile); 
                    
                    tile = new RightUpSlope(i,type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                    tile.mNext = tile;
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(tile);
                        
                }
                else if (shape.equals("NonEdible"))
                {
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        mRootTiles.add(new NonEdibleTile(i, type, anchor));
                        i++;
                    }
                }
                else if (shape.equals("NonEdibleEdge"))
                {
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    float bodyScale = Float.valueOf(_tiledMap.getTileProperty(i, "BodyScale", "1.0"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        mRootTiles.add(new NonEdibleEdgeTile(i, type, Direction.values()[ii], animationsNames, bodyScale));
                        i++;
                    }
                }
                else if (shape.equals("NonEdibleSlope"))
                {
                    SlopeTile tile;
                    float bodyScale = Float.valueOf(_tiledMap.getTileProperty(i, "BodyScale", "1.0"));
                    
                    tile = new RightDownSlope(i,type, animationsNames, false, false, false, maxHealth);
                    tile.setImmutable();
                    tile.setBodyScale(bodyScale);
                    mRootTiles.add(tile);
                    i++;
                    tile = new LeftDownSlope(i,type, animationsNames, false, false, false, maxHealth);
                    tile.setImmutable();
                    tile.setBodyScale(bodyScale);
                    mRootTiles.add(tile);
                    i++;
                    tile = new LeftUpSlope(i,type, animationsNames, false, false, false, maxHealth);
                    tile.setImmutable();
                    tile.setBodyScale(bodyScale);
                    mRootTiles.add(tile);
                    i++;
                    tile = new RightUpSlope(i,type, animationsNames, false, false, false, maxHealth);
                    tile.setImmutable();
                    tile.setBodyScale(bodyScale);
                    mRootTiles.add(tile);
                    i++;
                }
                else if (shape.equals("Water"))
                {
                    for (int rootId = i; i < rootId + 4; i++)
                    {
                        mRootTiles.add(new WaterTile(rootId, type, animationsNames));
                    }
                }
                else if (shape.equals("Zoomzoom"))
                {
                    String directionString = _tiledMap.getTileProperty(i, "Direction", "Right");
                    SkinDirection direction = directionMap.get(directionString);
                    mRootTiles.add(new ZoomzoomTile(i, direction, zoomZoomName));
                    i++;
                }
                else if (shape.equals("MelonSkin"))
                {
                    for (int rootId = i; i < rootId + 16; i++)
                    {
                        String directionString = _tiledMap.getTileProperty(i, "Direction", "Right");
                        SkinDirection direction = directionMap.get(directionString);
                        mRootTiles.add(new MelonSkinTile(i, type, direction, maxHealth));
                    }
                }
                else if (shape.equals("Edge"))
                {
                    Direction direction = fromDirection.get(_tiledMap.getTileProperty(i, "Direction", "Down"));
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        RootTile tile = new EdgeTile(i, type, direction, animationsNames, regrows, anchor, isFlammable, maxHealth);
                        for (int rootId = i; i < rootId + 2; i++)
                        {
                            mRootTiles.add(tile);
                        }
                        int health = maxHealth;
                        while (maxHealth > 1)
                        {
                            maxHealth--;
                            RootTile tile2 = new EdgeTile(i-2, type, direction, animationsNames, regrows, anchor, false, maxHealth);
                            tile.setNext(tile2);
                            tile2.setNext(tile);
                            /*for (int rootId = i + 16; i < rootId; i++)
                            {
                                mRootTiles.add(tile2);
                            }*/
                            tile = tile2;
                        }
                        maxHealth = health;
                    }
                }
                else if (shape.equals("Line"))
                {
                    Direction direction = fromDirection.get(_tiledMap.getTileProperty(i, "Direction", "Down"));
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        RootTile tile = new LineTile(i, type, direction, animationsNames, regrows, anchor, isFlammable, maxHealth);
                        for (int rootId = i; i < rootId + 4; i++)
                        {
                            mRootTiles.add(tile);
                        }
                        int health = maxHealth;
                        while (maxHealth > 1)
                        {
                            maxHealth--;
                            RootTile tile2 = new LineTile(i-4, type, direction, animationsNames, regrows, anchor, false, maxHealth);
                            tile.setNext(tile2);
                            tile2.setNext(tile);
                            /*for (int rootId = i + 16; i < rootId; i++)
                            {
                                mRootTiles.add(tile2);
                            }*/
                            tile = tile2;
                        }
                        maxHealth = health;
                    }
                }
                else
                {
                    throw new UnsupportedOperationException("Tileshape " + shape + " not recognised");
                }
            }
            else
            {
                mRootTiles.add(new EmptyTile());
                i++;
            }
        }
    }

    public sLevel.TileType getTileType(int _id)
    {
        return sLevel.TileType.eEdible;
    }
    
    public RootTile get(int index)
    {
        if (index >= mRootTiles.size())
        {
            return mRootTiles.get(0);
        }
        return mRootTiles.get(index);
    }
}
