package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 编辑按钮组件
 */
public class EditButtonComponent extends Component {

    /**
     * 绘制类型
     */
    private int editType = -1;
    private boolean minSize = true;
    private BufferedImage editImage;
    private int x;
    private int y;

    public EditButtonComponent(int editType, boolean minSize) {
        this.editType = editType;
        this.minSize = minSize;
        if (editType == 0) {
            if (minSize) {
                editImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_EMPTY);
            } else {
                editImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_EMPTY4);
            }
        } else if (editType <= AssetPoolUtils.SPRITE_SIZE) {
            editImage = AssetPoolUtils.TILE_SPRITE_IMG.getSubimage( (editType - 1) * Settings.DEFAULT_TILE_WIDTH,0,Settings.DEFAULT_TILE_WIDTH, Settings.DEFAULT_TILE_HEIGHT);
        }
    }

    public EditButtonComponent(int editType) {
        this(editType, true);
    }

    public int getEditType() {
        return editType;
    }

    @Override
    public void update(float dt) {
        if (minSize) {
            this.x = (entity.getTransform().getSize().width - Settings.TILE_WIDTH ) / 2 + (int) entity.getTransform().getPosition().x;
            this.y = (entity.getTransform().getSize().height - Settings.TILE_HEIGHT ) / 2 + (int) entity.getTransform().getPosition().y;
        } else {
            this.x = (entity.getTransform().getSize().width - Settings.TILE_WIDTH * 2 ) / 2 + (int) entity.getTransform().getPosition().x;
            this.y = (entity.getTransform().getSize().height - Settings.TILE_HEIGHT * 2 ) / 2 + (int) entity.getTransform().getPosition().y;
        }
    }

    @Override
    public void render(Graphics g) {
        if (minSize) {
            g.drawImage(editImage,x, y, Settings.TILE_WIDTH, Settings.TILE_HEIGHT , null);
        } else {
            if (editType == 0) {
                g.drawImage(editImage,x, y, Settings.TILE_WIDTH * 2, Settings.TILE_HEIGHT * 2, null);
            } else {
                g.drawImage(editImage,x, y, Settings.TILE_WIDTH , Settings.TILE_HEIGHT, null);
                g.drawImage(editImage,x + Settings.TILE_WIDTH, y, Settings.TILE_WIDTH , Settings.TILE_HEIGHT , null);
                g.drawImage(editImage,x, y + Settings.TILE_HEIGHT, Settings.TILE_WIDTH, Settings.TILE_HEIGHT, null);
                g.drawImage(editImage,x+ Settings.TILE_WIDTH, y + Settings.TILE_HEIGHT, Settings.TILE_WIDTH , Settings.TILE_HEIGHT , null);
            }
        }
    }

    public boolean isFlag(int x, int y, int flagX, int flagY) {
        return (flagX == y && x == flagY) ||
        (flagX == y && x == flagY + 1) ||
        (flagX + 1 == y && x == flagY) ||
         (flagX + 1 == y && x == flagY + 1);

    }

    public void drawGrid(int[][] grid, MouseEvent e,int flagX, int flagY) {
        int x = (e.getX() - Settings.PLAYGROUND_MARGIN_LEFT) / Settings.TILE_WIDTH;
        int y = (e.getY() - Settings.PLAYGROUND_MARGIN_TOP) / Settings.TILE_HEIGHT;
        if (x < 0 || x >= Settings.PLAYGROUND_COL || y < 0 || y >= Settings.PLAYGROUND_ROW
        || isFlag(x, y, flagX, flagY)
        ) return;
        grid[y][x] = editType;
        if (!minSize) {
            if (y + 1 < Settings.PLAYGROUND_ROW && !isFlag(x, y + 1, flagX, flagY)) grid[y + 1][x] = editType;
            if (x + 1 < Settings.PLAYGROUND_COL && y + 1 < Settings.PLAYGROUND_ROW  && !isFlag(x + 1, y + 1, flagX, flagY)) grid[y + 1][x + 1] = editType;
            if (x + 1 < Settings.PLAYGROUND_COL  && !isFlag(x + 1, y, flagX, flagY)) grid[y][x + 1] = editType;
        }
    }
}
