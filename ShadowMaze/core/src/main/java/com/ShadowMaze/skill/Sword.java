package com.ShadowMaze.skill;

import com.ShadowMaze.model.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ShadowMaze.model.Entity.Direction;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.screen.GameScreen;

public class Sword {

    private float speed = 200f;
    private boolean active;

    private Array<Texture> textureList = new Array<>();
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float width, height;
    private Entity.Direction direction;
    private float lifeTimer = 0f;
    private float maxLifeTime = 5.5f; // ví d?: skill t?n t?i 1.5 giây
    private float offsetX;
    private float offsetY;
    private Knight knight;
    private Vector2 position;  // V? trí th?c trên b?n ??
    private Vector2 velocity;  // H??ng bay

    public Sword(Direction direction) {
        this.direction = direction;
        active = false;

        String path = switch (direction) {
            case LEFT, RIGHT ->
                "skill/1";
            case UP ->
                "skill/down";
            case DOWN ->
                "skill/up";
            default ->
                "skill/1"; // fallback
        };

        FileHandle dir = Gdx.files.internal(path);
        FileHandle[] files = dir.list();

        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : files) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture texture = new Texture(file);
                textureList.add(texture);
                TextureRegion region = new TextureRegion(texture);
                frames.add(new TextureRegion(region));
            }
        }

        if (frames.size == 0) {
            throw new RuntimeException("Không tìm th?y ?nh trong th? m?c " + path);
        }

        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        width = frames.get(0).getRegionWidth();
        height = frames.get(0).getRegionHeight();
    }

    public void activate(Knight knight, Direction direction) {
        active = true;
        stateTime = 0;
        lifeTimer = 0; // reset th?i gian s?ng
        this.direction = direction;
        switch (direction) {
            case LEFT -> {
                offsetX = -GameScreen.TILE_SIZE / 2f;
                offsetY = 0;
            }
            case RIGHT -> {
                offsetX = GameScreen.TILE_SIZE / 2f;
                offsetY = 0;
            }
            case UP ->
                offsetY = +GameScreen.TILE_SIZE / 2f;
            case DOWN ->
                offsetY = -GameScreen.TILE_SIZE / 2f;
        }
    }

    public void update(float delta) {
        if (!active) {
            return;
        }

        stateTime += delta;
        lifeTimer += delta;

        if (lifeTimer >= maxLifeTime) {
            deactivate();
        }
    }

    public void render(SpriteBatch batch, float knightWorldX, float knightWorldY, int knightRenderX, int knightRenderY) {
        if (!active) {
            return;
        }

        TextureRegion frame = animation.getKeyFrame(stateTime, false);

        if (animation.isAnimationFinished(stateTime)) {
            active = false;
            return;
        }

        float drawX = knightRenderX;
        float drawY = knightRenderY;
        float offset = GameScreen.TILE_SIZE / 2f - 15; // ??y ra 1 n?a tile - 8px ?? g?n h?n
        // Tính toán v? trí xu?t hi?n skill d?a trên h??ng
        switch (direction) {
            case UP -> {
                drawY -= GameScreen.TILE_SIZE - offset; // V? skill phía trên ??u knight
            }
            case DOWN -> {
                drawY += GameScreen.TILE_SIZE - offset - 30; // V? skill phía d??i
            }
            case LEFT -> {
                drawX -= GameScreen.TILE_SIZE - offset - 10;
            }
            case RIGHT -> {
                drawX += GameScreen.TILE_SIZE - offset - 10;
            }
        }

        // Flip ?nh n?u c?n
        if (direction == Direction.LEFT && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (direction == Direction.RIGHT && frame.isFlipX()) {
            frame.flip(true, false);
        }

        batch.draw(
                frame,
                drawX,
                drawY,
                frame.getRegionWidth() * 2f,
                frame.getRegionHeight() * 2f
        );
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void dispose() {
        for (Texture t : textureList) {
            t.dispose();
        }
    }
}
