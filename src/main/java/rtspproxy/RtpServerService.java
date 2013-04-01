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
 * $Id: RtpServerService.java 293 2005-11-24 19:50:47Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/RtpServerService.java $
 * 
 */

package rtspproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.Provider.Service;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import rtspproxy.lib.PortManager;
import rtspproxy.proxy.ServerRtcpPacketHandler;
import rtspproxy.proxy.ServerRtpPacketHandler;

/**
 * @author Matteo Merli
 */
public class RtpServerService implements ProxyService {

	private static Logger log = Logger.getLogger(RtpServerService.class);

	static InetSocketAddress rtpAddress = null;
	static InetSocketAddress rtcpAddress = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see rtspproxy.ProxyService#start()
	 */
	public void start() throws Exception {
		int rtpPort = Config.getInt("proxy.server.rtp.port", 8000);
		int rtcpPort = Config.getInt("proxy.server.rtcp.port", 8001);
		String netInterface = Config.get("proxy.server.interface", null);
		boolean dinPorts = Config.getBoolean("proxy.server.dynamicPorts", false);

		// If dinPorts is true, we have to first check the availability
		// of the ports and choose 2 valid ports.
		if (dinPorts) {
			int[] ports = PortManager.findAvailablePorts(2, rtpPort);
			rtpPort = ports[0];
			rtcpPort = ports[1];
		}

		// Update properties with effective ports
		Config.setInt("proxy.server.rtp.port", rtpPort);
		Config.setInt("proxy.server.rtcp.port", rtcpPort);

		rtpAddress = new InetSocketAddress(InetAddress.getByName(netInterface), rtpPort);
		rtcpAddress = new InetSocketAddress(InetAddress.getByName(netInterface), rtcpPort);

		//try {
		//	Service rtpService, rtcpService;

		//	rtpService = new Service("RtpServerService", TransportType.DATAGRAM, rtpAddress);
		//	rtcpService = new Service("RtcpServerService", TransportType.DATAGRAM, rtcpAddress);

		//	Reactor.getRegistry().bind(rtpService, new ServerRtpPacketHandler());
		//	Reactor.getRegistry().bind(rtcpService, new ServerRtcpPacketHandler());
		//	log.info("RtpServerService Started - Listening on: " + InetAddress.getByName(netInterface) + " " + rtpPort
		//			+ "-" + rtcpPort);

		//} catch (IOException e) {
		//	log.fatal("Can't start the service. " + e);
		//	throw e;
		//}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rtspproxy.ProxyService#stop()
	 */
	public void stop() throws Exception {
		//for (Object service : Reactor.getRegistry().getServices("RtpServerService")) {
		//	Reactor.getRegistry().unbind((Service) service);
		//}
		//for (Object service : Reactor.getRegistry().getServices("RtcpServerService")) {
		//	Reactor.getRegistry().unbind((Service) service);
		//}

		log.info("RtpServerService Stopped");
	}

	public static IoSession newRtpSession(SocketAddress remoteAddress) {
		//return Reactor.getRegistry().getAcceptor(TransportType.DATAGRAM).newSession(remoteAddress, rtpAddress);
		return null;
	}

	public static IoSession newRtcpSession(SocketAddress remoteAddress) {
		//return Reactor.getRegistry().getAcceptor(TransportType.DATAGRAM).newSession(remoteAddress, rtcpAddress);
		return null;
	}

	public static InetSocketAddress getRtpAddress() {
		return rtpAddress;
	}

	public static InetSocketAddress getRtcpAddress() {
		return rtcpAddress;
	}

	public static InetAddress getHostAddress() {
		/*
		 * The InetAddress (IP) is the same for both RTP and RTCP.
		 */
		return rtpAddress.getAddress();
	}

	public static int getRtpPort() {
		return rtpAddress.getPort();
	}

	public static int getRtcpPort() {
		return rtcpAddress.getPort();
	}
}
