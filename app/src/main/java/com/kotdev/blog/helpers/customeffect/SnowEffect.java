package com.kotdev.blog.helpers.customeffect;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kotdev.blog.R;

import java.util.Random;

/**
 * Steps for creating snow effect:
 *      1) Define SnowEffect object
 *      2) Use setElements method
 *      3) Use startShowing method
 */
public class SnowEffect extends RelativeLayout {

    //Element location on screen constants
    // 0 -> element always in background
    // 1 -> element always in foreground
    // 2 -> element in background and foreground
    public static final int ELEMENT_IN_BACKGROUND = 0;
    public static final int ELEMENT_IN_FOREGROUND = 1;
    public static final int ELEMENT_IN_BOTH = 2;

    //Element speed constants
    private static final float ELEMENT_SPEED_MIN = 5;
    private static final float ELEMENT_SPEED_MAX = 25;
    private static final float ELEMENT_SPEED_DIF = ELEMENT_SPEED_MAX - ELEMENT_SPEED_MIN;
    private static final float ELEMENT_SPEED_DEFAULT = 12.5f;

    //Element show speed constants (ms)
    private static final int ELEMENT_SHOW_SPEED_MIN = 50;
    private static final int ELEMENT_SHOW_SPEED_MAX = 500;
    private static final float ELEMENT_SHOW_SPEED_RATIO = 4.5f;

    //Changed after onSizeChanged() call
    private int elementShowSpeedDefault = 150;

    private static final int ELEMENT_MAX_SIZE = 50;
    private static final int ELEMENT_MIN_SIZE = 20;
    private static final int ELEMENT_DIF_SIZE = ELEMENT_MAX_SIZE - ELEMENT_MIN_SIZE;

    private static final int ELEMENT_DRAWABLE_RES_DEFAULT = R.drawable.ic_snow_element;
    private static final int ELEMENT_COLOR_RES_DEFAULT = R.color.colorWhiteElement;

    private Context context;
    private final Random random = new Random();
    private final Handler handler = new Handler();

    //Element location on screen
    private int elementShowIn;

    private boolean isFalling = false;
    private boolean readyToShow = false;

    private int layoutHeight;
    private int layoutWidth;

    private SnowElement[] elements;
    private int elementShowSpeed;
    private float elementSpeed;

    private int duration = 15000;

    public SnowEffect(Context context) {
        super(context);

        init(context, null);
    }

    public SnowEffect(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public SnowEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public SnowEffect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        layoutHeight = h;
        layoutWidth = w;
        setDuration();

        elementShowSpeedDefault = (int)(layoutHeight / ELEMENT_SHOW_SPEED_RATIO);

        readyToShow = true;
    }

    /**
     * Start showing snow effect with duration (ms)
     *
     * @param duration of snowing effect
     */
    public void startShowing(int duration){
        if(isFalling)
            return;

        isFalling = true;
        int maxCount = duration / elementShowSpeed;
        final int[] count = {0};

        handler.post(new Runnable() {
            @Override
            public void run() {
                addSnowElement();

                if(++count[0] == maxCount || !isFalling){
                    isFalling = false;

                    return;
                }

                handler.postDelayed(this, elementShowSpeed);
            }
        });
    }

    /**
     * Start showing snow effect
     */
    public void startShowing(){
        if(isFalling)
            return;

        isFalling = true;

        handler.post(new Runnable() {
            @Override
            public void run() {
                addSnowElement();

                if(isFalling)
                    handler.postDelayed(this, elementShowSpeed);
            }
        });
    }

    /**
     * Stop showing effect smoothly
     */
    public void stopShowing(){
        isFalling = false;
    }

    /**
     * Stop showing effect immediately
     */
    public void stopShowingNow(){
        isFalling = false;
        int count = getChildCount();

        for(int i = 0; i < count; i++){
            View view = getChildAt(i);

            if(view instanceof SnowEffectImage)
                removeView(view);
        }
    }

    /**
     * Set elements array for snow effect
     *
     * @param elements - every element contain drawable and color
     */
    public void setElements(SnowElement... elements){
        this.elements = elements;
    }

    /**
     * Set element location on screen
     *
     * @param location 0 -> element always in background
     *                 1 -> element always in foreground
     *                 2 -> element in background and foreground
     */
    public void setElementShowIn(int location){
        elementShowIn = location;
    }

    /**
     * Set element falling speed (from 0 to 1)
     *
     * @param speed for element
     */
    public void setElementSpeed(float speed){
        if(speed <= 0 || speed > 1) {
            elementSpeed = ELEMENT_SPEED_DEFAULT;

            return;
        }

        speed = 1 - speed;

        elementSpeed = ELEMENT_SPEED_MIN + speed * ELEMENT_SPEED_DIF;
    }

