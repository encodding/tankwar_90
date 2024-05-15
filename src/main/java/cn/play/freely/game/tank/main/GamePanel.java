package cn.play.freely.game.tank.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Dimension size;
    private TankGame game;

    public GamePanel(TankGame game, int width, int height) {
        this.game = game;
        init(width, height);
    }

    private void init(int width,int height) {
        initSize(width, height);
    }

    private void initSize(int width, int height) {
        size = new Dimension(width, height);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        game.render(g);
    }
}
