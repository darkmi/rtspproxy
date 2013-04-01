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
 * $Id: PacketType.java 184 2005-08-27 10:16:13Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/proxy/PacketType.java $
 * 
 */

package rtspproxy.proxy;

public enum PacketType 
{
	
	/** A data packet (RTP) */
	DataPacket, 
	
	/** A control packet (RTCP) */
	ControlPacket
	
}
