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
 * $Id: Packet.java 203 2005-09-07 09:07:37Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtp/Packet.java $
 * 
 */

package rtspproxy.rtp;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Base interface for RTP and RTCP packets
 * 
 * @author mat
 */
public interface Packet {

	public IoBuffer toByteBuffer();
}
