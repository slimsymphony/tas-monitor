import com.nokia.ci.tas.commons.ItemType;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.service.monitor.MonitorClient;


public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Response rep = MonitorClient.checkStatus( "localhost",5451 );
		//System.out.println(rep);
		ItemType item = ItemType.OPERATION;
		item.addAttr( "ResetClientInfo", "" );
		String json = MonitorUtils.toJson( item.getAttrs() );
		System.out.println( json );
		item = ItemType.parse( json );
		System.out.println(item);
	}

}
