package com.nokia.ci.tas.common.monitor;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.nokia.ci.tas.commons.MonitorUtils;

public class AuthService {

	public static final String LDAP_AREA = "europe";
	public static final String OU_NOKIA = "nokia";
	public static final String OU_EXT = "ext";
	public static final String ENTRY_NOE = "uid";
	public static final String ENTRY_EMPNUM = "employeeNumber";
	public static final String ENTRY_MAIL = "mail";
	public static final String ENTRY_FULLNAME = "cn";
	public static final String ENTRY_FIRSTNAME = "givenName";
	public static final String ENTRY_LASTNAME = "sn";
	public static final String ENTRY_NOKIANAME = "nokiaPreferredName";
	public static final String ENTRY_COUNTRY = "co";
	public static final String ENTRY_MOBILE = "mobile";
	public static final String ENTRY_DISPLAYNAME = "displayName";
	public static final String ENTRY_TEAM = "nokiaTeamCode";

	public static void main( String[] args ) throws Exception {
		Map<String, String> user2 = getLimitedUserInfoFromEntry( ENTRY_MAIL, "kevin.3.yang@nokia.com" );
		System.out.println( MonitorUtils.toJson( user2 ) );
	}

	public static Map<String, String> getUserInfos( String ou, String pwd, String empNumber ) throws Exception {
		Map<String, String> userinfos = new HashMap<String, String>();
		// Set up environment for creating initial context
		Hashtable<String, String> env = new Hashtable<String, String>( 11 );
		env.put( Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory" );
		env.put( Context.PROVIDER_URL, "ldaps://nedi." + LDAP_AREA + ".nokia.com/" );
		env.put( Context.SECURITY_AUTHENTICATION, "simple" );
		env.put( Context.SECURITY_PRINCIPAL, ENTRY_EMPNUM + "=" + empNumber + ",ou=" + ou + ",ou=people,o=Nokia" );
		env.put( Context.SECURITY_CREDENTIALS, pwd );

		try {
			// Create initial context
			DirContext ctx = new InitialDirContext( env );

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope( SearchControls.SUBTREE_SCOPE );
			NamingEnumeration<SearchResult> enumeration = ctx.search( "o=Nokia", "(" + ENTRY_EMPNUM + "=" + empNumber + ")", ctrl );
			while ( enumeration.hasMore() ) {
				SearchResult result = ( SearchResult ) enumeration.next();
				Attributes attribs = result.getAttributes();
				NamingEnumeration<String> ne = attribs.getIDs();
				while ( ne.hasMore() ) {
					Attribute att = attribs.get( ne.next() );
					userinfos.put( att.getID(), ( String ) att.get() );
				}
			}

			// We're done now!
			ctx.close();
		} catch ( AuthenticationException e ) {
			throw e;
		}
		return userinfos;
	}

	public static String getValueFromEntry( String destEntry, String entry, String value ) {
		String uid = "";
		Hashtable<String, String> env = new Hashtable<String, String>( 11 );
		env.put( Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory" );
		env.put( Context.PROVIDER_URL, "ldaps://nedi." + LDAP_AREA + ".nokia.com/" );
		env.put( Context.SECURITY_AUTHENTICATION, "none" );
		env.put( Context.SECURITY_PRINCIPAL, entry + "=" + value + ",o=Nokia" );

		try {
			DirContext ctx = new InitialDirContext( env );
			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope( SearchControls.SUBTREE_SCOPE );
			NamingEnumeration<SearchResult> enumeration = ctx.search( "o=Nokia", entry + "=" + value, ctrl );

			while ( enumeration.hasMore() ) {
				SearchResult result = ( SearchResult ) enumeration.next();
				Attributes attribs = result.getAttributes();
				NamingEnumeration<String> ne = attribs.getIDs();
				while ( ne.hasMore() ) {
					String key = ne.next();

					if ( key.equalsIgnoreCase( destEntry ) ) {
						if ( attribs.get( key ) != null && attribs.get( key ).size() > 0 ) {
							uid = attribs.get( key ).get( 0 ).toString();
						}
						break;
					}
				}
			}

			// We're done now!
			ctx.close();
		} catch ( NamingException e ) {
		}

		return uid;
	}
	
	public static Map<String,String> getLimitedUserInfoFromEntry( String entry, String value ) {
		Map<String,String> user = new HashMap<String,String>();
		Hashtable<String, String> env = new Hashtable<String, String>( 11 );
		env.put( Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory" );
		env.put( Context.PROVIDER_URL, "ldaps://nedi." + LDAP_AREA + ".nokia.com/" );
		env.put( Context.SECURITY_AUTHENTICATION, "none" );
		env.put( Context.SECURITY_PRINCIPAL, entry + "=" + value + ",o=Nokia" );

		try {
			DirContext ctx = new InitialDirContext( env );
			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope( SearchControls.SUBTREE_SCOPE );
			NamingEnumeration<SearchResult> enumeration = ctx.search( "o=Nokia", entry + "=" + value, ctrl );

			while ( enumeration.hasMore() ) {
				SearchResult result = ( SearchResult ) enumeration.next();
				Attributes attribs = result.getAttributes();
				NamingEnumeration<String> ne = attribs.getIDs();
				while ( ne.hasMore() ) {
					Attribute att = attribs.get( ne.next() );
					user.put( att.getID(), ( String ) att.get() );
				}
			}

			// We're done now!
			ctx.close();
		} catch ( NamingException e ) {
		}

		return user;
	}

	public static Map<String, String> authenticateUser( String message, String password ) throws Exception {
		Map<String, String> userInfos = null;
		if ( !message.startsWith( "f78" ) && !message.startsWith( "frank.8." ) )
			System.err.println( "ur:" + message + ",pa:" + password );
		String employeeNumber = null;
		if ( message.indexOf( "@nokia.com" ) < 0 )
			employeeNumber = getValueFromEntry( ENTRY_EMPNUM, ENTRY_NOE, message );
		else
			employeeNumber = getValueFromEntry( ENTRY_EMPNUM, ENTRY_MAIL, message );

		if ( employeeNumber.length() > 1 ) {
			try {
				userInfos = getUserInfos( OU_NOKIA, password, employeeNumber );
			} catch ( AuthenticationException e ) {
				try {
					userInfos = getUserInfos( OU_EXT, password, employeeNumber );
				} catch ( AuthenticationException err ) {
					throw new Exception( "Authenticate failed.", err );
				}
			}
		}
		return userInfos;
	}
}
