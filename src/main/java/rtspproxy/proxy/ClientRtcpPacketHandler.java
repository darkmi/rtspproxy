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
 * $Id: ClientRtcpPacketHandler.java 290 2005-11-24 19:43:03Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/proxy/ClientRtcpPacketHandler.java $
 * 
 */

package rtspproxy.proxy;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import rtspproxy.lib.Exceptions;
import rtspproxy.rtp.rtcp.RtcpPacket;

/**
 * Handles RTCP packets from client and forward them to server. The RTSP 
 * session is obtained using the client IP address and port.
 * 
 * @author Matteo Merli
 */
public class ClientRtcpPacketHandler extends IoHandlerAdapter
{

	private static Logger log = Logger.getLogger( ClientRtcpPacketHandler.class );

	@Override
	public void sessionCreated( IoSession session ) throws Exception
	{
	}

	@Override
	public void messageReceived( IoSession session, Object buffer ) throws Exception
	{
		RtcpPacket packet = new RtcpPacket( (IoBuffer) buffer );
		// log.debug( "Received RTCP packet: " + packet.getType() );

		// / Track track = (Track)session.getAttribute( "track" );

		Track track = Track.getByClientAddress( (InetSocketAddress) session.getRemoteAddress() );

		if ( track == null ) {
			// drop packet
			log.debug( "Invalid address: "
					+ (InetSocketAddress) session.getRemoteAddress()
					+ " - Class: "
					+ ( (InetSocketAddress) session.getRemoteAddress() ).getAddress().getClass() );
			return;
		}

		track.forwardRtcpToServer( packet );
	}

	@SuppressWarnings("deprecation")
	@Override
	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	{
		log.debug( "Exception: " + cause );
		Exceptions.logStackTrace( cause );
		session.close();
	}
}
