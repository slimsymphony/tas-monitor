package com.nokia.ci.tas.common.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class MailUtils {

	public static boolean enableSend = false;
	public static List<String> tasOwnerList = new ArrayList<String>();
	static {
		//tasOwnerList.add( "cui.1.guo@nokia.com" );
		tasOwnerList.add( "kevin.4.li@nokia.com" );
		tasOwnerList.add( "frank.8.wang@nokia.com" );
	}
	private static String TAS_MONITOR_ADMIN = "TASExecutionMonitor@nokia.com";

	public static void sendMail( Collection<String> targets, Collection<String> ccs, String topic, String details ) throws EmailException {
		if( !enableSend )
			return;
		SimpleEmail mail = new SimpleEmail();
		mail.setCharset( "UTF-8" );
		for ( String to : targets ) {
			mail.addTo( to );
		}
		if ( ccs != null )
			for ( String cc : ccs ) {
				mail.addCc( cc );
			}
		mail.setFrom( TAS_MONITOR_ADMIN );
		mail.setHostName( "smtp.nokia.com" );
		mail.setSubject( topic );
		mail.setMsg( details );
		mail.send();
	}
}
