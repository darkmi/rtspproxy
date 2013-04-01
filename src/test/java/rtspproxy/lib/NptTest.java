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
 * $Id: NptTest.java 279 2005-11-13 21:42:59Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/lib/NptTest.java $
 * 
 */
package rtspproxy.lib;

import junit.framework.TestCase;

/**
 * @author mat
 */
public class NptTest extends TestCase
{

	public static void main( String[] args )
	{
		junit.textui.TestRunner.run( NptTest.class );
	}

	/*
	 * Test method for 'rtspproxy.lib.Npt.fromString(String)'
	 */
	public void testFromString1()
	{
		String s = "npt=0-23.4";
		Npt npt = Npt.fromString( s );

		assertNotNull( npt );
		assertEquals( 0.0, npt.getTimeStart() );
		assertEquals( 23.4, npt.getTimeEnd() );
		assertEquals( false, npt.isLive() );
		assertEquals( s, npt.toString() );
	}

	/*
	 * Test method for 'rtspproxy.lib.Npt.fromString(String)'
	 */
	public void testFromString2()
	{
		String s = "npt=0-";
		Npt npt = Npt.fromString( s );

		assertNotNull( npt );
		assertEquals( 0.0, npt.getTimeStart() );
		assertEquals( 0.0, npt.getTimeEnd() );
		assertEquals( false, npt.isLive() );
		assertEquals( s, npt.toString() );
	}

	/*
	 * Test method for 'rtspproxy.lib.Npt.fromString(String)'
	 */
	public void testFromString3()
	{
		String s = "npt=12.34-23.49";
		Npt npt = Npt.fromString( s );

		assertNotNull( npt );
		assertEquals( 12.34, npt.getTimeStart() );
		assertEquals( 23.49, npt.getTimeEnd() );
		assertEquals( false, npt.isLive() );
		assertEquals( s, npt.toString() );
	}

	/*
	 * Test method for 'rtspproxy.lib.Npt.fromString(String)'
	 */
	public void testFromString4()
	{
		String s = "npt=now-";
		Npt npt = Npt.fromString( s );

		assertNotNull( npt );
		assertEquals( 0.0, npt.getTimeStart() );
		assertEquals( 0.0, npt.getTimeEnd() );
		assertEquals( true, npt.isLive() );
		assertEquals( s, npt.toString() );
	}
/*	
	/*
	 * Test method for 'rtspproxy.lib.Npt.fromString(String)'
	 *
	public void testFromString5()
	{
		String s = "npt=12:05:35.3-";
		Npt npt = Npt.fromString( s );

		assertNotNull( npt );
		assertEquals( 12 * 3600 + 5 * 60 + 35.3, npt.getTimeStart() );
		assertEquals( 0.0, npt.getTimeEnd() );
		assertEquals( false, npt.isLive() );
		assertEquals( s, npt.toString() );
	}
*/
}
