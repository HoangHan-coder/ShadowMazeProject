package com.ShadowMaze.screen;

import com.ShadowMaze.uis.HpBar;
import com.ShadowMaze.uis.StaminaBar;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a battle screen where the player and an enemy are shown with animated sprites.
 * Displays background, health bars, and reacts to ESC key to return to GameScreen.
 * 
 * Assets:
 * - Animations loaded from /batte/1 (enemy) and /batte/2 (player)
 * - HP bars with background/fill textures
 * 
 * Author: letan
 */
public class BattleScreen implements Screen {

    private final Game game;                     // Reference to main game for screen switching
    private SpriteBatch batch;                   // Used to draw textures
    private Texture background;                  // Background texture of the battle screen

    private Animation<TextureRegion> playerAnimation; // Player animation
    private Animation<TextureRegion> enemyAnimation;  // Enemy animation
    private float stateTime;                     // Keeps track of animation frame timing

    private ArrayList<Texture> animationTextures; // Stores textures for disposal later
    private StaminaBar staminaBar;              // (unused) could be added later
    private ShapeRenderer shapeRenderer;        // (unused) can be used for debug
    private Texture staminaIcon;                // (unused) optional UI icon
    private HpBar hpBar;                        // HP bar for player
    private HpBar hpBar1;                       // HP bar for enemy
    private Texture hpBg, hpFill;               // Textures for HP bar background/fill

    /**
     * Constructor initializes all assets and UI for the battle screen.
     * 
     * @param game The main Game instance for managing screens
     */
    public BattleScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("batte/background.png")); // Load background

        animationTextures = new ArrayList<>();

        // Load animations for player and enemy
        playerAnimation = loadAnimationFromFolder("batte/2", 0.2f, false);
        enemyAnimation = loadAnimationFromFolder("batte/1", 0.2f, true); // flipped

        stateTime = 0f;

        shapeRenderer = new ShapeRenderer(); // currently unused
        hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));  // HP background
        hpFill = new Texture(Gdx.files.internal("menu/function/type6.png")); // HP fill
        hpBar = new HpBar(70, 30, 200, 20, 100, hpBg, hpFill);              // Player HP
        hpBar1 = new HpBar(870, 30, 200, 20, 100, hpBg, hpFill);            // Enemy HP
    }

    /**
     * Loads an animation from a folder with sorted frames.
     * 
     * @param folderPath Path to animation folder
     * @param frameDuration Duration for each frame
     * @param flipX Whether to flip horizontally (used for enemy)
     * @return Animation<TextureRegion> for use in render()
     */
    private Animation<TextureRegion> loadAnimationFromFolder(String folderPath, float frameDuration, boolean flipX) {
        FileHandle folder = Gdx.files.internal(folderPath);
        FileHandle[] files = folder.list();
        Array<TextureRegion> frames = new Array<>();

        Arrays.sort(files, (f1, f2) -> f1.name().compareTo(f2.name())); // Sort to get correct frame order

        for (FileHandle file : files) {
            if (file.name().startsWith("frame_") && file.extension().equals("png")) {
                Texture tex = new Texture(file);
                tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); 
                animationTextures.add(tex); // store for dispose
                TextureRegion region = new TextureRegion(tex);

                if (flipX && !region.isFlipX()) {
                    region.flip(true, false); // flip enemy to face left
                }

                frames.add(region);
            }
        }

        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    /**
     * Called every frame to update and render the screen.
     * 
     * @param delta Time since last frame
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // clear with black

        stateTime += delta;

        // Get current frames from both animations
        TextureRegion currentPlayer = playerAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentEnemy = enemyAnimation.getKeyFrame(stateTime, true);

        // Update HP bars
        hpBar.update(delta);
        hpBar1.update(delta);

        // Draw everything
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(currentPlayer, 100, 100); // player position
        batch.draw(currentEnemy, 600, -60);  // enemy position (adjusted up)
        hpBar.render(batch);                 // draw player HP
        hpBar1.render(batch);                // draw enemy HP
        batch.end();

        // Escape to go back to GameScreen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new GameScreen(game));
        }
    }

    /**
     * Disposes all textures and resources used.
     */
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Texture tex : animationTextures) {
            tex.dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}
