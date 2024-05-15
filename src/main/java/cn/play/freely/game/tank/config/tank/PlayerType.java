package cn.play.freely.game.tank.config.tank;

import cn.play.freely.game.tank.entity.tank.TankType;

public enum PlayerType implements TankType {
    PLAYER_1(0), PLAYER_2(1);
    private final int code;

    PlayerType(int code) {
        this.code = code;
    }

    @Override
    public int getTypeCode() {
        return code;
    }
}
