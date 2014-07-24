package com.nokia.ci.tas.communicator.monitor;

import java.io.File;

public class TestStationManagerFactory {
	private static TestStationManager instance;
	public synchronized static TestStationManager getInstance() {
		if( instance == null )
			instance = new TestStationManagerImpl();
		return instance;
	}
	
	public synchronized static TestStationManager getInstance( File ds ) {
		if( instance == null )
			instance = new TestStationManagerImpl( ds );
		return instance;
	}
}
