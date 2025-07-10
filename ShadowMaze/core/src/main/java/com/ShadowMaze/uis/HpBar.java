package com.ShadowMaze.uis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class HpBar {

    private float maxHp;
    private float currentHp;
    private float displayHp = 0f;
    private float displaySpeed = 60f;

    private int x, y, width, height;

    private NinePatch bgPatch;
    private NinePatch fillPatch;

    public HpBar(int x, int y, int width, int height, float maxHp, Texture bg, Texture fill) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHp = maxHp;
        this.currentHp = maxHp;

        this.bgPatch = new NinePatch(bg, 5, 5, 5, 5);
        this.fillPatch = new NinePatch(fill, 5, 5, 5, 5);
    }

    public void setCurrentHp(float hp) {
        this.currentHp = Math.max(0, Math.min(maxHp, hp));
    }

    public void update(float delta) {
        if (Math.abs(displayHp - currentHp) > 0.5f) {
            if (displayHp < currentHp) {
                displayHp += displaySpeed * delta;
                if (displayHp > currentHp) displayHp = currentHp;
            } else {
                displayHp -= displaySpeed * delta;
                if (displayHp < currentHp) displayHp = currentHp;
            }
        } else {
            displayHp = currentHp;
        }
    }

    public void render(SpriteBatch batch) {
        float ratio = displayHp / maxHp;

        batch.begin();
        bgPatch.draw(batch, x, y, width, height);                 
        fillPatch.draw(batch, x, y, width * ratio, height);          
        batch.end();
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public float getMaxHp() {
        return maxHp;
    }
}
