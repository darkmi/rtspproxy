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
 * $Id: ProxyHandler.java 319 2005-12-08 08:21:59Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/proxy/ProxyHandler.java $
 * 
 */

package rtspproxy.proxy;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import rtspproxy.Config;
import rtspproxy.RtpClientService;
import rtspproxy.rtsp.RtspCode;
import rtspproxy.rtsp.RtspMessage;
import rtspproxy.rtsp.RtspRequest;
import rtspproxy.rtsp.RtspResponse;
import rtspproxy.rtsp.RtspTransport;
import rtspproxy.rtsp.RtspTransportList;

/**
 * @author mat
 */
public class ProxyHandler {

	private static Logger log = Logger.getLogger(ProxyHandler.class);

	/** Used to save a reference to this handler in the IoSession */
	protected static final String ATTR = ProxyHandler.class.toString() + "Attr";

	private IoSession clientSession = null;
	private IoSession serverSession = null;

	/**
	 * Creates a new ProxyHandler from a client side protocol session.
	 * 
	 * @param clientSession
	 */
	public ProxyHandler(IoSession clientSession) {
		this.clientSession = clientSession;
	}

	public void passToServer(RtspMessage message) {
		log.debug("Pass to server");
		if (message.getHeader("Session") != null) {
			ProxySession proxySession = ProxySession.getByClientSessionID(message.getHeader("Session"));
			if (proxySession != null) {
				// Session is Ok
				message.setHeader("Session", proxySession.getServerSessionId());
			} else {
				// Error. The client specified a session ID but it's
				// not valid
				sendResponse(clientSession, RtspResponse.errorResponse(RtspCode.SessionNotFound));
				return;
			}
		}
		if (serverSession == null && message.getType() == RtspMessage.Type.TypeResponse) {
			log.error("We can't send a response message to an uninitialized serverSide");
			return;
		} else if (serverSession == null) {
			RtspRequest request = (RtspRequest) message;
			try {
				connectServerSide(request.getUrl());

			} catch (IOException e) {
				log.error(e);
				// closeAll();
			} finally {
				if (serverSession == null)
					return;
			}
		}

		switch (message.getType()) {
		case TypeRequest:
			serverSession.setAttribute("lastRequestVerb", ((RtspRequest) message).getVerb());
			sendRequest(serverSession, (RtspRequest) message);
			break;

		case TypeResponse:
			sendResponse(serverSession, (RtspResponse) message);
			break;

		default:
			log.error("Message type not valid: " + message.getType());
		}
	}

	public void passToClient(RtspMessage message) {
		log.debug("Pass to client");
		if (message.getHeader("Session") != null) {
			ProxySession proxySession = ProxySession.getByServerSessionID(message.getHeader("Session"));
			if (proxySession != null) {
				// Session is Ok
				message.setHeader("Session", proxySession.getClientSessionId());
			} else {
				if (message.getType() == RtspMessage.Type.TypeResponse) {
					// create a proxy session on the fly if message is a
					// response. Certain mobile handset clients
					// tend to start a RSTP session without its own session id
					// and wait for the session object from the
					// remote server
					proxySession = new ProxySession();

					proxySession.setServerSessionId(message.getHeader("Session"));
					message.setHeader("Session", proxySession.getClientSessionId());
					log.debug("Created a new proxy session on-the-fly.");
				} else {
					// Error. The client specified a session ID but it's
					// not valid
					sendResponse(clientSession, RtspResponse.errorResponse(RtspCode.SessionNotFound));
					return;
				}
			}
		}
		switch (message.getType()) {
		case TypeRequest:
			clientSession.setAttribute("lastRequestVerb", ((RtspRequest) message).getVerb());
			sendRequest(clientSession, (RtspRequest) message);
			break;

		case TypeResponse:
			sendResponse(clientSession, (RtspResponse) message);
			break;

		default:
			log.error("Message type not valid: " + message.getType());
		}
	}

