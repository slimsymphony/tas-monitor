package com.nokia.ci.tas.communicator.monitor;

import java.util.List;

import com.nokia.ci.tas.common.monitor.MonitorException;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

public interface TestStationManager {
	
	List<TestStation> getAllStations();
	
	List<TestStation> getAllStationsByGroup( String group );
	
	List<String> getGroups();

	void addStation( TestStation station ) throws MonitorException;

	void updateStation( TestStation oldStation, TestStation newStation ) throws MonitorException;

	void deleteStation( TestStation station ) throws MonitorException;
}
