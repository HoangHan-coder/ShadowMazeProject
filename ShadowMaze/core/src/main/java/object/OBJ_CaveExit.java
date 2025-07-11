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
public class OBJ_CaveExit extends SuperObject{
    public OBJ_CaveExit() {
        name = "Cave";
        image = new Texture("Object/cave_exit.png");
        collision = true;
    }
}
