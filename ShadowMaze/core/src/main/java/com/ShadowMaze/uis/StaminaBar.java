// File: StaminaBar.java
package com.ShadowMaze.uis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StaminaBar {

    private float maxStamina;
    private float currentStamina;
    private float regenRate = 20f; // t?c ?? h?i stamina m?i gi�y
    private int x, y, width, height;
    private float displayStamina = 0f;
    private float displaySpeed = 60f; // t?c ?? hi?n th? m??t (??n v?: stamina/gi�y)
    private Texture icon;
    private int iconSize = 24; // k�ch th??c vu�ng c?a icon
    private int iconPadding = 6; // kho?ng c�ch t? icon ??n thanh

    public void regenerate(float delta) {
        if (currentStamina < maxStamina) {
            this.currentStamina += regenRate * delta;
            System.out.println(this.currentStamina);
            if (currentStamina > maxStamina) {
                currentStamina = maxStamina;
            }
        }
    }

    public StaminaBar(int x, int y, int width, int height, float maxStamina, Texture icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxStamina = maxStamina;
        this.currentStamina = maxStamina;
        this.icon = icon;
    }

    public void setCurrentStamina(float stamina) {
        this.currentStamina = Math.max(0, Math.min(maxStamina, stamina));
    }

    public void renderIcon(SpriteBatch batch) {
        if (icon != null) {
            batch.begin();
            batch.draw(icon, 130, 15, 50, 50);
            batch.end();
        }

    }

    public void renderBar(ShapeRenderer shapeRenderer) {
        float percent = currentStamina / maxStamina;
        float barWidth = width * percent;


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);


        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(x, y, barWidth, height);
        shapeRenderer.end();
    }

    public float getCurrentStamina() {
        return currentStamina;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public void update(float stamina) {
        setCurrentStamina(stamina);
    }
}