	/**
	 * A SETUP request should treated more carefully tha other RTSP requests.
	 * The proxy will perform some hijacking on the communication between client
	 * and server, such as modifying RTP/RTCP port.
	 * 
	 * @param request
	 *        SETUP request message
	 */
	public void passSetupRequestToServer(RtspRequest request) {
		ProxySession proxySession = null;

		if (request.getHeader("Session") != null) {
			// The client already specified a session ID.
			// Let's validate it
			proxySession = ProxySession.getByClientSessionID(request.getHeader("Session"));
			if (proxySession != null) {
				// Session ID is ok
				request.setHeader("Session", proxySession.getServerSessionId());
			} else {
				// Error. The client specified a session ID but it's
				// not valid
				log.debug("Invalid sessionId: " + request.getHeader("Session"));
				sendResponse(clientSession, RtspResponse.errorResponse(RtspCode.SessionNotFound));
				return;
			}
		}
		serverSession.setAttribute("lastRequestVerb", request.getVerb());

		log.debug("Client Transport:" + request.getHeader("Transport"));

		RtspTransportList rtspTransportList = new RtspTransportList(request.getHeader("Transport"));
		log.debug("Parsed:" + rtspTransportList.toString());

		int proxyRtpPort = Config.getInt("proxy.server.rtp.port", -1);
		int proxyRtcpPort = Config.getInt("proxy.server.rtcp.port", -1);

		// I'm saving the client Transport header before modifying it,
		// because I will need to know which port the client will
		// use for RTP/RTCP connections.
		int[] clientPorts = rtspTransportList.get(0).getClientPort();
		clientSession.setAttribute("clientPorts", clientPorts);
		clientSession.setAttribute("setupURL", request.getUrl().toString());

		for (RtspTransport transport : rtspTransportList.getList()) {
			log.debug("Transport:" + transport);

			if (transport.getLowerTransport() == RtspTransport.LowerTransport.TCP) {
				log.debug("Transport is TCP based.");
			} else {

				// / int clientPort[] = transport.getClientPort();
				transport.setClientPort(new int[] { proxyRtpPort, proxyRtcpPort });
				log.debug("Transport Rewritten: " + transport);
			}
		}

		if (proxySession == null) {
			proxySession = new ProxySession();
			clientSession.setAttribute(ProxySession.ATTR, proxySession);
		}

		request.setHeader("Transport", rtspTransportList.toString());

		log.debug("Sending SETUP request: \n" + request);

		sendRequest(serverSession, request);
	}

	/**
	 * Forward a RTSP SETUP response message to client.
	 * 
	 * @param response
	 *        Setup response message
	 */
	public void passSetupResponseToClient(RtspResponse response) {
		// If there isn't yet a proxySession, create a new one
		ProxySession proxySession = ProxySession.getByServerSessionID(response.getHeader("Session"));
		if (proxySession == null) {
			proxySession = (ProxySession) clientSession.getAttribute(ProxySession.ATTR);
			if (proxySession == null) {
				proxySession = new ProxySession();
				clientSession.setAttribute(ProxySession.ATTR, proxySession);
			}
		}

		if (proxySession.getServerSessionId() == null) {
			proxySession.setServerSessionId(response.getHeader("Session"));
		}

		// Modify transport parameters for the client.
		RtspTransportList rtspTransportList = new RtspTransportList(response.getHeader("Transport"));

		// int proxyRtpPort = Config.getInt( "proxy.client.rtp.port", -1 );
		// int proxyRtcpPort = Config.getInt( "proxy.client.rtcp.port", -1 );
		String netInterface = Config.get("proxy.client.interface", null);

		RtspTransport transport = rtspTransportList.getList().get(0);
		log.debug("Transport:" + transport);

		// Create a new Track object
		Track track = proxySession.addTrack((String) clientSession.getAttribute("setupURL"), transport.getSSRC());

		// Setting client and server info on the track
		InetAddress serverAddress = null;
		if (transport.getSource() != null) {
			try {
				serverAddress = InetAddress.getByName(transport.getSource());
			} catch (UnknownHostException e) {
				log.warn("Unknown host: " + transport.getSource());
			}
		} else {
			serverAddress = ((InetSocketAddress) serverSession.getRemoteAddress()).getAddress();
		}
		int[] serverPorts = transport.getServerPort();
		track.setServerAddress(serverAddress, serverPorts[0], serverPorts[1]);

		InetAddress clientAddress = null;
		try {
			clientAddress = Inet4Address
					.getByName(((InetSocketAddress) clientSession.getRemoteAddress()).getHostName());
		} catch (UnknownHostException e) {
			log.warn("Unknown host: " + clientSession.getRemoteAddress());
		}
		int clientPorts[] = (int[]) clientSession.getAttribute("clientPorts");
		track.setClientAddress(clientAddress, clientPorts[0], clientPorts[1]);

		if (transport.getLowerTransport() == RtspTransport.LowerTransport.TCP) {
			log.debug("Transport is TCP based.");
		} else {
			transport.setSSRC(track.getProxySSRC().toHexString());
			transport.setServerPort(new int[] { RtpClientService.getRtpPort(), RtpClientService.getRtcpPort() });
			// transport.setClientPort( );
			try {
				transport.setSource(InetAddress.getByName(netInterface).getHostAddress());
			} catch (UnknownHostException e) {
				transport.setSource(netInterface);
			}

			// Obtaing client specified ports
			int ports[] = (int[]) clientSession.getAttribute("clientPorts");
			transport.setClientPort(ports);

			log.debug("Transport Rewritten: " + transport);
		}

		response.setHeader("Session", proxySession.getClientSessionId());
		response.setHeader("Transport", transport.toString());

		log.debug("SENDING RESPONSE TO CLIENT:\n" + response);

		sendResponse(clientSession, response);
	}

