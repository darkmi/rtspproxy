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
 * $Id: RtcpInfo.java 188 2005-08-27 10:27:02Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtp/rtcp/RtcpInfo.java $
 * 
 */

package rtspproxy.rtp.rtcp;

import java.nio.ByteBuffer;

/**
 * Interface for all the RTCP types of packets.
 */
public interface RtcpInfo {
	public ByteBuffer toBuffer();
}
