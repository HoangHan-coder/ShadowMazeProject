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
 * Váº½ mÃª cung sá»­ dá»¥ng SpriteBatch vá»›i texture cho tÆ°á»?ng vÃ  ná»?n.
 *
 * <p>
 * 0 = wall, 1 = path</p>
 */
public class MazeRenderer {

    private final int[][] maze;     // MÃª cung 2D
    private final int cellSize;     // KÃ­ch thÆ°á»›c 1 Ã´ (px)
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
        // Nút nh? góc trái
        resumeUp = new Texture(Gdx.files.internal("menu/function/pause.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(resumeUp));

        // B?ng menu t?m d?ng
        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();

        // T?o các nút trong b?ng menu
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type1.png"))));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type3.png"))));
        ImageButton quitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("menu/function/type4.png"))));

        // Thêm các nút vào b?ng menu
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

        // Gán s? ki?n cho nút resume trong menu popup
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Screen current = game.getScreen();
                Screen next = new MainMenuScreen(game);
                game.setScreen(new FadeTransitionScreen(game, current, next));
            }
        });

        // Table ch?a nút pause
        Table topLeftTable = new Table();
        topLeftTable.bottom().left().padBottom(20).padLeft(20);
        topLeftTable.setFillParent(true);
        topLeftTable.add(pauseButton).size(100, 40);

        // Thêm m?i th? vào stage
        stage.addActor(topLeftTable);       // nút pause nh?
        stage.addActor(pauseMenuTable);     // b?ng menu hi?n ra
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Váº½ toÃ n bá»™ mÃª cung báº±ng batch Ä‘Æ°á»£c truyá»?n tá»« ngoÃ i.
     *
     * @param batch SpriteBatch Ä‘ang dÃ¹ng trong GameScreen
     */
    public void render(SpriteBatch batch) {
       
    }

    public void dispose() {
    }
}
