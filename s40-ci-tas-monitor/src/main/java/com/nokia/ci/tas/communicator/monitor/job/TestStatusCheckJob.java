package com.nokia.ci.tas.communicator.monitor.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.ci.tas.common.monitor.MailUtils;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.commons.Test;
import com.nokia.ci.tas.commons.TestAdapter;
import com.nokia.ci.tas.communicator.monitor.MonitorClient;
import com.nokia.ci.tas.communicator.monitor.StatusMonitor;
import com.nokia.ci.tas.communicator.monitor.TestStationManager;
import com.nokia.ci.tas.communicator.monitor.TestStationManagerFactory;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

public class TestStatusCheckJob implements Job {

	private static long TIMEOUT = 12 * 60 * 60 * 1000;
	private static List<String> filterKeys = new ArrayList<String>();
	static {
		filterKeys.add( "mtbf" );
	}

	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		try {
			Map<String, Timestamp> tests = StatusMonitor.getRunningTests();
			Map<TestStation, Timestamp> stations = StatusMonitor.getRunningStations();

			TestStationManager tsm = TestStationManagerFactory.getInstance();
			List<TestStation> tss = tsm.getAllStations();
			Response res = null;
			Map<String, TestStation> currtests = new HashMap<String, TestStation>();

			for ( TestStation ts : tss ) {
				res = MonitorClient.isAliveSafe( ts );
				if ( res == null ) {
					if ( stations.containsKey( ts ) ) {
						MailUtils.sendMail( MailUtils.tasOwnerList, null, "The TestStation " + ts + " was stopped", "Dear owner:\n The system detected that the TestStation" + ts
								+ " was not connected now. Please Check the station's status manually." + "\nThanks & Br\n Yours TAS Monitor System" );
						stations.remove( ts );
					}
					continue;
				} else {
					if ( !stations.containsKey( ts ) ) {
						stations.put( ts, new Timestamp( System.currentTimeMillis() ) );
					}
					if ( res.getItems() != null && res.getItems().size() > 0 ) {
						TestAdapter ta = ( TestAdapter ) res.getItems().get( 0 );
						if ( ta != null && ta.getTests() != null && ta.getTests().size() > 0 ) {
							for ( Test test : ( ( TestAdapter ) res.getItems().get( 0 ) ).getTests().keySet() ) {
								currtests.put( test.getId(), ts );
							}
						}
					}
				}
			}

			for ( Map.Entry<String,Timestamp> entry: tests.entrySet() ) {
				String testId = entry.getKey();
				if ( currtests.containsKey( testId ) ) {
					if ( !checkFilter( testId ) ) {// long term jobs
						long interval = System.currentTimeMillis() - entry.getValue().getTime();
						if ( interval > TIMEOUT ) {// over timeout
							MailUtils.sendMail( MailUtils.tasOwnerList, null, "Test[" + testId + "] execution over time", "The execution test[" + testId + "] running @"
									+ currtests.get( testId ) + " and starting from " + tests.get( testId ) + " have been over " + ( interval / ( 60 * 1000 ) )
									+ " minutes.\n Please Check the status of this test.\n Thanks & Br\n Yours TAS Monitor System" );
						}
					}
				} else {
					if ( checkFilter( testId ) ) {
						MailUtils.sendMail( MailUtils.tasOwnerList, null, "Test[" + testId + "] execution Stopped", "The execution test[" + testId + "] running start from "
								+ tests.get( testId ) + " have been ended. It was a long-term test, so maybe it was ended by accident."
								+ "\n Please Check the status of this test.\n Thanks & Br\n Yours TAS Monitor System" );
					}
				}
			}

		} catch ( Exception e ) {
			System.err.println( "Execute Test check job met problem." );
			e.printStackTrace();
			JobExecutionException jee = new JobExecutionException( "Execute Task check job met problem.", e );
			jee.setRefireImmediately( true );
		}
	}

	private static boolean checkFilter( String testId ) {
		String l = testId.toLowerCase();
		for ( String key : filterKeys ) {
			if ( l.indexOf( key ) >= 0 )
				return true;
		}
		return false;
	}
}
