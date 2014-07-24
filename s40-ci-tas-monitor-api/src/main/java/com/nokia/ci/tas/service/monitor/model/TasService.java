package com.nokia.ci.tas.service.monitor.model;

import com.nokia.ci.tas.commons.MonitorUtils;

public class TasService {
	private String host;
	private int port;
	private int servicePort;
	
	public TasService( String host, int port ) {
		if( host!=null&&host.indexOf(":")>0 ){
			String[] arr = host.split(":");
			this.host = arr[0];
			this.servicePort = Integer.parseInt( arr[1] );
		}else{
			this.host = host;
			this.servicePort = 33333;
		}
		this.port = port;
	}
	
	public TasService( String host, int port, int servicePort ) {
		this.host = host;
		this.port = port;
		this.servicePort = servicePort;
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

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	@Override
	public String toString() {
		return MonitorUtils.toJson( this );
	}

	@Override
	public boolean equals( Object o ) {
		if ( o == null || !( o instanceof TasService ) )
			return false;
		TasService t = ( TasService ) o;
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
