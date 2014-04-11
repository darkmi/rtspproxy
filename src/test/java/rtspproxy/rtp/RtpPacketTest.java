/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: RtpPacketTest.java 309 2005-12-01 22:36:28Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/rtp/
 * RtpPacketTest.java $
 */

package rtspproxy.rtp;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.mina.core.buffer.IoBuffer;

import rtspproxy.lib.number.UnsignedByte;
import rtspproxy.lib.number.UnsignedInt;
import rtspproxy.lib.number.UnsignedShort;

/**
 * @author mat
 */
public class RtpPacketTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(RtpPacketTest.class);
  }

  public void testRtpPacket() {
    // Set the values
    byte version = 2;
    boolean padding = true;
    boolean extension = false;
    byte csrcCount = 3;
    boolean marker = false;
    UnsignedByte payloadType = new UnsignedByte(123);
    UnsignedShort sequence = new UnsignedShort(1234);
    UnsignedInt timestamp = new UnsignedInt(1231234);
    UnsignedInt ssrc = new UnsignedInt(43212L);
    UnsignedInt csrc[] = {new UnsignedInt(8), new UnsignedInt(8), new UnsignedInt(8)};
    byte payload[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    /* Construct a packet */
    RtpPacket p1 = new RtpPacket();
    p1.setVersion(version);
    p1.setPadding(padding);
    p1.setExtension(extension);
    p1.setCsrcCount(csrcCount);
    p1.setMarker(marker);
    p1.setPayloadType(payloadType);
    p1.setSequence(sequence);
    p1.setTimestamp(timestamp);
    p1.setSsrc(ssrc);
    p1.setCsrc(csrc);
    p1.setPayload(payload);

    /* Convert it to a ByteBuffer */
    IoBuffer buffer = p1.toByteBuffer();
    // System.err.println("Buffer1: " + buffer);

    /* Reconvert it to a RtpPacket object */
    RtpPacket p2 = new RtpPacket(buffer);

    /* Test that they are equals */
    // System.err.println("Buffer2: " + p2.toByteBuffer());
    assertEquals(p1.getVersion(), p2.getVersion());
    assertEquals(p1.isPadding(), p2.isPadding());
    assertEquals(p1.isExtension(), p2.isExtension());
    assertEquals(p1.getCsrcCount(), p2.getCsrcCount());
    assertEquals(p1.isMarker(), p2.isMarker());
    assertEquals(p1.getPayloadType(), p2.getPayloadType());
    assertEquals(p1.getSequence(), p2.getSequence());
    assertTrue(Arrays.equals(p1.getCsrc(), p2.getCsrc()));
    assertTrue(Arrays.equals(p1.getPayload(), p2.getPayload()));
  }
}
