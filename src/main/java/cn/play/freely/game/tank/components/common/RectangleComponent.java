package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;

import java.awt.*;
import java.util.Objects;

public class RectangleComponent extends Component {

    private Color bgColor;
    private Color borderColor;

    public RectangleComponent(Color color) {
        this.bgColor = color;
        this.borderColor = null;
    }

    public RectangleComponent(Color color, Color borderColor) {
        this.bgColor = color;
        this.borderColor = borderColor;
    }

    @Override
    public void render(Graphics g) {
        Rectangle hitbox = entity.getHitbox();
        g.setColor(bgColor);
        g.fillRect(hitbox.x,hitbox.y,hitbox.width, hitbox.height);
        if (Objects.nonNull(borderColor)) {
            g.setColor(borderColor);
            g.drawRect(hitbox.x,hitbox.y,hitbox.width, hitbox.height);
        }
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
