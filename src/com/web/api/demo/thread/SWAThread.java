package com.web.api.demo.thread;

import com.web.api.demo.SmsDemoWebClient;

public class SWAThread extends Thread {
	private String seg = "13800";
	private String content = "";
	private int count = 0;
	private int operId;
	private int start = 100000;

	public SWAThread(String seg, String content, int count, int operId) {
		super();
		this.seg = seg;
		this.content = content;
		this.count = count;
		this.operId = operId;
	}

	public void run() {
		for (int i = 0; i < count; i++) {
			String mobile = seg + String.valueOf(start + i);
			SmsDemoWebClient.sendMessage(mobile, content, operId, "", i,
					(short) 0, (short) 0, (short) 0, "");
		}
		System.out.println(seg + "thread 退出！");
	}
}
