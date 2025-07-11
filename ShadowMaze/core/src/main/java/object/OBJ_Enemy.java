package object;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Represents an animated enemy object on the map.
 * The enemy uses multiple textures as animation frames and is drawn relative to the player's position.
 */
public class OBJ_Enemy extends SuperObject {

    private Animation<TextureRegion> animation;   // Animation composed of texture regions
    private float stateTime = 0f;                 // Tracks elapsed time for animation
    private Array<Texture> frames = new Array<>(); // Holds original textures for disposal
    private float scale = 3f;                     // Rendering scale multiplier

    /**
     * Constructor initializes the enemy name and loads animation frames from disk.
     */
    public OBJ_Enemy() {
        name = "Enemy";

        // Load all animation frames from the "icons" folder
        Array<TextureRegion> regions = new Array<>();
        for (int i = 0; i < 4; i++) {
            // Load image as texture
            Texture tex = new Texture(Gdx.files.internal("icons/Special" + i + ".png"));
            frames.add(tex);                     // Store texture for later disposal
            regions.add(new TextureRegion(tex)); // Convert texture to region for animation
        }

        // Create animation using 0.15s per frame
        animation = new Animation<>(0.15f, regions);
    }

    /**
     * Draws the animated enemy on the screen if it's within the visible camera bounds.
     * @param screen Reference to the current GameScreen for accessing batch and camera offsets
     */
    @Override
    public void drawObject(GameScreen screen) {
        // Update the animation timer with the frame's delta time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get the current animation frame
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        // Convert world coordinates to screen coordinates relative to the knight
        int screenX = mapX - screen.knight.positionX + screen.knight.renderX;
        int screenY = mapY - screen.knight.positionY + screen.knight.renderY;

        // Only draw the enemy if it is within the screen's visible tile bounds
        if (mapX + GameScreen.TILE_SIZE > screen.knight.positionX - screen.knight.renderX
                && mapX - GameScreen.TILE_SIZE < screen.knight.positionX + screen.knight.renderX
                && mapY + GameScreen.TILE_SIZE > screen.knight.positionY - screen.knight.renderY
                && mapY - GameScreen.TILE_SIZE < screen.knight.positionY + screen.knight.renderY) {

            float frameWidth = currentFrame.getRegionWidth();   // Frame width in pixels
            float frameHeight = currentFrame.getRegionHeight(); // Frame height in pixels

            // Draw the current animation frame at the calculated screen position
            screen.batch.draw(currentFrame,
                    screenX, screenY,
                    frameWidth * scale,
                    frameHeight * scale);
        }
    }

    /**
     * Disposes all texture resources used by the enemy to free memory.
     * Must be called when the object is no longer needed.
     */
    public void dispose() {
        for (Texture tex : frames) {
            tex.dispose(); // Release texture memory
        }
    }
}
