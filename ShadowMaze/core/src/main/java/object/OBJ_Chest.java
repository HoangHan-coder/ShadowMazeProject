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
public class OBJ_Chest extends SuperObject {

    public OBJ_Chest() {
        name = "Big chest";
        setDefault();
    }

    private void setDefault() {
        image = new Texture("Object/big_chest.png");
    }

}
