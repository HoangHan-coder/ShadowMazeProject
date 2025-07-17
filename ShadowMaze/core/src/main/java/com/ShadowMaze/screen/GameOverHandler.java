package com.ShadowMaze.screen;

import com.ShadowMaze.model.FadeTransitionScreen;
import com.ShadowMaze.model.ScoreBoard;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class GameOverHandler {

    private Texture gameOverImage;
    private Texture scorePanelImage;
    private BitmapFont font;
    private BitmapFont totalCore;
    private float gameOverTime;
    private boolean active;

    private final ImageButton btnReplay;
    private final ImageButton btnQuit;

    private final ScoreBoard scoreBoard;
    private final Game game;
    private GameScreen gs;
    private final Image gameOverImageActor;
    private final Image scorePanelImageActor;
    private boolean hasTriggered = true;
    private Label scoreLabel;
    private Label timeLabel;
    private Skin skin;
    private Stage stage;

    public GameOverHandler(GameScreen gs, ScoreBoard scoreBoard) {
        stage = new Stage();
        gameOverImage = new Texture(Gdx.files.internal("menu/function/over.png"));
        scorePanelImage = new Texture(Gdx.files.internal("menu/function/score.png"));
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        scoreBoard = gs.scoreBoard;
        this.scoreBoard = scoreBoard;
        game = (Game) Gdx.app.getApplicationListener();

        // Buttons
        Texture btnReplayTex = new Texture(Gdx.files.internal("menu/function/replaynew.png"));
        Texture btnQuitTex = new Texture(Gdx.files.internal("menu/function/quitnew.png"));

        ImageButton.ImageButtonStyle styleReplay = new ImageButton.ImageButtonStyle();
        styleReplay.imageUp = new TextureRegionDrawable(new TextureRegion(btnReplayTex));

        ImageButton.ImageButtonStyle styleQuit = new ImageButton.ImageButtonStyle();
        styleQuit.imageUp = new TextureRegionDrawable(new TextureRegion(btnQuitTex));

        btnReplay = new ImageButton(styleReplay);
        btnReplay.setSize(150, 60);
        btnReplay.setPosition(440, Gdx.graphics.getHeight());

        btnQuit = new ImageButton(styleQuit);
        btnQuit.setSize(150, 60);
        btnQuit.setPosition(620, Gdx.graphics.getHeight());

        // Events
        btnReplay.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gs.isGameOver = false;
                System.out.println(hasTriggered);
                // Chuy?n màn hình
                Screen current = game.getScreen();
                Screen next = new GameScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        btnQuit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gs.isGameOver = false;
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        this.gs = gs;
        // === Kh?i t?o Image t? Texture ===
        gameOverImageActor = new Image(new TextureRegionDrawable(new TextureRegion(gameOverImage)));
        scorePanelImageActor = new Image(new TextureRegionDrawable(new TextureRegion(scorePanelImage)));

// Kích th??c
        gameOverImageActor.setSize(300, 300);
        scorePanelImageActor.setSize(400, 400);

// ??t v? trí ban ??u ngoài màn hình (hi?u ?ng ?i xu?ng)
        gameOverImageActor.setPosition(460, Gdx.graphics.getHeight());
        scorePanelImageActor.setPosition(405, Gdx.graphics.getHeight());

// M? ban ??u
        gameOverImageActor.getColor().a = 0;
        scorePanelImageActor.getColor().a = 0;
        btnReplay.getColor().a = 0;
        btnQuit.getColor().a = 0;
        // T?o skin r?ng
        skin = new Skin();

// T?o Label style t? font
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;

// Kh?i t?o Label
        scoreLabel = new Label("Score: 0", labelStyle);
        scoreLabel.setSize(200, 40);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(510, Gdx.graphics.getHeight());
        scoreLabel.getColor().a = 0;  // ban ??u ?n

        timeLabel = new Label("Time: 00:00", labelStyle);
        timeLabel.setSize(200, 40);
        timeLabel.setAlignment(Align.center);
        timeLabel.setPosition(510, Gdx.graphics.getHeight());
        timeLabel.getColor().a = 0;

    }

    public void trigger(Stage stage) {
        this.active = true;
        this.gameOverTime = 0;
        if (!hasTriggered) {
            return;  // ?ã g?i r?i thì không g?i n?a
        }
        // Add actors to stage
        stage.addActor(scorePanelImageActor);
        stage.addActor(gameOverImageActor);
        stage.addActor(btnReplay);
        stage.addActor(btnQuit);
        stage.addActor(scoreLabel);
        stage.addActor(timeLabel);

// Game Over
        gameOverImageActor.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(460, 300, 1f)
        ));

// Score Panel
        scorePanelImageActor.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(405, 105, 1f)
        ));

// Buttons
        btnReplay.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(440, 130, 1f)
        ));
        btnQuit.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(620, 130, 1f)
        ));

// Labels
        scoreLabel.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(510, 260, 1f)
        ));
        timeLabel.addAction(Actions.parallel(
                Actions.fadeIn(1f),
                Actions.moveTo(510, 220, 1f)
        ));

    }

    public boolean isSetTriggered() {
        return hasTriggered = false;
    }

    public boolean isTriggered() {
        return hasTriggered;
    }

    public void reset() {
        hasTriggered = false;
        this.active = false;
        this.gameOverTime = 0;
        scoreBoard.reset();
    }

    public boolean isActive() {
        return active;
    }

    public void update(float delta) {
        if (active) {
            gameOverTime += delta;

            // C?p nh?t n?i dung label
            scoreLabel.setText("Score: " + scoreBoard.scoreActual);
            timeLabel.setText(String.format("Time: %02d:%02d", gs.ui.min, gs.ui.second));
        }
    }

    public void render(SpriteBatch batch, int screenWidth, int screenHeight, float delta) {

    }

    public void addToStage(Stage stage) {
        stage.addActor(scorePanelImageActor);
        stage.addActor(gameOverImageActor);
        stage.addActor(btnReplay);
        stage.addActor(btnQuit);

    }

    public void dispose() {
        gameOverImage.dispose();
        scorePanelImage.dispose();
        font.dispose();

        gameOverImageActor.remove();
        scorePanelImageActor.remove();
        btnReplay.remove();
        btnQuit.remove();
        scoreLabel.remove();
        timeLabel.remove();
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public ImageButton getReplayButton() {
        return btnReplay;
    }

    public ImageButton getQuitButton() {
        return btnQuit;
    }

}
