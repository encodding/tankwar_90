package cn.play.freely.game.tank.config;

public enum Dir {
    UP(0, 2, 0, -1),
    RIGHT(1, 3, 1, 0),
    DOWN(2, 0, 0, 1),
    LEFT(3, 1, -1, 0);

    private final int index;
    private final int reverseIndex;
    private final int vecX;
    private final int vecY;

    public int getIndex() {
        return index;
    }

    public int getReverseIndex() {
        return reverseIndex;
    }

    public int getVecX() {
        return vecX;
    }

    public int getVecY() {
        return vecY;
    }

    private Dir(int index, int reverseIndex, int vecX, int vecY) {
        this.index = index;
        this.reverseIndex = reverseIndex;
        this.vecX = vecX;
        this.vecY = vecY;
    }
}
