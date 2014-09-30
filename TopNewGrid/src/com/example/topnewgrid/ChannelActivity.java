package com.example.topnewgrid;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.tools.FieldsInfo;
import com.example.topnewgrid.view.DragGrid;
import com.example.topnewgrid.view.OtherGridView;
/**
 * ÆµÄæ¦ìºÞ²z
 * @Author RA
 * @Blog http://blog.csdn.net/vipzjyno1
 */
public class ChannelActivity extends Activity implements OnItemClickListener {
	/** 	Åã¥ÜÄæ¦ìªºGRIDVIEW  */
	private DragGrid userGridView;
	/** 	ÁôÂÃÄæ¦ìªºGRIDVIEW  */
	private OtherGridView otherGridView;
	/** 	Åã¥ÜÄæ¦ìªºAadapter  */
	private DragAdapter userAdapter;
	/** 	ÁôÂÃÄæ¦ìªºAadapterÆ÷*/
	private OtherAdapter otherAdapter;
	/** Æä	Åã¥ÜÄæ¦ìlist 		*/
	private ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 	ÁôÂÃÄæ¦ìlist 		*/
	private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** Äæ¦ì¬O§_¦b²¾°Ê,¥Ø«e¬O°Êµe®ÄªG§¹¦¨«á¦A¶i¦æ§ó·s¸ê®Æ,¦¹­­¨î¥Î¨Ó­­¨î¾Þ§@¤ÓÀWÁc¾É­P¼Æ¾Ú¿ù»~ */	
	boolean isMove = false;
	
	private LinearLayout LayoutOther;
	private LinearLayout LayoutMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe_activity);
		initView();
		initData();
		
		int[] location = new int[2];
	  	otherGridView.getLocationOnScreen(location);
	    int x = location[0];
        int y = location[1];
        Log.d("TEXT", "otherGridView Screenx--->" + x + "  " + "Screeny--->" + y);
	}
	
	/** ªì©l¤ÆÄæ¦ì¼Æ¾Ú */
	private void initData() {
	    userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
	    otherChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getOtherChannel());
	    userAdapter = new DragAdapter(this, userChannelList);
	    userGridView.setAdapter(userAdapter);
	    otherAdapter = new OtherAdapter(this, otherChannelList);
	    otherGridView.setAdapter(this.otherAdapter);
	    
	    otherGridView.setOnItemClickListener(this);
	    userGridView.setOnItemClickListener(this);
	    
	    FieldsInfo.setUserGridView(userGridView);
	    FieldsInfo.setOtherGridView(otherGridView);
	    FieldsInfo.setUserAdapter(userAdapter);
	    FieldsInfo.setUserFieldslList(userChannelList);
	    FieldsInfo.setOtherAdapter(otherAdapter);
	    FieldsInfo.setOtherFieldslList(otherChannelList);
	    
	   
		
	}
	
	
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);	
		
		LayoutMain = (LinearLayout)  findViewById(R.id.layout_main);
		
		
		LayoutOther = (LinearLayout)  findViewById(R.id.layout_other);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** GRIDVIEW  ItemClick */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
		//Èç¹ûµã»÷µÄÊ±ºò£¬Ö®Ç°¶¯»­»¹Ã»½áÊø£¬ÄÇÃ´¾ÍÈÃµã»÷ÊÂ¼þÎÞÐ§
		if(isMove){
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			//position 0,1 µLªk¾Þ§@
			if (position != 0 && position != 1) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					
					TextView newTextView = (TextView) view.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//¨ú±oÂI¿ïÄæ¦ì
					otherAdapter.setVisible(false);
					//¥[¨ìÁôÂÃÄæ¦ì
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								//¨ú±o²×ÂI®y¼Ð
								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
								userAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null){
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				//±NÂI¿ïªºÁôÂÃÄæ¦ì¥[¤J¨ìÅã¥ÜÄæ¦ì
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							//¨ú±o²×ÂI®y¼Ð
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation , endLocation, channel,otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * ÂI¿ïÄæ¦ì²¾°Ê°Êµe
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		//¨ú±o°_©lÄæ¦ìVIEW®y¼Ð
		moveView.getLocationInWindow(initLocation);
		//¨ú±o²¾°Ê±oÄæ¦ìVIEW,¨Ã©ñ¨ì¹ïÀ³ªºVIEW
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		//«Ø¥ß²¾°Ê°Êµe
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);//°ÊµeÅã¥Ü®É¶¡
		//°Êµe³]©w
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);//°Êµe§¹¦¨«á,VIEW¹ï¶H¤£«O¯d¦b²×¤î¦ì¸m
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// §PÂ_¬OÂI¿ïDragGrid©Î¬OOtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}else{
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	/**
	 * ¨ú±o²¾°ÊªºVIEW©ñ¨ì¹ïÀ³ViewGroup¤¤
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}
	
	/**
	 * «Ø¥ß²¾°ÊITEM¹ïÀ³ªºViewGroupÈÝÆ÷
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}
	
	/**
	 * ¨ú±oÂI¿ïªºItemªº¹ïÀ³View
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	/** save Äæ¦ì  */
	private void saveChannel() {
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).deleteAllChannel();
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
	}
	
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}
}
