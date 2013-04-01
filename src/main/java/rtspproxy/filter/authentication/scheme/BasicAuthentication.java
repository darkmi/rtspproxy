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
 * $Id: BasicAuthentication.java 328 2005-12-08 13:17:33Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter/authentication/scheme/BasicAuthentication.java $
 * 
 */

package rtspproxy.filter.authentication.scheme;

import org.apache.log4j.Logger;

import rtspproxy.lib.Base64;

/**
 * Implementation of the Basic authentication scheme.
 * 
 * @author Matteo Merli
 */
public class BasicAuthentication implements AuthenticationScheme
{

	private static Logger log = Logger.getLogger( BasicAuthentication.class );

	public String getName()
	{
		return "Basic";
	}

	public Credentials getCredentials( String authString )
	{
		String username;
		String password;

		try {
			// authString = Basic [base64 data]
			authString = authString.split( " " )[1];
			// Basic scheme credential are BASE64 encoded.
			byte[] decBytes = Base64.decode( authString );
			StringBuilder sb = new StringBuilder();
			for ( byte b : decBytes )
				sb.append( (char)b );
			String auth = sb.toString();

			log.debug( "auth: " + auth );
			username = auth.split( ":", 2 )[0];
			password = auth.split( ":", 2 )[1];
			log.debug( "username=" + username + " - password=" + password );
		} catch ( Exception e ) {
			log.info( "Malformed authString: " + authString );
			return null;
		}

		return new Credentials( username, password );
	}

}
