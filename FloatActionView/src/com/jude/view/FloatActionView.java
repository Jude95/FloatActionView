package com.jude.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Jude on 2014/8/13.
 * 
 * FAB核心类。只能在右下竖直向上伸缩
 * 
 */
public class FloatActionView extends ViewGroup implements View.OnClickListener{
    private static String TAG = "FloatView";
    private boolean isExpanded = false;
    private FloatViewAdapter mAdapter;

    private View mMainView;
    private View[] mExpandView;

    private boolean isClicklock;

    //伸展动画持续时间
    private static final int ANIMATOR_TIME_EXPAND = 220;
    //伸展动画插值器
    private static final Interpolator ANIMATOR_INTERPOLATOR_EXPAND = new OvershootInterpolator();
    //收缩动画持续时间
    private static final int ANIMATOR_TIME_SHRINK = 120;
    //收缩动画插值器
    private static final Interpolator ANIMATOR_INTERPOLATOR_SHRINK = new AccelerateInterpolator();

    private OnFloatingViewClickListerner mListener;

    public FloatActionView(Context context) {
        this(context,null);
    }

    public FloatActionView(Context ctx, AttributeSet attr){
        this(ctx, attr, 0);
    }

    public FloatActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int bottom = b-t;
        int right = r-l;
        //
        if(mExpandView!=null){
            for (int i = 0 ; i<mExpandView.length;i++){
                Rect rect = CountRect(mExpandView[i],right,bottom);
                mExpandView[i].layout(rect.left,rect.top,rect.right,rect.bottom);
            }
        }

        if(mMainView!=null){
            Rect rect = CountRect(mMainView,right,b-t);
            mMainView.layout(rect.left,rect.top,rect.right,rect.bottom);
            mMainView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 展开fab
     */
    public void expand(){
        if(mMainView!=null&&!isExpanded&&!isClicklock){
            isClicklock = true;
            isExpanded = true;
            startExpandAnimation();
            if(mListener!=null){
            	mListener.OnExpand(mMainView);
            }
            
        }
    }

    /**
     * 收缩fab
     */
    public void shrink(){
        if(mMainView!=null&&isExpanded&&!isClicklock){
            isClicklock = true;
            isExpanded = false;
            startShrinkAnimation();
            if(mListener!=null){
            	mListener.OnShrink(mMainView);
            }
        }
    }

    private Rect CountRect(View view,int right,int bottom){
        Rect rect = new Rect();
        int r = right;
        int b = bottom ;
        int l = r - view.getMeasuredWidth();
        int t = b - view.getMeasuredHeight();
        rect.set(l,t,r,b);
        return rect;
    }

    public void setAdapter(FloatViewAdapter adapter){
        this.mAdapter = adapter;
        initViews();
        requestLayout();
    }

    public void startExpandAnimation(){
        int deltabottom = -mMainView.getMeasuredHeight();
        
        RotateAnimation amin = new RotateAnimation(0, 135, mMainView.getWidth()/2, mMainView.getWidth()/2);
		amin.setDuration(150);
		amin.setFillAfter(true);
		mMainView.startAnimation(amin);
		
        for(int i = 0 ;i<mExpandView.length;i++ ){
            setAnimation(mExpandView[i],deltabottom,false);
            deltabottom-=mExpandView[i].getMeasuredHeight();
        }
    }
    
    public void startShrinkAnimation(){
        int deltabottom = mMainView.getMeasuredHeight();
        
		RotateAnimation amin = new RotateAnimation(135, 0, mMainView.getWidth()/2, mMainView.getWidth()/2);
		amin.setDuration(150);
		amin.setFillAfter(true);
		mMainView.startAnimation(amin);
        
        for(int i = 0 ;i<mExpandView.length;i++ ){
            setAnimation(mExpandView[i],deltabottom,true);
            deltabottom+=mExpandView[i].getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        shrink();
        return true;
    }

    private void setAnimation(final View view , final int deltaY , boolean isgoback){
        ValueAnimator anit = ValueAnimator.ofInt((int)view.getY(),(int)view.getY()+deltaY);
        if(!isgoback){
            anit.setInterpolator(ANIMATOR_INTERPOLATOR_EXPAND);
            anit.setDuration(ANIMATOR_TIME_EXPAND);
        }else{
            anit.setInterpolator(ANIMATOR_INTERPOLATOR_SHRINK);
            anit.setDuration(ANIMATOR_TIME_SHRINK);
        }
        anit.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setY((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anit.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(isExpanded?VISIBLE:INVISIBLE);
                isClicklock = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anit.start();
    }

    private void initViews(){
        removeAllViews();
        isExpanded = false;
        if(mAdapter!=null){
            mExpandView = new View[mAdapter.getCount()];
            View clickableView;
            for(int i=0;i<mAdapter.getCount();i++){
                mExpandView[i] = mAdapter.getExpandView(i,this);
                mExpandView[i].setVisibility(View.INVISIBLE);
                clickableView = mAdapter.getItemsClickableView(mExpandView[i],i);
                clickableView.setTag(i+1);
                clickableView.setOnClickListener(this);
                addView(mExpandView[i]);
            }
            mMainView = mAdapter.getMainView(this);
            mMainView.setOnClickListener(this);
            mMainView.setTag(0);
            addView(mMainView);
        }
    }

    public void setOnFloatingClickListener(OnFloatingViewClickListerner mListener){
         this.mListener = mListener;
    }
    
    @Override
    public void onClick(View view) {
        if(getChildCount()<=1){
            return ;
        }
        if(mListener!=null){
            mListener.OnItemClick(view,((Integer)view.getTag())-1);
            shrink();
        }
        if(isExpanded&&((Integer)view.getTag() == 0)){//main鎸夐挳鍗曞嚮
            shrink();
        }else{
            expand();
        }

    }
    
    public View getMainView(){
    	return mMainView;
    }
    
    public View getItemView(int position){
    	if(position<mExpandView.length){
    		return mExpandView[position];
    	}
    	return null;
    }
    
    public interface OnFloatingViewClickListerner{
        public void OnExpand(View mainView);
        public void OnShrink(View mainView);
        public void OnItemClick(View itemView, int position);
    }
    public interface FloatViewAdapter {
        public int getCount();
        public View getMainView(ViewGroup parent);
        public View getExpandView(int position, ViewGroup parent);
        public View getItemsClickableView(View ItemView, int position);
    }
}
