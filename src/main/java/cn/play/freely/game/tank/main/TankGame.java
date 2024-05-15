package cn.play.freely.game.tank.main;

import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.scenes.LevelEditorScene;
import cn.play.freely.game.tank.scenes.Scene;
import cn.play.freely.game.tank.scenes.WelcomeScene;
import cn.play.freely.game.tank.scenes.LevelScene;
import cn.play.freely.game.tank.util.LevelUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * 坦克游戏
 */
public class TankGame implements  Runnable, MouseMotionListener, MouseListener, KeyListener {

    private final GameWindow window;
    private final GamePanel panel;
    private Scene currentScene;
    private Thread renderThread;

    private static TankGame tankGame;

    public static TankGame get() {
        if (Objects.isNull(tankGame)) {
            tankGame = new TankGame();
        }
        return tankGame;
    }

    public static Scene getCurrentScene() {
        return get().currentScene;
    }

    public TankGame() {
        changeScene(0);
        panel = new GamePanel(this, Settings.WIDTH, Settings.HEIGHT);
        window = new GameWindow(panel);
        panel.requestFocus();
        this.panel.addMouseListener(this);
        this.panel.addMouseMotionListener(this);
        this.panel.addKeyListener(this);
        startRenderLoop();
        try {
            LevelUtils.loadLevels();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene(int index) {
        switch (index) {
            case 0:
                currentScene = WelcomeScene.get();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            case 2:
                currentScene = LevelEditorScene.get();
                break;
        }
        currentScene.setGame(this);
    }

    private void startRenderLoop() {
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / Settings.FPS;
        double timePerUpdate = 1_000_000_000.0 / Settings.UPS;
        long lastCheck = System.currentTimeMillis();;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;
        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                panel.requestFocus();
                update((float) (timePerUpdate / 1E9f));
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                panel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000 ) {
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void render(Graphics g) {
        currentScene.render(g);
    }

    public void update(float dt) {
        currentScene.update(dt);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentScene.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentScene.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentScene.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentScene.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentScene.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentScene.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentScene.mouseMoved(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        currentScene.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentScene.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentScene.keyReleased(e);
    }

    public Scene getScene() {
        return currentScene;
    }
}
