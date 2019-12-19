/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class SpikeTile extends NonEdibleTile
{
    private static String[] getAnimationsNames()
    {
        String[] strings = new String[AnimationType.eAnimationsMax.ordinal()];
        strings[AnimationType.eFireHit.ordinal()] = "Default";
        strings[AnimationType.eDamage.ordinal()] = "Default";
        strings[AnimationType.eSpawn.ordinal()] = "Default";
        strings[AnimationType.eSpit.ordinal()] = "Default";
        strings[AnimationType.eJump.ordinal()] = "Hard";
        return strings;
    }
    public SpikeTile(int _id, int _rotation)
    {
        super(_id, TileType.eSpikes, true);
        mSlopeType = _rotation;
    }
    
    @Override
    protected Shape createShape(int _xTile, int _yTile)
    {
        //PolygonShape shape = new PolygonShape();
        //shape.setAsBox(0.25f, 0.25f, new Vec2(_xTile,_yTile).add(new Vec2(0.25f,0.25f)), 0);
        //return shape;
        PolygonShape shape = new PolygonShape();
        switch (mSlopeType)
        {
            case 0:
            {
                shape.setAsBox(0.5f, 0.25f, new Vec2(_xTile,_yTile+0.25f), 0.0f);
                break;
            }
            case 1:
            {
                shape.setAsBox(0.25f, 0.5f, new Vec2(_xTile-0.25f,_yTile), 0.0f);
                break;
            }
            case 2:
            {
                shape.setAsBox(0.5f, 0.25f, new Vec2(_xTile,_yTile-0.25f), 0.0f);
                break;
            }
            case 3:
            {
                shape.setAsBox(0.25f, 0.5f, new Vec2(_xTile+0.25f,_yTile), 0.0f);
                break;
            }
            default:
                assert(false);
        }
        return shape;
    }
    
}
