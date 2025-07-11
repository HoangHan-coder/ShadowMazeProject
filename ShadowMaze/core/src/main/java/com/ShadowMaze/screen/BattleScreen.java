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

public class BattleScreen implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private Texture background;

    private Animation<TextureRegion> playerAnimation;
    private Animation<TextureRegion> enemyAnimation;
    private float stateTime;

    private ArrayList<Texture> animationTextures; // ?? dispose
    private StaminaBar staminaBar;
    private ShapeRenderer shapeRenderer;
    private Texture staminaIcon;
    private HpBar hpBar;
    private HpBar hpBar1;
    private Texture hpBg, hpFill;

    public BattleScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("batte/background.png"));

        animationTextures = new ArrayList<>();

        // Load frame t? th? m?c
        playerAnimation = loadAnimationFromFolder("batte/2", 0.2f, false);
        enemyAnimation = loadAnimationFromFolder("batte/1", 0.2f, true); 

        stateTime = 0f;
        shapeRenderer = new ShapeRenderer();
        hpBg = new Texture(Gdx.files.internal("menu/function/type5.png"));
        hpFill = new Texture(Gdx.files.internal("menu/function/type6.png"));
        hpBar = new HpBar(70, 30, 200, 20, 100, hpBg, hpFill);
        hpBar1 = new HpBar(870, 30, 200, 20, 100, hpBg, hpFill);

    }

    private Animation<TextureRegion> loadAnimationFromFolder(String folderPath, float frameDuration, boolean flipX) {
        FileHandle folder = Gdx.files.internal(folderPath);
        FileHandle[] files = folder.list();
        Array<TextureRegion> frames = new Array<>();

        Arrays.sort(files, (f1, f2) -> f1.name().compareTo(f2.name()));

        for (FileHandle file : files) {
            if (file.name().startsWith("frame_") && file.extension().equals("png")) {
                Texture tex = new Texture(file);
                tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); 
                animationTextures.add(tex);
                TextureRegion region = new TextureRegion(tex);

                if (flipX && !region.isFlipX()) {
                    region.flip(true, false); 
                }

                frames.add(region);
            }
        }

        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stateTime += delta;

        TextureRegion currentPlayer = playerAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentEnemy = enemyAnimation.getKeyFrame(stateTime, true);

      
        hpBar.update(delta);
        hpBar1.update(delta);

        batch.begin(); 
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(currentPlayer, 100, 100);
        batch.draw(currentEnemy, 600, -60);
        hpBar.render(batch); 
        hpBar1.render(batch); 

        batch.end(); // ?? CH? END 1 L?N

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Texture tex : animationTextures) {
            tex.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }
}
