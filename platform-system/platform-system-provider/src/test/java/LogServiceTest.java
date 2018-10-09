
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.provider.SysProviderApplication;


/**
 * Junit Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SysProviderApplication.class)
public class LogServiceTest {
	
	
	@Autowired
	OperateLogService operateLogService;
	
	@Test
	public void testAdd(){
		boolean insertLog = operateLogService.insertLog("1","2","3","4","5");
		System.err.println(insertLog);
	}
	
}
