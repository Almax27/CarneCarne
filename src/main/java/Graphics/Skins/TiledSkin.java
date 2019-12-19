/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import Level.CaveInSearcher;
import World.sWorld;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TiledSkin implements iSkin
{
    boolean mAlwaysOnTop = false;
    ArrayList<CaveInSearcher.TempTile> mTiles;
    CaveInSearcher.TempTile tiles[][];
    public TiledSkin(ArrayList<CaveInSearcher.TempTile> _tiles, int _width, int _height)
    {
        mTiles = _tiles;
        tiles = new CaveInSearcher.TempTile[_width][_height];
        for (CaveInSearcher.TempTile tile: _tiles)
        {
            tiles[tile.x][tile.y] = tile;
        }
    }
    public void setId(int _x, int _y, int _id)
    {
        tiles[_x][_y].setId(_id);
    }
    public void render(float _x, float _y)
    {
        for (CaveInSearcher.TempTile tile: mTiles)
        {
            if (tile.image != null)
            {
                Vec2 position = new Vec2(tile.x, tile.y);
                position.x += _x;
                position.y += _y;
                Vec2 pixelPosition = sWorld.translateToWorld(position);
                tile.image.draw(pixelPosition.x, pixelPosition.y);
            }
        }
    }

    public void setRotation(float _radians)
    {
        for (CaveInSearcher.TempTile tile: mTiles)
        {
            if (tile.image != null)
            {
                tile.image.setRotation(_radians);
            }
        }
    }
    
    public void setAlpha(float _alpha) {
        for(CaveInSearcher.TempTile tile : mTiles)
        {
            tile.image.setAlpha(_alpha);
        }
    }

    
    
    public void setDimentions(float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void restart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIsLooping(boolean _isLooping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpeed(float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRotation(String _animation, float _radians) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float activateSubSkin(String _animation, boolean _isLooping, float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deactivateSubSkin(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restart(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(String _animation, float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOffset(String _animation, Vec2 _offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vec2 getOffset(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAnimating() {
        return false;
    }

    public boolean isAnimating(String _subSkin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAt(int _index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop(String _subSkin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAt(String _subSkin, int _index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlpha(String _animation, float _alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlwaysOnTop(boolean _isAlwaysOnTop) {
        mAlwaysOnTop = _isAlwaysOnTop;
    }

    
}
