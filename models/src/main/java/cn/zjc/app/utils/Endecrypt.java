package cn.zjc.app.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密与解密
 * 
 * @author 张吉超
 * @package cn.zjc.app.utils
 * @date 2020-04-17 13:18
 * @copyright © 349655468@qq.com
 */
public class Endecrypt {

    /**
     * MD5加密
     * 
     * @param plainText
     *            待加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String plainText) {
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(plainText.getBytes());
	    byte b[] = md.digest();
	    int i;
	    StringBuffer buf = new StringBuffer("");
	    for (int offset = 0; offset < b.length; offset++) {
		i = b[offset];
		if (i < 0)
		    i += 256;
		if (i < 16)
		    buf.append("0");
		buf.append(Integer.toHexString(i));
	    }
	    return buf.toString();
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 进行MD5加密
     * 
     * @param strSrc
     *            原始的字符串
     * @return 指定加密方式为md5后的byte[]
     */
    private static byte[] md5byte(String strSrc) {
	byte[] returnByte = null;
	try {
	    MessageDigest md5 = MessageDigest.getInstance("MD5");
	    returnByte = md5.digest(strSrc.getBytes("GBK"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return returnByte;
    }

    /**
     * 得到3-DES的密钥匙 根据接口规范，密钥匙为24个字节，md5加密出来的是16个字节，因此后面补8个字节的0
     * 
     * @param spKey
     *            原始的spKey
     * @return byte[] 指定加密方式为md5后的byte[]
     */
    private static byte[] getEnKey(String spKey) {
	byte[] desKey = null;
	try {
	    byte[] desKey1 = md5byte(spKey);
	    desKey = new byte[24];
	    int i = 0;
	    while (i < desKey1.length && i < 24) {
		desKey[i] = desKey1[i];
		i++;
	    }
	    if (i < 24) {
		desKey[i] = 0;
		i++;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return desKey;
    }

    /**
     * 3-DES加密
     * 
     * @param src
     *            要进行3-DES加密的byte[]
     * @param enKey
     *            3-DES加密密钥
     * @return byte[] 3-DES加密后的byte[]
     */
    public static byte[] encrypt(byte[] src, byte[] enKey) {
	byte[] encryptedData = null;
	try {
	    DESedeKeySpec dks = new DESedeKeySpec(enKey);
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	    SecretKey key = keyFactory.generateSecret(dks);
	    Cipher cipher = Cipher.getInstance("DESede");
	    cipher.init(Cipher.ENCRYPT_MODE, key);
	    encryptedData = cipher.doFinal(src);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return encryptedData;
    }

    /**
     * 对字符串进行Base64编码
     * 
     * @param src
     *            要进行编码的字符
     * @return 进行编码后的字符串
     */
    public static String getBase64Encode(byte[] src) {
	String requestValue = "";
	try {
	    BASE64Encoder base64en = new BASE64Encoder();
	    requestValue = base64en.encode(src);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return requestValue;
    }

    /** 去掉字符串的换行符号 */
    private static String filter(String str) {
	String output = null;
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < str.length(); i++) {
	    int asc = str.charAt(i);
	    if (asc != 10 && asc != 13)
		sb.append(str.subSequence(i, i + 1));
	}
	output = new String(sb);
	return output;
    }

    /**
     * 对字符串进行URLDecoder.encode(strEncoding)编码
     * 
     * @param src
     *            要进行编码的字符串
     * @return 进行编码后的字符串
     */
    public static String getURLEncode(String src) {
	String requestValue = "";
	try {
	    requestValue = URLEncoder.encode(src, "UTF-8");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return requestValue;
    }

    /**
     * 3-DES加密
     * 
     * @param src
     *            要进行3-DES加密的String
     * @param spKey
     *            分配的 spKey
     * @return 3-DES加密后的String
     */
    public static String encrypt3DES(String src, String spKey) {
	String requestValue = "";
	try {
	    // 得到3-DES的密钥匙
	    byte[] enKey = getEnKey(spKey);
	    // 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
	    byte[] src2 = src.getBytes("UTF-16LE");
	    // 进行3-DES加密后的内容的字节
	    byte[] encryptedData = encrypt(src2, enKey);
	    // 进行3-DES加密后的内容进行BASE64编码
	    String base64String = getBase64Encode(encryptedData);
	    // BASE64编码去除换行符后
	    String base64Encrypt = filter(base64String);
	    // 对BASE64编码中的HTML控制码进行转义的过程
	    requestValue = getURLEncode(base64Encrypt);
	    // System.out.println(requestValue);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return requestValue;
    }

    /**
     * 对字符串进行URLDecoder.decode(strEncoding)解码
     * 
     * @param src
     *            要进行解码的字符串
     * @return 进行解码后的字符串
     */
    public static String getURLDecoderdecode(String src) {
	String requestValue = "";
	try {
	    requestValue = URLDecoder.decode(src, "UTF-8");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return requestValue;
    }

    /**
     * 进行3-DES解密（密钥匙等同于加密的密钥匙）。
     * 
     * @param debase64
     *            要进行3-DES解密byte[]
     * @param spKey
     *            分配的SPKEY
     * @return String 3-DES解密后的String
     */
    public static String decrypt(byte[] debase64, String spKey) {
	String strDe = null;
	Cipher cipher = null;
	try {
	    cipher = Cipher.getInstance("DESede");
	    byte[] key = getEnKey(spKey);
	    DESedeKeySpec dks = new DESedeKeySpec(key);
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	    SecretKey sKey = keyFactory.generateSecret(dks);
	    cipher.init(Cipher.DECRYPT_MODE, sKey);
	    byte ciphertext[] = cipher.doFinal(debase64);
	    strDe = new String(ciphertext, "UTF-16LE");
	} catch (Exception ex) {
	    strDe = "";
	    ex.printStackTrace();
	}
	return strDe;
    }

    /**
     * 3-DES解密
     * 
     * @param src
     *            要进行3-DES解密的String
     * @param spKey
     *            分配的SPKEY
     * @return 3-DES加密后的String
     */
    public static String decrypt3DES(String src, String spKey) {
	String result = "";
	try {
	    // 得到3-DES的密钥匙
	    // URLDecoder.decodeTML控制码进行转义的过程
	    String URLValue = getURLDecoderdecode(src);
	    // 进行3-DES加密后的内容进行BASE64编码
	    BASE64Decoder base64Decode = new BASE64Decoder();
	    byte[] base64DValue = base64Decode.decodeBuffer(URLValue);
	    // 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
	    result = decrypt(base64DValue, spKey);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }
}
