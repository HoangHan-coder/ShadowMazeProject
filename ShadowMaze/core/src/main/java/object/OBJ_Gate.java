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
public class OBJ_Gate extends SuperObject{

    public OBJ_Gate() {
        name = "Gate";
        image = new Texture("Object/gate.png");
        collision = true;    
    }        
}
