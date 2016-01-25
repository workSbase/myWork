package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

public class ResultBin {
	public float sn;
	public boolean ls;
	public float bg;
	public float ed;
	public ArrayList<KeyWords> ws;

	public float sc;

	public class KeyWords {
		public float bg;
		public ArrayList<Word> cw;
		public String slot;
	}

	public class Word {
		public float id;
		public String w;
		public float gm;
		public float sc;
	}
}
