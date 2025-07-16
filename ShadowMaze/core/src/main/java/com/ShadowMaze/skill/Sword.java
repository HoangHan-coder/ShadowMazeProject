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

/**
 * Represents a Sword skill effect that plays an animation 
 * in a specific direction relative to the knight.
 */
public class Sword {

    private float speed = 200f;
    private boolean active;

    private Array<Texture> textureList = new Array<>();
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float width, height;
    private Entity.Direction direction;
    private float lifeTimer = 0f;
    private float maxLifeTime = 5.5f; // How long the sword effect lasts
    private float offsetX;
    private float offsetY;
    private Knight knight;
    private Vector2 position;  // Absolute position on the map
    private Vector2 velocity;  // Movement direction

    /**
     * Loads the sword skill animation based on direction.
     * @param direction Direction of the sword attack (UP, DOWN, LEFT, RIGHT)
     */
    public Sword(Direction direction) {
        this.direction = direction;
        active = false;

        // Choose folder based on direction
        String path = switch (direction) {
            case LEFT, RIGHT -> "skill/1";
            case UP -> "skill/down";
            case DOWN -> "skill/up";
            default -> "skill/1"; // Fallback path
        };

        FileHandle dir = Gdx.files.internal(path);
        FileHandle[] files = dir.list();

        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : files) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture texture = new Texture(file); // Load texture
                textureList.add(texture); // Save for disposal
                TextureRegion region = new TextureRegion(texture);
                frames.add(new TextureRegion(region)); // Add frame
            }
        }

        if (frames.size == 0) {
            throw new RuntimeException("No images found in folder: " + path);
        }

        animation = new Animation<>(0.1f, frames); // Create animation
        stateTime = 0f;

        width = frames.get(0).getRegionWidth();
        height = frames.get(0).getRegionHeight();
    }

    /**
     * Activates the sword skill at knights current position and direction.
     * @param knight The knight using the skill
     * @param direction The direction in which the skill will be performed
     */
    public void activate(Knight knight, Direction direction) {
        active = true;
        stateTime = 0;
        lifeTimer = 0; // Reset the skills lifetime
        this.direction = direction;

        // Set offset based on attack direction
        switch (direction) {
            case LEFT -> {
                offsetX = -GameScreen.TILE_SIZE / 2f;
                offsetY = 0;
            }
            case RIGHT -> {
                offsetX = GameScreen.TILE_SIZE / 2f;
                offsetY = 0;
            }
            case UP -> offsetY = +GameScreen.TILE_SIZE / 2f;
            case DOWN -> offsetY = -GameScreen.TILE_SIZE / 2f;
        }
    }

    /**
     * Updates the sword skill's animation state and lifetime.
     * @param delta Time passed since last frame
     */
    public void update(float delta) {
        if (!active) return;

        stateTime += delta; // Update animation timer
        lifeTimer += delta; // Update skill duration

        // Deactivate skill after max duration
        if (lifeTimer >= maxLifeTime) {
            deactivate();
        }
    }

    /**
     * Renders the sword effect at the correct position relative to the knight.
     * @param batch SpriteBatch used for drawing
     * @param knightWorldX The knights world X position
     * @param knightWorldY The knights world Y position
     * @param knightRenderX The knights screen render X position
     * @param knightRenderY The knights screen render Y position
     */
    public void render(SpriteBatch batch, float knightWorldX, float knightWorldY, int knightRenderX, int knightRenderY) {
        if (!active) return;

        TextureRegion frame = animation.getKeyFrame(stateTime, false);

        // End animation if finished
        if (animation.isAnimationFinished(stateTime)) {
            active = false;
            return;
        }

        float drawX = knightRenderX;
        float drawY = knightRenderY;
        float offset = GameScreen.TILE_SIZE / 2f - 15; // Push out from knight a bit

        // Adjust position based on attack direction
        switch (direction) {
            case UP -> drawY -= GameScreen.TILE_SIZE - offset;
            case DOWN -> drawY += GameScreen.TILE_SIZE - offset - 30;
            case LEFT -> drawX -= GameScreen.TILE_SIZE - offset - 10;
            case RIGHT -> drawX += GameScreen.TILE_SIZE - offset - 10;
        }

        // Flip the sprite if necessary
        if (direction == Direction.LEFT && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (direction == Direction.RIGHT && frame.isFlipX()) {
            frame.flip(true, false);
        }

        // Draw the sword animation frame
        batch.draw(
            frame,
            drawX,
            drawY,
            frame.getRegionWidth() * 2f,
            frame.getRegionHeight() * 2f
        );
    }

    /**
     * @return true if the sword skill is currently active
     */
    public boolean isActive() {
        return active;
    }

    /** Deactivates the sword skill effect */
    public void deactivate() {
        active = false;
    }

    /** Frees all textures used by the skill */
    public void dispose() {
        for (Texture t : textureList) {
            t.dispose();
        }
    }
}
