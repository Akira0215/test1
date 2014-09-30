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
	 * �w�]���C��
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * �w�]��L���C��
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** �P�_sqllite�O�_�w���O�� */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		
		defaultUserChannels.add(new ChannelItem(1, "����", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "���^", 2, 1));
		defaultUserChannels.add(new ChannelItem(3, "�T��", 3, 1));
		defaultUserChannels.add(new ChannelItem(4, "�R�i", 4, 1));
		defaultUserChannels.add(new ChannelItem(5, "��X", 5, 1));
		defaultUserChannels.add(new ChannelItem(6, "��q", 6, 1));
		defaultUserChannels.add(new ChannelItem(7, "�`�q", 7, 1));
		defaultUserChannels.add(new ChannelItem(8, "�R�q", 8, 1));
		defaultUserChannels.add(new ChannelItem(9, "��q", 9, 1));
		defaultUserChannels.add(new ChannelItem(10, "�̰�", 10, 1));
		defaultUserChannels.add(new ChannelItem(11, "�̧C", 11, 1));
		defaultUserChannels.add(new ChannelItem(12, "�}�L", 12, 1));
		defaultUserChannels.add(new ChannelItem(13, "���T", 13, 1));
		defaultUserChannels.add(new ChannelItem(14, "�Q��", 14, 1));
		defaultUserChannels.add(new ChannelItem(15, "�ɶ�", 15, 1));
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * ��l�����޲z
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * ���l�ƩҦ����
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * ���o�Ҧ����
	 * @return ��Ʈw���]�w ? ��Ʈw������]�w : �w�]�Τ������� ;
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
	 * ���o�Ҧ����
	 * @return ��Ʈw���]�w ? ��Ʈw����������]�w : �w�]�Τ��������]�w ;
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
	 * save ��������Ʈw
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
	 * save ���������Ʈw
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
	 * ��l�ƩҦ����]�w
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
