/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   Copyright (C) 2005 - Matteo Merli - matteo.merli@gmail.com            *
 *                                                                         *
 ***************************************************************************/

/*
 * $Id: PlainTextAuthenticationProvider.java 336 2005-12-08 20:48:05Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter/authentication/PlainTextAuthenticationProvider.java $
 * 
 */

package rtspproxy.filter.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import rtspproxy.Config;
import rtspproxy.Reactor;
import rtspproxy.filter.authentication.scheme.Credentials;

/**
 * @author Matteo Merli
 */
public class PlainTextAuthenticationProvider implements AuthenticationProvider
{

	private static Logger log = Logger.getLogger( PlainTextAuthenticationProvider.class );

	private static Properties usersDb = new Properties();

	public void init() throws Exception
	{
		// Load users from file
		String fileName = Config.getHome()
				+ File.separator
				+ Config.get( "proxy.filter.authentication.text.file", "conf" + File.separator
						+ "users.txt" );

		try {
			usersDb.load( new FileInputStream( new File( fileName ) ) );

		} catch ( FileNotFoundException e ) {
			log.fatal( "Users file not found:" + e );
			Reactor.stop();
		} catch ( IOException e ) {
			log.fatal( "Error reading users file: " + e );
			Reactor.stop();
		} catch ( IllegalArgumentException e ) {
			log.fatal( "The users file is not valid" );
			Reactor.stop();
		}

	}

	public void shutdown() throws Exception
	{
		// Do nothing
	}

	public boolean isAuthenticated( Credentials credentials )
	{
		String storedPassword = usersDb.getProperty( credentials.getUserName() );
		if ( storedPassword == null )
			// User is not present
			return false;

		if ( storedPassword.compareTo( credentials.getPassword() ) == 0 )
			// Password is ok
			return true;
		else
			// Password is wrong
			return false;
	}

}
