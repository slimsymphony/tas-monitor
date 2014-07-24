package com.nokia.ci.tas.common.monitor;

public class MonitorException extends Exception {

	private static final long serialVersionUID = -6829357428283384907L;

	public MonitorException( String message, Throwable t ) {
		super( message, t );
	}

	public MonitorException( String message ) {
		super( message );
	}

	public MonitorException( Throwable t ) {
		super( "Monitor Exception", t );
	}
}
