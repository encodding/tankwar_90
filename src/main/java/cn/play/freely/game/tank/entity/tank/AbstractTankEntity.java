package cn.play.freely.game.tank.entity.tank;

import cn.play.freely.game.tank.components.tank.TankComponent;

public abstract class AbstractTankEntity implements TankEntity {
    protected TankComponent component;
    protected TankType type;
    protected TankLevel level;
    protected float movingSpeed;

    AbstractTankEntity() {
        this.movingSpeed = 0.25f;
    }


    @Override
    public TankType getType() {
        return type;
    }

    @Override
    public TankLevel getLevel() {
        return level;
    }

    @Override
    public float getMoveSpeed() {
        return movingSpeed;
    }

    @Override
    public void setComponent(TankComponent tankComponent) {
        this.component = tankComponent;
    }
}
