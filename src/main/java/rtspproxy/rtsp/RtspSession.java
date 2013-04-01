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
 * $Id: RtspSession.java 337 2005-12-08 21:06:48Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/rtsp/RtspSession.java $
 * 
 */

package rtspproxy.rtsp;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import rtspproxy.lib.number.UnsignedLong;

/**
 * RTSP is primarly a connection-less protocol, that means that RTSP request can
 * be made over multiples TCP connections. To identify such a "session", a
 * 64-bit identifier is used.
 */
public class RtspSession
{

	private static Logger log = Logger.getLogger( RtspSession.class );

	private static Map<String, RtspSession> sessions = new ConcurrentHashMap<String, RtspSession>();

	// Members
	/** Session ID */
	private String sessionId;
	/** Session associated tracks */
	private Map<String, Track> tracks = new ConcurrentHashMap<String, Track>();

	/**
	 * Creates a new empty RtspSession and stores it.
	 * 
	 * @param id
	 *        Session identifier
	 * @return The newly created session
	 */
	static public RtspSession create( String sessionId )
	{

		if ( sessions.get( sessionId ) != null ) {
			log.error( "Session key conflit!!" );
			return null;
		}
		RtspSession session = new RtspSession( sessionId );
		sessions.put( sessionId, session );
		log.debug( "New session created - id=" + sessionId );
		return session;
	}

	/**
	 * @return a new RtspSession with a new random ID
	 */
	static public RtspSession create()
	{
		return create( newSessionID() );
	}

	/**
	 * Access an opened session.
	 * 
	 * @param id
	 *        Session identifier
	 * @return The RtspSession identified by id or null if not present
	 */
	static public RtspSession get( String id )
	{
		if ( id == null )
			return null;

		long key = Long.valueOf( id );
		return sessions.get( key );
	}

	/**
	 * Close a session and remove resources.
	 * 
	 * @param id
	 *        Session identifier
	 */
	static public void close( String id )
	{
		long key = Long.valueOf( id );
		close( key );
	}

	/**
	 * Close the session and removes it.
	 * 
	 * @param id
	 *        the session ID
	 */
	static public void close( long id )
	{
		sessions.remove( id );
	}

	protected RtspSession( String sessionId )
	{
		this.sessionId = sessionId;
	}

	/**
	 * @return the session ID
	 */
	public String getId()
	{
		return sessionId;
	}

	/**
	 * @param control
	 *        the key to access the track
	 * @return the track
	 */
	public Track getTrack( String control )
	{
		return tracks.get( control );
	}

	/**
	 * @return the number of track contained in this sessions
	 */
	public int getTracksCount()
	{
		return tracks.size();
	}

	/**
	 * Adds a new track to the session
	 * 
	 * @param track
	 *        a Track object
	 */
	public void addTrack( Track track )
	{
		String control = track.getControl();
		tracks.put( control, track );
	}

	// / Session ID generation

	private static Random random = new Random();

	/**
	 * Creates a unique session ID
	 * 
	 * @return the session ID
	 */
	private static String newSessionID()
	{
		String id;
		synchronized ( random ) {
			while ( true ) {

				id = new UnsignedLong( random ).toString();
				if ( sessions.get( id ) == null ) {
					// Ok, the id is unique
					return id;
				}
			}
			// try with another id
		}
	}
}
