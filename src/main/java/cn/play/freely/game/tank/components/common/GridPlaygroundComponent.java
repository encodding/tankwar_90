package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.components.input.MouseMotionComponent;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

public class GridPlaygroundComponent extends Component {

    private int[][] grid;
    private boolean editable = false;
    private boolean showGrid = false;
    private EditButtonComponent editButtonComponent;

    public GridPlaygroundComponent() {
        this.grid = new int[ Settings.PLAYGROUND_ROW][Settings.PLAYGROUND_COL];
        initGrid();
    }

    public void initGrid() {
        this.grid[this.grid.length - 1][11] = 1;
        this.grid[this.grid.length - 2][11] = 1;
        this.grid[this.grid.length - 3][11] = 1;
        this.grid[this.grid.length - 2][12] = 6;
        this.grid[this.grid.length - 3][12] = 1;
        this.grid[this.grid.length - 3][13] = 1;
        this.grid[this.grid.length - 3][14] = 1;
        this.grid[this.grid.length - 2][14] = 1;
        this.grid[this.grid.length - 1][14] = 1;
    }


    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        this.entity.add(new MouseMotionComponent().onDragged(MouseEvent.BUTTON1, this::onDragged).onClick(MouseEvent.BUTTON1, this::onClick));
    }

    private void onDragged(MouseEvent e, GameEntity entity, int offsetX, int offsetY) {
        onClick(e, entity);
    }

    private void onClick(MouseEvent e, GameEntity entity) {
        if (!editable || Objects.isNull(editButtonComponent)) return;
        editButtonComponent.drawGrid(this.grid, e, this.grid.length - 2, 12);
    }

    public void setGrid(int[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                this.grid[y][x] = grid[y][x];
            }
        }
    }

    @Override
    public void render(Graphics g) {
        renderBackground(g);
        renderGridLines(g);
        renderTiles(g);
    }

    private void renderTiles(Graphics g) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 0) continue;
                if (grid[y][x] != 6 ) {
                    g.drawImage(AssetPoolUtils.TILE_SPRITE_IMG.getSubimage( (grid[y][x] - 1) * Settings.DEFAULT_TILE_WIDTH,0,Settings.DEFAULT_TILE_WIDTH, Settings.DEFAULT_TILE_HEIGHT),
                            Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                            Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT,
                            Settings.TILE_WIDTH,
                            Settings.TILE_HEIGHT, null);
                    int finalX = x, finalY = y;
                    debug(() -> {
                        g.setColor(Color.red);
                        g.drawRect(Settings.PLAYGROUND_MARGIN_LEFT + finalX * Settings.TILE_WIDTH,
                                Settings.PLAYGROUND_MARGIN_TOP + finalY * Settings.TILE_HEIGHT,
                                Settings.TILE_WIDTH,
                                Settings.TILE_HEIGHT);
                    });
                } else {
                    g.drawImage(AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_FLAG),
                            Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                            Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT,
                            Settings.TILE_WIDTH * 2,
                            Settings.TILE_HEIGHT * 2, null);
                }
            }
        }
    }

    private void renderGridLines(Graphics g) {
        if (!showGrid) return;
        int max = Math.max(Settings.PLAYGROUND_COL - 1,Settings.PLAYGROUND_ROW - 1);
        g.setColor(new Color(0xcccccc));
        for (int i = 0; i < max; i++) {
            if (i < Settings.PLAYGROUND_COL - 1) {
                g.drawLine(
                        Settings.PLAYGROUND_MARGIN_LEFT + (i + 1) * Settings.TILE_WIDTH,
                        Settings.PLAYGROUND_MARGIN_TOP,
                        Settings.PLAYGROUND_MARGIN_LEFT + (i + 1) * Settings.TILE_WIDTH,
                        Settings.PLAYGROUND_MARGIN_TOP + Settings.PLAYGROUND_HEIGHT);
            }
            if (i < Settings.PLAYGROUND_ROW - 1) {
                g.drawLine(  Settings.PLAYGROUND_MARGIN_LEFT,
                        Settings.PLAYGROUND_MARGIN_TOP + (i + 1) * Settings.TILE_HEIGHT,
                        Settings.PLAYGROUND_MARGIN_LEFT + Settings.PLAYGROUND_WIDTH,
                        Settings.PLAYGROUND_MARGIN_TOP + (i + 1) * Settings.TILE_HEIGHT);
            }
        }
    }

    private void renderBackground(Graphics g) {
        g.setColor(new Color(0x040404));
        Rectangle hitbox = entity.getHitbox();
        g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public void edit(EditButtonComponent editButtonComponent) {
        this.editButtonComponent = editButtonComponent;
    }

    public void showGrid() {
        this.showGrid = true;
    }

    public void hiddenGrid() {
        this.showGrid = false;
    }

    public void enableEdit() {
        this.editable = true;
    }

    public void disableEdit() {
        this.editable = true;
    }

    public void resetGrid() {
        for (int y = 0; y < grid.length; y++) {
            Arrays.fill(grid[y], 0);
        }
        initGrid();
    }

    public int[][] getGridData() {
        return grid;
    }
}
