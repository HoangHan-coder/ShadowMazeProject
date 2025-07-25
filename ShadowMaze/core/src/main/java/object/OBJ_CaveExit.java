/*
 * Cave exit object class representing the winning point in the maze.
 */
package object;

import com.badlogic.gdx.graphics.Texture;

/**
 * The OBJ_CaveExit class defines the cave exit object in the game.
 * This object typically signifies the end of a level or maze when the player reaches it.
 * 
 * It extends from the SuperObject base class and initializes its image and properties.
 * 
 * Author: NgKaitou
 */
public class OBJ_CaveExit extends SuperObject {
    
    /**
     * Constructs a new cave exit object with predefined properties:
     * - Name set to "Cave"
     * - Image loaded from "Object/cave_exit.png"
     * - Collision enabled to prevent walking through it
     */
    public OBJ_CaveExit() {
        setDefault();
        isOpened = false;
    }
    
    
    
    private void setDefault() {
        name = "Cave exit";                    
        image = new Texture("Object/cave_exit_close.png");             
        collision = true; 
    }    
}
