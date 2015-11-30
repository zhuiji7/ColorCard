package com.zhuiji7.colorcard.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhuiji7.colorcard.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cww on 15-11-27.
 */
public class GameView extends View {
    int[] color = {Color.rgb(233, 89, 20), Color.BLUE, Color.CYAN, Color.DKGRAY, Color.MAGENTA, Color.GREEN, Color.YELLOW, Color.RED};

    private Context mycontext;
    private int level = 4;//默认等级
    private int padding = 3;
    private int canvasH;//一格画布的高度
    private int canvasW;//一格画布的宽度
    private ColorCard lastClickedCard;//翻开的色板
    private boolean gameStart;//标识游戏是否开始
    private Bitmap cardBitmap;
    private OnFinishListener listener;
    private ArrayList<ColorCard> cards = new ArrayList<ColorCard>();
    private ArrayList<ColorCard> animatorCards = new ArrayList<ColorCard>();//动画列表
    private ArrayList<AnimatorShadow> animatorShadowCards = new ArrayList<AnimatorShadow>();//消失动画列表
    private SoundPool soundPool;//声音池
    private int s_click;//点击色块声音
    private int s_dis;//色块消失声音
    private int s_error;//错误
    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mycontext = context;
        cardBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.green_card)).getBitmap();
        initCards();
        initSound();
    }

    public void startGame(){
        gameStart = true;
        showBack();
    }

    public void setOnFinishListener(OnFinishListener listener){
        this.listener = listener;
    }

    public interface OnFinishListener{
        public void onFinish();
    }

    private void initSound(){
        //创建一个SoundPool对象，该对象可以容纳2个音频流
        soundPool=new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        s_click = soundPool.load(mycontext,R.raw.s_click,1);
        s_dis = soundPool.load(mycontext,R.raw.s_dis,1);
        s_error = soundPool.load(mycontext,R.raw.s_error,1);
    }

    //默认正方形
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, widthSpecSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasW = w / level;
        canvasH = h / level;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAllCards(canvas, cards);
        drawAnimator(canvas);
        drawAnimatorShadow(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameStart){
            click((int)event.getX(),(int)event.getY());
        }
        return super.onTouchEvent(event);
    }

    //初始化色板
    private void initCards() {
        cards = new ArrayList<ColorCard>();
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                ColorCard colorcard = new ColorCard();
                PicPoint point = new PicPoint();
                point.setX(i);
                point.setY(j);
                colorcard.setPoint(point);
                cards.add(colorcard);
            }
        }
        Collections.shuffle(cards);
        int size = cards.size();
        for (int i = 0; i < size; i++) {
            cards.get(i).setColor(color[i / 2]);
        }
    }


    private void drawAllCards(Canvas canvas, ArrayList<ColorCard> cardlist) {
        int size = cardlist.size();
        for (int i = 0; i < size; i++) {
            drawCard(canvas, cardlist.get(i));
        }

    }

    private void drawCard(Canvas canvas, ColorCard colorCard) {
        Paint p = new Paint();
        p.setColor(colorCard.getColor());

        if (colorCard.isShow()) {
            if (colorCard.isFace()) {
                canvas.drawRect(colorCard.getPoint().getX() * canvasW + padding, colorCard.getPoint().getY() * canvasH + padding,
                        (colorCard.getPoint().getX() + 1) * canvasW - padding, (colorCard.getPoint().getY() + 1) * canvasH - padding, p);
            } else {
                Rect rc = new Rect(colorCard.getPoint().getX() * canvasW + padding, colorCard.getPoint().getY() * canvasH + padding,
                        (colorCard.getPoint().getX() + 1) * canvasW - padding, (colorCard.getPoint().getY() + 1) * canvasH - padding);
                canvas.drawBitmap(cardBitmap, null, rc, null);
            }
        }
    }

    //显示动画
    private void drawAnimator(Canvas canvas){
        int size = animatorCards.size();
        for(int i = 0;i < size;i ++){
            ColorCard colorCard = animatorCards.get(i);
            Paint p = new Paint();
            p.setColor(colorCard.getColor());
            canvas.drawRect(colorCard.getPoint().getX() * canvasW + padding, colorCard.getPoint().getY() * canvasH + padding,
                    (colorCard.getPoint().getX() + 1) * canvasW - padding, (colorCard.getPoint().getY() + 1) * canvasH - padding, p);
        }
    }

    private void drawAnimatorShadow(Canvas canvas){
        int size = animatorShadowCards.size();
        for(int i = 0;i < size;i ++){
            Paint p = new Paint();

            AnimatorShadow animatorShadow = animatorShadowCards.get(i);
            p.setColor(animatorShadow.getFirstShadowCard().getColor());
            canvas.drawRect(animatorShadow.getFirstShadowCard().getPoint().getX(), animatorShadow.getFirstShadowCard().getPoint().getY(),
                    animatorShadow.getFirstShadowCard().getPoint().getX() + canvasW, animatorShadow.getFirstShadowCard().getPoint().getY() + canvasH,
                    p);
            canvas.drawRect(animatorShadow.getSecendShadowCard().getPoint().getX(),animatorShadow.getSecendShadowCard().getPoint().getY(),
                    animatorShadow.getSecendShadowCard().getPoint().getX() + canvasW,animatorShadow.getSecendShadowCard().getPoint().getY() + canvasH,
                    p);
        }
    }

    //显示色板颜色
    private void showFace(){
        int size = cards.size();
        for(int i = 0;i < size;i ++){
            cards.get(i).setIsFace(true);
            cards.get(i).setIsShow(true);
        }
        invalidate();
    }

    //显示色板背面
    private void showBack(){
        int size = cards.size();
        for(int i = 0;i < size;i ++){
            cards.get(i).setIsFace(false);
            cards.get(i).setIsShow(true);
        }
        invalidate();
    }

    //点击色板，并做相应的操作
    private void click(int x,int y){
        PicPoint pp = new PicPoint();
        pp.setX(x/canvasW);
        pp.setY(y / canvasH);
        ColorCard clickedCard = findColorCard(pp);
        if(!clickedCard.isShow()){
            return;
        }

        if(lastClickedCard != null){
            if(isTheSameOne(lastClickedCard,clickedCard)){
                return;
            }

            if(isTheSameColor(lastClickedCard,clickedCard)){
                lastClickedCard.setIsShow(false);
                clickedCard.setIsShow(false);
                soundPool.play(s_dis, 1, 1, 0, 0, 1);
                showAnimatorShadow(lastClickedCard,clickedCard);
            }else{
                lastClickedCard.setIsFace(false);
                clickedCard.setIsFace(false);
                soundPool.play(s_error, 1, 1, 0, 0, 1);
                showAnimator(lastClickedCard);
                showAnimator(clickedCard);
            }

            lastClickedCard = null;
        }else{
            clickedCard.setIsFace(true);
            lastClickedCard = clickedCard;
            soundPool.play(s_click, 1, 1, 0, 0, 1);
        }
        if(isFinish()){
            if(listener != null){
                listener.onFinish();
            }
        }
        invalidate();

    }

    //通过色板脚标查找相应的色板
    private ColorCard findColorCard(PicPoint pp){
        ColorCard colorCard = new ColorCard();
        int size = cards.size();
        for(int i = 0;i < size;i ++){
            if(pp.getX() == cards.get(i).getPoint().getX() && pp.getY() == cards.get(i).getPoint().getY()){
                colorCard = cards.get(i);
                break;
            }
        }
        return colorCard;
    }

    //是否同一个色板
    private boolean isTheSameOne(ColorCard fir,ColorCard sec){
        if(fir.getPoint().getX() == sec.getPoint().getX() && fir.getPoint().getY() == sec.getPoint().getY()){
            return true;
        }
        return false;
    }

    //是否相同颜色
    private boolean isTheSameColor(ColorCard fir,ColorCard sec){
        if(fir.getColor() == sec.getColor()){
            return true;
        }
        return false;
    }

    //检查是否完成游戏
    private boolean isFinish(){
        boolean isFinish = true;
        int size = cards.size();
        for(int i = 0;i < size;i ++){
            if(cards.get(i).isShow()){
                isFinish = false;
                break;
            }
        }
        return isFinish;
    }

    //播放动画，显示色板0.2秒
    private void showAnimator(final ColorCard colorCard){
        animatorCards.add(colorCard);
        ValueAnimator animator = ValueAnimator.ofInt(1,1);
        animator.setDuration(200);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorCards.remove(colorCard);
                invalidate();
            }
        });
        invalidate();
    }

    //播放相同颜色色板合并动画
    private void showAnimatorShadow(ColorCard firstColorCard,ColorCard secondColorCard){
        final AnimatorShadow animatorShadow = new AnimatorShadow(firstColorCard,secondColorCard,canvasW,canvasH);
        animatorShadowCards.add(animatorShadow);
        ValueAnimator animator = ValueAnimator.ofInt(1,100);
        animator.setDuration(300);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorShadow.setProgress((int)animation.getAnimatedValue());
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorShadowCards.remove(animatorShadow);
                invalidate();
            }
        });
        invalidate();
    }
}
