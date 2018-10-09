import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yxhl.platform.common.redis.util.RedisLockUtil;
import com.yxhl.stationbiz.system.provider.SysProviderApplication;


/**
 * Junit Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SysProviderApplication.class)
public class LockTest {
	@Autowired
	private RedisLockUtil lock;
	
	
	@Test
	public void testLock(){
		boolean setLock = lock.lock("test1","tangcan", 300000);
		System.out.println("Lock is :" + setLock);
	}
	
	@Test
	public void testUnLock(){
		lock.unlock("test1","liwei");
	}
}
