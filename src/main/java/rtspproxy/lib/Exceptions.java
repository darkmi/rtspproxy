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
 * $Id: Exceptions.java 291 2005-11-24 19:48:52Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/lib/Exceptions.java $
 * 
 */

package rtspproxy.lib;

import org.apache.log4j.Logger;

/**
 * Provide logging for stack trace of exceptions.
 * 
 * @author Matteo Merli
 */
public class Exceptions
{

	private static Logger log = Logger.getRootLogger();

	private final static String CRLF = "\r\n";

	/**
	 * Print a stack trace of the throwable in the log appender.
	 * The trace is only appended if the log level is higher
	 * than DEBUG.
	 *  
	 * @param throwable 
	 */
	public static synchronized void logStackTrace( Throwable throwable )
	{
		if ( !log.isDebugEnabled() )
			return;

		StringBuilder sb = new StringBuilder();
		sb.append( throwable.toString() + CRLF );
		StackTraceElement[] trace = throwable.getStackTrace();
		for ( int i = 0; i < trace.length; i++ )
			sb.append( "\tat " + trace[i] + CRLF );

		Throwable cause = throwable.getCause();
		if ( cause != null )
			sb = getStackTraceAsCause( sb, cause, trace );

		log.debug( sb.toString() );
	}

	private static synchronized StringBuilder getStackTraceAsCause( StringBuilder sb,
			Throwable cause, StackTraceElement[] causedTrace )
	{
		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = cause.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while ( m >= 0 && n >= 0 && trace[m].equals( causedTrace[n] ) ) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		sb.append( "Caused by: " + cause.toString() );
		for ( int i = 0; i <= m; i++ )
			sb.append( "\tat " + trace[i] + CRLF );
		if ( framesInCommon != 0 )
			sb.append( "\t... " + framesInCommon + " more" + CRLF );

		// Recurse if we have a cause
		Throwable ourCause = cause.getCause();
		if ( ourCause != null )
			sb = getStackTraceAsCause( sb, cause, trace );

		return sb;
	}
}
