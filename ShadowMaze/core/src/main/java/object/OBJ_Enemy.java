package com.ShadowMaze.object;

import com.ShadowMaze.model.SuperObject;
import com.ShadowMaze.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class OBJ_Enemy extends SuperObject {

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private Array<Texture> frames = new Array<>();
    private float scale = 3f; // h? s? phóng to

    public OBJ_Enemy() {
        name = "Enemy";

        // Load all frames in folder
        Array<TextureRegion> regions = new Array<>();
        for (int i = 0; i < 4; i++) {
            Texture tex = new Texture(Gdx.files.internal("icons/Special" + i + ".png"));
            frames.add(tex); // ?? dispose sau
            regions.add(new TextureRegion(tex));
        }

        animation = new Animation<>(0.15f, regions);
    }

    @Override
    public void drawObject(GameScreen screen) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        int screenX = mapX - screen.knight.positionX + screen.knight.renderX;
        int screenY = mapY - screen.knight.positionY + screen.knight.renderY;

        if (mapX + GameScreen.TILE_SIZE > screen.knight.positionX - screen.knight.renderX
                && mapX - GameScreen.TILE_SIZE < screen.knight.positionX + screen.knight.renderX
                && mapY + GameScreen.TILE_SIZE > screen.knight.positionY - screen.knight.renderY
                && mapY - GameScreen.TILE_SIZE < screen.knight.positionY + screen.knight.renderY) {

            // L?y kích th??c frame g?c
            float frameWidth = currentFrame.getRegionWidth();
            float frameHeight = currentFrame.getRegionHeight();

            // V? ?nh ?ã scale
            screen.batch.draw(currentFrame,
                    screenX, screenY,
                    frameWidth * scale,
                    frameHeight * scale);
        }
    }

    public void dispose() {
        for (Texture tex : frames) {
            tex.dispose();
        }
    }
}
