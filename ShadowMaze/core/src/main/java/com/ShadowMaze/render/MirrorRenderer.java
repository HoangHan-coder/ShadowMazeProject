package com.ShadowMaze.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

/**
 * Responsible for rendering animated mirror images (e.g. illusions or visual clones)
 * at predefined tile-based positions on the screen.
 * 
 * It supports scaling and offsetting for smoother visual alignment.
 * 
 * Usage:
 * - Supply an Animation<TextureRegion> and a list of [x, y] tile positions.
 * - Call update(delta) each frame.
 * - Call render(...) with proper offsets and tile size.
 * 
 * Example use-case: duplicate characters in magic mirrors or hallucinations.
 * 
 * Author: letan
 */
public class MirrorRenderer {

    private Animation<TextureRegion> animation;    // Animation to draw for each mirror clone
    private float stateTime = 0f;                  // Time counter for animation progress
    private List<int[]> mirrorPositions;           // List of tile positions [x, y] for drawing mirrors

    /**
     * Constructs a MirrorRenderer with the given animation and tile positions.
     * 
     * @param animation The looping animation for mirrored sprites
     * @param positions List of tile-based [x, y] positions to draw the mirror image
     */
    public MirrorRenderer(Animation<TextureRegion> animation, List<int[]> positions) {
        this.animation = animation;
        this.mirrorPositions = positions;
    }

    /**
     * Updates the animation timer. Must be called once per frame.
     * 
     * @param delta Time in seconds since last frame
     */
    public void update(float delta) {
        stateTime += delta;
    }

    /**
     * Renders the mirror animation at each specified tile position.
     * 
     * @param batch The SpriteBatch used to draw
     * @param tileSize The size of a tile (usually 48 or 64)
     * @param offsetX Offset in pixels to align map's origin with screen
     * @param offsetY Same as offsetX but for vertical direction
     */
    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY) {
        float scale = 2.8f; // Enlarge mirror sprite to make it stand out
        TextureRegion frame = animation.getKeyFrame(stateTime, true); // Get current frame (looping)

        for (int[] pos : mirrorPositions) {
            int x = pos[0]; // tile X
            int y = pos[1]; // tile Y

            float drawX = offsetX + x * tileSize; // convert tile to pixel
            float drawY = offsetY + y * tileSize;

            float scaledSize = tileSize * scale;
            float offset = (scaledSize - tileSize) / 2f; // center the scaled sprite

            // Draw the scaled animation frame centered at tile
            batch.draw(frame,
                    drawX - offset, drawY - offset,
                    scaledSize, scaledSize
            );
        }
    }
}
