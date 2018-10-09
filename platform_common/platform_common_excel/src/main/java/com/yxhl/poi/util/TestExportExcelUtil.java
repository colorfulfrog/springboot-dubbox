package com.yxhl.poi.util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * 测试文件导出
 *
 */
public class TestExportExcelUtil {
	
	public static void testMergeCell() throws FileNotFoundException {
		//新建excel报表
        XSSFWorkbook excel = new XSSFWorkbook();
        //添加一个sheet，名字叫"我的POI之旅"
        XSSFSheet hssfSheet = excel.createSheet("我的POI之旅");

        //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
        CellRangeAddress cellRangeAddress =new CellRangeAddress(0, 3, 3, 9);

        //在sheet里增加合并单元格
        hssfSheet.addMergedRegion(cellRangeAddress);
        //生成第一行
        Row row = hssfSheet.createRow(0);
        Cell first = row.createCell(3);
        first.setCellValue("合并单元格");
        //cell 位置3-9被合并成一个单元格，
        //不管你怎样创建第4个cell还是第5个cell…然后在写数据，都是无法写入的。
        Cell secondCell = row.createCell(10);

        secondCell.setCellValue("不在合并单元格范围");

        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream("D:/students.xlsx");
            excel.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void testMergeCell2() throws FileNotFoundException {
		//新建excel报表
        XSSFWorkbook excel = new XSSFWorkbook();
        //添加一个sheet，名字叫"我的POI之旅"
        XSSFSheet hssfSheet = excel.createSheet("我的POI之旅");

        //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
        CellRangeAddress nameMerge =new CellRangeAddress(0, 1, 0, 0);
        hssfSheet.addMergedRegion(nameMerge); //姓名
        CellRangeAddress employeeIdMerge =new CellRangeAddress(0, 1, 1, 1);
        hssfSheet.addMergedRegion(employeeIdMerge); //工号
        CellRangeAddress stationMerge =new CellRangeAddress(0, 1, 2, 2);
        hssfSheet.addMergedRegion(stationMerge); //车站
        CellRangeAddress salePointMerge =new CellRangeAddress(0, 1, 3, 3);
        hssfSheet.addMergedRegion(salePointMerge); //售票点
        CellRangeAddress typeMerge =new CellRangeAddress(0, 1, 4, 4);
        hssfSheet.addMergedRegion(typeMerge); //类型
        CellRangeAddress saleMerge =new CellRangeAddress(0, 0, 5, 8);
        hssfSheet.addMergedRegion(saleMerge); //售票
        
        //生成第一行
        Row row = hssfSheet.createRow(0);
        Cell nameCell = row.createCell(0);
        nameCell.setCellValue("姓名");
        Cell employeeIdCell = row.createCell(1);
        employeeIdCell.setCellValue("工号");
        Cell stationCell = row.createCell(2);
        stationCell.setCellValue("车站");
        Cell salePointCell = row.createCell(3);
        salePointCell.setCellValue("售票点");
        Cell typeMergeCell = row.createCell(4);
        typeMergeCell.setCellValue("类型");
        Cell saleMergeCell = row.createCell(5);
        saleMergeCell.setCellValue("售票");
        
        //生成第二行
        Row row2 = hssfSheet.createRow(1);
        Cell fullTicketCell = row2.createCell(5);
        fullTicketCell.setCellValue("全票");
        Cell countTicketCell = row2.createCell(6);
        countTicketCell.setCellValue("折扣票");
        Cell saleAmountCell = row2.createCell(7);
        saleAmountCell.setCellValue("金额");
        Cell oilCell = row2.createCell(8);
        oilCell.setCellValue("燃油费");
        
        
        
        

        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream("D:/merge.xlsx");
            excel.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) throws Exception{
		/*ExportExcelUtil<Student> util = new ExportExcelUtil<Student>();
		 // 准备数据
        List<Student> list = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
        	 list.add(new Student(111,"张三asdf","男"));
             list.add(new Student(111,"李四asd","男"));
             list.add(new Student(111,"王五","女"));
        }
        String[] columnNames = { "ID", "姓名", "性别" };
        util.exportExcel("用户导出", columnNames, list, new FileOutputStream("D:/test.xlsx"), ExportExcelUtil.EXCEl_FILE_2007);*/
        
    	// 准备数据
 		/*List<LinkedHashMap<String, Object>> list2 = new ArrayList<LinkedHashMap<String, Object>>();
 		LinkedHashMap<String, Object> map = null;
 		for (int i = 0; i < 10; i++) {
 			map = new LinkedHashMap<String,Object>();
 			map.put(i+"0", i+"0");
 			map.put(i+"1", "姓名："+i);
 			map.put(i+"2", "性别："+i);
 			list2.add(map);
 		}
 		String[] columnNames2 = { "ID", "姓名", " 性别"};
 		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet("结算单导出");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(20);
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cellHeader;
		for (int i = 0; i < columnNames2.length; i++) {
			cellHeader = row.createCell(i);
			cellHeader.setCellStyle(ExportExcel.getHeaderStyle(workbook));
			cellHeader.setCellValue(new XSSFRichTextString(columnNames2[i]));
		}
 		ExportExcel.exportExcel2007(workbook, sheet, 1, list2, new FileOutputStream("D:/结算单.xlsx"));*/
		
		testMergeCell2();
	}
}

