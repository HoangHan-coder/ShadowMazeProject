package com.ShadowMaze.screen;

import com.ShadowMaze.core.Main;
import com.ShadowMaze.model.FadeTransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class QuitGame implements Screen {

    private final Game game; // class Main k? th?a Game
    private Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private Texture aboutTextTexture;
    private ImageButton quitButton; // (neu muon co nut quit)

    public QuitGame(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("menu/quit/background.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // === ?nh goodbye (bye.png) ===
        Texture byeTexture = new Texture("menu/quit/bye.png");
        Image byeImage = new Image(new TextureRegionDrawable(new TextureRegion(byeTexture)));
        byeImage.setPosition(
                (Gdx.graphics.getWidth() - byeImage.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - byeImage.getHeight()) / 2f
        );
        byeImage.getColor().a = 0; // an ban dau

        // Hieu ung: hien ra 2s -> doi 1s -> bien mat 1s -> goi showThanksImage()
        byeImage.addAction(Actions.sequence(
                Actions.fadeIn(2f), // Hien ra 2s
                Actions.delay(1f), // Dung lai 1s
                Actions.fadeOut(1f), // Bien mat 1s
                Actions.run(() -> showThanksImage()) // Hien anh tiep theo
        ));

        stage.addActor(byeImage);
    }

    private void showThanksImage() {
        Texture thanksTexture = new Texture("menu/quit/see.png");
        Image thanksImage = new Image(new TextureRegionDrawable(new TextureRegion(thanksTexture)));
        thanksImage.setPosition(
                (Gdx.graphics.getWidth() - thanksImage.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - thanksImage.getHeight()) / 2f
        );
        thanksImage.getColor().a = 0; // an ban dau

        // Hieu ung: hien ra 2s -> dung lai 1s -> thoat game
        thanksImage.addAction(Actions.sequence(
                Actions.fadeIn(2f),
                Actions.delay(1f),
                Actions.run(() -> Gdx.app.exit())
        ));

        stage.addActor(thanksImage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        float scale = 0.6f;
        float newWidth = background.getWidth() * scale;
        float newHeight = background.getHeight() * scale;
        float x = (Gdx.graphics.getWidth() - newWidth) / 2f;
        float y = (Gdx.graphics.getHeight() - newHeight) / 2f;
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // V? thêm h??ng d?n t?i ?ây n?u c?n
        batch.end();

        // V? background và layer chính
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // V? stage (?nh text m? d?n hi?n lên)
        stage.act(delta);
        stage.draw();

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
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        aboutTextTexture.dispose();
        stage.dispose();
    }
}
