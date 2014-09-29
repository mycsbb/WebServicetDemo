package com.web.api.demo;

import java.util.Iterator;
import java.util.List;
import com.sms.webservice.client.SmsReturnObj;
import com.sms.webservice.client.SmsWebClient;
import com.sms.webservice.domain.SmsMORecord;
import com.sms.webservice.domain.SmsReportRecord;

public class SmsDemoWebClient {

	public static void main(String[] argv) {
		/*SWAServer server = new SWAServer(); 
		server.start();*/
		initClient();
		String mobile = "18611966723";
		String content = "根据中国证监会举报中心委托，特向您发送此条短信。您的注册验证码为:123456";
		int operId = 10100;
		sendMessage(mobile, content, operId, "", 1,(short) 0, (short) 0, (short) 0, "");
		/*sendMorephoneMessage();
		receiveRPT();
		sendGroupMessage(); 
		receiveMO(); 
		regInst(); 
		unregInst();*/
	}

	public static void initClient() {
		if (!SmsWebClient.enable()) {
			int ret = 0;
			try {
				ret = SmsWebClient.init("http://www3.chinaclear.cn/smsWebservice/SmsWebInterface.ws","ZJHjcj", "CSRC_jcj_201");
				System.out.println(ret);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("短信平台接口初始化过程中异常！");
			} 
			if (ret == -1 || !SmsWebClient.enable()) {
				System.out.println("短信平台接口初始化失败！");
				return;
			}
		}
	}

	public static void sendMessage(java.lang.String mobilephone,
			java.lang.String content, int operId, java.lang.String tosend_time,
			int sms_id, short backlist_filter, short fbdword_filter,
			short priority, java.lang.String valid_time) {
		// 单个手机号码发送
		try {
			SmsReturnObj retObj = SmsWebClient.webSendMessage(mobilephone,
					content, operId, tosend_time, sms_id, backlist_filter,
					fbdword_filter, priority, valid_time);
			if (retObj.getReturnCode() != 1) {
				System.out.println("短信发送失败，原因为：" + retObj.getReturnMsg());
			} else {
				System.out.println("短信发送成功！返回结果为：" + retObj.getReturnMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("短信发送过程发生异常!");
		}
	}

	public static void sendMorephoneMessage() {
		// 多个手机号码发送
		try {
			SmsReturnObj retObj = SmsWebClient.webSendMessage(
					"13520782089,13241822626", "多个手机号码,短信发送测试", 10030, "", 1,
					(short) 0, (short) 0, (short) 0, "");
			if (retObj.getReturnCode() != 1) {
				System.out.println("短信发送失败，原因为：" + retObj.getReturnMsg());
			} else {
				System.out.println("短信发送成功！返回结果为：" + retObj.getReturnMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("短信发送过程发生异常!");
		}
	}

	public static void sendGroupMessage() {
		// 群组短信发送
		try {
			SmsReturnObj retObj = SmsWebClient.webSendGroupMessage("10000",
					"群组短信测试", 10009, "", 2, (short) 0, (short) 0, "");
			if (retObj.getReturnCode() != 1) {
				System.out.println("群组短信发送失败，原因为：" + retObj.getReturnMsg());
			} else {
				System.out.println("群组短信发送成功！返回结果为：" + retObj.getReturnMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("群组短信发送过程发生异常!");
		}
	}

	public static void receiveRPT() {
		// 回执接收
		try {
			List list = SmsWebClient.webReceiveReport(0, 10009);
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				System.out.println("收到上行状态报告：");
				while (it.hasNext()) {
					SmsReportRecord record = (SmsReportRecord) it.next();
					System.out.println("手机号码：" + record.mobilephone + "\t回执代码"
									+ record.report + "\t状态报告描述："
									+ record.reportstatus);
				}
			} else {
				System.out.println("没有状态报告！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("接收上行过程发生异常!");
		}
	}

	public static void receiveMO() {
		// 上行接收
		try {
			List list = SmsWebClient.webReceiveMO(10009, 0, 10);
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				System.out.println("收到上行短信：");
				while (it.hasNext()) {
					SmsMORecord record = (SmsMORecord) it.next();
					System.out.println("手机号码：" + record.mobilephone + "\t内容："+ record.content);
				}
			} else {
				System.out.println("没有上行短信！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("接收上行过程发生异常!");
		}
	}

	public static void regInst() {
		// 指令注册
		try {
			SmsReturnObj retObj = SmsWebClient.webRegisterInstruction(10009,"webUser inst", "query", "#", "", "");
			if (retObj.getReturnCode() != 1) {
				System.out.println("指令注册失败，原因为：" + retObj.getReturnMsg());
			} else {
				System.out.println("指令注册成功！返回结果为：" + retObj.getReturnMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("指令注册过程发生异常!");
		}
	}

	public static void unregInst() {
		// 指令注销
		try {
			SmsReturnObj retObj = SmsWebClient.webUnregisterInstruction(10020,46, "query");
			if (retObj.getReturnCode() != 1) {
				System.out.println("指令注销失败，原因为：" + retObj.getReturnMsg());
			} else {
				System.out.println("指令注销成功！返回结果为：" + retObj.getReturnMsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("指令注销过程发生异常!");
		}
	}
}
