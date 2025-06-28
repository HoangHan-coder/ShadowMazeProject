package com.ShadowMaze.render;

import com.ShadowMaze.model.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Gdx;

/**
 * Class chịu trách nhiệm vẽ nhân vật chính với animation.
 */
public class PlayerRenderer {
    private Player player;
    private Animation<TextureRegion> animation;
    private float stateTime;

    private boolean facingLeft = false; // Hướng nhân vật (trái/phải)

    public PlayerRenderer(Player player, Animation<TextureRegion> animation) {
        this.player = player;
        this.animation = animation;
        this.stateTime = 0f;

        // Đảm bảo texture không bị mờ (nét pixel rõ)
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY) {
        int x = player.getPositionX();
        int y = player.getPositionY();

        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        // Flip nếu hướng di chuyển thay đổi (tuỳ mở rộng sau này)
        if (facingLeft && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (!facingLeft && frame.isFlipX()) {
            frame.flip(true, false);
        }

        // Vẽ frame tại vị trí nhân vật quy đổi từ mê cung → pixel
        float drawX = offsetX + x * tileSize;
        float drawY = offsetY + y * tileSize;

        batch.draw(frame, drawX, drawY, tileSize, tileSize);
    }

    // ================= Getter / Setter =================

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }
}
