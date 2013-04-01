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
 * $Id: Reactor.java 333 2005-12-08 16:49:37Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/Reactor.java $
 * 
 */

package rtspproxy;

import org.apache.log4j.Logger;

/**
 * 
 */
public class Reactor {

	private static Logger log = Logger.getLogger(Reactor.class);

	//private static ServiceRegistry registry = new SimpleServiceRegistry();

	private static ProxyService rtspService;
	private static ProxyService rtpClientService;
	private static ProxyService rtpServerService;

	private static boolean isStandalone = false;

	public static void setStandalone(boolean standalone) {
		isStandalone = standalone;
	}

	/**
	 * Constructor. Creates a new Reactor and starts it.
	 */
	static public void start() throws Exception {
		rtspService = new RtspService();
		rtspService.start();

		rtpClientService = new RtpClientService();
		rtpClientService.start();

		rtpServerService = new RtpServerService();
		rtpServerService.start();
	}

	static public void stop() {
		try {
			// registry.unbindAll();
			if (rtspService != null)
				rtspService.stop();
			if (rtpClientService != null)
				rtpClientService.stop();
			if (rtpServerService != null)
				rtpServerService.stop();
		} catch (Exception e) {
			log.debug("Error shutting down: " + e);
		}

		log.info("Shutdown completed");

		if (isStandalone)
			Runtime.getRuntime().halt(0);
	}

	//protected static synchronized ServiceRegistry getRegistry() {
	//	return registry;
	//}
}
