/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import World.PhysicsFactories.iPhysicsFactory;
import Level.sLevel;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class SlopeFactory implements iPhysicsFactory {
    
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        sLevel.TileType tileType = (sLevel.TileType)_parameters.get("TileType");
        int slopeType = ((Integer)_parameters.get("slopeType")).intValue();
        PolygonShape shape = new PolygonShape();
        shape.m_vertexCount = 3;
        float hx = 0.5f;
        float hy = 0.5f;
        switch (slopeType)
        {
            case 0:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(1.0f/1.414f, -1.0f/1.414f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
            case 1:
            {
                shape.m_vertices[0] = new Vec2( hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(1.0f, 0.0f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, -1.0f/1.414f);
                break;
            }
            case 2:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, -hy);
                shape.m_vertices[2] = new Vec2( hx, hy);
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f, 0.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, 1.0f/1.414f);
                break;
            }
            case 3:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, -hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f/1.414f, 1.0f/1.414f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
        }
        shape.m_centroid.x = 0;
        shape.m_centroid.y = 0;
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.groupIndex = tileType.ordinal();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        BodyDef def = new BodyDef();
        //def.userData = _entity;
        def.position = new Vec2((position.x),(position.y));
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body; 
    }
}
