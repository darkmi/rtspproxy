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
 * $Id: Credentials.java 328 2005-12-08 13:17:33Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter/authentication/scheme/Credentials.java $
 * 
 */

package rtspproxy.filter.authentication.scheme;

/**
 * Holds the credentials (username and password) sent by the client.
 * 
 * @author Matteo Merli
 */
public class Credentials
{

	private String userName;
	private String password;

	public Credentials( String userName, String password )
	{
		this.userName = userName;
		this.password = password;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *        The password to set.
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName
	 *        The userName to set.
	 */
	public void setUserName( String userName )
	{
		this.userName = userName;
	}
}
