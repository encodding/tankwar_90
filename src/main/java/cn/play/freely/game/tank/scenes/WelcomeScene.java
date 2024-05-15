package cn.play.freely.game.tank.scenes;

import cn.play.freely.game.tank.components.input.KeyCodeComponent;
import cn.play.freely.game.tank.components.common.SpriteComponent;
import cn.play.freely.game.tank.components.common.TextButtonComponent;
import cn.play.freely.game.tank.config.Constant;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.entity.Transform;
import cn.play.freely.game.tank.util.AssetPoolUtils;
import cn.play.freely.game.tank.util.LevelUtils;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class WelcomeScene extends Scene {

    private BufferedImage logoImage;
    private BufferedImage loadingImage;
    private final GameEntity startGameButton;
    private final GameEntity editLevelButton;
    private final GameEntity selectIcon;
    private final GameEntity[] buttons;

    private int selectIndex = 0;

    private WelcomeScene() {
        logoImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_UI_LOGO);
        loadingImage = AssetPoolUtils.loadTexture(Constant.TEXTURE_UI_TANKING_LOADING);

//      开始游戏按钮
        startGameButton = new GameEntity("StartGameButton", new Transform(
                new Vec2f(0,(int)(190 * Settings.SCALE) +  (int)(logoImage.getHeight() * Settings.SCALE)
                                +  (int)(loadingImage.getHeight() * Settings.SCALE)),
                new Dimension(Settings.WIDTH, 60)
        ));
        startGameButton.getProps().setProperty("idx", "1");
        startGameButton.add(new TextButtonComponent("开始游戏")
                .setFont(AssetPoolUtils.FONT_1).setColor(Color.WHITE));
        addToScene(startGameButton);

//      编辑关卡按钮
        editLevelButton = new GameEntity("EditLevelButton", new Transform(
                new Vec2f(0,(250 * Settings.SCALE) + (logoImage.getHeight() * Settings.SCALE)
                        + (loadingImage.getHeight() * Settings.SCALE)),
                new Dimension(Settings.WIDTH, 60)
        ));
        editLevelButton.getProps().setProperty("idx", "2");
        editLevelButton.add(new TextButtonComponent("编辑关卡").setFont(AssetPoolUtils.FONT_1).setColor(Color.WHITE));
        addToScene(editLevelButton);

//      欢迎界面
        GameEntity welcomeScene = new GameEntity("welcomeScene", new Transform(
                new Vec2f(0,0),
                new Dimension(Settings.WIDTH, Settings.HEIGHT)
        ));
        buttons = new GameEntity[] {startGameButton, editLevelButton};

        welcomeScene.add(new KeyCodeComponent()
                .onClick(KeyEvent.VK_W, this::onClickW)
                .onClick(KeyEvent.VK_S, this::onClickS)
                .onClick(KeyEvent.VK_ENTER, this::onClickEnter)
        );
        addToScene(welcomeScene);


        selectIcon = new GameEntity("SelectIcon", new Transform(
                new Vec2f(),
                new Dimension()
        ));
        selectIcon.getTransform().setRotate(180);
        selectIcon.add(new SpriteComponent(Constant.TEXTURE_UI_ICON));




        buttons[selectIndex].get(TextButtonComponent.class).setColor(Color.RED);
        addToScene(selectIcon);
    }

    @Override
    protected void doUpdateEntityAfter(float dt) {
        for (GameEntity button : buttons) {
            button.get(TextButtonComponent.class).setColor(Color.WHITE);
        }
        buttons[selectIndex].get(TextButtonComponent.class).setColor(Color.RED);

        TextButtonComponent component = buttons[selectIndex].get(TextButtonComponent.class);
        selectIcon.getTransform().setPosition(component.getX() - 80, component.getY() - 32);
    }

    private void onClickS(KeyEvent keyEvent, GameEntity entity) {
        int tempIndex = selectIndex - 1;
        if (tempIndex < 0)  {
            selectIndex = buttons.length - 1;
        } else {
            selectIndex = tempIndex;
        }
    }

    private void onClickEnter(KeyEvent keyEvent, GameEntity entity) {
        Optional.ofNullable(buttons[selectIndex].getProps().getProperty("idx"))
                        .ifPresent(idx -> {
                            game.changeScene(Integer.parseInt(idx));
                            if (game.getScene() instanceof LevelScene) {
                                ((LevelScene)game.getScene()).setGrid(LevelUtils.get(0));
                                ((LevelScene)game.getScene()).setLevelIndex(0);
                            }
                        });
    }

    private void onClickW(KeyEvent keyEvent, GameEntity entity) {
        selectIndex = (selectIndex + 1) % buttons.length;

    }

    private static final Scene welcomeScene = new WelcomeScene();

    public static Scene get() {
        return welcomeScene;
    }


    @Override
    protected void doRender(Graphics g) {
        g.setColor(new Color(0x000));
        g.fillRect(0,0, Settings.WIDTH, Settings.HEIGHT);

        g.drawImage(logoImage,  (Settings.WIDTH - (int)(logoImage.getWidth() * Settings.SCALE)) / 2 ,(int)(100 * Settings.SCALE),
                (int)(logoImage.getWidth() * Settings.SCALE),
                (int)(logoImage.getHeight() * Settings.SCALE), null);

        g.drawImage(loadingImage,  (Settings.WIDTH - (int)(loadingImage.getWidth() * Settings.SCALE)) / 2 ,
                (int)(150 * Settings.SCALE) +  (int)(logoImage.getHeight() * Settings.SCALE),
                (int)(loadingImage.getWidth() * Settings.SCALE),
                (int)(loadingImage.getHeight() * Settings.SCALE), null);
    }



}
