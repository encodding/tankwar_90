package cn.play.freely.game.tank.components.bullet;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.components.common.AnimationComponent;
import cn.play.freely.game.tank.components.tank.TankComponent;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.entity.bullet.BulletEntityBuilder;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulletManager extends Component {
    private BulletEntityBuilder bulletBuilder;
    private List<BulletComponent> bulletComponents;
    private TankComponent tankComponent;

    public BulletManager(TankComponent tankComponent) {
        this.tankComponent = tankComponent;
        this.bulletComponents = new ArrayList<>();
        this.bulletBuilder = new BulletEntityBuilder(tankComponent);
    }


    public List<BulletComponent> getBulletComponents() {
        return bulletComponents;
    }

    @Override
    public void render(Graphics g) {
        for (int i = 0; i < bulletComponents.size(); i++) {
            bulletComponents.get(i).render(g);
        }
        for (int i = 0; i < explodeAnimations.size(); i++) {
            explodeAnimations.get(i).render(g);
        }
    }

    @Override
    public void update(float dt) {
        updateBullet(dt);
        updateExplode(dt);
    }

    private void updateExplode(float dt) {
        for (int i = 0; i < explodeAnimations.size(); i++) {
            explodeAnimations.get(i).update(dt);
        }
    }

    private void updateBullet(float dt) {
        for (int i = 0; i < bulletComponents.size(); i++) {
            BulletComponent bulletComponent = bulletComponents.get(i);
            if (tankComponent.canMove(bulletComponent)) {
                bulletComponent.update(dt);
            } else {
                bulletComponent.stop();
                bulletComponents.remove(bulletComponent);
                explode(bulletComponent.getX(), bulletComponent.getY());
            }
        }
    }

    private List<GameEntity> explodeAnimations = new ArrayList<>();

    public void explode(BulletComponent bulletComponent) {
        bulletComponent.stop();
        bulletComponents.remove(bulletComponent);
        explode(bulletComponent.getX(), bulletComponent.getY());
    }

    private void explode(int x, int y) {
        boolean hasExplode = false;
        Iterator<GameEntity> iterator = explodeAnimations.iterator();
        AnimationComponent animationComponent;
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();
            animationComponent = entity.get(AnimationComponent.class);
            if (animationComponent.isCompleted()) {
                iterator.remove();
                break;
            }
            if (entity.getHitbox().contains(x, y)) {
                animationComponent.increaseLoop();
                hasExplode = true;
                break;
            }
        }
        if (!hasExplode) {
            GameEntity animation = new GameEntity(
                    "Animation",
                    new Transform(new Vec2f(x,y), new Dimension())
            );
            animationComponent = new AnimationComponent("/textures/explode/explode_level_1.png",
                    1, 5, 5);
            animationComponent.setOffsetX(- 25);
            animationComponent.setOffsetY(- 25);
            animation.add(animationComponent);
            explodeAnimations.add(animation);
        }
    }

    public void fire() {
        bulletComponents.add(bulletBuilder.createBullet());
    }

    public int size() {
        return bulletComponents.size();
    }
}
