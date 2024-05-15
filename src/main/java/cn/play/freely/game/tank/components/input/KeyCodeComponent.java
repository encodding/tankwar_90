package cn.play.freely.game.tank.components.input;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.entity.GameEntity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyCodeComponent extends Component implements KeyListener {

    private final Map<Integer, Boolean> clickFlags;
    private final Map<Integer, KeyCodeComponent.KeyCodeHandler> clickHandlers;


    public KeyCodeComponent() {
        clickFlags = new HashMap<>();
        clickHandlers = new HashMap<>();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Map.Entry<Integer, Boolean> entry : clickFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getKeyCode()) {
                this.clickFlags.replace(key, true);
                this.clickHandlers.get(key).pressed(e, entity);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Map.Entry<Integer, Boolean> entry : clickFlags.entrySet()) {
            Integer key = entry.getKey();
            if (key == e.getKeyCode() && entry.getValue()) {
                this.clickHandlers.get(key).handle(e, entity);
                this.clickFlags.replace(key, false);
            }
            this.clickHandlers.get(key).released(e, entity);
        }
    }

    public KeyCodeComponent onClick(int keyCode, KeyCodeHandler handler) {
        clickFlags.put(keyCode, false);
        clickHandlers.put(keyCode, handler);
        return this;
    }


    public interface KeyCodeHandler {
        void handle(KeyEvent e, GameEntity entity);
        default void pressed(KeyEvent e, GameEntity entity) {}
        default void released(KeyEvent e, GameEntity entity) {}
    }

}
