package com.ShadowMaze.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreBoard {

    private int score;
    private BitmapFont font;
    private Texture bgTexture;

    private Array<Texture> iconFrames = new Array<>();
    private float animationTimer = 0f;
    private float frameDuration = 0.07f; // m?i frame 0.07s
    private int currentFrameIndex = 0;

    public ScoreBoard() {
        score = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.2f);

        // Quét th? m?c ui/score/ và thêm các file .png
        FileHandle dirHandle = Gdx.files.internal("score/");
        for (FileHandle file : dirHandle.list()) {
            if (file.name().toLowerCase().endsWith(".png")) {
                iconFrames.add(new Texture(file));
            }
        }

        // N?u không có ?nh nào -> thêm ?nh m?c ??nh ?? tránh l?i
        if (iconFrames.size == 0) {
            iconFrames.add(new Texture("ui/score/coin.png")); // fallback
        }
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public void render(SpriteBatch batch, float x, float y) {
        // C?p nh?t animation
        animationTimer += Gdx.graphics.getDeltaTime();
        if (animationTimer >= frameDuration) {
            animationTimer = 0f;
            currentFrameIndex = (currentFrameIndex + 1) % iconFrames.size;
        }
        System.out.println(score);
        Texture currentIcon = iconFrames.get(currentFrameIndex);
        int VisitX = 70;
        int VisitY = 600;
        batch.draw(currentIcon, VisitX,VisitY, 32, 32);
        font.draw(batch, String.valueOf(score), VisitX + 60,VisitY + 28);
        font.draw(batch, "Score: " + score, x + 50, y + 35);
    }

    public void dispose() {
        font.dispose();
        bgTexture.dispose();
        for (Texture tex : iconFrames) {
            tex.dispose();
        }
    }
}
