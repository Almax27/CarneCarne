/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.HashMap;
/**
 *
 * @author alasdair
 */
public interface iEntityFactory {
    
    public Entity useFactory(HashMap _parameters);
}
