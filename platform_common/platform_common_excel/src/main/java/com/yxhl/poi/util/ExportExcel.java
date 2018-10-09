package com.yxhl.poi.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导出Excel公共方法
 */
public class ExportExcel {
	
	/**
	 * <p>
	 * 通用Excel导出方法,将数据写入Excel文件中 <br>
	 * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
	 * </p>
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格头部标题集合
	 * @param dataset
	 *            需要显示的数据集合,集合中放置Map，一个map就是一行记录
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param dataRowIndex
	 *            数据开始行
	 */
	public static void exportExcel2007(XSSFWorkbook workbook, XSSFSheet sheet,int dataRowIndex, List<LinkedHashMap<String, Object>> dataset,OutputStream out) {
		// 生成并设置数据样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成数据字体
		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 表格数据行
		XSSFRow row = null;
		// 遍历集合数据，产生数据行
		XSSFRichTextString richString;
		Pattern p = Pattern.compile("^//d+(//.//d+)?$");
		Matcher matcher;
		XSSFCell cell;
		Object value;
		String textValue;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for (LinkedHashMap<String, Object> map : dataset) {
			int cellIndex = 0;
			row = sheet.createRow(dataRowIndex); // 生成行
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for (Entry<String, Object> entry : entrySet) { // 循环列
				cell = row.createCell(cellIndex);
				cell.setCellStyle(style);
				try {
					value = entry.getValue();
					// 判断值的类型后进行强制类型转换
					textValue = null;
					if (value instanceof Integer) {
						cell.setCellValue((Integer) value);
					} else if (value instanceof Float) {
						textValue = String.valueOf((Float) value);
						cell.setCellValue(textValue);
					} else if (value instanceof Double) {
						textValue = String.valueOf((Double) value);
						cell.setCellValue(textValue);
					} else if (value instanceof Long) {
						cell.setCellValue((Long) value);
					}
					if (value instanceof Boolean) {
						textValue = "是";
						if (!(Boolean) value) {
							textValue = "否";
						}
					} else if (value instanceof Date) {
						textValue = sdf.format((Date) value);
					} else {
						// 其它数据类型都当作字符串简单处理
						if (value != null) {
							textValue = value.toString();
						}else {
							textValue="";
						}
					}
					if (textValue != null) {
						matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							richString = new XSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
				cellIndex++;
			}
			dataRowIndex++;
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static XSSFCellStyle getHeaderStyle(XSSFWorkbook workbook) {
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(new XSSFColor(java.awt.Color.LIGHT_GRAY));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setColor(new XSSFColor(java.awt.Color.BLACK));
		font.setFontHeightInPoints((short) 11);
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}
	
	/**
	 * <p>
	 * 导出带有头部标题行的Excel <br>
	 * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
	 * </p>
	 * 
	 * @param title 表格标题
	 * @param headers 头部标题集合
	 * @param dataset 数据集合
	 * @param out 输出流
	 */
	public static void exportExcel(String fileName, XSSFWorkbook workbook, XSSFSheet sheet, int dataRowIndex,List<LinkedHashMap<String, Object>> dataset, HttpServletResponse response) {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");  
			response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
			exportExcel2007(workbook, sheet, dataRowIndex, dataset, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(outputStream !=null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
