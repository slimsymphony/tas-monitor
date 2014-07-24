import com.nokia.ci.tas.commons.MessageItem;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.service.monitor.MonitorClient;


public class MonitorToolTest {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		Response resp = null;
		MonitorClient smc = new  MonitorClient();
		//resp = smc.stopTest( "127.0.0.1", 5451, "Retry test #0" );
		//resp = smc.cleanClients( "127.0.0.1", 5451, "*" );
		
		
		com.nokia.ci.tas.communicator.monitor.MonitorClient cmc = new com.nokia.ci.tas.communicator.monitor.MonitorClient("127.0.0.1",5452);
		cmc.stopTest( "127.0.0.1", 5452, "Retry test #0" );
		
		for(MessageItem<?> item : resp.getItems()) {
			System.out.println(item.toString());
		}
		
	}

}
