package com.ShadowMaze.render;

import com.ShadowMaze.model.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

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

        // �?ảm bảo texture không bị m�? (nét pixel rõ)
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    public void update(float delta) {
        stateTime += delta;
    }

    /**
     * Vẽ nhân vật, hỗ trợ playerY để đi�?u chỉnh chi�?u cao khi nhảy/rơi.
     *
     * @param batch SpriteBatch từ GameScreen
     * @param tileSize Kích thước ô
     * @param offsetX �?ộ lệch X
     * @param offsetY �?ộ lệch Y
     * @param playerY Vị trí phụ theo trục Y (để nhảy mượt hơn)
     */
    public void render(SpriteBatch batch,int tileSize, int offsetX, int offsetY) {
        float drawX = offsetX + player.getRenderX() * tileSize;
        float drawY = offsetY + player.getRenderY() * tileSize;

        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        // Flip nếu hướng di chuyển thay đổi (tuỳ mở rộng sau này)
        if (facingLeft && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (!facingLeft && frame.isFlipX()) {
            frame.flip(true, false);
        }
        float scale = 2.8f; // Ph�ng to nh�n v?t g?p 2 l?n

        float drawWidth = tileSize * scale;
        float drawHeight = tileSize * scale;

// C?n gi?a nh�n v?t (n?u b?n mu?n n� v?n n?m gi?a � l??i)
        float offsetDrawX = drawX - (drawWidth - tileSize) / 2f;
        float offsetDrawY = drawY - (drawHeight - tileSize) / 2f + 40;

        batch.draw(frame, offsetDrawX, offsetDrawY, drawWidth, drawHeight);
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
