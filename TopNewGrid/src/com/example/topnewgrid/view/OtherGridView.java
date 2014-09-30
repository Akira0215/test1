package com.example.topnewgrid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.tools.DataTools;
import com.example.topnewgrid.tools.FieldsInfo;

public class OtherGridView extends GridView {
	
	/** 點擊時X位址 */
	public int downX;
	/** 點擊時Y位址 */
	public int downY;
	/** 點擊時對應整個介面的X位址 */
	public int windowX;
	/** 點擊時對應整個介面的Y位址 */
	public int windowY;
	/** そVIew 上面的X */
	private int win_view_x;
	/** そVIew 上面的Y */
	private int win_view_y;
	/** 拖動X的距離  */
	int dragOffsetX;
	/** 拖動Y的距離  */
	int dragOffsetY;
	/** 長按時對應Position */
	public int dragPosition;
	/** UP後對應的ITEM的Position */
	private int dropPosition;
	/** 開始拖動ITEM的Position*/
	private int startPosition;
	/** item高 */
	private int itemHeight;
	/** item寬 */
	private int itemWidth;
	/** 拖動時對應ITEM的VIEW */
	private View dragImageView = null;
	/** 長按時對應ITEM的VIEW */
	private ViewGroup dragItemView = null;
	/** WindowManagerん */
	private WindowManager windowManager = null;
	/** */
	private WindowManager.LayoutParams windowParams = null;
	/** item總量 */
	private int itemTotalCount;
	/** 每行ITEM數量*/
	private int nColumns = 4;
	/** 行數 */
	private int nRows;
	/** 剩餘部分 */
	private int Remainder;
	/** 是否在移動 */
	private boolean isMoving = false;
	/** */
	private int holdPosition;
	/** 拖動時放大倍數 */
	private double dragScale = 1.2D;
	/** 震動器ん  */
	private Vibrator mVibrator;
	/** 每個ITEM之間水平間距 */
	private int mHorizontalSpacing = 15;
	/** 每個ITEM之間垂直間距 */
	private int mVerticalSpacing = 15;
	/** 移動時最後動畫的ID */
	private String LastAnimationID;
	
	/** 是否在拖曳中 */
	private boolean isdraging = false;

