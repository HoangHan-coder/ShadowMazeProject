/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ShadowMaze.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

/**
 *
 * @author letan
 */
public class MirrorRenderer {

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private List<int[]> mirrorPositions; // danh sách v? trí g??ng

    public MirrorRenderer(Animation<TextureRegion> animation, List<int[]> positions) {
        this.animation = animation;
        this.mirrorPositions = positions;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void render(SpriteBatch batch, int tileSize, int offsetX, int offsetY) {
        float scale = 2.8f; // Phóng to 2.8 l?n
        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        for (int[] pos : mirrorPositions) {
            int x = pos[0];
            int y = pos[1];

            float drawX = offsetX + x * tileSize;
            float drawY = offsetY + y * tileSize;

            float scaledSize = tileSize * scale;
            float offset = (scaledSize - tileSize) / 2f;

            // V? ?nh ?ã ???c phóng to và c?n gi?a
            batch.draw(frame,
                    drawX - offset, drawY - offset, // T?a ?? g?c ?ã tr? l?ch
                    scaledSize, scaledSize // Kích th??c phóng to
            );
        }

    }
}
