package cn.play.freely.game.tank.components.common;


import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.util.AssetPoolUtils;
import cn.play.freely.game.tank.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteComponent extends Component {

    public BufferedImage spriteImage;

    public SpriteComponent(String spriteImagePath) {
        super();
        spriteImage = AssetPoolUtils.loadTexture(spriteImagePath);
    }

    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        entity.getTransform().setSize(spriteImage.getWidth(), spriteImage.getHeight());
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(ImageUtils.rotateImage(spriteImage, entity.getTransform().getRotate()), (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height, null);
    }

    @Override
    public void update(float dt) {

    }
}