	public OtherGridView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}
	
	public void init(Context context){
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		//dip 轉 px
		mHorizontalSpacing = DataTools.dip2px(context, mHorizontalSpacing);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			windowX = (int) ev.getX();
			windowY = (int) ev.getY();
			setOnItemClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean bool = true;
		if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION) {
			// 移動時對應X,Y位址
			bool = super.onTouchEvent(ev);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) ev.getX();
				windowX = (int) ev.getX();
				downY = (int) ev.getY();
				windowY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				onDrag(x, y ,(int) ev.getRawX() , (int) ev.getRawY());
				if (!isMoving){
					OnMove(x, y);
				}
				if (pointToPosition(x, y) != AdapterView.INVALID_POSITION){
					break;
				}
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();
				onDrop(x, y);
				requestDisallowInterceptTouchEvent(false);
				break;

			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	/** 拖動ITEM */
	private void onDrag(int x, int y , int rawx , int rawy) {
		
		Log.i("TEXT", "onDrop x : " + x + " onDrop y : " + y + " dragPosition : " + dragPosition);
		
		if (dragImageView != null) {
			
			int tempPostion = pointToPosition(x, y);
			
			//判斷對應是否上移
			if(tempPostion == -1 && y < -100 && !isdraging)
			{
				isdraging = true;
				FieldsInfo.moveHideFieldtoShowField(rawx - win_view_x,rawy - win_view_y,dragPosition,dragImageView);
			}
			
			windowParams.alpha = 0.6f;
//			windowParams.x = rawx - itemWidth / 2;
//			windowParams.y = rawy - itemHeight / 2;
			windowParams.x = rawx - win_view_x;
			windowParams.y = rawy - win_view_y;
			if (dragImageView != null)
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}

	/** 放開拖動 */
	private void onDrop(int x, int y) {
		// 根據拖動所移動的x,y座標取得拖動位置下方的ITEM所對應的POSTION
		
		Log.i("TEXT", "onDrop x : " + x + " onDrop y : " + y + " dragPosition : " + dragPosition);
		
		int tempPostion = pointToPosition(x, y);
		
		//判斷對應是否上移
		if(tempPostion == -1 && y < -100 && !isdraging)
		{
			FieldsInfo.moveHideFieldtoShowField(dragPosition);
		}
		else
		{
			dropPosition = tempPostion;
			OtherAdapter mDragAdapter = (OtherAdapter) getAdapter();
			//顯示剛拖動ITEM
			mDragAdapter.setShowDropItem(true);
			//更新顯示ITEM
			mDragAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	/**
	 * 長按
	 * @param ev
	 */
	public void setOnItemClickListener(final MotionEvent ev) {
		setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int x = (int) ev.getX();// 長按X座標
				int y = (int) ev.getY();// 長按Y座標
				
				Log.i("TEXT", "onItemLongClick x : " + x + " y : " + y);

				startPosition = position;//第一次點擊postion
				dragPosition = position;
//				if (startPosition <= 1) {
//					return false;
//				}
				ViewGroup dragViewGroup = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
				TextView dragTextView = (TextView)dragViewGroup.findViewById(R.id.text_item);
				dragTextView.setSelected(true);
				dragTextView.setEnabled(false);
				itemHeight = dragViewGroup.getHeight();
				itemWidth = dragViewGroup.getWidth();
				
				Log.i("TEXT", "onItemLongClick itemHeight : " + itemHeight + " itemWidth : " + itemWidth);
				
				itemTotalCount = OtherGridView.this.getCount();
				int row = itemTotalCount / nColumns;// 算出行數
				Remainder = (itemTotalCount % nColumns);// 計算最後一行多餘數量
				if (Remainder != 0) {
					nRows = row + 1;
				} else {
					nRows = row;
				}
				// ��
				if (dragPosition != AdapterView.INVALID_POSITION) {
					//
					win_view_x = windowX - dragViewGroup.getLeft();//
					win_view_y = windowY - dragViewGroup.getTop();//
					dragOffsetX = (int) (ev.getRawX() - x);//
					dragOffsetY = (int) (ev.getRawY() - y);//
					dragItemView = dragViewGroup;
					dragViewGroup.destroyDrawingCache();
					dragViewGroup.setDrawingCacheEnabled(true);
					Bitmap dragBitmap = Bitmap.createBitmap(dragViewGroup.getDrawingCache());
					mVibrator.vibrate(50);//設定震動時間
					startDrag(dragBitmap, (int)ev.getRawX(),  (int)ev.getRawY());
					hideDropItem();
					dragViewGroup.setVisibility(View.INVISIBLE);
					isMoving = false;
					requestDisallowInterceptTouchEvent(true);
					return true;
				}
				return false;
			}
		});
	}
	
	public void startDrag(Bitmap dragBitmap, int x, int y) {
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		//Gravity.TOP|Gravity.LEFT;涴跺斛剕樓  
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;
//		windowParams.x = x - (int)((itemWidth / 2) * dragScale);
//		windowParams.y = y - (int) ((itemHeight / 2) * dragScale);
		
		windowParams.x = x - win_view_x;
		windowParams.y = y  - win_view_y; 
//		this.windowParams.x = (x - this.win_view_x + this.viewX);
//		this.windowParams.y = (y - this.win_view_y + this.viewY);
		//設定拖曳ITEM寬高  
		windowParams.width = (int) (dragScale * dragBitmap.getWidth());
		windowParams.height = (int) (dragScale * dragBitmap.getHeight());
		this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE                           
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE                           
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON                           
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		this.windowParams.format = PixelFormat.TRANSLUCENT;
		this.windowParams.windowAnimations = 0;
		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(dragBitmap);
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
		windowManager.addView(iv, windowParams);
		dragImageView = iv;
	}

	/** 停止拖動並初始化 */
	public void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
	
	/** 隱藏拖動ITEM*/
	private void hideDropItem() {
		((OtherAdapter) getAdapter()).setShowDropItem(false);
	}
	
	/** 動畫 */
	public Animation getMoveAnimation(float toXValue, float toYValue) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0F,
				Animation.RELATIVE_TO_SELF,toXValue, 
				Animation.RELATIVE_TO_SELF, 0.0F,
				Animation.RELATIVE_TO_SELF, toYValue);// 當前位置移動到指定位置
		mTranslateAnimation.setFillAfter(true);// 設定動畫完成後,view item停在最終位置
		mTranslateAnimation.setDuration(300L);
		return mTranslateAnimation;
	}
	
	/** 移動時處理 */
	public void OnMove(int x, int y) {
		// 拖動的VIEW下方的position
		int dPosition = pointToPosition(x, y);
//		 判斷下方POSITON是否是最開始2個不能拖動的
//		if (dPosition > 1) {
	        if ((dPosition == -1) || (dPosition == dragPosition)){
	        	return;
	        }
		    dropPosition = dPosition;
		    if (dragPosition != startPosition){
		    	dragPosition = startPosition;
		    }
			int movecount;
			
		    if ((dragPosition == startPosition) || (dragPosition != dropPosition)){
		    	//需要移動的item數量
		    	movecount = dropPosition - dragPosition;
		    }else{
		    	
		    	movecount = 0;
		    }
		    if(movecount == 0){
		    	return;
		    }
		    
		    int movecount_abs = Math.abs(movecount);
		    
			if (dPosition != dragPosition) {
				//dragGroup設為不可見
				ViewGroup dragGroup = (ViewGroup) getChildAt(dragPosition);
				dragGroup.setVisibility(View.INVISIBLE);
				float to_x = 1;//當前下方positon
				float to_y;//當前下方右邊positon
				//x_vlaue移動距離百分比
				float x_vlaue = ((float) mHorizontalSpacing / (float) itemWidth) + 1.0f;
				//y_vlaue移動距離百分比
				float y_vlaue = ((float) mVerticalSpacing / (float) itemHeight) + 1.0f;
				Log.d("x_vlaue", "x_vlaue = " + x_vlaue);
				for (int i = 0; i < movecount_abs; i++) {
					 to_x = x_vlaue;
					 to_y = y_vlaue;
					//向左
					if (movecount > 0) {
						// 判斷是否同一行
						holdPosition = dragPosition + i + 1;
						if (dragPosition / nColumns == holdPosition / nColumns) {
							to_x = - x_vlaue;
							to_y = 0;
						} else if (holdPosition % 4 == 0) {
							to_x = 3 * x_vlaue;
							to_y = - y_vlaue;
						} else {
							to_x = - x_vlaue;
							to_y = 0;
						}
					}else{
						//向右,下移到上,右移到左
						holdPosition = dragPosition - i - 1;
						if (dragPosition / nColumns == holdPosition / nColumns) {
							to_x = x_vlaue;
							to_y = 0;
						} else if((holdPosition + 1) % 4 == 0){
							to_x = -3 * x_vlaue;
							to_y = y_vlaue;
						}else{
							to_x = x_vlaue;
							to_y = 0;
						}
					}
					ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
					Animation moveAnimation = getMoveAnimation(to_x, to_y);
					moveViewGroup.startAnimation(moveAnimation);
					//�蝳p果是最後一個移動,設置它的最後動畫ID為LastAnimationID
					if (holdPosition == dropPosition) {
						LastAnimationID = moveAnimation.toString();
					}
					moveAnimation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							isMoving = true;
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							// 動畫完成
							if (animation.toString().equalsIgnoreCase(LastAnimationID)) {
								OtherAdapter mDragAdapter = (OtherAdapter) getAdapter();
								mDragAdapter.exchange(startPosition,dropPosition);
								startPosition = dropPosition;
								dragPosition = dropPosition;
								isMoving = false;
							}
						}
					});
				}
			}
//		}
	}
}
