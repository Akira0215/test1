package com.example.topnewgrid.adapter;

import java.util.List;

import com.example.topnewgrid.R;
import com.example.topnewgrid.bean.ChannelItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** �O�_��ܩ���ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** ���postion */
	private int holdPosition;
	/** �O�_���� */
	private boolean isChanged = false;
	/** �O�_�i�� */
	boolean isVisible = true;
	/** �i������C�� */
	public List<ChannelItem> channelList;
	/** TextView Ƶ��줺�e�� */
	private TextView item_text;
	/** �n�R���� position */
	public int remove_position = -1;

	public DragAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		ChannelItem channel = getItem(position);
		item_text.setText(channel.getName());
		if ((position == 0) || (position == 1)){
//			item_text.setTextColor(context.getResources().getColor(R.color.black));
			item_text.setEnabled(false);
		}
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if(remove_position == position){
			item_text.setText("");
		}
		return view;
	}

	/** �[�J��������C�� */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** �즲�ܧ�Ƨ� */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		ChannelItem dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}
	
	/** ���o���C�� */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}

	/** �]�w�n�R����position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** �R���ؼ���� */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}
	
	/** �]�w������list */
	public void setListDate(List<ChannelItem> list) {
		channelList = list;
	}
	
	/**  */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**  */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	/** ��ܩ�UITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}