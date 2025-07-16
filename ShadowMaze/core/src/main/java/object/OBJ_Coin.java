/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author NgKaitou
 */
public class OBJ_Coin extends SuperObject {

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private Array<Texture> loadedTextures = new Array<>();

    public OBJ_Coin(float mapX, float mapY) {
        this.name = "Coin";
        this.mapX = mapX;
        this.mapY = mapY;
        loadAnimation();
    }

    private void loadAnimation() {
        Array<TextureRegion> frames = new Array<>();
        FileHandle dir = Gdx.files.internal("score/"); // Th? m?c ch?a coin_1.png, coin_2.png,...

        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png")) {
                Texture tex = new Texture(file);
                loadedTextures.add(tex);
                frames.add(new TextureRegion(tex));
            }
        }

        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    @Override
    public void drawObject(GameScreen gs) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime);

        float screenX = gs.SCREEN_WIDTH / 2f + (mapX - gs.knight.getPositionX()) - GameScreen.TILE_SIZE / 2f;
        float screenY = gs.SCREEN_HEIGHT / 2f + (mapY - gs.knight.getPositionY()) - GameScreen.TILE_SIZE / 2f;

        gs.batch.draw(currentFrame, screenX, screenY, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
    }
    public void dispose() {
        for (Texture t : loadedTextures) {
            t.dispose();
        }
    }
}
