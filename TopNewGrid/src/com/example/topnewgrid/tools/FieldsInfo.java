package com.example.topnewgrid.tools;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.view.DragGrid;
import com.example.topnewgrid.view.OtherGridView;

public class FieldsInfo {
	
	/** 	顯示欄位的Aadapter  */
	public static DragAdapter userAdapter;
	/** 	隱藏欄位的Aadapterん*/
	public static OtherAdapter otherAdapter;
	
	private static DragGrid userGridView;

	private static OtherGridView otherGridView;
	

	/** 	顯示欄位list 		*/
	public static ArrayList<ChannelItem> userFieldslList = new ArrayList<ChannelItem>();
	
	/** む	隱藏欄位list 		*/
	public static ArrayList<ChannelItem> otherFieldsList = new ArrayList<ChannelItem>();
	
	public static void setUserGridView(DragGrid usergridview)
	{	
		userGridView = usergridview;
	}
	
	public static void setOtherGridView(OtherGridView othergridview)
	{	
		otherGridView = othergridview;
	}
	
	public static void setUserAdapter(DragAdapter useradapter)
	{	
		userAdapter = useradapter;
	}
	
	public static DragAdapter getUserAdapter()
	{	
		return userAdapter;
	}
	
	public static void setOtherAdapter(OtherAdapter otheradapter)
	{	
		otherAdapter = otheradapter;
	}
	
	public static OtherAdapter getOtherAdapter()
	{	
		return otherAdapter;
	}
	
	public static void setUserFieldslList(ArrayList<ChannelItem> FieldslList)
	{	
		userFieldslList = FieldslList;
	}
	
	public static ArrayList<ChannelItem> getUserFieldslList()
	{	
		return userFieldslList;
	}
	
	
	public static void setOtherFieldslList(ArrayList<ChannelItem> OtherlList)
	{	
		otherFieldsList = OtherlList;
	}
	
	public static ArrayList<ChannelItem> getOtherFieldslList()
	{	
		return otherFieldsList;
	}
	
	public static void moveShowFieldtoHideField(int position)
	{	
		final ChannelItem channel = userAdapter.getItem(position);//取得點選欄位
		otherAdapter.setVisible(false);
		//加到隱藏欄位
		otherAdapter.addItem(channel);
		userAdapter.setRemove(position);
		
		otherAdapter.setVisible(true);
		otherAdapter.notifyDataSetChanged();
		userAdapter.remove();
	}
	
	public static void moveHideFieldtoShowField(int position)
	{	
		final ChannelItem channel = otherAdapter.getItem(position);//取得點選欄位
		userAdapter.setVisible(false);
		//加到隱藏欄位
		userAdapter.addItem(channel);
		otherAdapter.setRemove(position);
		
		userAdapter.setVisible(true);
		userAdapter.notifyDataSetChanged();
		otherAdapter.remove();
	}
	
	public static void moveHideFieldtoShowField(int x,int y,int position,View dragImageView)
	{	
		final ChannelItem channel = otherAdapter.getItem(position);//取得點選欄位
		userAdapter.setVisible(false);
		//加到隱藏欄位
		userAdapter.addItem(channel);
		otherAdapter.setRemove(position);
		
		userAdapter.setVisible(true);
		userAdapter.notifyDataSetChanged();
		otherAdapter.remove();
		
		otherGridView.stopDrag();
		userGridView.ViewtoFront(x,y,dragImageView);
//		dragImageView.bringToFront();
//		otherGridView.bringToFront();
	}
}
