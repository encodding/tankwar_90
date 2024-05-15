package cn.play.freely.game.tank.entity.tank;

import cn.play.freely.game.tank.components.tank.TankComponent;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.tank.PlayerLevel;
import cn.play.freely.game.tank.config.tank.PlayerType;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerTank extends AbstractTankEntity {

    public static final int MAX_HEALTH = 3;

    private int health = MAX_HEALTH;


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    private static final BufferedImage[][] PLAYER_TANK_TEXTURES;
    static {
        PLAYER_TANK_TEXTURES = new BufferedImage[2][4 * 8];
        BufferedImage bufferedImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_TANK_PLAYER1);
        for (int i = 0; i < PLAYER_TANK_TEXTURES[0].length; i++) {
            int x = i % 8;
            int y = i / 8;
            PLAYER_TANK_TEXTURES[0][i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
        bufferedImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_TANK_PLAYER2);
        for (int i = 0; i < PLAYER_TANK_TEXTURES[1].length; i++) {
            int x = i % 8;
            int y = i / 8;
            PLAYER_TANK_TEXTURES[1][i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
    }
    public PlayerTank() {
        this(PlayerType.PLAYER_1, PlayerLevel.LEVEL_1);
    }
    public PlayerTank(PlayerType type) {
        this.type = type;
        this.level = PlayerLevel.LEVEL_1;
        this.movingSpeed = 0.5f;
    }
    public PlayerTank(PlayerType type, PlayerLevel level) {
        this.type = type;
        this.level = level;
        this.movingSpeed = 0.5f;
    }

    /**
     * 获取当前帧的玩家坦克图像
     *
     * @param index
     * @return
     */
    @Override
    public Image getCurrentFrame(int index) {
        return PLAYER_TANK_TEXTURES[type.getTypeCode()]
                [index + component.getDir().getIndex() * 8 + 2 * level.getLevelCode()];
    }

    @Override
    public void setComponent(TankComponent tankComponent) {
        super.setComponent(tankComponent);
        component.setAutoTurn(false); // 关闭自动转向
        component.setMoving(false); // 关闭移动
        component.setAutoFire(false); // 关闭自动发射子弹
    }

    public void addHealth(int value) {
        this.health += value;
    }
}
