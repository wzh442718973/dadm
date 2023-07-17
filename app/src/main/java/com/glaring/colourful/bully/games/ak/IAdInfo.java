package com.glaring.colourful.bully.games.ak;

public interface IAdInfo {
    String getAdKey();

    boolean isEnable();

    boolean checkAction(IAdRely rely);

    boolean isNeedUpdate();

    /**
     * @param showExit true: 在广告显示直至退出后才进入下一次广告
     *                 false: 在下一个广告时间间隔有效
     */
    void adShowBegin(boolean showExit);

    void adShowEnd(boolean success);
}
