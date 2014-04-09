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
 * $Id: RtspService.java 314 2005-12-04 13:56:30Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/RtspService.java $
 * 
 */

package rtspproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import rtspproxy.proxy.ClientSide;
import rtspproxy.rtsp.Handler;

/**
 * @author Matteo Merli
 */
public class RtspService implements ProxyService {
	private static Logger log = Logger.getLogger(RtspService.class);
	private static final int MIN_READ_BUFFER_SIZE = 2048;

	@SuppressWarnings("unused")
	public void start() throws IOException {
		// get port and network interface from config file
		int[] ports = Config.getIntArray("proxy.rtsp.port", Handler.DEFAULT_RTSP_PORT);
		String netInterface = Config.get("proxy.rtsp.interface", null);

		NioSocketAcceptor acceptor;
		acceptor = new NioSocketAcceptor();
		//acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
		acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		acceptor.setHandler(new ClientSide());
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setMinReadBufferSize(MIN_READ_BUFFER_SIZE);
		acceptor.getSessionConfig().setReuseAddress(true);

		if (netInterface == null) {
			for (int port : ports) {
				acceptor.bind(new InetSocketAddress(port));
				log.info("RtspService Started - Listening on: " + InetAddress.getByName(netInterface) + ":" + port);
			}

		} else {
			for (int port : ports) {
				acceptor.bind(new InetSocketAddress(netInterface, port));
			}
		}

		for (int port : ports) {
			//try {

				//if (netInterface == null){
				//	service = new Service("RtspService", TransportType.SOCKET, port);

				//}
				//else{
				//	service = new Service("RtspService", TransportType.SOCKET,
				//			new InetSocketAddress(netInterface, port));
				//}

				//Reactor.getRegistry().bind(service, new ClientSide(), new RtspClientFilters());

				//log.info("RtspService Started - Listening on: " + InetAddress.getByName(netInterface) + ":" + port);

			//} catch (IOException e) {
			//	log.fatal(e.getMessage() + " (port = " + port + ")");
			//	throw e;
			//}
		}
	}

	public void stop() throws Exception {
		//for (Object service : Reactor.getRegistry().getServices("RtspService")) {
		//	Reactor.getRegistry().unbind((Service) service);
		//}
		log.info("RtspService Stopped");
	}
}
