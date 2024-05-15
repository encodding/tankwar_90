package cn.play.freely.game.tank.entity;

import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.util.Objects;

public class Transform {

    private final Vec2f position;
    private final Dimension size;
    private int rotate;

    public Transform(Vec2f position, Dimension size) {
        this.position = position;
        this.size = size;
        this.rotate = 0;
    }

    public Vec2f getPosition() {
        return position;
    }

    public void setPosition(Vec2f position) {
        if (Objects.isNull(position)) return;
        setPosition(position.x, position.y);
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        if (Objects.isNull(size)) return;
        setSize(size.width, size.height);
    }

    public void setSize(int width, int height) {
        this.size.width = width;
        this.size.height = height;
    }



}
