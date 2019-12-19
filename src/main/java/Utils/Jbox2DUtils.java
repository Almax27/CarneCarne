/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;

/**
 *
 * @author A203945
 */
public class Jbox2DUtils {
    //sets as box with position at topright
    public static void setAsBox(PolygonShape _shape, Vec2 _pos, Vec2 _dimensions) //FIXME: attempt to fix 0.5f issue
    {
        Vec2 center = _dimensions.mul(0.5f);
        _shape.m_vertexCount = 4;
        _shape.m_vertices[0] = new Vec2(0,0);
        _shape.m_vertices[1] = new Vec2(_dimensions.x,0);
        _shape.m_vertices[2] = new Vec2(_dimensions.x,_dimensions.y);
        _shape.m_vertices[3] = new Vec2(0,_dimensions.y);
//        _shape.m_vertices[0] = new Vec2(-center.x,-center.y);
//        _shape.m_vertices[1] = new Vec2(center.x,-center.y);
//        _shape.m_vertices[2] = new Vec2(center.x,center.y);
//        _shape.m_vertices[3] = new Vec2(-center.x,center.y);
        _shape.m_normals[0] = new Vec2(0,-1);
        _shape.m_normals[1] = new Vec2(1,0);
        _shape.m_normals[2] = new Vec2(0,1);
        _shape.m_normals[3] = new Vec2(-1,0);

    }
}
