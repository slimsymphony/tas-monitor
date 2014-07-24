package com.nokia.ci.tas.communicator.monitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.nokia.ci.tas.commons.ItemType;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

public class StationCache {
	private static Map<TestStation, Response> stationStatus = new LinkedHashMap<TestStation, Response>();
	private final static Semaphore available = new Semaphore( 1, true );
	private final static Semaphore availableReadonly = new Semaphore( 3, true );
	private static List<String> groups = new ArrayList<String>();
	public static Map<TestStation, Response> get() {
		Map<TestStation, Response> status = null;
		try {
			availableReadonly.acquire();
			available.acquire();
			status = new LinkedHashMap<TestStation, Response>( stationStatus );
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			available.release();
			availableReadonly.release();
		}
		return status;
	}
	
	public static List<String> getGroups() {
		return groups;
	}
	
	public static void refresh() {
		try {
			available.acquire();
			stationStatus.clear();
			TestStationManager tsm = TestStationManagerFactory.getInstance();
			groups = tsm.getGroups();
			Response res = null;
			for ( TestStation ts : tsm.getAllStations() ) {
				res = MonitorClient.isAliveSafe( ts, new String[] { ItemType.TEST.name(), ItemType.PRODUCT.name(), ItemType.TESTNODE.name() } );
				stationStatus.put( ts, res );
			}
		} catch ( Exception e ) {
			System.err.println( "Execute Test check job met problem." );
			e.printStackTrace();
		} finally {
			available.release();
		}
	}
}
