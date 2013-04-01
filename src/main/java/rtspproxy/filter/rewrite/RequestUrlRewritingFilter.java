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
 * $URL: https://rbieniek@svn.berlios.de/svnroot/repos/rtspproxy/trunk/src/main/java/rtspproxy/rtsp/Handler.java $
 * 
 */
package rtspproxy.filter.rewrite;

import java.net.URL;

/**
 * This filter is used to rewrite the requested URL before passing it
 * to the upstream server.
 * 
 * @author Rainer Bieniek
 */
public interface RequestUrlRewritingFilter {
	/**
	 * rewrite the request URL.
	 * @return a replacement URL or null if the URL is not to be modified.
	 */
	public URL rewriteRequestUrl(URL request);

	/**
	 * rewrite an URL in a response header.
	 * @return a replacement URL or null if the URL is not to be modified.
	 */
	public URL rewriteResponseHeaderUrl(URL request);
}
