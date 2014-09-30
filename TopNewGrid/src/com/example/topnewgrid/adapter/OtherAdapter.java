package com.example.topnewgrid.adapter;

import java.util.List;

import com.example.topnewgrid.R;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.view.OtherGridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<ChannelItem> channelList;
	private TextView item_text;
	/** 是否顯示底部ITEM */
	private boolean isItemShow = false;
	/** 是否改變 */
	private boolean isChanged = false;
	/** 控制的postion */
	private int holdPosition;
	/**  */
	boolean isVisible = true;
	/** 要刪除的position */
	public int remove_position = -1;

	public OtherAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		ChannelItem channel = getItem(position);
		item_text.setText(channel.getName());
		if (!isVisible && (position == -1 + channelList.size())){
			item_text.setText("");
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
	
	/** */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}
	
	/**  */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/**  */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
	}
	
	/** 拖曳變更排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		ChannelItem dragItem = getItem(dragPostion);
		
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

	/**  */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}
	/**  */
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
	/** 顯示放下ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}