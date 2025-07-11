// File: StaminaBar.java
package com.ShadowMaze.uis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * StaminaBar displays a stamina meter that regenerates over time and can be rendered
 * as both an icon and a colored bar using LibGDX's rendering tools.
 * 
 * Author: NgKaitou
 */
public class StaminaBar {

    private float maxStamina;         // Maximum stamina value
    private float currentStamina;     // Actual current stamina value
    private float regenRate = 20f;    // Regeneration speed (stamina/second)

    private int x, y, width, height;  // Position and size of the stamina bar

    private float displayStamina = 0f;  // For future animated display (not yet animated here)
    private float displaySpeed = 60f;   // Intended display animation speed (unused)

    private Texture icon;            // Icon representing stamina (e.g., a thunderbolt)
    private int iconSize = 24;       // Size of the icon (currently not used)
    private int iconPadding = 6;     // Distance from icon to bar (currently not used)

    /**
     * Constructs a StaminaBar instance.
     *
     * @param x           X coordinate of the bar
     * @param y           Y coordinate of the bar
     * @param width       Width of the bar
     * @param height      Height of the bar
     * @param maxStamina  Maximum stamina value
     * @param icon        Icon texture to be shown near the bar
     */
    public StaminaBar(int x, int y, int width, int height, float maxStamina, Texture icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxStamina = maxStamina;
        this.currentStamina = maxStamina;
        this.icon = icon;
    }

    /**
     * Regenerates stamina over time. Call this every frame with delta time.
     * 
     * @param delta The time since the last frame
     */
    public void regenerate(float delta) {
        if (currentStamina < maxStamina) {
            this.currentStamina += regenRate * delta;
            System.out.println(this.currentStamina);
            if (currentStamina > maxStamina) {
                currentStamina = maxStamina;
            }
        }
    }

    /**
     * Sets the current stamina value (clamped between 0 and maxStamina).
     *
     * @param stamina The new stamina value
     */
    public void setCurrentStamina(float stamina) {
        this.currentStamina = Math.max(0, Math.min(maxStamina, stamina));
    }

    /**
     * Renders the stamina icon using the SpriteBatch.
     * The position and size are hardcoded here.
     * 
     * @param batch The SpriteBatch used for drawing
     */
    public void renderIcon(SpriteBatch batch) {
        if (icon != null) {
            batch.draw(icon, 130, 15, 50, 50); // Draw icon at a fixed screen location
        }
    }

    /**
     * Renders the stamina bar using a ShapeRenderer.
     * Background is dark gray; filled part is orange.
     * 
     * @param shapeRenderer The ShapeRenderer used for drawing the bar
     */
    public void renderBar(ShapeRenderer shapeRenderer) {
        float percent = currentStamina / maxStamina; // Percent of stamina remaining
        float barWidth = width * percent;            // Width of the filled bar portion

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);     // Background color
        shapeRenderer.rect(x, y, width, height);     // Draw background

        shapeRenderer.setColor(Color.ORANGE);        // Fill color
        shapeRenderer.rect(x, y, barWidth, height);  // Draw filled bar portion

        shapeRenderer.end();
    }

    /**
     * @return The current stamina value
     */
    public float getCurrentStamina() {
        return currentStamina;
    }

    /**
     * @return The maximum stamina value
     */
    public float getMaxStamina() {
        return maxStamina;
    }

    /**
     * Updates stamina directly (same as setCurrentStamina).
     *
     * @param stamina New stamina value
     */
    public void update(float stamina) {
        setCurrentStamina(stamina);
    }
}
