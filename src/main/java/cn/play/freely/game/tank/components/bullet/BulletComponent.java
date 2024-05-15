package cn.play.freely.game.tank.components.bullet;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.components.tank.TankComponent;
import cn.play.freely.game.tank.config.Dir;
import cn.play.freely.game.tank.entity.GameType;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.util.AssetPoolUtils;
import cn.play.freely.game.tank.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 子弹
 */
public class BulletComponent extends Component {

    private final TankComponent component;
    private BufferedImage bulletImage;
    private final int width = 8;
    private final int height = 10;
    private float x;
    private float y;
    private int offsetX = 0;
    private int offsetY = 0;
    private float moveSpeed = 1f;
    private final Dir dir;
    /**
     * 是否还在移动中
     */
    private boolean isMoving = true;

    public BulletComponent(TankComponent component) {
        this.component = component;
        if (component.getEntity().has(GameType.ENEMY)) {
            moveSpeed = 0.6f;
        }
        this.dir = component.getDir();
        initBullet();
    }

    private void initBullet() {
        // 默认是右边
        BufferedImage originBulletImage = AssetPoolUtils.loadTexture("/textures/bullet/normal.png");
        int deg = 0;
        offsetX = 0;
        offsetY = 0;
        switch (dir) {
            case UP:
                deg = 270;
                offsetY = -20;
                break;
            case DOWN:
                deg = 90;
                offsetY = 20;
                break;
            case LEFT:
                deg = 180;
                offsetX = -20;
                break;
            case RIGHT:
                offsetX = 20;
                break;
        }
        bulletImage = ImageUtils.rotateImage(originBulletImage, deg);
        Transform transform = component.getEntity().getTransform();

        this.x = (int)(transform.getPosition().x + (transform.getSize().width - width) / 2);
        this.y = (int)(transform.getPosition().y + (transform.getSize().width - width) / 2);
    }

    @Override
    public void render(Graphics g) {
        drawBullet(g);
    }

    private void drawBullet(Graphics g) {
        if (!isMoving) return;
        g.drawImage(bulletImage, (int)(x + offsetX),(int)(y + offsetY), width, height, null);
        debug(() -> {
            g.setColor(Color.red);
            g.drawRect((int)(x + offsetX),(int)(y + offsetY), width, height);
        });
    }

    @Override
    public void update(float dt) {
        updatePos();
    }


    private void updatePos() {
        if (!isMoving) return;
        switch (dir) {
            case UP:
                this.y -= moveSpeed;
                break;
            case DOWN:
                this.y += moveSpeed;
                break;
            case LEFT:
                this.x -= moveSpeed;
                break;
            case RIGHT:
                this.x += moveSpeed;
                break;
        }
    }

    public int getX() {
        return (int)(x + offsetX);
    }
    public int getNextX() {
        if (dir == Dir.LEFT) {
            return (int)(x + offsetX - moveSpeed);
        } else if (dir == Dir.RIGHT) {
            return (int)(x + offsetX + moveSpeed);
        } else {
            return (int)(x + offsetX);
        }
    }

    public int getY() {
        return (int)(y + offsetY);
    }

    public int getNextY() {
        if (dir == Dir.UP) {
            return (int)(y + offsetY - moveSpeed);
        } else if (dir == Dir.DOWN) {
            return (int)(y + offsetY + moveSpeed);
        } else {
            return (int)(y + offsetY);
        }
    }

    public void stop() {
        this.isMoving = false;
    }


    public Dir getDir() {
        return dir;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

}
