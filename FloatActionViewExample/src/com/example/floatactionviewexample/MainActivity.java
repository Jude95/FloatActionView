package com.example.floatactionviewexample;

import com.jude.view.FloatActionView;
import com.jude.view.fab.HintFloatViewAdapter;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

	private FloatActionView fab;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatActionView) findViewById(R.id.fab);
        fab.setAdapter(new MyFloatViewAdapter(this));
    }
    
    
}

class MyFloatViewAdapter extends HintFloatViewAdapter{

	public MyFloatViewAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getMainDrawableResource() {
		// TODO Auto-generated method stub
		return R.drawable.fab_plus_00bcd4;
	}

	@Override
	public int getItemDrawableResource(int position) {	
		return R.drawable.fab_commonmap_00bcd4+position;
	}

	@Override
	public String getItemHint(int position) {
		return "提示文本";
	}
	
	
}
