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
 * $Id: NoPortAvailableException.java 152 2005-08-20 19:15:09Z mat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/lib/NoPortAvailableException.java $
 * 
 */

package rtspproxy.lib;

public class NoPortAvailableException extends Exception
{
	static final long serialVersionUID = 0x33DD33DD55L;

	@Override
	public String getMessage()
	{
		return "No UDP port available";
	}
}
