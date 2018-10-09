
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverVehicleService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.security.VehicleSecurityCheckService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.provider.SysProviderApplication;


/**
 * Junit Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SysProviderApplication.class)
public class dictServiceTest {
	
	
	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
    private VehicleSecurityCheckService vehicleSecurityCheckService;
	
	@Autowired
    private DriverVehicleService driverVehicleService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
    private BasicPriceService basicPriceService;
	
	@Test
	public void testAdd(){
		Dictionary dict = new Dictionary();
//		dict.setConfigKey("type");
//		List<Dictionary> byKey = dictionaryService.getByKey(dict);
//		String key = "bus_status";
//		List<Dictionary> rediesByKey = dictionaryService.getRediesByKey(key);
//		System.err.println(rediesByKey.toString());
//		
//		List<String> dictionaryId=new ArrayList<>();
//		dictionaryId.add("8c1b2bbc1493440d9aff5d0c2838a1ee");
//		boolean deleteById = dictionaryService.deleteById(dictionaryId);
//		System.err.println(rediesByKey.toString());
		
//		DriverVehicle dv = new DriverVehicle();
//		dv.setDriverId("bf406ca2dc4141f2954cb369f11e270a");
//		List<String> listId = new ArrayList<>();
//		listId.add("56acb4dee71440938e9e581a52fcca38");
//		dv.setListId(listId);
//		boolean add = driverVehicleService.add(dv);
//		System.err.println(add);
		
		
		
//		boolean deleteBydrId = driverVehicleService.deleteBydrId(listId);
//		System.err.println(deleteBydrId);
//		
//		boolean add = driverVehicleService.add(dv);
		
//		String currentPage="1";
//         String pageSize="10";
//		Page<DriverVehicle> page = new Page<DriverVehicle>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
//		Page<DriverVehicle> selPageList = driverVehicleService.selPageList(page, dv);
//		System.err.println(selPageList.toString());
		
//		DriverVehicle one = driverVehicleService.getOne("2f5f27f6c20d48f0b2247e9d4feab4cb");
//		System.err.println(one);
		
//		DriverVehicle dv = new DriverVehicle();
//		List<String> listId = new ArrayList<>();
//		listId.add("55d72e7b7309462faba607e9467e5fd9");
//		listId.add("a9fccc2e9b9548afad14f1e14e674bf3");
//		listId.add("072e1a70321745aa9b13276a41b85b5d");
//		dv.setListId(listId);
//		dv.setDriverId("019336961c444989a5b2e2e4f90e31a8");
//		boolean updateDv = driverVehicleService.updateDv(dv);
		
//		DriverVehicle dv = new DriverVehicle();
//		dv.setDriverId("019336961c444989a5b2e2e4f90e31a8");
//		List<String> listId = new ArrayList<>();
//		listId.add("072e1a70321745aa9b13276a41b85b5d");
//		dv.setListId(listId);
//		boolean updateDv = driverVehicleService.add(dv);
		
		
//		Vehicle v = new Vehicle();
//		v.setId("444c9925261644b28fae8553cb67760e");
//		v.setCarNo("444444");
//		vehicleService.updatev(v);
		
//		VehicleSecurityCheck vc = new VehicleSecurityCheck();
//		vc.setVehicleId("1");
//		vc.setId("1");
//		boolean add = vehicleSecurityCheckService.add(vc);
		
//		String busId = "240ac164b90648358eb871c725b99ea0";
		
//		List<BusPriceResp> list = basicPriceService.getBusPrice(busId);
		
//		BusPriceRequest req = new BusPriceRequest();
//		List<BasicPrice> busPrice = new ArrayList<BasicPrice>();
//		
//		req.setBusPrice(busPrice);
//		basicPriceService.add(req);
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		Date nowDate = new Date();
//		String pBeginTime = "2018-09-15 08:22:33";
//		try {
//			Date endDate = format.parse(pBeginTime);
//			long nd = 1000 * 24 * 60 * 60;
//		    long nh = 1000 * 60 * 60;
//		    long nm = 1000 * 60;
//		    // long ns = 1000;
//		    // 获得两个时间的毫秒时间差异
//		    long diff = endDate.getTime() - nowDate.getTime();
//		    // 计算差多少天
//		    long day = diff / nd;
//		    // 计算差多少小时
//		    long hour = diff % nd / nh;
//		    // 计算差多少分钟
//		    long min = diff % nd % nh / nm;
//		    System.err.println(nowDate.toString());
//		    System.err.println(endDate.toString());
//		    System.err.println(day + "天" + hour + "小时" + min + "分钟");
//		    
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//		Date nowDate = new Date();
//		String date = format.format(nowDate);
//		Date parse;
//		try {
//			parse = format.parse(date);
//			String pBeginTime = "16:38";
//			Date endDate;
//			try {
//				endDate = format.parse(pBeginTime);
//				long nd = 1000 * 24 * 60 * 60;
//			    long nh = 1000 * 60 * 60;
//			    long nm = 1000 * 60;
//			    // long ns = 1000;
//			    // 获得两个时间的毫秒时间差异
//			    long diff = endDate.getTime() - parse.getTime();
//			    // 计算差多少天
//			    long day = diff / nd;
//			    // 计算差多少小时
//			    long hour = diff % nd / nh;
//			    // 计算差多少分钟
//			    long min = diff % nd % nh / nm;
//			    System.err.println(parse.toString());
//			    System.err.println(endDate.toString());
//			    System.err.println(day + "天" + hour + "小时" + min + "分钟");
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
		Date plan_run_time = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String rundate = sdf.format(plan_run_time);
		String kg = " ";
		
		
		String a = "2018-09-19";
		String b = " ";
		String c = "14:25";
		String pBeginTime = rundate+b+c;
		System.err.println(pBeginTime);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date parse;
		Date nowDate = new Date();
		try {
			parse = format.parse(pBeginTime);
			System.err.println(parse.toString());
			long nd = 1000 * 24 * 60 * 60;
		    long nh = 1000 * 60 * 60;
		    long nm = 1000 * 60;
		    // long ns = 1000;
		    // 获得两个时间的毫秒时间差异
		    long diff = parse.getTime() - nowDate.getTime();
		    // 计算差多少天
		    long day = diff / nd;
		    // 计算差多少小时
		    long hour = diff % nd / nh;
		    // 计算差多少分钟
		    long min = diff % nd % nh / nm;
		    System.err.println(nowDate.toString());
		    System.err.println(day + "天" + hour + "小时" + min + "分钟");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		Date date = new Date();
//		String nowDate = format.format(date);
//		String pDate = format.format(pBeginTime);
//		int compareTo = pDate.compareTo(nowDate);
//		System.err.println(compareTo);
		
		
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
//		String payment = null;
//		int cnum = 10;	//当前售票员的当前票号
//		int pnum = cnum-1;	//当前售票员的当前票号-1
//		payment= String.valueOf(cnum-1);
//		System.err.println(payment);
		
//		Date date = new Date();
//		Date date1 = new Date();
//		System.err.println(date.toString());
//		System.err.println(date1.toString());
//		long time = date.getTime();
//		long time2 = date1.getTime();
//		System.err.println(time);
//		System.err.println(time2);
//		String run_time = "16:38";
//		DateFormat format1 = new SimpleDateFormat("HH:mm");
//		
//		Date endDate = format1.parse(run_time);
//		
//		String nowDate1 = format1.format(date);
//		
//		int compareTo = run_time.compareTo(nowDate1);
//		System.err.println(compareTo);
//		
		
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String nowDate = format.format(date);
//		String pDate = format.format(date1);
//		int compareTo = pDate.compareTo(nowDate);
//		System.err.println(compareTo);
		
		
		
		
		
	}
	
}
