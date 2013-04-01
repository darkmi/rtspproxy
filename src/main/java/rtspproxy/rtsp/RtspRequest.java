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
 * $Id: RtspRequest.java 202 2005-09-07 09:06:17Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtsp/RtspRequest.java $
 * 
 */

package rtspproxy.rtsp;

import java.net.URL;

/**
 * @author mat
 * 
 */
public class RtspRequest extends RtspMessage {

	public enum Verb {
		None, ANNOUNCE, DESCRIBE, GET_PARAMETER, OPTIONS, PAUSE, PLAY, RECORD, REDIRECT, SETUP, SET_PARAMETER, TEARDOWN
	};

	private Verb verb;
	private URL url;

	/**
	 * 
	 */
	public RtspRequest() {
		super();
		verb = Verb.None;
	}

	public Type getType() {
		return Type.TypeRequest;
	}

	public String getVerbString() {
		return verb.toString();
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public Verb getVerb() {
		return verb;
	}

	/**
	 * Sets the verb of the request from a string.
	 * 
	 * @param strVerb
	 *        String containing the the verb
	 */
	public void setVerb(String strVerb) {
		try {
			this.verb = Verb.valueOf(strVerb);
		} catch (Exception e) {
			this.verb = Verb.None;
			// System.out.println( "Invalid verb: " + strVerb );
		}
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

	/**
	 * Return a serialized version of the RTSP request message that will be sent
	 * over the network. The message is in the form:
	 * 
	 * <pre>
	 * [verb] SP [url] SP "RTSP/1.0" CRLF
	 * [headers] CRLF
	 * CRLF 
	 * [buffer]
	 * </pre>
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getVerbString() + " ");
		sb.append(url != null ? url : "*");
		sb.append(" RTSP/1.0\r\n");
		sb.append(getHeadersString());

		// Insert a blank line
		sb.append(CRLF);

		if (getBufferSize() > 0) {
			sb.append(getBuffer());
		}

		return sb.toString();
	}

}
