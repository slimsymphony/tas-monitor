import com.nokia.ci.tas.commons.ItemType;
import com.nokia.ci.tas.commons.Request;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.communicator.monitor.MonitorClient;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;


public class TestTimeout {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		// TODO Auto-generated method stub
		Response res = MonitorClient.isAliveSafe( new TestStation("3cnd02409.noe.nokia.com",5451) );
		System.out.println( res );
		Response res2 = MonitorClient.isAliveSafe( new TestStation("3cnd04230.noe.nokia.com",5451) );
		System.out.println( res2 );
		Response res3 = MonitorClient.isAliveSafe( new TestStation("3cnd02409.noe.nokia.com",5451), new String[]{ ItemType.TEST.name(), ItemType.PRODUCT.name() } );
		System.out.println( res3 );
		Response res4 = MonitorClient.isAliveSafe( new TestStation("3cnd04230.noe.nokia.com",5451), new String[]{ ItemType.TEST.name(), ItemType.PRODUCT.name() } );
		System.out.println( res4 );
		
		Request req = new Request();
		req.addItem( ItemType.TESTNODE.name() );
		req.addItem( ItemType.PRODUCT.name() );
		req.addItem( ItemType.TEST.name() );
		//req.addItem( ItemType.LOG.name() );
		
		Response res5 = MonitorClient.sendRequest( new TestStation("3cnd02409.noe.nokia.com",5451), req );
		System.out.println( res5 );
		Response res6 = MonitorClient.sendRequest( new TestStation("3cnd04230.noe.nokia.com",5451), req );
		System.out.println( res6 );
	}

}
