package com.nokia.ci.tas.communicator.monitor.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.commons.Test;
import com.nokia.ci.tas.commons.TestAdapter;
import com.nokia.ci.tas.communicator.monitor.MonitorClient;
import com.nokia.ci.tas.communicator.monitor.StatusMonitor;
import com.nokia.ci.tas.communicator.monitor.TestStationManager;
import com.nokia.ci.tas.communicator.monitor.TestStationManagerFactory;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

public class RoutineCheckJob implements Job {

	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		Map<String, Timestamp> tests = null;
		Map<TestStation, Timestamp> stations = null;
		try {
			tests = StatusMonitor.getRunningTests();
			stations = StatusMonitor.getRunningStations();

			TestStationManager tsm = TestStationManagerFactory.getInstance();
			List<TestStation> tss = tsm.getAllStations();
			List<String> handledTests = new ArrayList<String>();
			Response res = null;

			for ( TestStation ts : tss ) {
				res = MonitorClient.isAliveSafe( ts );
				if ( res == null ) {
					continue;
				} else {
					if ( !stations.keySet().contains( ts ) )
						stations.put( ts, new Timestamp( System.currentTimeMillis() ) );
					if ( res.getItems() != null && res.getItems().size() > 0 ) {
						TestAdapter ta = ( TestAdapter ) res.getItems().get( 0 );
						if ( ta!=null && ta.getTests() != null && ta.getTests().size() > 0 ) {
							for ( Test test : ta.getTests().keySet() ) {
								handledTests.add( test.getId() );
								if ( !tests.containsKey( test.getId() ) ) {
									tests.put( test.getId(), new Timestamp( System.currentTimeMillis() ) );
								}
							}
						}
					}
				}
			}

			Iterator<String> it = tests.keySet().iterator();
			while ( it.hasNext() ) {
				String key = it.next();
				if ( !handledTests.contains( key ) )
					it.remove();
			}
		} catch ( Exception e ) {
			System.err.println( "Execute Test check job met problem." );
			e.printStackTrace();
			JobExecutionException jee = new JobExecutionException( "Execute Task check job met problem.", e );
			jee.setRefireImmediately( true );
		} finally {
			tests = null;
			stations = null;
		}
	}
}
