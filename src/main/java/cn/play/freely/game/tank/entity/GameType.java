package cn.play.freely.game.tank.entity;

public enum GameType implements GameObjectType {
    /**
     * 水
     */
    WATER(0b0000_0001),
    /**
     * 地砖
     */
    TILE(0b0000_0010),
    /**
     * 玩家坦克
     */
    PLAYER(0b0000_0100),
    /**
     * 敌军坦克
     */
    ENEMY(0b0000_1000),
    /**
     * 坦克
     */
    TANK(0b0001_0000),
    /**
     * 砖块
     */
    BRICK(0b0010_0000),
    /**
     * 草
     */
    GRASS(0b0100_0000),
    /**
     * 雪地
     */
    SNOW(0b1000_0000),
    /**
     * 火砖
     */
    FIRE_BRICK(0b1_0000_0000),
    /**
     * 瓷砖
     */
    CERAMIC_BRICK(0b10_0000_0000),
    FLAG(0b100_0000_0000);
    ;

    private final int type;

    GameType(int type) {
        this.type = type;
    }

    @Override
    public int type() {
        return type;
    }
}
