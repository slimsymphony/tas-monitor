import junit.framework.Assert;

import org.junit.Test;

import com.nokia.ci.tas.communicator.monitor.model.TestStation;


public class CompareTest {

	@Test
	public void testCompare() {
		TestStation ts = new TestStation("3cnd04150.china.nokia.com",8080);
		TestStation ts2 = new TestStation("3cnd04150.china.nokia.com",8080);
		Assert.assertEquals( ts, ts2 );
		Assert.assertEquals( ts.hashCode(), ts2.hashCode() );
	}
}
