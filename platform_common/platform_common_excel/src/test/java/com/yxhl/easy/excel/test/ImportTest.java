package com.yxhl.easy.excel.test;

import java.util.List;

import org.junit.Test;

import com.yxhl.easy.excel.ExcelContext;
import com.yxhl.easy.excel.demo.Elplan;
import com.yxhl.easy.excel.result.ExcelImportResult;
import com.yxhl.easy.excel.test.model.ELPjPlan;
import com.yxhl.easy.excel.test.model.StudentModel;
/**
 * Excel导入测试
 * @author elead-rd
 *
 */
public class ImportTest {
	
	// 测试时文件磁盘路径
	private static String path = "d:/test/test.xlsx";
	// 配置文件路径
	private ExcelContext context = ExcelContext.newInstance("excel-config.xml");
	// Excel配置文件中配置的id
	private static String excelId = "student";
	
	/**
	 * 导入Excel,使用了com.yxhl.easy.excel.test.ExportTest.testExportCustomHeader()方法生成的Excel
	 * @throws Exception
	 */
	@Test
	public void testImport()throws Exception{
		ExcelContext context = ExcelContext.newInstance("excel-config.xml");
		ExcelImportResult result = context.doImport(excelId, path);
		System.out.println(result.getHeader());
		List<StudentModel> stus = result.getListBean();
		for (StudentModel model : stus) {
			System.out.println(model);
		}
	}
	
	@Test
	public void testImports()throws Exception{
		ExcelContext context = ExcelContext.newInstance("excel-config.xml");
		List<ExcelImportResult> result = context.doImport(new String [] {"student","student2"}, path);
		System.out.println(result.size());
		for (ExcelImportResult r : result) {
			System.out.println(r.getListBean());
		}
	}
		
	@Test
	public void testImportELPlans()throws Exception{
		ExcelContext context = ExcelContext.newInstance("plan-config.xml");
		ExcelImportResult result = context.doImport("elplanImport", "d:/test/plantimport.xlsx");
		List<ELPjPlan> list = result.getListBean();
		com.yxhl.easy.excel.test.Test<Elplan<Elplan>> te = new com.yxhl.easy.excel.test.Test<Elplan<Elplan>>();
		for (ELPjPlan elPjPlan : list) {
			if(elPjPlan.getPlan().getId() == null)
			{
				te.addList.add(elPjPlan.getPlan());
			}else{
				te.excelMap.put(elPjPlan.getPlan().getId(), elPjPlan.getPlan());
			}
		}
		buildELPlans(list);
	}
	
	private void buildELPlans(List<ELPjPlan> list)
	{
		/*Map<String,Elplan> wbsMap = new HashMap<String, Elplan>();
		Map<Integer,String> keyMap = new HashMap<Integer, String>();
		for (ELPjPlan elPjPlan : list) {
			elPjPlan.getPlan().setId(UUID.randomUUID().toString());
			wbsMap.put(elPjPlan.getWbs(), elPjPlan.getPlan());
			keyMap.put(elPjPlan.getIndex(), elPjPlan.getPlan().getId());
			String parentWBS = this.getParentWBS(elPjPlan.getWbs());
			System.out.println(elPjPlan.getWbs() + " - parentWBS :" + parentWBS);
			if(parentWBS != null){
				//设置父
				elPjPlan.getPlan().setParent(wbsMap.get(parentWBS).getId());
			}
			if(!elPjPlan.getRelIndex().isEmpty()){
				//设置前置任务
				String key = keyMap.get(elPjPlan.getRelIndex());
				elPjPlan.getPlan().setRelplan(key);
			}
			DBHelper.service.saveOrUpdate(elPjPlan.getPlan());
		}
		System.out.println("save start....");
		//DBHelper.service.batchSave(wbsMap.values());
		System.out.println("save end.......");*/
	}
	
	private String getParentWBS(String wbs)
	{
		int n = wbs.lastIndexOf(".");
		if(n>0){
			return wbs.substring(0,n);
		}
		return null;
	}
}
