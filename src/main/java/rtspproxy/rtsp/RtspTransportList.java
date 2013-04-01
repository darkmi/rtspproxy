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
 * $Id: RtspTransportList.java 274 2005-11-13 21:32:01Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtsp/RtspTransportList.java $
 * 
 */

package rtspproxy.rtsp;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a list of transport headers.
 */
public class RtspTransportList
{

	private List<RtspTransport> transportList;

	/**
	 * Constructor. Creates a list of transport type.
	 */
	public RtspTransportList( String transportHeader )
	{
		transportList = new ArrayList<RtspTransport>();

		for ( String transport : transportHeader.split( "," ) ) {
			transportList.add( new RtspTransport( transport ) );
		}
	}

	public List<RtspTransport> getList()
	{
		return transportList;
	}

	public RtspTransport get( int index )
	{
		return transportList.get( index );
	}

	/**
	 * @return The number of transports defined.
	 */
	public int count()
	{
		return transportList.size();
	}

	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		int i = 0;
		for ( RtspTransport t : transportList ) {
			if ( i++ != 0 )
				buf.append( "," );
			buf.append( t.toString() );
		}
		return buf.toString();
	}

}
