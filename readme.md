简要说明
-
一个使用adapter和clickListener模式的Float Action Button

使用简单，自己继承基础Adapter实现各样的Fab。

自带一个仿Evernote的fab。

 ![image](https://github.com/zhuchenxi1995/FloatActionView/raw/master/img.png)

使用方法
-
首先添加FloatActionView。你给他多大空间。他就在多大空间里伸缩。并靠右下对齐。

	<com.jude.view.FloatActionView
	    android:id="@+id/fab"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    >
	    
	</com.jude.view.FloatActionView>

给它设置适配器

	fab = (FloatActionView) findViewById(R.id.fab);
    fab.setAdapter(new MyFloatViewAdapter(this));

适配器代码

	class MyFloatViewAdapter extends HintFloatViewAdapter{
	
		public MyFloatViewAdapter(Context ctx) {
			super(ctx);
		}
	
		@Override
		public int getCount() {
			return 5;
		}
	
		@Override
		public int getMainDrawableResource() {
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

这样就好了。可以自己继承FloatViewAdapter以实现更多样式