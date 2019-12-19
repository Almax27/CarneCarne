/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.Stack;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
abstract public class SlopeTile extends RootTile
{
    boolean mMutable;
    private float mBodyScale = 0.0f;
    public SlopeTile(int _id, int _slopeType, sLevel.TileType _tileType, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super (TileShape.eSlope, _id, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth);
        mMutable = true;
        mSlopeType = _slopeType;
    }
    void setImmutable()
    {
        mMutable = false;
    }
    @Override
    Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        PolygonShape shape = new PolygonShape();
        shape.m_vertexCount = 3;
        float hx = 0.5f;
        float hy = 0.5f;
        Vec2 topLeft = new Vec2(-hx, -hy);
        Vec2 topRight = new Vec2(hx, -hy);
        Vec2 bottomLeft = new Vec2(-hx, hy);
        Vec2 bottomRight = new Vec2(hx, hy);
        switch (mSlopeType)
        {
            case 0:
            {
                topLeft.y += mBodyScale;
                bottomRight.x -= mBodyScale;
                shape.m_vertices[0] = topLeft;
                shape.m_vertices[1] = bottomRight;
                shape.m_vertices[2] = bottomLeft;
                shape.m_normals[0] = new Vec2(1.0f/1.414f, -1.0f/1.414f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
            case 1:
            {
                topRight.y += mBodyScale;
                bottomLeft.x += mBodyScale;
                shape.m_vertices[0] = topRight;
                shape.m_vertices[1] = bottomRight;
                shape.m_vertices[2] = bottomLeft;
                shape.m_normals[0] = new Vec2(1.0f, 0.0f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, -1.0f/1.414f);
                break;
            }
            case 2:
            {
                topLeft.x += mBodyScale;
                bottomRight.y -= mBodyScale;
                shape.m_vertices[0] = topLeft;
                shape.m_vertices[1] = topRight;
                shape.m_vertices[2] = bottomRight;
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f, 0.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, 1.0f/1.414f);
                break;
            }
            case 3:
            {
                topRight.x -= mBodyScale;
                bottomLeft.y -= mBodyScale;
                shape.m_vertices[0] = topLeft;
                shape.m_vertices[1] = topRight;
                shape.m_vertices[2] = bottomLeft;
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f/1.414f, 1.0f/1.414f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
        }
        shape.m_centroid.x = _xTile;
        shape.m_centroid.y = _yTile;
        for (int i = 0; i < shape.m_vertexCount; ++i)
        {
            shape.m_vertices[i] = shape.m_vertices[i].add(shape.m_centroid);
        }
        return fixtureWithMaterial(shape, _body, _tile);
    }
    abstract public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid);
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        if (mMutable)
        {
            boolean boundaries[] = new boolean[2];
            boundaries[0] = false;
            boundaries[1] = false;
            
            getEdges(boundaries, _xTile, _yTile, _tileGrid);

            int textureUnit = 0;
            for (int i = 0; i < 2; i++)
            {
                if (!boundaries[i])
                    textureUnit |= (1 << i);
            }
            _stack.push(_xTile);
            _stack.push(_yTile);
            _stack.push(mId+textureUnit);
        }
    }

    void setBodyScale(float _bodyScale)
    {
        mBodyScale = 1.0f - _bodyScale;
    }
}
