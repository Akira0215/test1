package com.example.topnewgrid.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.SQLException;
import android.util.Log;

import com.example.topnewgrid.dao.ChannelDao;
import com.example.topnewgrid.db.SQLHelper;

public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 預設欄位列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 預設其他欄位列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** 判斷sqllite是否已有記錄 */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		
		defaultUserChannels.add(new ChannelItem(1, "成交", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "漲跌", 2, 1));
		defaultUserChannels.add(new ChannelItem(3, "幅度", 3, 1));
		defaultUserChannels.add(new ChannelItem(4, "買進", 4, 1));
		defaultUserChannels.add(new ChannelItem(5, "賣出", 5, 1));
		defaultUserChannels.add(new ChannelItem(6, "單量", 6, 1));
		defaultUserChannels.add(new ChannelItem(7, "總量", 7, 1));
		defaultUserChannels.add(new ChannelItem(8, "買量", 8, 1));
		defaultUserChannels.add(new ChannelItem(9, "賣量", 9, 1));
		defaultUserChannels.add(new ChannelItem(10, "最高", 10, 1));
		defaultUserChannels.add(new ChannelItem(11, "最低", 11, 1));
		defaultUserChannels.add(new ChannelItem(12, "開盤", 12, 1));
		defaultUserChannels.add(new ChannelItem(13, "振幅", 13, 1));
		defaultUserChannels.add(new ChannelItem(14, "昨收", 14, 1));
		defaultUserChannels.add(new ChannelItem(15, "時間", 15, 1));
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化欄位管理
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * ь初始化所有欄位
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 取得所有欄位
	 * @return 資料庫欄位設定 ? 資料庫選擇欄位設定 : 預設用戶顯示欄位 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			
			int count = maplist.size();
			
			Log.i("TEXT", "getUserChannel count : " + count);
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}
	
	/**
	 * 取得所有欄位
	 * @return 資料庫欄位設定 ? 資料庫選擇隱藏欄位設定 : 預設用戶隱藏欄位設定 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}
	
	/**
	 * save 顯示欄位到資料庫
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * save 隱藏欄位到資料庫
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * 初始化所有欄位設定
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		
		Log.i("TEXT", "initDefaultChannel defaultUserChannels.size() : " + defaultUserChannels.size());
		Log.i("TEXT", "initDefaultChannel defaultOtherChannels.size() : " + defaultOtherChannels.size());
		
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
