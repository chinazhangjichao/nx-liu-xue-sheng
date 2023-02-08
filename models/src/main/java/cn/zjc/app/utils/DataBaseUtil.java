package cn.zjc.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 数据库操作
 * 
 * @author 张吉超
 * @date 2020-4-23
 */
public class DataBaseUtil {
    private static String DBHOST = "";
    private static String DBNAME = "";
    private static String DBUSER = "";
    private static String DBPWD = "";

    static {
	Properties prop = new Properties();
	InputStream is = DataBaseUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
	try {
	    prop.load(is);
	    DBHOST = prop.getProperty("jdbc.dbhost");
	    DBNAME = prop.getProperty("jdbc.dbname");
	    DBUSER = prop.getProperty("jdbc.username");
	    DBPWD = prop.getProperty("jdbc.password");
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    /**
     * 数据库备份
     * 
     * @param fileName
     * @return
     */
    public static boolean backup(String fileName) {
	String cmd = "mysqldump -h" + DBHOST + " -u" + DBUSER + " -p" + DBPWD + " " + DBNAME + " >\"" + fileName + "\"";
	try {
	    Process process = Runtime.getRuntime().exec("cmd /c" + cmd);
	    if (process.waitFor() == 0) {
		return true;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * 数据库还原
     * 
     * @param fileName
     * @return
     */
    public static boolean restore(String fileName) {
	String cmd = "mysql -h" + DBHOST + " -u " + DBUSER + " -p" + DBPWD + " " + DBNAME + " <  \"" + fileName + "\"";
	try {
	    Process exec = Runtime.getRuntime().exec("cmd /c " + cmd);
	    if (exec.waitFor() == 0) {
		return true;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }
}
