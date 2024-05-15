package cn.play.freely.game.tank.entity;

import cn.play.freely.game.tank.components.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 游戏实体
 */
public class GameEntity implements Comparable<GameEntity> {
    /**
     * 游戏实体名称
     */
    private String name;
    /**
     * 游戏组件
     */
    private final List<Component> components;

    private Properties properties;

    private Transform transform;

    private int zIndex = 0;

    private int type = 0;

    public String getName() {
        return name;
    }

    public GameEntity(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.properties = new Properties();
        this.zIndex = 0;
        this.type = 0;
    }

    public GameEntity(String name, Transform transform, GameObjectType... types) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.properties = new Properties();
        this.zIndex = 0;
        this.type = GameObjectType.with(types);
    }

    public GameEntity(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.properties = new Properties();
        this.zIndex = zIndex;
        this.type = 0;
    }

    public GameEntity(String name, Transform transform, int zIndex, GameObjectType... types) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.properties = new Properties();
        this.zIndex = zIndex;
        this.type = GameObjectType.with(types);
    }

    public boolean has(GameObjectType type) {
        return GameObjectType.has(this.type, type);
    }

    public int getzIndex() {
        return zIndex;
    }

    public Properties getProps() {
        return properties;
    }

    public Rectangle getHitbox() {
        return new Rectangle((int)transform.getPosition().x,
                (int)transform.getPosition().y, transform.getSize().width, transform.getSize().height);
    }

    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public void render(Graphics g) {
        for (Component component : components) {
            component.render(g);
        }
    }


    public void add(Component component) {
        if (Objects.isNull(component)) return;
        if (!components.contains(component)) {
            components.add(component);
            component.setEntity(this);
        }
    }

    public <T extends Component> T get(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }


    /**
     * 获取所有组件
     * @return
     */
    public List<Component> getAllComponents() {
        return components;
    }

    @Override
    public int compareTo(GameEntity o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}
