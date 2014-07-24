package com.nokia.ci.tas.service.monitor.job;

import org.apache.commons.mail.EmailException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nokia.ci.tas.common.monitor.MailUtils;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.service.monitor.MonitorClient;
import com.nokia.ci.tas.service.monitor.TasServiceManager;
import com.nokia.ci.tas.service.monitor.model.TasService;

public class RoutineCheckJob implements Job {

	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		try {
			TasServiceManager tsm = TasServiceManager.getInstance();
			Response res = null;
			for( TasService ts : tsm.getAllServices() ) {
				res = MonitorClient.checkStatus( ts.getHost(), ts.getPort() );
				if( res == null ) {
					System.err.println( "TAS Service:"+ts.toString()+" is out of service." );
					try {
						MailUtils.sendMail( MailUtils.tasOwnerList, null, "TAS Service is stopped!", "Hi, Owners:\r\n\t The Tas Service[" + ts.getHost() + ":" + ts.getPort()
								+ "] is stopped currently.\r\n\t Please contact related administrators to recovery this problem ASAP! \r\n\t Thanks & Br\r\n\t Yours TAS Monitor System " );
					} catch ( EmailException e1 ) {
						System.err.println( "Send Alert Mail failed." );
						e1.printStackTrace();
					}
				}
			}
		} catch ( Exception e ) {
			System.err.println( "Execute Tas service check job met problem." );
			e.printStackTrace();
			JobExecutionException jee = new JobExecutionException( "Execute Tas service check job met problem.", e );
			jee.setRefireImmediately( true );
		}
	}
}
