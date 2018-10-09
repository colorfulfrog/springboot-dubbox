package com.yxhl.stationbiz.web.consumer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yxhl.platform.common.entity.DynamicBean;
import com.yxhl.poi.util.ExportDynamicExcelWrapper;
import com.yxhl.poi.util.ExportExcel;
import com.yxhl.poi.util.ExportExcelUtil;
import com.yxhl.poi.util.ExportExcelWrapper;
import com.yxhl.poi.util.Student;

/**
 * http://localhost:8187/platform-web/test/get/excel
 * @author lw
 *
 */
@Controller("test")
@RequestMapping("/test")
public class ExcelExportController {
	
	@RequestMapping("/get/excel")
	public void getExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 准备数据
		List<Student> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			 list.add(new Student(111,"张三asdf","男"));
		     list.add(new Student(111,"李四asd","男"));
		     list.add(new Student(111,"王五","女"));
		}
		String[] columnNames = { "ID", "姓名", " 性别"};
		String fileName = "excel1";
		ExportExcelWrapper<Student> util = new ExportExcelWrapper<Student>();
		util.exportExcel(fileName, fileName, columnNames, list, response, ExportExcelUtil.EXCEl_FILE_2007);
	}
	
	/**
	 * http://localhost:8187/platform-web/test/get/dynamicExcel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/get/dynamicExcel")
	public void getDynamicExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1 设置类成员属性
		HashMap<String,Object> propertyMap = new HashMap<String,Object>();
		propertyMap.put("id", Class.forName("java.lang.Integer"));
		propertyMap.put("name", Class.forName("java.lang.String"));
		propertyMap.put("address", Class.forName("java.lang.String"));
		
		// 准备数据
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		for (int i = 0; i < 2; i++) {
			// 2生成动态 Bean
			DynamicBean bean = new DynamicBean(propertyMap);
			// 3 给 Bean 设置值
			bean.setValue("id", new Integer(1+i));
			bean.setValue("name", "454"+i);
			bean.setValue("address", "789"+i);
			list.add(bean);
		}
		
		String[] columnNames = { "ID", "姓名", " 地址"};
		String fileName = "动态bean导出";
		ExportDynamicExcelWrapper util = new ExportDynamicExcelWrapper();
		util.exportExcel(fileName, fileName, columnNames, list, response, ExportExcelUtil.EXCEl_FILE_2007);
	}
	
	/**
	 * 导出excel，数据源为List<Map>
	 * http://localhost:8187/platform-web/test/get/mapExcel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/get/mapExcel")
	public void getMapExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 准备数据
		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> map = null;
		for (int i = 0; i < 10; i++) {
			map = new LinkedHashMap<String,Object>();
			map.put(i+"0", i+"0");
			map.put(i+"1", "姓名："+i);
			map.put(i+"2", "性别："+i);
			list.add(map);
		}
		String[] columnNames = { "ID", "姓名", " 性别"};
		String fileName = "excel2";
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet("结算单导出");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(20);
		
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cellHeader;
		for (int i = 0; i < columnNames.length; i++) {
			cellHeader = row.createCell(i);
			cellHeader.setCellStyle(ExportExcel.getHeaderStyle(workbook));
			cellHeader.setCellValue(new XSSFRichTextString(columnNames[i]));
		}
		ExportExcel.exportExcel(fileName,workbook, sheet, 1, list, response);
	}
}

