package com.nokia.ci.tas.service.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.reflect.TypeToken;
import com.nokia.ci.tas.common.monitor.MonitorException;
import com.nokia.ci.tas.commons.MonitorUtils;
import com.nokia.ci.tas.service.monitor.model.TasService;

public class TasServiceManager {
	final private File ds;
	private static TasServiceManager instance;

	public synchronized static TasServiceManager getInstance() {
		if ( instance == null )
			instance = new TasServiceManager();
		return instance;
	}
	
	public synchronized static TasServiceManager getInstance( File ds ) {
		if ( instance == null )
			instance = new TasServiceManager( ds );
		return instance;
	}

	protected TasServiceManager( File ds ) {
		this.ds = ds;
	}

	protected TasServiceManager() {
		ds = new File( System.getProperty( "user.dir" ), "services.json" );
		if ( !ds.exists() )
			try {
				ds.createNewFile();
			} catch ( IOException e ) {
				System.err.println( "Create new Tas services Database Failed." );
				e.printStackTrace();
			}
	}

	public List<TasService> getAllServices() {
		List<TasService> services = null;
		FileInputStream fin = null;
		try {
			fin = new FileInputStream( ds );
			StringWriter sw = new StringWriter( ( int ) ds.length() );
			IOUtils.copy( fin, sw );
			services = MonitorUtils.fromJson( sw.toString(), new TypeToken<List<TasService>>() {
			}.getType() );
			if ( services == null )
				services = new ArrayList<TasService>();
		} catch ( Exception e ) {
			System.err.println( "Get All Stations failed." );
			e.printStackTrace();
		} finally {
			MonitorUtils.close( fin );
		}
		return services;
	}

	private synchronized void save( List<TasService> ts ) throws MonitorException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream( ds );
			fos.write( MonitorUtils.toJson( ts ).getBytes() );
			fos.flush();
		} catch ( Exception e ) {
			throw new MonitorException( "Save Tas Service info failed.", e );
		} finally {
			MonitorUtils.close( fos );
		}
	}

	public void addStation( TasService service ) throws MonitorException {
		List<TasService> services = getAllServices();
		if ( services.contains( service ) ) {
			return;
		} else {
			services.add( service );
			save( services );
		}
	}

	public void updateStation( TasService oldStation, TasService newStation ) throws MonitorException {
		List<TasService> ts = getAllServices();
		if ( !ts.contains( oldStation ) ) {
			throw new MonitorException( "Not found Station record to be update, service=" + oldStation + ", current services=" + MonitorUtils.toJson( ts ) );
		} else {
			ts.remove( oldStation );
			save( ts );
			addStation( newStation );
		}
	}

	public void deleteStation( TasService station ) throws MonitorException {
		List<TasService> ts = getAllServices();
		ts.remove( station );
		save( ts );
	}
}
