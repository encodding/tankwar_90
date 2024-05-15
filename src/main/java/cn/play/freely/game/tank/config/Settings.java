package cn.play.freely.game.tank.config;

public class Settings {

    public static final int FPS = 120;
    public static final int UPS = 200;

    public static final float SCALE = 1.0f;

    public static final int DEFAULT_TILE_WIDTH = 24;
    public static final int  DEFAULT_TILE_HEIGHT = 24;
    public static final int TILE_WIDTH = (int)(DEFAULT_TILE_WIDTH * SCALE);
    public static final int TILE_HEIGHT =  (int)(DEFAULT_TILE_HEIGHT * SCALE);


    public static final int PLAYGROUND_ROW = 13 * 2;
    public static final int PLAYGROUND_COL = 13 * 2;
    public static final int PLAYGROUND_WIDTH = TILE_WIDTH  * PLAYGROUND_ROW;
    public static final int PLAYGROUND_HEIGHT = TILE_HEIGHT  * PLAYGROUND_COL;
    public static final int PLAYGROUND_MARGIN_TOP = TILE_HEIGHT;
    public static final int PLAYGROUND_MARGIN_BOTTOM = TILE_HEIGHT;
    public static final int PLAYGROUND_MARGIN_LEFT = TILE_HEIGHT;
    public static final int PLAYGROUND_MARGIN_RIGHT = TILE_HEIGHT * 6;
    public static final int WIDTH = PLAYGROUND_WIDTH + PLAYGROUND_MARGIN_RIGHT + PLAYGROUND_MARGIN_LEFT;
    public static final int HEIGHT = PLAYGROUND_HEIGHT + PLAYGROUND_MARGIN_TOP + PLAYGROUND_MARGIN_BOTTOM;



    public static final int TANK_INITIALIZE_FRAME_WIDTH = 32;
    public static final int TANK_INITIALIZE_FRAME_HEIGHT = 32;

    public static final int TANK_WIDTH = 32;
    public static final int TANK_HEIGHT = 32;

}
