import java.io.File;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nokia.ci.tas.common.monitor.MonitorException;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.communicator.monitor.TestStationManager;
import com.nokia.ci.tas.communicator.monitor.TestStationManagerFactory;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;


public class StationManagerTest {

	/**
	 * @param args
	 * @throws MonitorException 
	 */
	public static void main( String[] args ) throws MonitorException {
		TestStationManager tm = TestStationManagerFactory.getInstance( new File("C:/develop/eclipse-workspaces/jee_workspace/s40-ci-tas-monitor/src/main/resources/stations.json") );
		List<TestStation> ts = null;
		
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
		ts = MonitorUtils.fromJson( MonitorUtils.toJson( ts ), new TypeToken<List<TestStation>>() {}.getType() );
		System.out.println( MonitorUtils.toJson( ts ) );
		
		TestStation t0 = new TestStation("3cnd04150.noe.nokia.com",5451);
		tm.addStation( t0 );
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
		ts = MonitorUtils.fromJson( MonitorUtils.toJson( ts ), new TypeToken<List<TestStation>>() {}.getType() );
		System.out.println( MonitorUtils.toJson( ts ) );
		
		TestStation t = new TestStation("3cnd04151.noe.nokia.com",5451);
		tm.addStation( t );
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
		
		TestStation t2 = new TestStation("3cnd04152.noe.nokia.com",5451);
		tm.updateStation( t, t2 );
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
		
		tm.deleteStation( t2 );
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
		
		tm.deleteStation( t0 );
		ts = tm.getAllStations();
		System.out.println( MonitorUtils.toJson( ts ) );
	}

}
