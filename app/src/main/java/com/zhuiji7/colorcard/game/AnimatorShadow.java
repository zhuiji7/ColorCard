package com.zhuiji7.colorcard.game;

/**
 * Created by cww on 15-11-30.
 */
public class AnimatorShadow {
    private int whight;
    private int hight;
    private PicPoint targetPoint;
    private int progress = 1 ;
    private ColorCard firstCard;
    private ColorCard secendCard;

    private ColorCard firstShadowCard;
    private ColorCard secendShadowCard;

    public AnimatorShadow(ColorCard fir,ColorCard sec,int whight,int hight){
        this.whight = whight;
        this.hight = hight;
        targetPoint = new PicPoint();
        firstCard = fir;
        secendCard = sec;
        targetPoint.setX((firstCard.getPoint().getX() * whight + secendCard.getPoint().getX() * whight) / 2);
        targetPoint.setY((firstCard.getPoint().getY() * hight + secendCard.getPoint().getY() * hight) / 2);
        firstShadowCard = firstCard.clone();
        firstShadowCard.setIsFace(true);
        firstShadowCard.setIsShow(true);
        secendShadowCard = secendCard.clone();
        secendShadowCard.setIsFace(true);
        secendShadowCard.setIsShow(true);
        setProgress(1);
    }

    public ColorCard getFirstShadowCard() {
        return firstShadowCard;
    }

    public ColorCard getSecendShadowCard() {
        return secendShadowCard;
    }

    /*
    *设置进度并根据进度计算出对应的动画位置
    */
    public void setProgress(int progress) {
        if(progress < 1 || progress > 100){
            return;
        }
        this.progress = progress;
        PicPoint firstP = new PicPoint();
        firstP.setX(firstCard.getPoint().getX() * whight + (targetPoint.getX() - firstCard.getPoint().getX() * whight) * progress / 100);
        firstP.setY(firstCard.getPoint().getY() * hight + (targetPoint.getY() - firstCard.getPoint().getY() * hight) * progress / 100);
        firstShadowCard.setPoint(firstP);

        PicPoint secendP = new PicPoint();
        secendP.setX(secendCard.getPoint().getX() * whight + (targetPoint.getX() - secendCard.getPoint().getX() * whight) * progress / 100);
        secendP.setY(secendCard.getPoint().getY() * hight + (targetPoint.getY() - secendCard.getPoint().getY() * hight) * progress / 100);
        secendShadowCard.setPoint(secendP);
    }
}
