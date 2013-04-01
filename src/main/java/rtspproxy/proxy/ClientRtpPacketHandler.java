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
 * $Id: ClientRtpPacketHandler.java 309 2005-12-01 22:36:28Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/proxy/ClientRtpPacketHandler.java $
 * 
 */

package rtspproxy.proxy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import rtspproxy.lib.Exceptions;
import rtspproxy.rtp.RtpPacket;

/**
 * Handles RTP packets from clients arriving at the proxy public port.
 * This is a special case, because generally clients will not send
 * any RTP packet (only RTCP).
 * 
 * @author Matteo Merli
 */
public class ClientRtpPacketHandler extends IoHandlerAdapter
{

	private static Logger log = Logger.getLogger( ClientRtpPacketHandler.class );

	@Override
	public void sessionCreated( IoSession session ) throws Exception
	{
	}

	@Override
	public void messageReceived( IoSession session, Object buffer ) throws Exception
	{
		RtpPacket packet = new RtpPacket( (IoBuffer) buffer );
		log.debug( "Received RTP packet: " + packet.getSequence() );

		Track track = Track.getByClientAddress( (InetSocketAddress) session.getRemoteAddress() );

		if ( track == null ) {
			// drop packet
			log.debug( "Packet received from unknown client: " + session.getRemoteAddress() ); 
			return;
		}

		track.forwardRtpToServer( packet );
	}

	@Override
	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	{
		log.debug( "Exception: " + cause );
		Exceptions.logStackTrace( cause );
		session.close();
	}
}