    public boolean isFalling() {
        return isFalling;
    }

    private void setDuration(){
        duration = (int)(layoutHeight * elementSpeed);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;

        getAttrs(context, attrs);
    }

    /**
     * Get attrs for SnowEffect
     *
     * @param context
     * @param attrs for SnowEffect
     */
    private void getAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SnowEffect
        );

        elementShowIn = a.getInt(R.styleable.SnowEffect_elementShowIn, ELEMENT_IN_FOREGROUND);

        float speed = a.getFloat(R.styleable.SnowEffect_elementSpeed, -1);
        setElementSpeed(speed);

        elementShowSpeed = a.getInt(R.styleable.SnowEffect_elementShowSpeedMillis, elementShowSpeedDefault);
        if(elementShowSpeed < ELEMENT_SHOW_SPEED_MIN || elementShowSpeed > ELEMENT_SHOW_SPEED_MAX)
            elementShowSpeed = elementShowSpeedDefault;

        a.recycle();
    }

    /**
     * Add element with animations (fall, bend and alpha)
     */
    private void addSnowElement(){
        if(!readyToShow)
            return;

        final SnowEffectImage image = new SnowEffectImage(context);
        addView(image);

        AnimationSet set = new AnimationSet(false);
        int startDuration = duration / 3;
        int durationFalling = random.nextInt(startDuration * 2) + startDuration;

        addFallAnimation(set, durationFalling);
        addBendAnimation(set, durationFalling);
        addAlphaAnimation(set, durationFalling);

        addAnimationListener(set, image);

        image.setAnimation(set);
        set.startNow();
    }

    private void addFallAnimation(AnimationSet set, int durationFalling){
        TranslateAnimation position = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                Animation.RELATIVE_TO_SELF,
                -100,
                layoutHeight
        );

        position.setDuration(durationFalling);
        set.addAnimation(position);
    }

    private void addBendAnimation(AnimationSet set, int durationFalling){
        int angle = random.nextInt(30) - 15;
        RotateAnimation rotate = new RotateAnimation(0, angle);

        rotate.setStartOffset(0);
        rotate.setDuration(durationFalling);
        set.addAnimation(rotate);
    }

    private void addAlphaAnimation(AnimationSet set, int durationFalling){
        AlphaAnimation alpha = new AlphaAnimation(1f, 0);

        alpha.setStartOffset(durationFalling / 3);
        alpha.setDuration(durationFalling / 3 * 2);
        set.addAnimation(alpha);
    }

    private void addAnimationListener(AnimationSet set, ImageView image){
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(GONE);

                removeViewOnUiThread(image);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Remove snow element after end of animation
     *
     * @param view to be removed
     */
    private void removeViewOnUiThread(View view) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                removeView(view);
            }
        }, 100);
    }

    private void addView(ImageView image){
        int imageSize = random.nextInt(ELEMENT_DIF_SIZE) + ELEMENT_MIN_SIZE;
        int leftMargin = layoutWidth - imageSize - random.nextInt(layoutWidth) - 1;
        LayoutParams params = new LayoutParams(imageSize, imageSize);
        SnowElement element = getRandomElement();

        image.setImageDrawable(element.getDrawable());
        setDrawableColor(image, element.getColor());
        image.setClickable(false);
        params.setMargins(leftMargin, 0, 0, 0);

        addView(image, getElementLocation(imageSize), params);
    }

    /**
     * Get element location index by imageSize
     *
     * @param imageSize size of element
     * @return location for element
     */
    private int getElementLocation(int imageSize){
        int index = 0;

        if(elementShowIn == ELEMENT_IN_BACKGROUND)
            index = 0;
        else if(elementShowIn == ELEMENT_IN_FOREGROUND)
            index = -1;
        else if(elementShowIn == ELEMENT_IN_BOTH)
            index = (imageSize > ELEMENT_DIF_SIZE * 0.6 + ELEMENT_MIN_SIZE)? -1 : 0;

        return index;
    }

    private SnowElement getRandomElement(){
        if(elements == null || elements.length == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return new SnowElement(context, ELEMENT_DRAWABLE_RES_DEFAULT, ELEMENT_COLOR_RES_DEFAULT);
            }
        }

        int index = random.nextInt(elements.length);

        return elements[index];
    }

    /**
     * Set color to ImageView (snow element)
     *
     * @param image - snow element
     * @param color - for snow element
     */
    private void setDrawableColor(ImageView image, int color){
        Drawable drawable = image.getDrawable();

        if(drawable == null)
            return;

        drawable.setColorFilter(color,
                PorterDuff.Mode.SRC_ATOP);
    }
}