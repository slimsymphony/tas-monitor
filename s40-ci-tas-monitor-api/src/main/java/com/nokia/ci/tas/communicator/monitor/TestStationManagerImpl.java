package com.nokia.ci.tas.communicator.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.gson.reflect.TypeToken;
import com.nokia.ci.tas.common.monitor.MonitorException;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.communicator.monitor.model.TestStation;

public class TestStationManagerImpl implements TestStationManager {

	final private File ds;

	public TestStationManagerImpl( File ds ) {
		this.ds = ds;
	}

	public TestStationManagerImpl() {
		ds = new File( System.getProperty( "user.dir" ), "stations.json" );
		if ( !ds.exists() )
			try {
				ds.createNewFile();
			} catch ( IOException e ) {
				System.out.println( "Create new Test Station Database Failed." );
				e.printStackTrace();
			}
	}

	private synchronized void save( List<TestStation> ts ) throws MonitorException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream( ds );
			fos.write( MonitorUtils.toJson( ts ).getBytes() );
			fos.flush();
		} catch ( Exception e ) {
			throw new MonitorException( "Save Test Station info failed.", e );
		} finally {
			MonitorUtils.close( fos );
		}
	}

	@Override
	public List<TestStation> getAllStations() {
		List<TestStation> ts = new ArrayList<TestStation>();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream( ds );
			StringWriter sw = new StringWriter( ( int ) ds.length() );
			IOUtils.copy( fin, sw );
			ts = MonitorUtils.fromJson( sw.toString(), new TypeToken<List<TestStation>>() {
			}.getType() );
			if ( ts == null )
				ts = new ArrayList<TestStation>();
		} catch ( Exception e ) {
			System.out.println( "Get All Stations failed." );
			e.printStackTrace();
		} finally {
			MonitorUtils.close( fin );
		}
		return ts;
	}

	@Override
	public void addStation( TestStation station ) throws MonitorException {
		List<TestStation> ts = getAllStations();
		if ( ts.contains( station ) ) {
			return;
		} else {
			ts.add( station );
			save( ts );
		}
	}

	@Override
	public void updateStation( TestStation oldStation, TestStation newStation ) throws MonitorException {
		List<TestStation> ts = getAllStations();
		if ( !ts.contains( oldStation ) ) {
			throw new MonitorException( "Not found Station record to be update, station=" + oldStation
					+ ", current stations=" + MonitorUtils.toJson( ts ) );
		} else {
			ts.remove( oldStation );
			save( ts );
			addStation( newStation );
		}
	}

	@Override
	public void deleteStation( TestStation station ) throws MonitorException {
		List<TestStation> ts = getAllStations();
		ts.remove( station );
		save( ts );
	}

	@Override
	public List<TestStation> getAllStationsByGroup( String group ) {
		List<TestStation> ts = getAllStations();
		if ( group != null && !group.trim().isEmpty() ) {
		Iterator<TestStation> it = ts.iterator();
			while ( it.hasNext() ) {
				TestStation t = it.next();
				if ( !group.equalsIgnoreCase( t.getGroup() ) ) {
						it.remove();
				}
			}
		}
		return ts;
	}

	@Override
	public List<String> getGroups() {
		List<TestStation> ts = getAllStations();
		Set<String> groups = new HashSet<String>();
		for(TestStation t : ts) {
			if( t.getGroup()!=null && !t.getGroup().trim().isEmpty() ) {
				if(!groups.contains( t.getGroup() )) {
					groups.add( t.getGroup() );
				}
			}
		}
		return new ArrayList<String>(groups);
	}

}
