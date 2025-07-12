/*
 * Represents a locked gate object in the game world.
 */
package object;

import com.badlogic.gdx.graphics.Texture;

/**
 * The {@code OBJ_Gate} class defines a gate object that can be placed on the map.
 * The gate typically blocks the player's path and can be opened using a key.
 * 
 * It extends from the {@code SuperObject} class and sets the gate's default properties.
 * 
 * Author: NgKaitou
 */
public class OBJ_Gate extends SuperObject {

    /**
     * Constructs a new gate object with:
     * - Name set to "Gate"
     * - Image loaded from "Object/gate.png"
     * - Collision enabled to prevent walking through the gate
     */
    public OBJ_Gate() {
        name = "Gate";                                // Identifier for object logic
        image = new Texture("Object/gate.png");       // Visual representation of the gate
        collision = true;                             // Gate blocks movement until opened
    }
}
