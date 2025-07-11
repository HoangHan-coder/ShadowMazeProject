package com.ShadowMaze.screen;

import com.ShadowMaze.render.MenuRenderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;

/**
 * MainMenuScreen serves as an entry screen before transitioning to the actual menu renderer.
 * It loads UI dependencies, configures the stage, and then sets MenuRenderer as the active screen.
 * 
 * Note: This screen only runs briefly before switching to MenuRenderer.
 * 
 * Author: NgKaitou
 */
public class MainMenuScreen extends GameScreen implements Screen {

    private Stage stage;        // LibGDX scene2d stage for UI handling
    private Skin skin;          // VisUI skin (may not be used in this class)
    private Texture bgTexture;  // Optional background texture (currently unused)

    /**
     * Constructor for MainMenuScreen.
     * 
     * @param game The main Game instance used to control screen flow.
     */
    public MainMenuScreen(Game game) {
        super(game); // Call superclass constructor (GameScreen)
    }

    /**
     * Called once when the screen becomes active.
     * Initializes stage, sets window size, loads UI, and immediately transitions to MenuRenderer.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());           // Create a stage for UI
        Gdx.input.setInputProcessor(stage);                // Handle input via the stage

        Gdx.graphics.setWindowedMode(1200, 658);           // Set game window size

        if (!VisUI.isLoaded()) {
            VisUI.load();                                  // Load VisUI skin if not already loaded
        }

        // Immediately switch to MenuRenderer screen
        game.setScreen(new MenuRenderer(game));

        // This code below is not effective since screen switches before it's used
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
    }

    /**
     * Called every frame while this screen is active.
     * Note: In this screen, render is only active briefly before switching to MenuRenderer.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);                     // Clear screen with black color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);           // Clear color buffer

        stage.act(delta);                                    // Update UI elements
        stage.draw();                                        // Draw UI to the screen
    }

    /**
     * Called when the window is resized.
     * 
     * @param width  New width
     * @param height New height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Releases all resources associated with this screen.
     * Be careful: `skin` and `bgTexture` are not initialized, which may cause a NullPointerException.
     */
    @Override
    public void dispose() {
        stage.dispose();

        // Safely dispose resources only if they are initialized
        if (skin != null) skin.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }
}
