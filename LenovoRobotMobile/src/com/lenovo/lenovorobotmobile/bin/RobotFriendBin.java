package com.lenovo.lenovorobotmobile.bin;

import java.util.ArrayList;

public class RobotFriendBin {
	public String description;
	public String name;
	public String pictureUrl;
	public int youYueId;
	public ArrayList<FriendRobotList> friendRobotList;

	public class FriendRobotList {
		public String anotherName;
		public String phoneNumber;
		public String robotDescription;
		public String robotFriendName;
		public String robotPictureUrl;
		public int robotStata;
		public int robotYouYueId;
	}
}
