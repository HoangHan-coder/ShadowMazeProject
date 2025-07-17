package com.ShadowMaze.uis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * HpBar represents a visual health bar using NinePatch textures.
 * It animates the display of HP changes smoothly and supports full rendering with LibGDX's SpriteBatch.
 * 
 * Author: NgKaitou
 */
public class HpBar {

    private float maxHp;         // Maximum HP value
    private float currentHp;     // Actual HP value
    private float displayHp = 0f; // Smoothly animated HP for visual display
    private float displaySpeed = 60f; // Speed of HP animation per second

    private int x, y, width, height; // Position and size of the health bar on screen

    private NinePatch bgPatch;   // Background patch (empty bar)
    private NinePatch fillPatch; // Filled patch (current HP level)

    /**
     * Constructor for HpBar.
     * 
     * @param x       X position on screen
     * @param y       Y position on screen
     * @param width   Width of the health bar
     * @param height  Height of the health bar
     * @param maxHp   Maximum HP value
     * @param bg      Background texture (usually empty)
     * @param fill    Fill texture (represents the current HP)
     */
    public HpBar(int x, int y, int width, int height, float maxHp, Texture bg, Texture fill) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHp = maxHp;
        this.currentHp = maxHp; // Start with full health

        // Create 9-patch regions from textures with 5px stretchable borders
        this.bgPatch = new NinePatch(bg, 5, 5, 5, 5);
        this.fillPatch = new NinePatch(fill, 5, 5, 5, 5);
    }

    /**
     * Sets the current HP value.
     * Ensures the value stays within valid bounds (0 to maxHp).
     * 
     * @param hp The new HP value to set.
     */
    public void setCurrentHp(float hp) {
        this.currentHp = Math.max(0, Math.min(maxHp, hp)); // Clamp between 0 and maxHp
    }

    /**
     * Updates the visual display HP to animate smoothly toward the actual HP.
     * 
     * @param delta Time passed since last frame (used for smooth animation)
     */
    public void update(float delta) {
        if (Math.abs(displayHp - currentHp) > 0.5f) {
            if (displayHp < currentHp) {
                displayHp += displaySpeed * delta;
                if (displayHp > currentHp) {
                    displayHp = currentHp;
                }
            } else {
                displayHp -= displaySpeed * delta;
                if (displayHp < currentHp) {
                    displayHp = currentHp;
                }
            }
        } else {
            // Snap to target value if difference is small
            displayHp = currentHp;
        }
    }

    /**
     * Renders the HP bar using the current displayHp value.
     * 
     * @param batch The SpriteBatch to draw the bar to.
     */
    public void render(SpriteBatch batch) {
        float ratio = displayHp / maxHp; // Compute how much of the bar to fill
        batch.begin();
        bgPatch.draw(batch, x, y, width, height);                   // Draw background bar
        fillPatch.draw(batch, x, y, width * ratio, height);         // Draw filled HP portion
        batch.end();
    }

    /**
     * @return The current (actual) HP value.
     */
    public float getCurrentHp() {
        return currentHp;
    }

    /**
     * @return The maximum HP value.
     */
    public float getMaxHp() {
        return maxHp;
    }
}
