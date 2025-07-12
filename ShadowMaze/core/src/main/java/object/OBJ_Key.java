/*
 * Represents a collectible key item in the game.
 */
package object;

import com.badlogic.gdx.graphics.Texture;

/**
 * The {@code OBJ_Key} class defines a key object that the player can pick up.
 * The key is typically used to unlock gates or other locked objects within the game.
 * 
 * This class extends {@code SuperObject} and initializes the key's name and image.
 * 
 * Author: NgKaitou
 */
public class OBJ_Key extends SuperObject {

    /**
     * Constructs a new key object with:
     * - Name set to "Key"
     * - Image loaded from "Object/key.png"
     * - Collision is assumed to be false (not blocking movement)
     */
    public OBJ_Key() {
        name = "Key";                              // Identifier used for logic checks
        image = new Texture("Object/key.png");     // Texture displayed for the key
        // No collision = player can walk over and collect it
    }

}
