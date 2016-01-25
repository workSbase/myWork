package com.lenovo.lenovorobot_new.activity;

public class Record {

	private String callId;
	private String recordValue;

	public Record(String callId, String recordValue) {

		this.callId = callId;
		this.recordValue = recordValue;
	}

	public String getCallId() {
		return callId;
	}

	public String getRecordValue() {
		return recordValue;
	}
}
