package cn.zjc.app.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * 融合通信--短信
 */
public class SMSRongHeTongXin {

	private static final String USER = "hongzepaimai";
	private static final String PASSWORD = "hongzepaimai123";
	private static final String SIGN = "山拍网";

	/**
	 * 
	 * @param phone    手机号码
	 * @param content  短信内容
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static boolean sendSms(String phone, String content) {
		try {
			String str = post("http://service.winic.org:8009/sys_port/gateway/index.asp",
					"id=" + URLEncoder.encode(USER, "gb2312") + "&pwd=" + PASSWORD + "&to=" + phone + "&content=" + URLEncoder.encode(content + "【" + SIGN + "】", "gb2312") + "&time=");
			if (null != str && "000".equals(str.substring(0, 3))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String post(String path, String params) throws Exception {
		BufferedReader in = null;
		PrintWriter out = null;
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(path);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			out = new PrintWriter(httpConn.getOutputStream());
			out.println(params);
			out.flush();
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = "";
				in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				return content.toString();
			} else {
				throw new Exception("请求出现了问题!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
			httpConn.disconnect();
		}
		return null;
	}

	public static void main(String[] args) {
		sendSms("13295386832", "您的验证码是：309520");
	}
}
