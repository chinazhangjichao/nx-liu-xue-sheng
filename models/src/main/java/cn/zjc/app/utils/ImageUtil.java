package cn.zjc.app.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

/**
 * 图片压缩辅助类
 * 
 * @author 张吉超
 *
 * @date 2021年3月15日
 */

public class ImageUtil {
	private ImageUtil() {
	}

	/**
	 * 根据指定大小和指定精度压缩图片
	 * 
	 * @param srcPath      源图片地址
	 * @param desPath      目标图片地址
	 * @param desFileSize  指定图片大小，单位kb
	 * @param accuracy     精度，递归压缩的比率，建议小于0.9
	 * @param desMaxWidth  目标最大宽度
	 * @param desMaxHeight 目标最大高度
	 * @return 目标文件路径
	 */
	public static boolean commpressPicForScale(String srcPath, String desPath, long desFileSize, double accuracy, int desMaxWidth, int desMaxHeight) {
		if (!StringUtils.hasLength(srcPath) || !StringUtils.hasLength(desPath)) {
			return false;
		}
		if (!new File(srcPath).exists()) {
			System.out.println("文件不存在");
			return false;
		}
		try {
			File srcFile = new File(srcPath);
			// 获取图片信息
			BufferedImage bim = ImageIO.read(srcFile);
			int srcWidth = bim.getWidth();
			int srcHeight = bim.getHeight();
			// 先转换成jpg
			Thumbnails.Builder builder = Thumbnails.of(srcFile).outputFormat("jpg");
			// 指定大小（宽或高超出会才会被缩放）
			if (srcWidth > desMaxWidth || srcHeight > desMaxHeight) {
				builder.size(desMaxWidth, desMaxHeight);
			} else {
				// 宽高均小，指定原大小
				builder.size(srcWidth, srcHeight);
			}
			// 写入到内存
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 字节输出流（写入到内存）
			builder.toOutputStream(baos);
			// 递归压缩，直到目标文件大小小于desFileSize
			byte[] bytes = commpressPicCycle(baos.toByteArray(), desFileSize, accuracy);
			// 输出到文件
			File desFile = new File(desPath);
			FileOutputStream fos = new FileOutputStream(desFile);
			fos.write(bytes);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] commpressPicCycle(byte[] bytes, long desFileSize, double accuracy) throws IOException {
		// File srcFileJPG = new File(desPath);
		long srcFileSizeJPG = bytes.length;
		// 2、判断大小，如果小于500kb，不压缩；如果大于等于500kb，压缩
		if (srcFileSizeJPG <= desFileSize * 1024) {
			return bytes;
		}
		// 计算宽高
		BufferedImage bim = ImageIO.read(new ByteArrayInputStream(bytes));
		int srcWdith = bim.getWidth();
		int srcHeigth = bim.getHeight();
		int desWidth = new BigDecimal(srcWdith).multiply(new BigDecimal(accuracy)).intValue();
		int desHeight = new BigDecimal(srcHeigth).multiply(new BigDecimal(accuracy)).intValue();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 字节输出流（写入到内存）
		Thumbnails.of(new ByteArrayInputStream(bytes)).size(desWidth, desHeight).outputQuality(accuracy).toOutputStream(baos);
		return commpressPicCycle(baos.toByteArray(), desFileSize, accuracy);
	}
}
