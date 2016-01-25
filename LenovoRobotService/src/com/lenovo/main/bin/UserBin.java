package com.lenovo.main.bin;

import java.io.Serializable;
import java.util.ArrayList;

public class UserBin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String description;
	public String id;
	public String name;
	public String pictureUrl;
	public String youYueId;

	public ArrayList<FriendList> friendList;

	public class FriendList implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String anotherName;
		public String description;
		public String id;
		public String isRobot;
		public String name;
		public String onLine;
		public String pictureUrl;
	}
}
