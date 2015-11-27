package com.zhuiji7.colorcard.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cww on 15-11-27.
 */
public class GameView extends View {
    int[] color = {Color.rgb(233, 89, 20),Color.BLUE,Color.CYAN,Color.DKGRAY,Color.MAGENTA,Color.GREEN,Color.YELLOW,Color.RED};

    private int level = 4;//默认等级
    private int padding = 3;
    private int viewH;//view的高度
    private int viewW;//view的宽度
    private int canvasH;//一格画布的高度
    private int canvasW;//一格画布的宽度
    private ArrayList<ColorCard> cards = new ArrayList<ColorCard>();

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCards();
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
        viewW = w;
        viewH = h;
        canvasW = w / level;
        canvasH = h / level;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAllCards(canvas, cards);
    }

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
        for(int i = 0;i < size;i ++){
            cards.get(i).setColor(color[i/2]);
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
            canvas.drawRect(colorCard.getPoint().getX() * canvasW + padding, colorCard.getPoint().getY() * canvasH + padding,
                    (colorCard.getPoint().getX() + 1) * canvasW - padding, (colorCard.getPoint().getY() + 1) * canvasH - padding, p);

        }

    }
}
