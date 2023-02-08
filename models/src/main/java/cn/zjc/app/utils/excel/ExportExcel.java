package cn.zjc.app.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param title   表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 */
	public HSSFWorkbook exportExcel(String title, String[] headers, Collection<T> dataset) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 产生表头
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length));
		HSSFRow rowTitle = sheet.createRow(0);
		HSSFCell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellValue(title);
		cellTitle.setCellStyle(style);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(1);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 1;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			Class tCls = t.getClass();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = tCls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
					Object value = getMethod.invoke(t, new Object[]{});
					// 判断值的类型后进行强制类型转换
					String textValue = null == value ? "" : value.toString();
					if (textValue != null) {
						Pattern pidcard = Pattern.compile("^\\d{17}[0-9xX]$");
						Pattern pnum = Pattern.compile("^\\d+(\\.\\d+)?$");
						if (pidcard.matcher(textValue).matches()) {
							// 是数字当作double处理
							cell.setCellValue(textValue);
						} else if (pnum.matcher(textValue).matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							richString.applyFont(font);
							cell.setCellValue(richString);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
		}
		return workbook;
	}

	/**
	 * 导出的时候不生成顶部的标题行
	 * @param title
	 * @param headers
	 * @param dataset
	 * @return
	 */
	public HSSFWorkbook exportExcelWithoutHeader(String title, String[] headers, Collection<T> dataset) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			Class tCls = t.getClass();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = tCls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
					Object value = getMethod.invoke(t, new Object[]{});
					// 判断值的类型后进行强制类型转换
					String textValue = null == value ? "" : value.toString();
					if (textValue != null) {
						Pattern pidcard = Pattern.compile("^\\d{17}[0-9xX]$");
						Pattern pnum = Pattern.compile("^\\d+(\\.\\d+)?$");
						if (pidcard.matcher(textValue).matches()) {
							// 是数字当作double处理
							cell.setCellValue(textValue);
						} else if (pnum.matcher(textValue).matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							richString.applyFont(font);
							cell.setCellValue(richString);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return workbook;
	}
}
