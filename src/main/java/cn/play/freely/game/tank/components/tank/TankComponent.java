package cn.play.freely.game.tank.components.tank;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.components.bullet.BulletManager;
import cn.play.freely.game.tank.components.bullet.BulletComponent;
import cn.play.freely.game.tank.components.common.TileComponent;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Dir;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.GameType;
import cn.play.freely.game.tank.entity.tank.PlayerTank;
import cn.play.freely.game.tank.entity.tank.TankEntity;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.main.TankGame;
import cn.play.freely.game.tank.util.AssetPoolUtils;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import static cn.play.freely.game.tank.config.Settings.*;

/**
 * 坦克
 */
public class TankComponent extends Component {

    private Dir dir = Dir.UP;
    private final TankEntity tankEntity;
    private BufferedImage tankInitializeImage;
    /**
     * 初始化完成标识
     */
    private boolean isInitialized = false;
    /**
     * 是否自动移动
     */
    private boolean isAutoTurn = true;
    private boolean isAutoFire = true;
    /**
     * 是否移动中
     */
    private boolean isMoving = true;
    private boolean died = false;
    private int movingFrameIndex = 0;
    private BulletManager bulletManager;


    public  TankComponent(TankEntity tankEntity) {
        this.initDir = this.dir.name();
        this.tankEntity = tankEntity;
        this.tankEntity.setComponent(this);
        this.approachCollisionRect = new Rectangle();
        tankInitializeImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_TANK_INITIALIZE);
    }

    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        this.bulletManager = new BulletManager(this);
        this.initX = (int)entity.getTransform().getPosition().x;
        this.initY = (int)entity.getTransform().getPosition().y;
    }

    @Override
    public void render(Graphics g) {
        // 绘制坦克生成动画
        drawAnimationForInitializingTank(g);
        Optional.ofNullable(bulletManager).ifPresent(manager -> manager.render(g));
        if(isInitialized && !died) {
            // 绘制坦克
            drawTank(g);
            debug(() -> {
                drawHitBox(g);
                drawApproachCollision(g);
            });
        }
    }

    private void drawHitBox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(
                (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height);
    }

    private void drawApproachCollision(Graphics g) {
        g.setColor(Color.orange);
        g.drawRect(approachCollisionRect.x,
                approachCollisionRect.y,
                approachCollisionRect.width,
                approachCollisionRect.height
        );
    }


    private int animationFrameIndex = 0; // 动画帧数索引
    private int singleFrameShowTimeCount = 0;
    private static final int INITIALIZE_ANIMATION_LOOP_COUNT = 2; // 动画循环次数
    private static final int INITIALIZE_ANIMATION_FRAME_COUNT = 4; // 动画帧数
    private static final int SINGLE_FRAME_RUNTIME_COUNT = 10; // 单帧动画运行次数

    private int initX;
    private int initY;
    private String initDir;

    /**
     * 绘制初始化坦克动画
     * @param g
     */
    private void drawAnimationForInitializingTank(Graphics g) {
        if (isInitialized) return;
        int idx = animationFrameIndex % INITIALIZE_ANIMATION_FRAME_COUNT;
        g.drawImage(tankInitializeImage.getSubimage(idx * TANK_INITIALIZE_FRAME_WIDTH,0, TANK_INITIALIZE_FRAME_WIDTH, TANK_INITIALIZE_FRAME_HEIGHT), (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y , (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), null);
        if (singleFrameShowTimeCount > SINGLE_FRAME_RUNTIME_COUNT) { // 动画中的一帧展示10次切换下一帧
            animationFrameIndex++;
            singleFrameShowTimeCount = 0;
        }
        if (animationFrameIndex >= INITIALIZE_ANIMATION_LOOP_COUNT * INITIALIZE_ANIMATION_FRAME_COUNT) { // 初始化动画循环两次后初始完成
            isInitialized = true;
            singleFrameShowTimeCount = 0;
            animationFrameIndex = 0;
        }
        singleFrameShowTimeCount ++;
    }

    private void drawTank(Graphics g) {
        g.drawImage(tankEntity.getCurrentFrame(movingFrameIndex),
                (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height,
                null
        );
    }

    @Override
    public void update(float dt) {
        Optional.ofNullable(bulletManager).ifPresent(manager -> manager.update(dt));
        if (died)  {
            if (Objects.nonNull(bulletManager) && bulletManager.size() == 0) {
                TankGame.getCurrentScene().getEntities().remove(entity);
            }
            return;
        }
        if (isInitialized && isMoving) {
            if (movingFrameIndex + 1 > 1) {
                movingFrameIndex = 0;
            }   else {
                this.movingFrameIndex++;
            }
            updatePos(dt);
            updateBullet();
        }
        updateApproachCollisionRect();
    }

    private void updateApproachCollisionRect() {
        float offset = dir == Dir.LEFT || dir == Dir.UP ? approachCollisionSize : 1;
        int offsetCenterX = 0;
        int offsetCenterY = 0;
        if (dir.getVecY() == 0) {
            offsetCenterY = (entity.getTransform().getSize().height - 10) / 2;
        } else if (dir.getVecX() == 0) {
            offsetCenterX = (entity.getTransform().getSize().width - 10) / 2;
        }
        approachCollisionRect.x = (int)entity.getTransform().getPosition().x + (int)(dir.getVecX() * entity.getTransform().getSize().width * offset) + offsetCenterX;
        approachCollisionRect.y = (int)entity.getTransform().getPosition().y + (int)(dir.getVecY() * entity.getTransform().getSize().height * offset) + offsetCenterY;
        approachCollisionRect.width = dir.getVecX() == 0 ? 10 : entity.getTransform().getSize().width + (int)(Math.abs(dir.getVecX()) * (approachCollisionSize - 1) * entity.getTransform().getSize().width);
        approachCollisionRect.height = dir.getVecY() == 0 ? 10 :  entity.getTransform().getSize().height  + (int)(Math.abs(dir.getVecY()) * (approachCollisionSize - 1) * entity.getTransform().getSize().height);
    }


    private void updateBullet() {
        if (!isAutoFire) return;
        if (bulletCountNumber() == 0 && !isApproachCollision) {
            bulletManager.fire();
        }
    }

    public void fire() {
        bulletManager.fire();
    }

    private void updatePos(float dt) {
        Transform transform = entity.getTransform();
        Vec2f position = new Vec2f(transform.getPosition());
        position.y = position.y + dir.getVecY() * tankEntity.getMoveSpeed() ;
        position.x = position.x + dir.getVecX() * tankEntity.getMoveSpeed() ;
        if (canMove(position.x, position.y)) {
            transform.setPosition(position.x, position.y);
        } else if (isAutoTurn) {
            Dir value = Dir.values()[random.nextInt(Dir.values().length)];
            if (value != dir) {
                dir = value;
            }
        }
    }

    private boolean canMove(float x, float y) {
        return collisionDetection(x, y) && traversalFourPoints(x,y, ps -> PLAYGROUND_RECT.contains(ps[0], ps[1]));
    }

    private boolean isApproachCollision = false;
    private float approachCollisionSize = 3;
    private Rectangle approachCollisionRect;

    private boolean collisionDetection(float tankX, float tankY) {
        isApproachCollision = false;
        Rectangle hitbox = entity.getHitbox();
        List<GameEntity> entities = TankGame.getCurrentScene().filter(GameType.TILE);
        TileComponent component;
        for (int i = 0; i < entities.size(); i++) {
            GameEntity gameEntity = entities.get(i);
            component = gameEntity.get(TileComponent.class);
            if (component.getType() == 1 || component.getType() == 2 || component.getType() == 4) {
                if(component.getType() == 1 || component.getType() == 4) {
                    if (gameEntity.getHitbox().intersects(approachCollisionRect.x,
                            approachCollisionRect.y,
                            approachCollisionRect.width,
                            approachCollisionRect.height)) {
                        isApproachCollision = true;
                    }
                }
                if (intersects(tankX, tankY, hitbox.width, hitbox.height,gameEntity.getTransform().getPosition().x,
                        gameEntity.getTransform().getPosition().y,
                        gameEntity.getTransform().getSize().width,
                        gameEntity.getTransform().getSize().height)) {
                    return false;
                }
            }
        }

        if (!PLAYGROUND_RECT.contains(approachCollisionRect.x,
                approachCollisionRect.y,
                approachCollisionRect.width,
                approachCollisionRect.height
                )) {
            isApproachCollision = true;
        }
        return true;
    }

    public boolean intersects(float tankX, float tankY, float tankWidth, float tankHeight,
                              float tileX, float tileY, float tileWidth, float tileHeight) {
        float tw = tankWidth;
        float th = tankHeight;
        float rw = tileWidth;
        float rh = tileHeight;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        rw += tileX;
        rh += tileY;
        tw += tankX;
        th += tankY;
        //      overflow || intersect
        return ((rw < tileX || rw > tankX) &&
                (rh < tileY || rh > tankY) &&
                (tw < tankX || tw > tileX) &&
                (th < tankY || th > tileY));
    }

    public boolean traversalFourPoints(float x, float y, Function<float[], Boolean> handler) {
        float tempX = x, tempY = y;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                tempX += TANK_WIDTH;
            }
            if (i == 2) {
                tempY += TANK_HEIGHT;
            }
            if (i == 3) {
                tempX -= TANK_WIDTH;
            }
            if (!handler.apply(new float[]{tempX, tempY})) {
                return false;
            }
        }
        return true;
    }


    static Random random = new Random();

    static Rectangle PLAYGROUND_RECT = new Rectangle(
            Settings.PLAYGROUND_MARGIN_LEFT,
            Settings.PLAYGROUND_MARGIN_TOP,
            Settings.PLAYGROUND_WIDTH,
            Settings.PLAYGROUND_HEIGHT
    );

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }


    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setAutoTurn(boolean autoTurn) {
        isAutoTurn = autoTurn;
    }

    public void setAutoFire(boolean autoFire) {
        isAutoFire = autoFire;
    }

    public boolean canMove(BulletComponent bulletComponent) {
        // 判断是否超出边界
        if (!PLAYGROUND_RECT.contains(new Rectangle(bulletComponent.getNextX(), bulletComponent.getNextY(),
                bulletComponent.getWidth(), bulletComponent.getHeight()))) return false;
        int bulletX = bulletComponent.getNextX();
        int bulletY = bulletComponent.getNextY();
        List<GameEntity> entities = TankGame.getCurrentScene().filter(GameType.TILE);
        // 子弹和砖块发生碰撞
        for (int i = 0; i < entities.size(); i++) {
            GameEntity gameEntity = entities.get(i);
            TileComponent component = gameEntity.get(TileComponent.class);
            if (component.getType() == 1 || component.getType() == 4) {
                if (gameEntity.getHitbox().intersects(bulletX, bulletY, bulletComponent.getWidth(), bulletComponent.getHeight())) {
                    if (component.getType() == 1) {
                        TankGame.getCurrentScene().getEntities().remove(gameEntity);
                    }
                    return false;
                }
            }
        }
//      子弹与坦克发生碰撞
        if ( entity.has(GameType.PLAYER)){
            List<GameEntity> enemies = TankGame.getCurrentScene().filter(GameType.ENEMY);
            for (int i = 0; i < enemies.size(); i++) {
                GameEntity gameEntity = enemies.get(i);
                TankComponent tankComponent = gameEntity.get(TankComponent.class);
                for (BulletComponent component : tankComponent.getBulletManager().getBulletComponents()) {
                    if (intersects(bulletX, bulletY, bulletComponent.getWidth(), bulletComponent.getHeight(),
                            component.getX(), component.getY(), component.getWidth(), component.getHeight()
                    )) {
                        tankComponent.getBulletManager().explode(component);
                        return false;
                    }
                }
                if (gameEntity.getHitbox().intersects(bulletX, bulletY, bulletComponent.getWidth(), bulletComponent.getHeight())
                    && !tankComponent.died
                ) {
                    gameEntity.get(TankComponent.class).death();
                    return false;
                }
            }
        } else if (entity.has(GameType.ENEMY)) {
            List<GameEntity> players = TankGame.getCurrentScene().filter(GameType.PLAYER);
            for (int i = 0; i < players.size(); i++) {
                GameEntity gameEntity = players.get(i);
                if (gameEntity.getHitbox().intersects(bulletX, bulletY, bulletComponent.getWidth(), bulletComponent.getHeight())) {
                    gameEntity.get(TankComponent.class).restart();
                    ((PlayerTank)gameEntity.get(TankComponent.class).getTankEntity()).addHealth(-1);
                    return false;
                }
            }
            List<GameEntity> flags = TankGame.getCurrentScene().filter(GameType.FLAG);
            for (GameEntity gameEntity : flags) {
                if (gameEntity.getHitbox().intersects(bulletX, bulletY, bulletComponent.getWidth(), bulletComponent.getHeight())) {
                    TankGame.getCurrentScene().getEntities().remove(gameEntity);

                    GameEntity gameEntity1 = new GameEntity("errorFlag", gameEntity.getTransform());
                    gameEntity1.add(TileComponent.create(7));
                    TankGame.getCurrentScene().addToScene(gameEntity1);
                    return false;
                }
            }
        }
        return true;
    }

    private void death() {
        this.died = true;
    }

    public void restart() {
        this.dir = Dir.valueOf(initDir);
        this.isInitialized = false;
        entity.getTransform().setPosition(initX, initY);
    }


    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public int bulletCountNumber() {
        return bulletManager.size();
    }

    public TankEntity getTankEntity() {
        return tankEntity;
    }
}
