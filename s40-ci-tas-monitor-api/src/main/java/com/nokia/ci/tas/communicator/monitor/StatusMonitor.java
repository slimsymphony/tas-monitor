package com.nokia.ci.tas.communicator.monitor;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.nokia.ci.tas.communicator.monitor.model.TestStation;

/**
 * Collection for saving all the running test status.
 * 
 * @author Frank Wang
 * @since Aug 9, 2012
 */
public class StatusMonitor {
	
	private final static Map<String, Timestamp> runningTests = new HashMap<String, Timestamp>();
	private final static Map<TestStation, Timestamp> runningStations = new HashMap<TestStation, Timestamp>();

	public synchronized static Map<String, Timestamp> getRunningTests() {
		return runningTests;
	}

	public synchronized static Map<TestStation, Timestamp> getRunningStations() {
		return runningStations;
	}

	public synchronized static void addRunningStation( TestStation station ) {
		if ( station == null )
			return;
		if ( !runningStations.containsKey( station ) )
			runningStations.put( station, new Timestamp( System.currentTimeMillis() ) );
	}

	public synchronized static void removeRunningStation( TestStation station ) {
		if ( station == null )
			return;
		runningStations.remove( station );
	}

	public synchronized static void addRunningTest( String testID ) {
		if ( testID == null || "".equals( testID.trim() ) )
			return;
		if ( !runningTests.containsKey( testID ) )
			runningTests.put( testID, new Timestamp( System.currentTimeMillis() ) );
	}

	public synchronized static void removeRunningTest( String testID ) {
		if ( testID == null || "".equals( testID.trim() ) )
			return;
		runningTests.remove( testID );
	}
}
