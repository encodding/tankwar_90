package cn.play.freely.game.tank.components.common;

import cn.play.freely.game.tank.components.Component;
import cn.play.freely.game.tank.config.Settings;
import cn.play.freely.game.tank.entity.GameEntity;
import cn.play.freely.game.tank.util.AssetPoolUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimationComponent extends Component {
    private int animationFrameIndex = 0; // 动画帧数索引
    private int countOfAnimationsRuns = 0; // 动画运行计数器
    private int numberOfAnimationsLoop ; // 动画循环次数
    private final int numberOfAnimationsFrame; // 动画帧数
    private final int numberOfAnimationsRunsPerFrame; // 单帧动画运行次数
    private boolean completed = false;

    private BufferedImage animationImage;

    private final  int width;
    private final  int height;

    private  int offsetX;
    private  int offsetY;

    /**
     *
     * @param numberOfAnimationsFrame 动画帧数
     */
    public AnimationComponent(String animationImagePath, int numberOfAnimationsFrame) {
        this(animationImagePath, 1, numberOfAnimationsFrame,  50);
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    /**
     *
     * @param numberOfAnimationsLoop 动画循环次数
     * @param numberOfAnimationsFrame 动画帧数
     * @param numberOfAnimationsRunsPerFrame 单帧动画运行次数
     */
    public AnimationComponent(String animationImagePath ,int numberOfAnimationsLoop, int numberOfAnimationsFrame, int numberOfAnimationsRunsPerFrame) {
        this.numberOfAnimationsLoop = numberOfAnimationsLoop;
        this.numberOfAnimationsFrame = numberOfAnimationsFrame;
        this.numberOfAnimationsRunsPerFrame = numberOfAnimationsRunsPerFrame;
        this.animationImage = AssetPoolUtils.loadTexture(animationImagePath);
        this.width = this.animationImage.getWidth() / numberOfAnimationsFrame;
        this.height = this.animationImage.getHeight();
    }

    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        entity.getTransform().setSize(width, height);
    }

    @Override
    public void render(Graphics g) {
        if (completed) return;
        int idx = animationFrameIndex % numberOfAnimationsFrame;
        g.drawImage(animationImage.getSubimage(idx * width,0, width, height), (int)entity.getTransform().getPosition().x + offsetX,
                (int)entity.getTransform().getPosition().y + offsetY , (int)(width * Settings.SCALE), (int)(height * Settings.SCALE), null);
    }


    @Override
    public void update(float dt) {
        if (completed) return;
        if (countOfAnimationsRuns > numberOfAnimationsRunsPerFrame) { // 动画中的一帧展示10次切换下一帧
            animationFrameIndex++;
            countOfAnimationsRuns = 0;
        }
        if (animationFrameIndex >= numberOfAnimationsLoop * numberOfAnimationsFrame) { // 动画循环numberOfAnimationsLoop次后完成
            completed = true;
            countOfAnimationsRuns = 0;
            animationFrameIndex = 0;
        }
        countOfAnimationsRuns ++;
    }

    public void  increaseLoop() {
        this.numberOfAnimationsLoop++;
    }

    public boolean isCompleted() {
        return completed;
    }
}
