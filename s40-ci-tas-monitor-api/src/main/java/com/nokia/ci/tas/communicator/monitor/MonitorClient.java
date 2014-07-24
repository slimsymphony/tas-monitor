package com.nokia.ci.tas.communicator.monitor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.nokia.ci.tas.common.monitor.MonitorException;
import com.nokia.ci.tas.commons.ItemType;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.commons.Request;
import com.nokia.ci.tas.commons.Response;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

/**
 * Test Monitor Client for Communicator.
 * 
 * @author Frank Wang
 * @since Jul 31, 2012
 */
public class MonitorClient {
	private String host;
	private int port;

	public MonitorClient( String host, int port ) {
		this.host = host;
		this.port = port;
	}
	
	public static String getStackTrace( Throwable t ) {
		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter(sw) );
		return sw.toString();
	}
	
	public static Response isAliveSafe( final TestStation station ) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Socket socket = null;
		OutputStream os = null;
		Future<String> task = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( station.getHost(), station.getPort() ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			req.addItem( ItemType.TEST.name() );
			os.write( MonitorUtils.compressData( req.toJson() ) );
			os.flush();
			final InputStream in = socket.getInputStream();
			task = executor.submit( new Callable<String>() {
				@Override
				public String call() throws Exception {
					try {
						ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
						byte[] buffer = new byte[4096];
						int n = 0;
						while ( -1 != ( n = in.read( buffer ) ) ) {
							bos.write( buffer, 0, n );
						}
						return new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" );
					} catch ( Exception e ) {
						System.out.println( "Send heartbeat signal failed.station=" + station +" Reason:"+getStackTrace(e));
					} finally {
						MonitorUtils.close( in );
					}
					return null;
				}
			} );
			return MonitorUtils.fromJson( task.get( 5, TimeUnit.SECONDS ), Response.class );
		} catch ( Exception e ) {
			if( task != null )
				task.cancel( true );
			System.out.println( "Send heartbeat signal failed.station=" + station +" Reason:"+getStackTrace(e) );
		} finally {
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
			executor.shutdownNow();
		}
		return null;
	}

	public static Response isAlive( TestStation station ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( station.getHost(), station.getPort() ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			req.addItem( ItemType.TEST.name() );
			os.write( MonitorUtils.compressData( req.toJson() ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send heartbeat signal failed.station=" + station );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}
	
	public static Response isAliveSafe( final TestStation station, final String[] items ) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Socket socket = null;
		OutputStream os = null;
		Future<String> task = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( station.getHost(), station.getPort() ), 800 );
			os = socket.getOutputStream();
			Request req = new Request();
			if ( items != null && items.length > 0 )
				for ( String item : items )
					req.addItem( item );
			os.write( MonitorUtils.compressData( req.toJson() ) );
			os.flush();
			final InputStream in = socket.getInputStream();
			task = executor.submit( new Callable<String>() {
				@Override
				public String call() throws Exception {
					
					try {
						
						ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
						byte[] buffer = new byte[4096];
						int n = 0;
						while ( -1 != ( n = in.read( buffer ) ) ) {
							bos.write( buffer, 0, n );
						}
						return new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" );
					} catch ( Exception e ) {
						System.out.println( "Send heartbeat signal failed.station=" + station +" Reason:"+getStackTrace(e) );
					} finally {
						MonitorUtils.close( in );
					}
					return null;
				}
			} );
			return MonitorUtils.fromJson( task.get( 25, TimeUnit.SECONDS ), Response.class );
		} catch ( Exception e ) {
			if( task != null )
				task.cancel( true );
			System.out.println( "Send heartbeat signal failed.station=" + station + " Reason:"+getStackTrace(e) );
		} finally {
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
			executor.shutdownNow();
		}
		return null;
	}

	public static Response isAlive( TestStation station, String[] items ) {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( station.getHost(), station.getPort() ), 300 );
			os = socket.getOutputStream();
			Request req = new Request();
			if ( items != null && items.length > 0 )
				for ( String item : items )
					req.addItem( item );
			os.write( MonitorUtils.compressData( req.toJson() ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			System.out.println( "Send heartbeat signal failed.station=" + station );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return null;
	}

	public boolean isAlive() {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket( host, port );
			os = socket.getOutputStream();
			os.write( MonitorUtils.compressData( new Request().toJson() ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			if ( MonitorUtils.fromJson( new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" ), Response.class ) instanceof Response ) {
				return true;
			}
		} catch ( Exception e ) {
			return false;
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
		return false;
	}
	
	public static Response sendRequest( final TestStation station, final Request req ) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Socket socket = null;
		OutputStream os = null;
		Future<String> task = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( station.getHost(), station.getPort() ), 800 );
			os = socket.getOutputStream();
			os.write( MonitorUtils.compressData( req.toJson() ) );
			os.flush();
			final InputStream in = socket.getInputStream();
			task = executor.submit( new Callable<String>() {
				@Override
				public String call() throws Exception {
					
					try {
						
						ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
						byte[] buffer = new byte[4096];
						int n = 0;
						while ( -1 != ( n = in.read( buffer ) ) ) {
							bos.write( buffer, 0, n );
						}
						return new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" );
					} catch ( Exception e ) {
						System.out.println( "Send heartbeat signal failed.station=" + station + ",request:" + req + ", Reason:"+getStackTrace(e) );
					} finally {
						MonitorUtils.close( in );
					}
					return null;
				}
			} );
			return MonitorUtils.fromJson( task.get( 8, TimeUnit.SECONDS ), Response.class );
		} catch ( Exception e ) {
			if( task != null )
				task.cancel( true );
			System.out.println( "Send Request  failed.station=" + station + ",request:" + req + ", Reason:"+getStackTrace(e) );
		} finally {
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
			executor.shutdownNow();
		}
		return null;
	}
	
	public Response sendRequest( Request request ) throws MonitorException {
		Socket socket = null;
		OutputStream os = null;
		InputStream in = null;
		try {
			socket = new Socket( host, port );
			os = socket.getOutputStream();
			os.write( MonitorUtils.compressData( request.toJson() ) );
			os.flush();
			in = socket.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
			byte[] buffer = new byte[4096];
			int n = 0;
			while ( -1 != ( n = in.read( buffer ) ) ) {
				bos.write( buffer, 0, n );
			}
			return MonitorUtils.fromJson( new String( MonitorUtils.decompress( bos.toByteArray() ), "ISO-8859-1" ), Response.class );
		} catch ( Exception e ) {
			throw new MonitorException( "[MonitorClient] Send Request failed.", e );
		} finally {
			MonitorUtils.close( in );
			MonitorUtils.close( os );
			MonitorUtils.close( socket );
		}
	}
	
	public static Response stopTest( String host, int port, String testId ) {
		Request request = new Request();
		ItemType item = ItemType.OPERATION;
		item.addAttr( "StopTest", testId );
		request.addItem( MonitorUtils.toJson( item.getAttrs() ) );
		return sendRequest( new TestStation(host,port), request);
	}
	
	public static Response freeProduct( String host, int port, String imei ) {
		Request request = new Request();
		ItemType item = ItemType.OPERATION;
		item.addAttr( "FreeProduct", imei );
		request.addItem( MonitorUtils.toJson( item.getAttrs() ) );
		return sendRequest( new TestStation(host,port), request);
	}
	
	public static Response removeProduct( String host, int port, String imei ) {
		Request request = new Request();
		ItemType item = ItemType.OPERATION;
		item.addAttr( "RemoveProduct", imei );
		request.addItem( MonitorUtils.toJson( item.getAttrs() ) );
		return sendRequest( new TestStation(host,port), request);
	}
}
