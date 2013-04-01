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
 * $Id: IpAddressProvider.java 322 2005-12-08 08:54:20Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter/ipaddress/IpAddressProvider.java $
 * 
 */

package rtspproxy.filter.ipaddress;

import java.net.InetAddress;

/**
 * @author Matteo Merli
 */
public interface IpAddressProvider
{

	public void init() throws Exception;

	public void shutdown() throws Exception;
	
	public boolean isBlocked( InetAddress address );
	
}
