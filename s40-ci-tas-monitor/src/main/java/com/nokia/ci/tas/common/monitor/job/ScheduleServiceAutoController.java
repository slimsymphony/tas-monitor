package com.nokia.ci.tas.common.monitor.job;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.DateBuilder.IntervalUnit;

import com.nokia.ci.tas.common.monitor.ScheduleUtils;
import com.nokia.ci.tas.communicator.monitor.job.StatusFetcher;

/**
 * Auto Start Scheduled Services.
 * 
 * @author Frank Wang
 * @since Jun 12, 2012
 */
public class ScheduleServiceAutoController implements ServletContextListener {

	@Override
	public void contextInitialized( ServletContextEvent sce ) {
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR, 10 );
		cal.set( Calendar.MINUTE, 00 );
		cal.set( Calendar.SECOND, 00 );
		try {
			//ScheduleUtils.scheduleIntervalJob( "StationChecker", TestStatusCheckJob.class, IntervalUnit.MINUTE, 20, Calendar.getInstance().getTime() );
			//ScheduleUtils.scheduleIntervalJob( "RoutineTestStationChecker", com.nokia.ci.tas.communicator.monitor.job.RoutineCheckJob.class, IntervalUnit.MINUTE, 15, Calendar.getInstance().getTime() );
			//ScheduleUtils.scheduleIntervalJob( "RoutineServiceChecker", com.nokia.ci.tas.service.monitor.job.RoutineCheckJob.class, IntervalUnit.MINUTE, 15, Calendar.getInstance().getTime() );
			ScheduleUtils.scheduleIntervalJob("Status", StatusFetcher.class, IntervalUnit.SECOND, 20, Calendar.getInstance().getTime() );
		} catch ( Exception e ) {
			System.err.println( "Start quartz Scheduler failed!" );
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed( ServletContextEvent sce ) {
		try {
			ScheduleUtils.endScheduler();
		} catch ( Exception e ) {
			System.err.println( "Shutdown Scheduler met problem." );
			e.printStackTrace();
		}
	}
}
