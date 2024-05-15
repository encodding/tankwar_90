package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileComponent extends Component {
    private final int type;
    private final BufferedImage tileImage;

    public int getType() {
        return type;
    }

    public TileComponent(int type) {
        this.type = type;
        if (type == 6) {
            tileImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_FLAG);
        } else if (type == 2) {
            tileImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_SEA_ANIM);
        } else if (type == 7) {
            tileImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_MAP_FLAG_FAILED);
        } else {
            tileImage = AssetPoolUtils.TILE_SPRITE_IMG.getSubimage( (type - 1) * Settings.DEFAULT_TILE_WIDTH,0,Settings.DEFAULT_TILE_WIDTH, Settings.DEFAULT_TILE_HEIGHT);
        }
    }

    public static TileComponent create(int index) {
        return new TileComponent(index);
    }

    private int aniIdx = 0;
    private int idx = 0;

    @Override
    public void render(Graphics g) {
        if (type == 2) {
            g.drawImage(tileImage.getSubimage((idx % 2) * 24, 0, 24, 24),
                    (int) entity.getTransform().getPosition().x, (int)entity.getTransform().getPosition().y,
                    entity.getTransform().getSize().width,
                    entity.getTransform().getSize().height, null);
        } else {
            g.drawImage(tileImage,
                    (int) entity.getTransform().getPosition().x, (int)entity.getTransform().getPosition().y,
                    entity.getTransform().getSize().width,
                    entity.getTransform().getSize().height, null);
        }

        debug(() -> {
            g.setColor(Color.red);
            g.drawRect((int) entity.getTransform().getPosition().x, (int)entity.getTransform().getPosition().y,
                    entity.getTransform().getSize().width,
                    entity.getTransform().getSize().height);
        });
    }

    @Override
    public void update(float dt) {
        if (aniIdx > 200) {
            aniIdx = 0;
            idx ++ ;
        }
        aniIdx++;
    }

    public int getX() {
        return (int)(entity.getTransform().getPosition().x - Settings.PLAYGROUND_MARGIN_LEFT ) / Settings.TILE_WIDTH;
    }

    public int getY() {
        return (int)(entity.getTransform().getPosition().y - Settings.PLAYGROUND_MARGIN_TOP ) / Settings.TILE_HEIGHT;
    }

}
