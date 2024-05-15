package cn.play.freely.game.tank.scenes;

import cn.play.freely.game.tank.components.common.EditButtonComponent;
import cn.play.freely.game.tank.components.common.GridPlaygroundComponent;
import cn.play.freely.game.tank.components.common.RectangleComponent;
import cn.play.freely.game.tank.components.common.TextButtonComponent;
import cn.play.freely.game.tank.components.input.MouseMotionComponent;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.util.LevelUtils;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LevelEditorScene extends Scene {

    GridPlaygroundComponent gridPlaygroundComponent;
    GameEntity playground;
    public LevelEditorScene() {
        playground = new GameEntity("Playground", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT, Settings.PLAYGROUND_MARGIN_TOP),
                new Dimension(Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT)));
        gridPlaygroundComponent = new GridPlaygroundComponent();
        gridPlaygroundComponent.enableEdit();
        playground.add(gridPlaygroundComponent);
        addToScene(playground);
        initEditButtons();
        initOptions();
    }




    private final int[] editButtons = {1,2,3,4,5,0};

    private void initEditButtons() {
        for (int i = 0; i < editButtons.length; i++) {
            GameEntity minButton = new GameEntity("MinEditButton" + i, new Transform(
                    new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                            Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * i  + 40 * i),
                    new Dimension(Settings.TILE_WIDTH + 10, Settings.TILE_WIDTH + 6)
            ));
            minButton.add(new RectangleComponent(Color.WHITE, Color.black));
            minButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, this::onClick));
            minButton.add(new EditButtonComponent(editButtons[i]));
            addToScene(minButton);

            GameEntity maxButton = new GameEntity("MaxEditButton" + i, new Transform(
                    new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 20 + Settings.TILE_WIDTH + 10,
                            Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * i  + 40 * i),
                    new Dimension(Settings.TILE_WIDTH * 2 + 10, Settings.TILE_WIDTH * 2 + 6)
            ));
            maxButton.add(new RectangleComponent(Color.WHITE, Color.black));
            maxButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, this::onClick));
            maxButton.add(new EditButtonComponent(editButtons[i], false));
            addToScene(maxButton);
        }
    }

    private void initOptions() {
        GridPlaygroundComponent component = playground.get(GridPlaygroundComponent.class);
        GameEntity showGridLines = new GameEntity("ShowGridLines", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * 6 + 40 * 6 ),
                new Dimension(100,30)
        ));
        showGridLines.getProps().setProperty("showGrid", "false");
        showGridLines.add(new RectangleComponent(Color.WHITE, Color.BLACK));
        showGridLines.add(new TextButtonComponent("显示网格"));
        showGridLines.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            if ("true".equals(entity.getProps().getProperty("showGrid"))) {
                component.hiddenGrid();
                entity.get(TextButtonComponent.class).setText("显示网格");
                entity.getProps().setProperty("showGrid", "false");
            } else if ("false".equals(entity.getProps().getProperty("showGrid"))) {
                component.showGrid();
                entity.get(TextButtonComponent.class).setText("隐藏网格");
                entity.getProps().setProperty("showGrid", "true");
            }
        }));
        addToScene(showGridLines);

        GameEntity startGameButton = new GameEntity("startGame", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * 7 + 40 * 6 + 20),
                new Dimension(100,30)
        ));
        startGameButton.add(new RectangleComponent(Color.WHITE, Color.BLACK));
        startGameButton.add(new TextButtonComponent("开始游戏"));
        startGameButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            game.changeScene(1);
            Scene scene = game.getScene();
            if (scene instanceof LevelScene) {
                ((LevelScene)scene).setGrid(gridPlaygroundComponent.getGridData());
            }
        }));
        addToScene(startGameButton);

        GameEntity resetButton = new GameEntity("resetButton", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * 9 + 40 * 7 + 20  ),
                new Dimension(100,30)
        ));
        resetButton.add(new RectangleComponent(Color.WHITE, Color.BLACK));
        resetButton.add(new TextButtonComponent("重置"));
        resetButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            component.resetGrid();
        }));
        addToScene(resetButton);

        GameEntity backMenuButton = new GameEntity("backMenuButton", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * 8 + 40 * 7 ),
                new Dimension(100,30)
        ));
        backMenuButton.add(new RectangleComponent(Color.WHITE, Color.BLACK));
        backMenuButton.add(new TextButtonComponent("返回菜单"));
        backMenuButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            game.changeScene(0);
        }));
        addToScene(backMenuButton);

        GameEntity saveMenuButton = new GameEntity("saveMenuButton", new Transform(
                new Vec2f(Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH + 10,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.TILE_HEIGHT * 10 + 40 * 8 ),
                new Dimension(100,30)
        ));
        saveMenuButton.add(new RectangleComponent(Color.WHITE, Color.BLACK));
        if (!LevelUtils.isJarDev()){
            saveMenuButton.add(new TextButtonComponent("保存"));
        }
        saveMenuButton.add(new MouseMotionComponent().onClick(MouseEvent.BUTTON1, (e, entity) -> {
            try {
                LevelUtils.saveLevel(gridPlaygroundComponent.getGridData());
            }catch (IOException eve) {
                eve.printStackTrace();
            }
        }));
        addToScene(saveMenuButton);
    }


    public void onClick(MouseEvent e, GameEntity entity) {
        gridPlaygroundComponent.edit(entity.get(EditButtonComponent.class));
    }

    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x7F7F7F));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);
    }

    private static final Scene levelScene = new LevelEditorScene();

    public static Scene get() {
        return levelScene;
    }
}
