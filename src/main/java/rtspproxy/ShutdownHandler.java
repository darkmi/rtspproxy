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
 * $Id: ShutdownHandler.java 333 2005-12-08 16:49:37Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/ShutdownHandler.java $
 * 
 */

package rtspproxy;

import org.apache.log4j.Logger;

import rtspproxy.lib.Exceptions;

/**
 * The thread holded by this class is started in the shutdown phase.
 * 
 * @author Matteo Merli
 */
public class ShutdownHandler extends Thread
{

	private static Logger log = Logger.getLogger( ShutdownHandler.class );

	public void run()
	{
		log.info( "Shutting down" );
		try {
			log.info( "Stopping " + Config.getName() + " " + Config.getVersion() );
			Reactor.stop();

		} catch ( Exception e ) {
			log.fatal( "Exception in the reactor: " + e );
			Exceptions.logStackTrace( e );
		}
	}

}
