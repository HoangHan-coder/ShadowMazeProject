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

/**
 * Represents the Fireball skill which can be charged and launched in a
 * direction. The fireball has animations, travels in 4 directions, and
 * disappears after a time or when out of bounds.
 */
public class Fireball {

    private Vector2 position; // Current position of the fireball
    private Vector2 velocity; // Directional velocity
    private float speed = 300f;

    private boolean active = false;
    private float lifeTime = 0f;
    private final float maxLifeTime = 2.0f;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private Array<Texture> textureList = new Array<>();

    private Entity.Direction direction;
    private float scale = 0.1f;           // Initial size (while charging)
    private final float maxScale = 2.0f;  // Maximum size after charge
    private boolean charging = true;      // Whether the fireball is charging
    private float mapWidthPixels;
    private float mapHeightPixels;

    // Animations for each direction
    private Animation<TextureRegion> animationRight;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationUp;
    private Animation<TextureRegion> animationDown;

    /**
     * Initializes animations for the fireball in all directions
     */
    public Fireball() {
        loadAnimations();
    }

    /**
     * Loads fireball animations in all 4 directions
     */
    private void loadAnimations() {
        animationRight = loadAnimationFrom("skill/fireball/right");
        animationLeft = loadAnimationFrom("skill/fireball/left");
        animationUp = loadAnimationFrom("skill/fireball/up");
        animationDown = loadAnimationFrom("skill/fireball/down");
    }

    /**
     * Loads animation frames from a folder path.
     *
     * @param folderPath The path to the direction-based animation folder
     * @return Animation object created from frames
     */
    private Animation<TextureRegion> loadAnimationFrom(String folderPath) {
        Array<TextureRegion> frames = new Array<>();
        FileHandle dir = Gdx.files.internal(folderPath);
        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);      // Load PNG texture
                textureList.add(tex);                 // Save for disposal later
                frames.add(new TextureRegion(tex));   // Convert to texture region
            }
        }
        return new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    /**
     * Constructor for launching fireball directly with a given position and
     * direction.
     *
     * @param startPos Start position of the fireball
     * @param dirr Direction the fireball should go
     */
    public Fireball(Vector2 startPos, Entity.Direction dirr) {
        if (dirr == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }

        this.position = new Vector2(startPos);
        this.direction = dirr;

        // Set velocity vector based on direction
        switch (dirr) {
            case UP ->
                velocity = new Vector2(0, -1);
            case DOWN ->
                velocity = new Vector2(0, 1);
            case LEFT ->
                velocity = new Vector2(-1, 0);
            case RIGHT ->
                velocity = new Vector2(1, 0);
            case IDLE ->
                velocity = new Vector2(0, 0); // Default for idle
            default ->
                throw new IllegalStateException("Unexpected direction value");
        }

        stateTime = 0f;
        this.active = true; // Must be active to update/render
    }

    /**
     * Loads animation frames if not using directional folders (fallback)
     */
    private void loadAnimation() {
        FileHandle dir = Gdx.files.internal("skill/fireball");
        FileHandle[] files = dir.list();

        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : files) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture texture = new Texture(file);
                textureList.add(texture);
                frames.add(new TextureRegion(texture));
            }
        }

        if (frames.isEmpty()) {
            throw new RuntimeException("No fireball frames found");
        }

        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    /**
     * Activates the fireball at the knight's position in a specified direction.
     *
     * @param knightPos The position of the knight
     * @param dir Direction to shoot fireball
     */
    public void activate(Vector2 knightPos, Entity.Direction dir) {
        this.direction = dir;
        float offset = GameScreen.TILE_SIZE / 2f;

        // Offset the starting point slightly based on direction
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

        velocity.scl(speed); // Scale direction to actual speed
        active = true;
        lifeTime = 0;
        stateTime = 0;
    }

    /**
     * Updates the state of the fireball: position, animation, lifetime.
     *
     * @param delta Time passed since last frame
     */
    public void update(float delta) {
        if (!active) {
            return;
        }

        stateTime += delta;

        if (charging) {
            // While charging, grow the scale
            if (scale < maxScale) {
                scale += delta * 2.5f;
            }
        } else {
            lifeTime += delta;

            // If time exceeded, deactivate
            if (lifeTime >= maxLifeTime) {
                active = false;
                return;
            }

            // Move the fireball forward
            position.add(velocity.cpy().scl(delta));

            // Check if out of map bounds
            if (position.x < 0 || position.x > mapWidthPixels
                    || position.y < 0 || position.y > mapHeightPixels) {
                active = false;
                System.out.println("[Fireball] Out of bounds. Deactivated.");
                return;
            }
        }
    }

    /**
     * Sets the map size in pixels (used for boundary checking).
     *
     * @param tileWidth Width of the map in tiles
     * @param tileHeight Height of the map in tiles
     */
    public void setMapSize(int tileWidth, int tileHeight) {
        this.mapWidthPixels = tileWidth * GameScreen.TILE_SIZE;
        this.mapHeightPixels = tileHeight * GameScreen.TILE_SIZE;
    }

    /**
     * Renders the fireball based on the knight's position and current animation
     * frame.
     *
     * @param batch SpriteBatch for rendering
     * @param knightWorldX X position of knight in world
     * @param knightWorldY Y position of knight in world
     */
    public void render(SpriteBatch batch, float knightWorldX, float knightWorldY) {
        if (!active) {
            return;
        }

        // Get the animation frame based on direction
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

        // Adjust width/height depending on direction
        float width = (direction == UP || direction == DOWN) ? 100 : 200;
        float height = (direction == UP || direction == DOWN) ? 200 : 100;

        // Convert world position to screen position
        float drawX = SCREEN_WIDTH / 2f + (position.x - knightWorldX) - width / 2f;
        float drawY = SCREEN_HEIGHT / 2f + (position.y - knightWorldY) - height / 2f;

        // Draw the fireball
        batch.draw(frame, drawX, drawY, width, height);
    }

    /**
     * Stops charging and launches the fireball
     */
    public void shoot() {
        this.charging = false;
        this.lifeTime = 0f;
    }

    /**
     * @return Whether the fireball is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Disposes of all textures used by the fireball
     */
    public void dispose() {
        for (Texture t : textureList) {
            t.dispose();
        }
    }
}
