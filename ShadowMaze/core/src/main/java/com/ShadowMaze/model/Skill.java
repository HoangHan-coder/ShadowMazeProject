package com.ShadowMaze.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Skill {

    private Vector2 position;
    private Vector2 velocity;
    private float speed = 200f;
    private boolean active;

    private Array<Texture> textureList = new Array<>();
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float width, height;

    public Skill() {
        active = false;

        // Load t?t c? ?nh có ?uôi .img trong th? m?c skills/
        FileHandle dir = Gdx.files.internal("skill/1");
        FileHandle[] files = dir.list();

        Array<TextureRegion> frames = new Array<>();
        System.out.println("hello");
        for (FileHandle file : files) {
            if (file.extension().equalsIgnoreCase(".png")) {
                System.out.println("di qua day");
                Texture texture = new Texture(file);
                textureList.add(texture);
                frames.add(new TextureRegion(texture));
            }
        }

        if (frames.size == 0) {
            throw new RuntimeException("Không tìm th?y ?nh .img nào trong th? m?c skills/");
        }

        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        width = frames.get(0).getRegionWidth();
        height = frames.get(0).getRegionHeight();
    }

    public void activate(float x, float y, float dirX, float dirY) {
        position = new Vector2(x + 20, y); // l?ch m?t chút cho ??p
        velocity = new Vector2(dirX, dirY).nor().scl(speed);
        active = true;
        stateTime = 0;
    }

    public void update(float delta) {
        if (!active) return;

        stateTime += delta;
        position.add(velocity.x * delta, velocity.y * delta);

        if (position.dst2(0, 0) > 5000 * 5000) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        System.out.println("dada");
        if (!active) return;

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, position.x - width / 2, position.y - height / 2);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void dispose() {
        for (Texture t : textureList) {
            t.dispose();
        }
    }
}
