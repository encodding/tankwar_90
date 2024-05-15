package cn.play.freely.game.tank.config.tank;

import cn.play.freely.game.tank.entity.tank.TankLevel;

public enum PlayerLevel implements TankLevel {
    LEVEL_1(0),
    LEVEL_2(1),
    LEVEL_3(2),
    LEVEL_4(3),
    ;

    private final  int code;

    PlayerLevel(int code) {
        this.code = code;
    }

    @Override
    public int getLevelCode() {
        return code;
    }
}
