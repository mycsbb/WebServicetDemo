package com.web.api.demo.server;

import com.web.api.demo.thread.SWAThread;

public class SWAServer {

	public void start() {
		SWAThread thread135 = new SWAThread("13500", "135号码段发送测试", 100, 10030);
		SWAThread thread136 = new SWAThread("13600", "136号码段发送测试", 100, 10030);
		SWAThread thread137 = new SWAThread("13700", "136号码段发送测试", 100, 10030);
		SWAThread thread138 = new SWAThread("13800", "136号码段发送测试", 100, 10030);
		SWAThread thread139 = new SWAThread("13900", "136号码段发送测试", 100, 10030);
		thread135.start();
		thread136.start();
		thread137.start();
		thread138.start();
		thread139.start();
	}
}
