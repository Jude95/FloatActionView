package com.jude.view.fab;

import com.jude.floatactionbutton.R;
import com.jude.view.FloatActionView.FloatViewAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Mr on 2014/8/14.
 * 带提示框的fab。仿Evernote。
 * 
 */
public abstract class HintFloatViewAdapter implements FloatViewAdapter {

    private Context ctx;
    private LayoutInflater mInflater;

    public HintFloatViewAdapter(Context ctx){
        this.ctx = ctx;
        mInflater = LayoutInflater.from(this.ctx);
    }

    @Override
    public abstract int getCount();

    //设置响应点击事件的view
    @Override
    public View getItemsClickableView(View ItemView, int position) {
        return ItemView;
    }

    @Override
    public View getMainView(ViewGroup parent) {
        View main = mInflater.inflate(R.layout.floatingmain,parent,false);
        ((ImageView)main.findViewById(R.id.img)).setImageResource(getMainDrawableResource());
        return main;
    }
    public abstract int getMainDrawableResource();


    @Override
    public View getExpandView(int position, ViewGroup parent) {
        View item = mInflater.inflate(R.layout.floatingitem,parent,false);
        ((ImageView)item.findViewById(R.id.img)).setImageResource(getItemDrawableResource(position));
        ((TextView)item.findViewById(R.id.hint)).setText(getItemHint(position));
        return item;
    }
    public abstract int getItemDrawableResource(int position);
    public abstract String getItemHint(int position);
}
