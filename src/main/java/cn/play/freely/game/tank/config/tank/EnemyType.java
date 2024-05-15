package cn.play.freely.game.tank.config.tank;

import cn.play.freely.game.tank.entity.tank.TankType;

public enum EnemyType implements TankType {
    TYPE_1(1),
    TYPE_2(2);

    private final int code;

    EnemyType(int code) {
        this.code = code;
    }

    @Override
    public int getTypeCode() {
        return code;
    }
}
