package com.ShadowMaze.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreBoard {

    
    private float scoreDisplay = 0;
    private final float speed = 25f;
    public int scoreActual;
    private final BitmapFont font;
    private Texture bgTexture;

    private final Array<Texture> iconFrames = new Array<>();
    private float animationTimer = 0f;
    private final float frameDuration = 0.07f; 
    private int currentFrameIndex = 0;

    public ScoreBoard() {
        scoreActual = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.2f);

        FileHandle dirHandle = Gdx.files.internal("score/");
        for (FileHandle file : dirHandle.list()) {
            if (file.name().toLowerCase().endsWith(".png")) {
                iconFrames.add(new Texture(file));
            }
        }

        if (iconFrames.size == 0) {
            iconFrames.add(new Texture("ui/score/coin.png")); // fallback
        }
    }

    public void addScore(int amount) {
        scoreActual += amount;            
    }

    public void update(float delta) {
        if (scoreDisplay < scoreActual) {
            scoreDisplay += speed * delta;
            if (scoreDisplay > scoreActual) {
                scoreDisplay = scoreActual;
            }
        }
    }
    
      public int getDisplayScore() {
        return (int)scoreDisplay;
    }

    public void reset() {
        this.scoreActual = 0;
    }

    public void render(SpriteBatch batch, float x, float y, float delta) {
        update(delta);
        animationTimer += Gdx.graphics.getDeltaTime();
        if (animationTimer >= frameDuration) {
            animationTimer = 0f;
            currentFrameIndex = (currentFrameIndex + 1) % iconFrames.size;
        }
//        System.out.println(score);
        Texture currentIcon = iconFrames.get(currentFrameIndex);
        int VisitX = 70;
        int VisitY = 600;
        batch.draw(currentIcon, VisitX, VisitY, 32, 32);
        font.draw(batch, String.valueOf(getDisplayScore()), VisitX + 60, VisitY + 28);
        font.draw(batch, "Score: " + scoreActual, x + 50, y + 35);
    }

    public void dispose() {
        font.dispose();
        bgTexture.dispose();
        for (Texture tex : iconFrames) {
            tex.dispose();
        }
    }
}
