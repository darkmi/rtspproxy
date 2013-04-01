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
 * $Id: PlainTextIpAddressProviderTest.java 326 2005-12-08 10:34:20Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/filter/ipaddress/PlainTextIpAddressProviderTest.java $
 * 
 */
package rtspproxy.filter.ipaddress;

import java.io.StringReader;
import java.net.InetAddress;

import rtspproxy.filter.ipaddress.PlainTextIpAddressProvider;

import junit.framework.TestCase;

/**
 * @author Matteo Merli
 */
public class PlainTextIpAddressProviderTest extends TestCase
{

	private static final String CRLF = "\r\n";

	public static void main( String[] args )
	{
		junit.textui.TestRunner.run( PlainTextIpAddressProviderTest.class );
	}

	public void test1() throws Exception
	{
		// prepare
		PlainTextIpAddressProvider provider = new PlainTextIpAddressProvider();
		StringBuilder rules = new StringBuilder();
		rules.append( "Deny *" + CRLF );
		rules.append( "Allow 127.0.0.1" + CRLF );
		provider.loadRules( new StringReader( rules.toString() ) );

		// tests
		assertTrue( provider.isBlocked( InetAddress.getByName( "10.0.0.2" ) ) );
		assertFalse( provider.isBlocked( InetAddress.getByName( "127.0.0.1" ) ) );

		// close
		provider.shutdown();
	}
	
	public void test2() throws Exception
	{
		// prepare
		PlainTextIpAddressProvider provider = new PlainTextIpAddressProvider();
		StringBuilder rules = new StringBuilder();
		rules.append( "Allow *" + CRLF );
		rules.append( "Deny 10.0.0.13" + CRLF );
		provider.loadRules( new StringReader( rules.toString() ) );

		// tests
		assertTrue( provider.isBlocked( InetAddress.getByName( "10.0.0.13" ) ) );
		assertFalse( provider.isBlocked( InetAddress.getByName( "127.0.0.1" ) ) );

		// close
		provider.shutdown();
	}

}
