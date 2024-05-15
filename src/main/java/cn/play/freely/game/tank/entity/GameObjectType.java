package cn.play.freely.game.tank.entity;

public interface GameObjectType {

    int type();
    static int with(GameObjectType ...types) {
        int type = 0;
        for (GameObjectType gameObjectType : types) {
            type |= gameObjectType.type();
        }
        return type;
    }

    static boolean has(int value, GameObjectType type) {
        return  (value & type.type()) == type.type();
    }
}
