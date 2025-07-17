/*
 * Base class for all interactable or visible objects on the map (e.g., key, gate, cave exit).
 */
package object;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * The {@code SuperObject} class serves as the base class for all interactive or static objects
 * in the game world, such as keys, gates, and exits. It contains common properties like name,
 * texture, collision flag, hitbox, and map position.
 * 
 * Author: NgKaitou
 */
public class SuperObject {

    /** Name of the object (used to identify in logic). */
    public String name;

    /** Texture image used to render the object. */
    public Texture image;

    /** Indicates whether the object blocks movement (e.g., gate = true, key = false). */
    public boolean collision = false;
    
    public boolean isOpened = false;

    /** Collision area of the object (hitbox). */
    public Rectangle solidArea = new Rectangle(0, 0, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);

    /** Default X offset for the hitbox. */
    public float solidAreaDefaultX = 0;

    /** Default Y offset for the hitbox. */
    public float solidAreaDefaultY = 0;

    /** X position on the map (in pixels). */
    public float mapX;

    /** Y position on the map (in pixels). */
    public float mapY;

    /**
     * Renders the object to the screen if it is within the current visible area of the player.
     *
     * @param gs The current GameScreen instance, which provides access to the player, batch, and tile size.
     */
    public void drawObject(GameScreen gs) {

        // Calculate on-screen coordinates based on player position
        float screenX = mapX - gs.knight.positionX + gs.knight.renderX;
        float screenY = mapY - gs.knight.positionY + gs.knight.renderY;

        // Check if object is within visible screen area before rendering
        if (mapX + GameScreen.TILE_SIZE > gs.knight.positionX - gs.knight.renderX
                && mapX - GameScreen.TILE_SIZE < gs.knight.positionX + gs.knight.renderX
                && mapY + GameScreen.TILE_SIZE > gs.knight.positionY - gs.knight.renderY
                && mapY - GameScreen.TILE_SIZE < gs.knight.positionY + gs.knight.renderY) {
                gs.batch.draw(image, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
            
            //debug hit box
//            int renderX = GameScreen.SCREEN_WIDTH / 2 - (GameScreen.TILE_SIZE / 2);
//            int renderY = GameScreen.SCREEN_HEIGHT / 2 - (GameScreen.TILE_SIZE / 2);
//            ShapeRenderer shapeRenderer = new ShapeRenderer();
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.rect(
//                    renderX + solidArea.x,
//                    renderY + solidArea.y,
//                    solidArea.width,
//                    solidArea.height
//            );
//
//            shapeRenderer.end();
        }
    }

}
