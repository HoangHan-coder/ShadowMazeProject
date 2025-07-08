package com.ShadowMaze.render;

import com.ShadowMaze.model.FadeTransitionScreen;
import com.ShadowMaze.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Vẽ mê cung sử dụng SpriteBatch với texture cho tư�?ng và n�?n.
 *
 * <p>
 * 0 = wall, 1 = path</p>
 */
public class MazeRenderer {

    private final int[][] maze;     // Mê cung 2D
    private final int cellSize;     // Kích thước 1 ô (px)
    private Texture resumeUp, resumeDown;
    private Texture optionsUp, optionsDown;
    private Texture quitUp, quitDown, quitDisabled;
    private Texture how;
    private Table pauseMenuTable;
    private boolean isPauseMenuVisible = false;
    private boolean isPaused = false;
    private MainMenuScreen screen;
    private Game game;

   
    public MazeRenderer(int[][] maze, int cellSize, Game game) {
        this.game = game;
        this.maze = maze;
        this.cellSize = cellSize;
    }

    public void createButtons(Stage stage) {
        // N�t nh? g�c tr�i
        resumeUp = new Texture(Gdx.files.internal("menu/function/pause.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(resumeUp));

        // B?ng menu t?m d?ng
        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();

        // T?o c�c n�t trong b?ng menu
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type1.png"))));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type3.png"))));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type4.png"))));

        // Th�m c�c n�t v�o b?ng menu
        pauseMenuTable.add(resumeButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(optionsButton).size(200, 60).pad(10).row();
        pauseMenuTable.add(quitButton).size(200, 60).pad(10);

        pauseMenuTable.setVisible(false); // ?n ban ??u

        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setPaused(true);
                pauseMenuTable.setVisible(true);
            }
        });

        resumeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setPaused(false);
                pauseMenuTable.setVisible(false);
            }
        });

        // G�n s? ki?n cho n�t resume trong menu popup
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        // Table ch?a n�t pause
        Table topLeftTable = new Table();
        topLeftTable.bottom().left().padBottom(20).padLeft(20);
        topLeftTable.setFillParent(true);
        topLeftTable.add(pauseButton).size(100, 40);

        // Th�m m?i th? v�o stage
        stage.addActor(topLeftTable);       // n�t pause nh?
        stage.addActor(pauseMenuTable);     // b?ng menu hi?n ra
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Vẽ toàn bộ mê cung bằng batch được truy�?n từ ngoài.
     *
     * @param batch SpriteBatch đang dùng trong GameScreen
     */
    public void render(SpriteBatch batch) {
       
    }

    public void dispose() {
    }
}
