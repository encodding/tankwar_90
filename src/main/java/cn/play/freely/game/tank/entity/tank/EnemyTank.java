package cn.play.freely.game.tank.entity.tank;

import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.tank.EnemyLevel;
import cn.play.freely.game.tank.config.tank.EnemyType;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyTank extends AbstractTankEntity {

    private static final BufferedImage[] ENEMY_TANK_TEXTURES;

    static {
        ENEMY_TANK_TEXTURES = new BufferedImage[8 * 8];
        BufferedImage bufferedImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_TANK_ENEMYS);
        for (int i = 0; i < ENEMY_TANK_TEXTURES.length; i++) {
            int x = i % 8;
            int y = i / 8;
            ENEMY_TANK_TEXTURES[i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
    }

    public EnemyTank() {
        this(EnemyType.TYPE_1, EnemyLevel.LEVEL_1);
    }

    public EnemyTank(EnemyType type, EnemyLevel level) {
        this.type = type;
        this.level = level;
    }

    /**
     *  敌人坦克绘制逻辑
     * type = 2 level = 0 => default + 2 * (type - 1) + 4 * 0
     * type = 2 level = 1 => default + 2 * (type - 1) + 4 * 1
     * type = 2 level = 2 => default + 2 * (type - 1) + 4 * 9
     * @param index
     * @return
     */
    @Override
    public Image getCurrentFrame(int index) {
        return ENEMY_TANK_TEXTURES[index + component.getDir().getIndex() * 8 + 2 * (type.getTypeCode() - 1) + 4 * level.getLevelCode()];
    }
}
