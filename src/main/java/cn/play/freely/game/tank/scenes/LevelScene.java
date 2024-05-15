package cn.play.freely.game.tank.scenes;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.components.common.TileComponent;
import cn.play.freely.game.tank.components.input.KeyCodeComponent;
import cn.play.freely.game.tank.components.tank.TankComponent;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.config.Dir;
import cn.play.freely.game.tank.config.tank.PlayerType;
import cn.play.freely.game.tank.entity.GameObjectType;
import cn.play.freely.game.tank.entity.GameType;
import cn.play.freely.game.tank.entity.tank.EnemyTank;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.entity.tank.PlayerTank;
import cn.play.freely.game.tank.util.AssetPoolUtils;
import cn.play.freely.game.tank.util.LevelUtils;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static cn.play.freely.game.tank.config.Settings.TANK_HEIGHT;
import static cn.play.freely.game.tank.config.Settings.TANK_WIDTH;

public class LevelScene extends Scene {
    private static final int MAX_ENEMIES_SIZE = 10;
    private int enemiesSize = 0;

    private boolean gameOver = false;

    private boolean pause = false;

    private static final String tank1 = "Player1";

    private static final String tank2 = "Player2";

    public LevelScene() {
        initResources();
        GameEntity playground = new GameEntity("Playground", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT, Settings.PLAYGROUND_MARGIN_TOP),
                new Dimension(Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT)));
        addToScene(playground);

        GameEntity levelScene = new GameEntity("LevelScene", new Transform(
                new Vec2f(0, 0),
                new Dimension(Settings.WIDTH, Settings.HEIGHT)));
        levelScene.add(new KeyCodeComponent().onClick(KeyEvent.VK_ESCAPE, (e, entity) -> {
            game.changeScene(0);
        }));
        addToScene(levelScene);

        // 玩家1
        GameEntity playerTank = new GameEntity("Player1",
                new Transform(
                        new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + 9 * Settings.TILE_WIDTH + (48 - TANK_WIDTH) /2,  (48 - TANK_HEIGHT) /2 +(Settings.PLAYGROUND_ROW - 2) * Settings.TILE_HEIGHT + Settings.PLAYGROUND_MARGIN_TOP),
                        new Dimension(TANK_WIDTH, TANK_HEIGHT)
                ), 1, GameType.TANK, GameType.PLAYER);
        playerTank.add(new TankComponent(new PlayerTank(PlayerType.PLAYER_1)));
        playerTank.add(createPlayer1KeyCodeComponent(playerTank));
        addToScene(playerTank);

        // 玩家2
        GameEntity playerTank1 = new GameEntity("Player2",
                new Transform(
                        new Vec2f(Settings.PLAYGROUND_MARGIN_RIGHT + 10 * Settings.TILE_WIDTH + (48 - TANK_WIDTH) /2,  (48 - TANK_HEIGHT) /2 +(Settings.PLAYGROUND_ROW - 2) * Settings.TILE_HEIGHT + Settings.PLAYGROUND_MARGIN_TOP),
                        new Dimension(TANK_WIDTH, TANK_HEIGHT)
                ), 1, GameType.TANK, GameType.PLAYER);
        playerTank1.add(new TankComponent(new PlayerTank(PlayerType.PLAYER_2)));
        playerTank1.add(createPlayer2KeyCodeComponent(playerTank1));
        addToScene(playerTank1);
    }


    private BufferedImage uiView;
    private BufferedImage levelFlag;
    private BufferedImage gameOverTexture;

    private boolean initialized;
    private void initResources() {
        uiView = AssetPoolUtils.loadTexture(Constant.TEXTURE_UI_UI_VIEW);
        levelFlag = AssetPoolUtils.loadTexture(Constant.TEXTURE_UI_LEVEL_FLAG);
        gameOverTexture = AssetPoolUtils.loadTexture(Constant.TEXTURE_UI_GAME_OVER);
        gameOverOffsetY = (Settings.HEIGHT + gameOverTexture.getHeight()) / 2.0f;
    }


    Random random = new Random();

    public void randomEnemies() {
        if (enemiesSize >= MAX_ENEMIES_SIZE) return;
        int a = random.nextInt(9);
        if (a == 0){
            a = a + 1;
        }
        if ((a + enemiesSize) > MAX_ENEMIES_SIZE + 1){
            a = (a + enemiesSize) - MAX_ENEMIES_SIZE + 1;
        }
        if (a > MAX_ENEMIES_SIZE){
            a = MAX_ENEMIES_SIZE;
        }
        int x, y;
        for (int i = 0; i < a; i++) {
            x = Settings.PLAYGROUND_MARGIN_LEFT + random.nextInt(Settings.PLAYGROUND_WIDTH - TANK_WIDTH);
            y = Settings.PLAYGROUND_MARGIN_TOP + random.nextInt(Settings.PLAYGROUND_HEIGHT / 4);
            if (!canGenerate(x, y, TANK_WIDTH, TANK_HEIGHT)) {
                continue;
            }
            GameEntity entity = new GameEntity("EnemyTank" + i, new Transform(
                    new Vec2f(x, y),
                    new Dimension(TANK_WIDTH, TANK_HEIGHT)
            ), 1, GameType.TANK, GameType.ENEMY);
            TankComponent tankComponent = new TankComponent(new EnemyTank());
            entity.add(tankComponent);
            addToScene(entity);
            i++;
            enemiesSize++;
        }
    }

    private boolean canGenerate(int x, int y, int w, int h) {
        List<GameEntity> tiles = filter(GameType.TILE);
        for (GameEntity tile : tiles) {
            TileComponent component = tile.get(TileComponent.class);
            if (component.getType() == 1 || component.getType() == 2 || component.getType() == 4) {
                if (tile.getHitbox().intersects(x, y, w, h)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Component createPlayer1KeyCodeComponent(GameEntity playerTank) {
        TankComponent tankComponent = playerTank.get(TankComponent.class);
        return new KeyCodeComponent()
          .onClick(KeyEvent.VK_W, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.UP)))
          .onClick(KeyEvent.VK_S, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.DOWN)))
          .onClick(KeyEvent.VK_A, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.LEFT)))
          .onClick(KeyEvent.VK_D, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.RIGHT)))
          .onClick(KeyEvent.VK_SPACE, (e, entity) -> {
                if (tankComponent.bulletCountNumber() == 0) {
                    tankComponent.fire();
                }

          });
    }

    private Component createPlayer2KeyCodeComponent(GameEntity playerTank) {
        TankComponent tankComponent = playerTank.get(TankComponent.class);
        return new KeyCodeComponent()
                .onClick(KeyEvent.VK_UP, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.UP)))
                .onClick(KeyEvent.VK_DOWN, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.DOWN)))
                .onClick(KeyEvent.VK_LEFT, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.LEFT)))
                .onClick(KeyEvent.VK_RIGHT, createHandler(tankComponent, () ->  tankComponent.setDir(Dir.RIGHT)))
                .onClick(KeyEvent.VK_NUMPAD9, (e, entity) -> {
                    if (tankComponent.bulletCountNumber() == 0) {
                        tankComponent.fire();
                    }

                });
    }

    private KeyCodeComponent.KeyCodeHandler createHandler(TankComponent tankComponent, Runnable runnable) {

        return new KeyCodeComponent.KeyCodeHandler() {
            @Override
            public void handle(KeyEvent e, GameEntity entity) {
                runnable.run();
                tankComponent.setMoving(false);
            }

            @Override
            public void pressed(KeyEvent e, GameEntity entity) {
                runnable.run();
                tankComponent.setMoving(true);
            }
        };
    }

    public void setGrid(int[][] grid) {
        initialized = false;
        GameEntity tileItem;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 0) continue;
                tileItem = new GameEntity("tileItem" + x + "_" + y, new Transform(
                        new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                                Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT),
                        new Dimension( grid[y][x] == 6 ? Settings.TILE_WIDTH * 2 : Settings.TILE_WIDTH,
                                grid[y][x] == 6 ? Settings.TILE_HEIGHT * 2 : Settings.TILE_HEIGHT)),
                        grid[y][x] == 5 ? 2: 0,
                        getTileType(grid[y][x])
                );
                tileItem.add(TileComponent.create(grid[y][x]));
                addToScene(tileItem);
            }
        }

        randomEnemies();
        initialized = true;
    }



    private GameObjectType[] getTileType(int type) {
        switch (type) {
            case 1:
                return new GameObjectType[] {GameType.TILE, GameType.BRICK , GameType.FIRE_BRICK};
            case 2:
                return new GameObjectType[] {GameType.TILE, GameType.WATER};
            case 3:
                return new GameObjectType[] {GameType.TILE, GameType.SNOW};
            case 4:
                return new GameObjectType[] {GameType.TILE,  GameType.BRICK ,GameType.CERAMIC_BRICK};
            case 5:
                return new GameObjectType[] {GameType.TILE, GameType.GRASS};
            case 6:
                return new GameObjectType[] {GameType.FLAG};
            default:
                return new GameObjectType[] {GameType.TILE};
        }

    }

    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x7F7F7F));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(Settings.PLAYGROUND_MARGIN_LEFT,
                Settings.PLAYGROUND_MARGIN_TOP,
                Settings.PLAYGROUND_WIDTH,
                Settings.PLAYGROUND_HEIGHT);
    }


    private static int offsetX = (Settings.PLAYGROUND_MARGIN_RIGHT - 20 * 2 - 10) / 2;
    private int player1Health = 0;

    private int player2Health = 0;
    @Override
    protected void doRenderEntityAfter(Graphics g) {
        drawEnemiesCount(g);
        drawPlayerLife(g);



    }

    private void drawPlayerLife(Graphics g) {
        int topOffset = Settings.HEIGHT - (20 + 10) *4  - 150;
        g.drawImage(uiView.getSubimage(28,0, 28, 14),
                Settings.PLAYGROUND_MARGIN_LEFT
                        + Settings.PLAYGROUND_WIDTH +
                        + offsetX,
                topOffset,
                40, 20, null
                );
        g.drawImage(uiView.getSubimage(14,0, 14, 14),
                Settings.PLAYGROUND_MARGIN_LEFT
                        + Settings.PLAYGROUND_WIDTH +
                        + offsetX,
                topOffset + 30,
                20, 20, null
        );
        g.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        g.drawString(String.valueOf(player1Health), Settings.PLAYGROUND_MARGIN_LEFT
                + Settings.PLAYGROUND_WIDTH +
                + offsetX + 30, topOffset + 30 + 16);

        g.drawImage(uiView.getSubimage(28 * 2,0, 28, 14),
                Settings.PLAYGROUND_MARGIN_LEFT
                        + Settings.PLAYGROUND_WIDTH +
                        + offsetX,
                topOffset + 30 * 2,
                40, 20, null
        );
        g.drawImage(uiView.getSubimage(14,0, 14, 14),
                Settings.PLAYGROUND_MARGIN_LEFT
                        + Settings.PLAYGROUND_WIDTH +
                        + offsetX,
                topOffset + 30 * 3,
                20, 20, null
        );
        g.drawString(String.valueOf(player2Health), Settings.PLAYGROUND_MARGIN_LEFT
                + Settings.PLAYGROUND_WIDTH +
                + offsetX + 30, topOffset + 30 * 3 + 16);

        g.drawImage(levelFlag.getSubimage(0,0, 40, 40),
                Settings.PLAYGROUND_MARGIN_LEFT
                        + Settings.PLAYGROUND_WIDTH +
                        + offsetX,
                topOffset + 30 * 4,
                40, 40, null
        );
        g.drawString(String.valueOf(this.levelIndex+1), Settings.PLAYGROUND_MARGIN_LEFT
                + Settings.PLAYGROUND_WIDTH +
                + offsetX + 30, topOffset + 30 * 4 + 50);
    }

    private void drawEnemiesCount(Graphics g) {
        for (int i = 0; i < MAX_ENEMIES_SIZE - (enemiesSize - filter(GameType.ENEMY).size()); i++) {
            g.drawImage(uiView.getSubimage(0,0, 14, 14),
                    Settings.PLAYGROUND_MARGIN_LEFT
                            + Settings.PLAYGROUND_WIDTH +
                            + offsetX + (10 + 20) * (i % 2) ,
                    Settings.PLAYGROUND_MARGIN_TOP + (20 + 10) * (i / 2), 20, 20, null);
        }
    }

    private static final Scene levelScene = new LevelScene();

    public static Scene get() {
        return levelScene;
    }

    private int addEnemiesTimeIndex = 0;

    @Override
    protected void doUpdateEntityAfter(float dt) {
        if (addEnemiesTimeIndex >= (1 / dt) * 10 || filter(GameType.ENEMY).isEmpty()) {
            randomEnemies();
            addEnemiesTimeIndex = 0;
        }
        addEnemiesTimeIndex++;
    }

    private int levelIndex;
    private boolean enableNextLevel;

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    private float gameOverOffsetY = 0;

    @Override
    public void render(Graphics g) {
        if (initialized && !pause) {
            super.render(g);
            if (gameOver) {
                g.drawImage(gameOverTexture,
                        Settings.PLAYGROUND_MARGIN_LEFT + (Settings.PLAYGROUND_WIDTH - gameOverTexture.getWidth()) / 2,
                        (Settings.HEIGHT - gameOverTexture.getHeight()) / 2 + (int)gameOverOffsetY ,gameOverTexture.getWidth(),gameOverTexture.getHeight(), null);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (initialized && !pause && !gameOver) {
            super.update(dt);
            if ( !enableNextLevel && MAX_ENEMIES_SIZE - (enemiesSize - filter(GameType.ENEMY).size()) == 0) { // 跳转至下一关
                if (this.levelIndex + 1 >= LevelUtils.levels.size()) {
                    game.changeScene(0);
                } else {
                    this.levelIndex++;
                    enableNextLevel = true;
                    pause = true;
                    resetGame();
                    setGrid(LevelUtils.get(levelIndex));
                    pause = false;
                    enableNextLevel = false;
                }
            }
            List<GameEntity> list = filter(GameType.PLAYER);
            Optional<GameEntity> gameEntity = list.stream()
                    .filter(g -> tank1.equals(g.getName()))
                    .findFirst();
            if (gameEntity.isPresent()){
                player1Health = ((PlayerTank) gameEntity.get().get(TankComponent.class).getTankEntity()).getHealth();
            }
            if (player1Health <= 0){
                List<GameEntity> list1 = filter(GameType.PLAYER);
                if (list1.size()>0){
                    moveTank(tank1);
                }
            }
            Optional<GameEntity> gameEntity1 = list.stream()
                    .filter(g -> tank2.equals(g.getName()))
                    .findFirst();
            if (gameEntity1.isPresent()){
                player2Health = ((PlayerTank) gameEntity1.get().get(TankComponent.class).getTankEntity()).getHealth();
            }
            if (player2Health <= 0){
                List<GameEntity> list1 = filter(GameType.PLAYER);
                if (list1.size()>0){
                    moveTank(tank2);
                }
            }
            if ((player1Health == 0 && player2Health == 0) || filter(GameType.FLAG).isEmpty()) {
                gameOver = true;
            }
        } else if (gameOver) {
            if (gameOverOffsetY > 0) {
                gameOverOffsetY -= 0.5f;
            }
        }
    }

    private void resetGame() {
        enableNextLevel = false;
        gameOver = false;
        pause = false;
        enemiesSize = 0;
        GameType[] types = new GameType[]{
                GameType.TILE,
                GameType.FLAG,
                GameType.ENEMY
        };
        entities.removeIf(item -> Arrays.stream(types).anyMatch(item::has));
        entities.stream().filter(item -> item.has(GameType.PLAYER)).forEach(item -> {
            item.get(TankComponent.class).restart();
        });
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }
}
