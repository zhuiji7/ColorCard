package com.zhuiji7.colorcard.game;

/**
 * Created by cww on 15-11-27.
 * 颜色板
 */
public class ColorCard implements Cloneable{
    private int color = 0xfffe44fe;
    private boolean isFace = true;
    private boolean isShow = true;
    private PicPoint point;



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFace() {
        return isFace;
    }

    public void setIsFace(boolean isFace) {
        this.isFace = isFace;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public PicPoint getPoint() {
        return point;
    }

    public void setPoint(PicPoint point) {
        this.point = point;
    }

    @Override
    public ColorCard clone(){
        ColorCard c = null;
        try {
            c = (ColorCard)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return c;
    }
}
