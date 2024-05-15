package cn.play.freely.game.tank.components.input;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.entity.GameEntity;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MouseMotionComponent  extends Component implements MouseListener, MouseMotionListener {

    private final Map<Integer, Boolean> clickFlags;
    private final Map<Integer, MouseHandler> clickHandlers;

    private Boolean hoverFlag;
    private MouseHandler  hoverHandler;
    private MouseHandler  leaveHandler;

    private final Map<Integer, Boolean> draggedFlags;
    private final Map<Integer, Point> draggedOffsets;
    private final Map<Integer, DraggedHandler>  draggedHandlers;

    public MouseMotionComponent() {
        this.clickHandlers = new HashMap<>();
        this.clickFlags = new HashMap<>();
        this.draggedFlags = new HashMap<>();
        this.draggedHandlers = new HashMap<>();
        this.draggedOffsets = new HashMap<>();
        this.hoverFlag = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Map.Entry<Integer, Boolean> entry : clickFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getButton() && entity.getHitbox().contains(e.getX(), e.getY())) {
                this.clickFlags.replace(key, true);
            }
        }

        for (Map.Entry<Integer, Boolean> entry : draggedFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getButton() && entity.getHitbox().contains(e.getX(), e.getY())) {
                this.draggedFlags.replace(key, true);
                this.draggedOffsets.put(key, new Point(entity.getHitbox().x - e.getX(), entity.getHitbox().y - e.getY()));
                this.draggedHandlers.get(key).start(e, entity);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Map.Entry<Integer, Boolean> entry : clickFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getButton()  && entity.getHitbox().contains(e.getX(), e.getY()) && entry.getValue()) {
                this.clickHandlers.get(key).handle(e, entity);
            }
            this.clickFlags.replace(key, false);
        }
        for (Map.Entry<Integer, Boolean> entry : draggedFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getButton() && entry.getValue()) {
                this.draggedFlags.replace(key, false);
                this.draggedHandlers.get(key).end(e, entity);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (Map.Entry<Integer, Boolean> entry : draggedFlags.entrySet()) {
            Integer key = entry.getKey();
            if (entry.getValue()) {
                Point point = draggedOffsets.get(key);
                this.draggedHandlers.get(key).dragging(e, entity, point.x, point.y);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (entity.getHitbox().contains(e.getX(), e.getY()) && !hoverFlag) {
            Optional.ofNullable(hoverHandler).ifPresent(handler -> {
                handler.handle(e, entity);
            });
            hoverFlag = true;
        } else if (!entity.getHitbox().contains(e.getX(), e.getY()) && hoverFlag) {
            Optional.ofNullable(leaveHandler).ifPresent(handler -> {
                handler.handle(e, entity);
            });
            hoverFlag = false;
        }
    }

    public MouseMotionComponent onClick(int mouseButton, MouseHandler handler) {
        this.clickFlags.put(mouseButton, false);
        this.clickHandlers.put(mouseButton, handler);
        return this;
    }

    public MouseMotionComponent onHover(MouseHandler handler) {
        this.hoverHandler = handler;
        return this;
    }

    public MouseMotionComponent onLeave(MouseHandler handler) {
        this.leaveHandler = handler;
        return this;
    }

    public MouseMotionComponent onDragged(int mouseButton, DraggedHandler handler) {
        this.draggedFlags.put(mouseButton, false);
        this.draggedHandlers.put(mouseButton, handler);
        return this;
    }

    public interface DraggedHandler {

        default void start(MouseEvent e, GameEntity entity) {}

        default void end(MouseEvent e, GameEntity entity) {};

        void dragging(MouseEvent e, GameEntity entity, int offsetX, int offsetY);
    }


    public interface MouseHandler {

        void handle(MouseEvent e, GameEntity entity) ;

    }


}
