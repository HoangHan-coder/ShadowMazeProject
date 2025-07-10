package com.ShadowMaze.render;

import com.ShadowMaze.model.FadeTransitionScreen;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class MenuRenderer implements Screen {

    private Stage stage;
    private Game game;
    private Texture resumeUp, resumeDown;
    private Texture optionsUp, optionsDown;
    private Texture quitUp, quitDown, quitDisabled;
    private Texture how;
    private Texture backgroundTexture = new Texture(Gdx.files.internal("menu/function/background.png"));
    private SpriteBatch batch = new SpriteBatch();

    private Animation<TextureRegion> walkAnimation;
    private float stateTime = 0f;
    private ArrayList<Texture> animationTextures = new ArrayList<>();
    private Animation<TextureRegion> idleAnimation;
    private ArrayList<Texture> idleTextures = new ArrayList<>();

        private GameScreen screen;
    public MenuRenderer(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        resumeUp = new Texture(Gdx.files.internal("menu/function/type1.png"));
        optionsUp = new Texture(Gdx.files.internal("menu/function/type3.png"));
        quitUp = new Texture(Gdx.files.internal("menu/function/type4.png"));
        how = new Texture(Gdx.files.internal("menu/function/type2.png"));

        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeUp));
        ImageButton howButton = new ImageButton(new TextureRegionDrawable(how));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(optionsUp));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(quitUp));
        quitButton.setDisabled(true);
        resumeButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                Screen current = game.getScreen();
                Screen next = new GameScreen(game);
                game.setScreen(new FadeTransitionScreen(game,current,next));
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.padBottom(200);
        table.add(resumeButton).size(200, 60).pad(5).row();
        table.add(howButton).size(200, 60).pad(5).row();
        table.add(optionsButton).size(200, 60).pad(5).row();
        table.add(quitButton).size(200, 60).pad(5);
        stage.addActor(table);

        // ? T?o animation t? nhi?u ?nh riêng
        FileHandle folder = Gdx.files.internal("menu/nv1");
        Array<TextureRegion> frames = new Array<>();
        for (FileHandle file : folder.list()) {
            if (file.name().startsWith("frame_") && file.extension().equals("png")) {
                Texture tex = new Texture(file);
                animationTextures.add(tex); // l?u ?? dispose sau
                frames.add(new TextureRegion(tex));
            }
        }

        // ? T?o animation l?p l?i m?i 0.1 giây
        walkAnimation = new Animation<TextureRegion>(0.1f, frames);
        // ? T?o animation th? hai t? folder menu/nv2
        FileHandle folder2 = Gdx.files.internal("menu/nv2");
        Array<TextureRegion> idleFrames = new Array<>();

        for (FileHandle file : folder2.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                idleTextures.add(tex);                  // l?u ?? dispose
                idleFrames.add(new TextureRegion(tex)); // thêm frame
            }
        }

        idleAnimation = new Animation<TextureRegion>(0.15f, idleFrames);  // 0.15 giây m?i frame

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getBackBufferHeight());
        batch.end();

        stage.act(delta);
        stage.draw();

        // ? C?p nh?t animation
        stateTime += delta;
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        batch.begin();
        batch.draw(currentFrame, 500, 200); // v? nhân v?t
        batch.end();
        TextureRegion idleFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(idleFrame, 100, 100); // v? idle nhân v?t bên ph?i
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
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();

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

        for (Texture tex : animationTextures) {
            tex.dispose();
        }
        for (Texture tex : idleTextures) {
            tex.dispose();
        }

    }
}