	/**
	 * Tries to connect to remote RTSP server.
	 * 
	 * @param url
	 *        the URI of the server
	 * @throws IOException
	 */
	private void connectServerSide(URL url) throws IOException {
		log.debug("Server url: " + url);
		String host = url.getHost();
		int port = url.getPort();
		if (port == -1)
			port = url.getDefaultPort();

		// Start communication.
		log.debug("Trying to connect to '" + host + "' " + port);

		try {

			/*
			 * TODO: Current implementation wait (future.join()) until the
			 * connection with server is completed. This could block the thread
			 * for a long time. Check how to do it in asyncronous way.
			 */
			// Create TCP/IP connector.
			IoConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(30000);
			//RtspServerFilters myFilter = new RtspServerFilters();
			//connector.getFilterChain().addLast("codec", myFilter);
			connector.setHandler(new ServerSide());
			ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
			//future.join();
			serverSession = future.getSession();

		} catch (UnresolvedAddressException e) {
			log.warn("Destination unreachable: " + host + ":" + port);
			sendResponse(clientSession, RtspResponse.errorResponse(RtspCode.DestinationUnreachable));
			return;
		}

		//------------------
		IoConnector connector = new NioSocketConnector();
		

		connector.connect(new InetSocketAddress("192.168.14.12", 554));

		//------------------

		log.debug("Connected!");

		// Save current ProxyHandler into the ProtocolSession
		serverSession.setAttribute(ProxyHandler.ATTR, this);

		log.debug("Server session: " + serverSession.getAttributeKeys());
	}

	/**
	 * Closes both sides of communication.
	 */
	@SuppressWarnings("deprecation")
	public synchronized void closeAll() {
		if (clientSession != null && clientSession.isConnected())
			clientSession.close();
		if (serverSession != null && serverSession.isConnected())
			serverSession.close();

		// Remove ProxySession and Track instances
		if (clientSession != null) {
			ProxySession proxySession = (ProxySession) clientSession.getAttribute(ProxySession.ATTR);
			if (proxySession != null)
				proxySession.close();
		}
	}

	/**
	 * Sends an RTSP request message
	 * 
	 * @param session
	 *        current IoSession
	 * @param request
	 *        the message
	 */
	private void sendRequest(IoSession session, RtspRequest request) {
		//request.setCommonHeaders();
		try {
			session.write(request);
		} catch (Exception e) {
			log.error(e.getCause());
		}
	}

	/**
	 * Sends an RTSP response message
	 * 
	 * @param session
	 *        current IoSession
	 * @param response
	 *        the message
	 */
	private void sendResponse(IoSession session, RtspResponse response) {
		//response.setCommonHeaders();
		try {
			session.write(response);
		} catch (Exception e) {
			log.error(e.getCause());
		}
	}

}
