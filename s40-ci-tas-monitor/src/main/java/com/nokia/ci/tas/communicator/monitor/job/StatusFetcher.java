package com.nokia.ci.tas.communicator.monitor.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.ci.tas.communicator.monitor.StationCache;

public class StatusFetcher implements Job {

	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		StationCache.refresh();
	}

}
