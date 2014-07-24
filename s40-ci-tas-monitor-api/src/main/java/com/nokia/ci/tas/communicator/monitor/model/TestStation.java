package com.nokia.ci.tas.communicator.monitor.model;

import com.nokia.ci.tas.commons.MonitorUtils;

/**
 * Represent a running Test Station (One JVM running TAS communicator.)
 * 
 * @author Frank Wang
 * @since Aug 1, 2012
 */
public class TestStation {

	private String host;
	private int port;
	private String group;

	public TestStation( String host, int port ) {
		this.host = host;
		this.port = port;
		this.group = "";
	}
	
	public TestStation( String host, int port, String group ) {
		this.host = host;
		this.port = port;
		this.group = group;
	}

	public String getHost() {
		return host;
	}

	public void setHost( String host ) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort( int port ) {
		this.port = port;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup( String group ) {
		this.group = group;
	}

	@Override
	public String toString() {
		return MonitorUtils.toJson( this );
	}

	@Override
	public boolean equals( Object o ) {
		if ( o == null || !( o instanceof TestStation ) )
			return false;
		TestStation t = ( TestStation ) o;
		if ( t.getHost().equalsIgnoreCase( host ) && t.getPort() == port ) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += port;
		if ( host != null ) {
			for ( char i : host.toCharArray() ) {
				hash += i;
			}
		}
		return hash;
	}
}
