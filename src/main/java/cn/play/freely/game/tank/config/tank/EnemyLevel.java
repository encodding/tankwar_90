package cn.play.freely.game.tank.config.tank;

import cn.play.freely.game.tank.entity.tank.TankLevel;

public enum EnemyLevel implements TankLevel {
    LEVEL_1(0),
    LEVEL_2(1),
    LEVEL_3(9);

    private final int code;

    EnemyLevel(int code) {
        this.code = code;
    }

    @Override
    public int getLevelCode() {
        return code;
    }
}
