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
 * $Id: Handler.java 248 2005-10-23 18:47:41Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtsp/Handler.java $
 * 
 */

package rtspproxy.rtsp;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Register the "rtsp://" scheme as a valid protocol.
 */
public class Handler extends URLStreamHandler {

	public static final int DEFAULT_RTSP_PORT = 554;

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return null;
	}

	/**
	 * @return the default RTSP port
	 */
	@Override
	protected int getDefaultPort() {
		return DEFAULT_RTSP_PORT;
	}

}
