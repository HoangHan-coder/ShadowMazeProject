package com.ShadowMaze.render;

import com.ShadowMaze.model.FadeTransitionScreen;
import com.ShadowMaze.screen.GameOverHandler;
import com.ShadowMaze.screen.GameScreen;
import com.ShadowMaze.screen.HowToPlayScreen;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

/**
 * The main menu screen renderer. This class handles rendering background,
 * buttons, and character animations in the main menu. It uses Scene2D UI for
 * layout and interaction.
 */
public class MenuRenderer implements Screen {

    private Stage stage;                        // Scene2D stage for UI
    private Game game;                          // Main game instance
    private Texture resumeUp, resumeDown;
    private Texture optionsUp, optionsDown;
    private Texture quitUp, quitDown, quitDisabled;
    private Texture how;
    private Texture backgroundTexture = new Texture(Gdx.files.internal("menu/function/background.png"));
    private SpriteBatch batch = new SpriteBatch(); // For drawing background and animations

    private Animation<TextureRegion> walkAnimation;  // Walking animation
    private float stateTime = 0f;                    // Time tracker for animation
    private ArrayList<Texture> animationTextures = new ArrayList<>();
    private Animation<TextureRegion> idleAnimation;  // Idle animation
    private ArrayList<Texture> idleTextures = new ArrayList<>();

    private GameScreen screen;
    

    public MenuRenderer(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize stage and input
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load button textures
        resumeUp = new Texture(Gdx.files.internal("menu/function/type1.png"));
        optionsUp = new Texture(Gdx.files.internal("menu/function/type3.png"));
        quitUp = new Texture(Gdx.files.internal("menu/function/type4.png"));
        how = new Texture(Gdx.files.internal("menu/function/type2.png"));

        // Create buttons
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeUp));
        ImageButton howButton = new ImageButton(new TextureRegionDrawable(how));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(optionsUp));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(quitUp));
        quitButton.setDisabled(true);  // Disabled for now

        // Add listener to start game on resume click
        resumeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Screen current = game.getScreen();
                Screen next = new GameScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });
        howButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Screen current = game.getScreen();
                Screen next = new HowToPlayScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });
// Arrange buttons in a table
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(200);
        table.add(resumeButton).size(200, 60).pad(5).row();
        table.add(howButton).size(200, 60).pad(5).row();
        table.add(optionsButton).size(200, 60).pad(5).row();
        table.add(quitButton).size(200, 60).pad(5);
        stage.addActor(table);

        // Load walk animation from folder
        FileHandle folder = Gdx.files.internal("menu/nv1");
        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : folder.list()) {
            if (file.name().startsWith("frame_") && file.extension().equals("png")) {
                Texture tex = new Texture(file);
                animationTextures.add(tex); // Store for later dispose
                frames.add(new TextureRegion(tex));
            }
        }
        walkAnimation = new Animation<>(0.1f, frames);  // 10 FPS

        // Load idle animation from folder
        FileHandle folder2 = Gdx.files.internal("menu/nv2");
        Array<TextureRegion> idleFrames = new Array<>();
        for (FileHandle file : folder2.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                idleTextures.add(tex);
                idleFrames.add(new TextureRegion(tex));
            }
        }
        idleAnimation = new Animation<>(0.15f, idleFrames);  // 6-7 FPS
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getBackBufferHeight());
        batch.end();

        // Update and draw UI stage
        stage.act(delta);
        stage.draw();

        // Update animation time
        stateTime += delta;

        // Draw walk animation at position (500, 200)
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(currentFrame, 500, 200);
        batch.end();

        // Draw idle animation at position (100, 100)
        TextureRegion idleFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(idleFrame, 100, 100);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        // Dispose UI stage and batch
        stage.dispose();
        batch.dispose();

        // Dispose textures
        if (resumeUp != null) {
            resumeUp.dispose();
        }
        if (resumeDown != null) {
            resumeDown.dispose();
        }
        if (optionsUp != null) {
            optionsUp.dispose();
        }
        if (optionsDown != null) {
            optionsDown.dispose();
        }
        if (quitUp != null) {
            quitUp.dispose();
        }
        if (quitDown != null) {
            quitDown.dispose();
        }
        if (quitDisabled != null) {
            quitDisabled.dispose();
        }
        if (how != null) {
            how.dispose();
        }
        backgroundTexture.dispose();

        // Dispose animation textures
        for (Texture tex : animationTextures) {
            tex.dispose();
        }
        for (Texture tex : idleTextures) {
            tex.dispose();
        }
    }
}
