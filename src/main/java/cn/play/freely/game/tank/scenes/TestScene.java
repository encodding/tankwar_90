package cn.play.freely.game.tank.scenes;

import cn.play.freely.game.tank.components.input.MouseMotionComponent;
import cn.play.freely.game.tank.components.common.RectangleComponent;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.Transform;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TestScene extends Scene{


    public TestScene() {
        GameEntity gameEntity = new GameEntity("这是一个方块", new Transform(new Vec2f(0,0),
                new Dimension(50, 50)
                ));
        RectangleComponent retangleComponent = new RectangleComponent(Color.RED);

        MouseMotionComponent mouseComponent = new MouseMotionComponent();

//        mouseComponent.onClick(MouseEvent.BUTTON1, (e) -> {
//            System.out.println("点击了一下");
//        });
        mouseComponent.onHover((e, entity) -> {
            retangleComponent.setBgColor(Color.PINK);
        });
        mouseComponent.onLeave((e, entity) -> {
            retangleComponent.setBgColor(Color.RED);
        });

        mouseComponent.onDragged(MouseEvent.BUTTON1, (e, entity, offsetX, offsetY) -> {
            gameEntity.getTransform().setPosition(e.getX() + offsetY, e.getY() + offsetY);
        });

        gameEntity.add(retangleComponent);
        gameEntity.add(mouseComponent);

        addToScene(gameEntity);
    }



    @Override
    protected void doRender(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0, 800, 600);
    }

}
