/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author NgKaitou
 */
public class OBJ_Gem extends SuperObject{

    public OBJ_Gem() {
        name = "";
        image = new Texture("Object/gem_empty.png");
        collision = true;
    }
    
}
