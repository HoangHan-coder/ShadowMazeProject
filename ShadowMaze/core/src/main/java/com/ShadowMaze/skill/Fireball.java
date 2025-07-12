/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.skill;

import com.ShadowMaze.model.Entity;
import com.ShadowMaze.model.Entity.Direction;
import static com.ShadowMaze.model.Entity.Direction.DOWN;
import static com.ShadowMaze.model.Entity.Direction.LEFT;
import static com.ShadowMaze.model.Entity.Direction.RIGHT;
import static com.ShadowMaze.model.Entity.Direction.UP;
import com.ShadowMaze.model.Knight;
import com.ShadowMaze.model.Map;
import com.ShadowMaze.screen.GameScreen;
import static com.ShadowMaze.screen.GameScreen.SCREEN_HEIGHT;
import static com.ShadowMaze.screen.GameScreen.SCREEN_WIDTH;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import static com.badlogic.gdx.math.MathUtils.map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Fireball {

    private Vector2 position;
    private Vector2 velocity;
    private float speed = 300f;

    private boolean active = false;
    private float lifeTime = 0f;
    private final float maxLifeTime = 2.0f;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private Array<Texture> textureList = new Array<>();

    private Entity.Direction direction;
    private float scale = 0.1f;           // B?t ??u nh?
    private final float maxScale = 2.0f;  // C?u l?a s? l?n d?n ??n ?ây
    private boolean charging = true;      // ?ang t? l?c
    private float mapWidthPixels;
    private float mapHeightPixels;
    private Animation<TextureRegion> animationRight;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationUp;
    private Animation<TextureRegion> animationDown;


    public Fireball() {
        loadAnimations();
    }

    private void loadAnimations() {
        animationRight = loadAnimationFrom("skill/fireball/right");
        animationLeft = loadAnimationFrom("skill/fireball/left");
        animationUp = loadAnimationFrom("skill/fireball/up");
        animationDown = loadAnimationFrom("skill/fireball/down");
    }

    private Animation<TextureRegion> loadAnimationFrom(String folderPath) {
        Array<TextureRegion> frames = new Array<>();
        FileHandle dir = Gdx.files.internal(folderPath);
        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                textureList.add(tex);
                frames.add(new TextureRegion(tex));
            }
        }
        return new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    public Fireball(Vector2 startPos, Entity.Direction dirr) {
        if (dirr == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }

        this.position = new Vector2(startPos);
        this.direction = dirr;
        switch (dirr) {
            case UP ->
                velocity = new Vector2(0, -1);
            case DOWN ->
                velocity = new Vector2(0, 1);
            case LEFT ->
                velocity = new Vector2(-1, 0);
            case RIGHT ->
                velocity = new Vector2(1, 0);
            case IDLE -> {
                // Gi?i pháp t?m th?i, có th? set h??ng m?c ??nh
                velocity = new Vector2(0, 0); // không bay
            }
            default ->
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        stateTime = 0f;
        this.active = true; // ? B?t bu?c ?? render ho?t ??ng!
    }

    private void loadAnimation() {
        FileHandle dir = Gdx.files.internal("skill/fireball");
        FileHandle[] files = dir.list();

        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : files) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture texture = new Texture(file);
                textureList.add(texture);
                TextureRegion region = new TextureRegion(texture);
                frames.add(region);
            }
        }

        if (frames.isEmpty()) {
            throw new RuntimeException("Không tìm th?y ?nh fireball");
        }

        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    public void activate(Vector2 knightPos, Entity.Direction dir) {
        this.direction = dir;
        float offset = GameScreen.TILE_SIZE / 2f;

        switch (dir) {
            case UP -> {
                position = new Vector2(knightPos.x, knightPos.y + offset);
                velocity = new Vector2(0, -1);
            }
            case DOWN -> {
                position = new Vector2(knightPos.x, knightPos.y - offset);
                velocity = new Vector2(0, 1);
            }
            case LEFT -> {
                position = new Vector2(knightPos.x - offset, knightPos.y);
                velocity = new Vector2(-1, 0);
            }
            case RIGHT -> {
                position = new Vector2(knightPos.x, knightPos.y);
                velocity = new Vector2(1, 0);
            }
            default -> {
                position = new Vector2(knightPos);
                velocity = new Vector2(0, 0);
            }
        }

        velocity.scl(speed);
        active = true;
        lifeTime = 0;
        stateTime = 0;
    }

    public void update(float delta) {
        if (!active) {
            return;
        }

        stateTime += delta;

        if (charging) {
            if (scale < maxScale) {
                scale += delta * 2.5f;
            }
        } else {
            lifeTime += delta;
            if (lifeTime >= maxLifeTime) {
                active = false;
                return;
            }

            position.add(velocity.cpy().scl(delta));

            // ? Ki?m tra n?u ra kh?i b?n ?? thì t?t skill
            if (position.x < 0 || position.x > mapWidthPixels
                    || position.y < 0 || position.y > mapHeightPixels) {
                active = false;
                System.out.println("? Fireball ?ã ra kh?i b?n ??, hu? skill.");
                return;
            }
        }
    }

    public void setMapSize(int tileWidth, int tileHeight) {
        this.mapWidthPixels = tileWidth * GameScreen.TILE_SIZE;
        this.mapHeightPixels = tileHeight * GameScreen.TILE_SIZE;
    }

    public void render(SpriteBatch batch, float knightWorldX, float knightWorldY) {
        if (!active) {
            return;
        }

        TextureRegion frame = switch (direction) {
            case LEFT ->
                animationLeft.getKeyFrame(stateTime);
            case RIGHT ->
                animationRight.getKeyFrame(stateTime);
            case UP ->
                animationUp.getKeyFrame(stateTime);
            case DOWN ->
                animationDown.getKeyFrame(stateTime);
            default ->
                animationRight.getKeyFrame(stateTime);
        };

        float width = (direction == UP || direction == DOWN) ? 100 : 200;
        float height = (direction == UP || direction == DOWN) ? 200 : 100;

        float drawX = SCREEN_WIDTH / 2f + (position.x - knightWorldX) - width / 2f;
        float drawY = SCREEN_HEIGHT / 2f + (position.y - knightWorldY) - height / 2f;

        batch.draw(frame, drawX, drawY, width, height);
    }

    public void shoot() {
        this.charging = false;
        this.lifeTime = 0f;
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        for (Texture t : textureList) {
            t.dispose();
        }
    }
}
