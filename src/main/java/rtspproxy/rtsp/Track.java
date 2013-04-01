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
 * $Id: Track.java 307 2005-12-01 21:36:10Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtsp/Track.java $
 * 
 */

package rtspproxy.rtsp;

// import rtspproxy.proxy.DataTunnel;

/**
 */
public class Track {

	private String control;

	// private DataTunnel dataTunnel;

	/**
	 * @return Returns the control.
	 */
	public String getControl() {
		return control;
	}

	/**
	 * @param control
	 *        The control to set.
	 */
	public void setControl(String control) {
		this.control = control;
	}
	/*
		public void setDataTunnel( DataTunnel dataTunnel )
		{
			this.dataTunnel = dataTunnel;
		}

		public DataTunnel getDataTunnel()
		{
			return dataTunnel;
		}
	*/
}
