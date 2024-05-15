package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.entity.Transform;

import java.awt.*;
import java.util.Objects;

public class TextButtonComponent extends Component {
    private String text;
    private Font font;

    private Color color;

    private int x;
    private int y;

    public TextButtonComponent setColor(Color color) {
        this.color = color;
        return this;
    }

    public TextButtonComponent setFont(Font font) {
        this.font = font;
        return this;
    }

    public TextButtonComponent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TextButtonComponent setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void render(Graphics g) {
        if (Objects.nonNull(color)) {
            g.setColor(color);
        }
        Transform transform = entity.getTransform();
        FontMetrics fm;
        if (Objects.nonNull(font)) {
            g.setFont(font);
            fm = g.getFontMetrics(font);
        } else {
           fm = g.getFontMetrics();
        }
        this.x = (int)transform.getPosition().x + (transform.getSize().width-(int)fm.getStringBounds(text, g).getWidth())/2;
        this.y = (int)transform.getPosition().y + (transform.getSize().height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
