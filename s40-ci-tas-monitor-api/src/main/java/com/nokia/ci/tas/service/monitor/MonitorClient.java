package com.nokia.ci.tas.service.monitor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.nokia.ci.tas.commons.ItemType;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.commons.Request;
import com.nokia.ci.tas.commons.Response;

/**
 * Test Monitor Client for tas service.
 * 
 * @author Frank Wang
 * @since Jul 31, 2012
 */
public class MonitorClient {
	
	public static Response checkStatus( String host, int port ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( host, port ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			req.addItem( ItemType.TESTNODEADAPTER.name() );
			req.addItem( ItemType.CLIENT.name() );
			os.write( req.toJson().getBytes( "ISO-8859-1" ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( bos.toByteArray(), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send heartbeat signal failed.host=" + host + ",port=" + port );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
	public static Response cleanClients( String host, int port, String specialClientHostAndPort ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( host, port ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			ItemType item = ItemType.OPERATION;
			item.addAttr( "ResetClientInfo", specialClientHostAndPort );
			req.addItem( MonitorUtils.toJson( item.getAttrs() ) );
			os.write( req.toJson().getBytes( "ISO-8859-1" ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( bos.toByteArray(), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send clean Clients request.host=" + host + ",port=" + port );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
	public static Response removeTarget( String host, int port, String imei ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( host, port ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			ItemType item = ItemType.OPERATION;
			item.addAttr( "RemoveTarget", imei );
			req.addItem( MonitorUtils.toJson( item.getAttrs() ) );
			os.write( req.toJson().getBytes( "ISO-8859-1" ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( bos.toByteArray(), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send clean Clients request.host=" + host + ",port=" + port );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
	public static Response resetTarget( String host, int port, String imei ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( host, port ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			ItemType item = ItemType.OPERATION;
			item.addAttr( "ResetTarget", imei );
			req.addItem( MonitorUtils.toJson( item.getAttrs() ) );
			os.write( req.toJson().getBytes( "ISO-8859-1" ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( bos.toByteArray(), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send clean Clients request.host=" + host + ",port=" + port );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
	public static Response stopTest( String host, int port, String testId ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( host, port ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			ItemType item = ItemType.OPERATION;
			item.addAttr( "StopTest", testId );
			req.addItem( MonitorUtils.toJson( item.getAttrs() ) );
			os.write( req.toJson().getBytes( "ISO-8859-1" ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( bos.toByteArray(), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send clean Clients request.host=" + host + ",port=" + port );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
}
