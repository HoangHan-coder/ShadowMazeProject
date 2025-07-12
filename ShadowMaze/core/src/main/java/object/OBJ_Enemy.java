package object;

import com.ShadowMaze.model.Map;
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
    private int direction = 0; // 0=trái, 1=xu?ng, 2=ph?i, 3=lên
    private float speed = 50f;
    private float moveTimer = 0f;
    private float moveInterval = 0.1f; // th?i gian ki?m tra h??ng ti?p theo
    public float mapX, mapY;

    public OBJ_Enemy() {
        name = "Enemy";
        mapX = 500; // G?n v? trí knight
        mapY = 500;
        // Load all frames in folder
        Array<TextureRegion> regions = new Array<>();
        for (int i = 0; i < 4; i++) {
            Texture tex = new Texture(Gdx.files.internal("icons/Special" + i + ".png"));
            frames.add(tex); // ?? dispose sau
            regions.add(new TextureRegion(tex));
        }

        animation = new Animation<>(0.15f, regions);
    }

    private boolean isWalkable(Map map, float x, float y) {
        int tileX = (int) (x / GameScreen.TILE_SIZE);
        int tileY = (int) (y / GameScreen.TILE_SIZE);

        return tileY >= 0 && tileY < map.tileNum.length
                && tileX >= 0 && tileX < map.tileNum[0].length
                && map.tileNum[tileY][tileX] == 1;
    }

    @Override
    public void drawObject(GameScreen screen) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        int screenX = (int) (mapX - screen.knight.positionX + screen.knight.renderX);
        int screenY = (int) (mapY - screen.knight.positionY + screen.knight.renderY);
        System.out.println("Draw enemy at screen (" + screenX + ", " + screenY + ")");
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

    public void update(float delta, Map map) {
        moveTimer += delta;

        float dx = 0, dy = 0;
        switch (direction) {
            case 0:
                dx = -1;
                break; // trái
            case 1:
                dy = 1;
                break;  // xu?ng
            case 2:
                dx = 1;
                break;  // ph?i
            case 3:
                dy = -1;
                break; // lên
        }

        float nextX = mapX + dx * speed * delta;
        float nextY = mapY + dy * speed * delta;

        float enemyWidth = animation.getKeyFrame(0).getRegionWidth() * scale;
        float enemyHeight = animation.getKeyFrame(0).getRegionHeight() * scale;

        float left = nextX;
        float right = nextX + enemyWidth;
        float top = nextY + enemyHeight;
        float bottom = nextY;

        boolean canMove
                = isWalkable(map, left, bottom)
                && isWalkable(map, right - 1, bottom)
                && isWalkable(map, left, top - 1)
                && isWalkable(map, right - 1, top - 1);

        if (canMove) {
            mapX = nextX;
            mapY = nextY;
        } else {
            // G?p t??ng ? ??i h??ng sau moveInterval
            if (moveTimer >= moveInterval) {
                moveTimer = 0f;

                int curTileX = (int) (mapX / GameScreen.TILE_SIZE);
                int curTileY = (int) (mapY / GameScreen.TILE_SIZE);

                Array<Integer> possibleDirections = new Array<>();
                for (int i = 0; i < 4; i++) {
                    int nx = curTileX, ny = curTileY;
                    switch (i) {
                        case 0:
                            nx--;
                            break;
                        case 1:
                            ny++;
                            break;
                        case 2:
                            nx++;
                            break;
                        case 3:
                            ny--;
                            break;
                    }

                    if (ny >= 0 && ny < map.tileNum.length
                            && nx >= 0 && nx < map.tileNum[0].length
                            && map.tileNum[ny][nx] == 1) {
                        possibleDirections.add(i);
                    }
                }

                if (possibleDirections.size > 0) {
                    direction = possibleDirections.random();
                }
            }
        }
    }

    public void dispose() {
        for (Texture tex : frames) {
            tex.dispose();
        }
    }
}
