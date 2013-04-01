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
 * $Id: RtcpPacketTest.java 309 2005-12-01 22:36:28Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/rtp/rtcp/RtcpPacketTest.java $
 * 
 */

package rtspproxy.rtp.rtcp;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.mina.core.buffer.IoBuffer;

import rtspproxy.lib.number.UnsignedInt;

/**
 * @author mat
 */
public class RtcpPacketTest extends TestCase
{

	public static void main( String[] args )
	{
		junit.textui.TestRunner.run( RtcpPacketTest.class );
	}

	/*
	 * Test method for 'rtspproxy.Config.get(String, String)'
	 */
	public void testRtcpPacket()
	{
		/* Construct a new dummy packet */
		RtcpPacket packet = new RtcpPacket();
		packet.version = 2;
		packet.padding = true;
		packet.count = 4;
		packet.packetType = RtcpPacket.Type.SDES.getValue();
		packet.length = 4;
		packet.setSsrc( new UnsignedInt( 0xADADADADADL ) );
		byte[] random_data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		packet.packetBuffer = random_data;

		/* Convert it to a ByteBuffer */
		IoBuffer buffer = packet.toByteBuffer();

		/* Recreate a RtcpPacket from buffer */
		RtcpPacket packet2 = new RtcpPacket( buffer );

		/* Compare the two packets */
		Assert.assertEquals( packet.version, packet2.version );
		Assert.assertEquals( packet.padding, packet2.padding );
		Assert.assertEquals( packet.count, packet2.count );
		Assert.assertEquals( packet.packetType, packet2.packetType );
		Assert.assertEquals( packet.length, packet2.length );
		Assert.assertEquals( packet.ssrc, packet2.ssrc );
		Assert.assertTrue( Arrays.equals( packet.packetBuffer, packet2.packetBuffer ) );
	}

}
