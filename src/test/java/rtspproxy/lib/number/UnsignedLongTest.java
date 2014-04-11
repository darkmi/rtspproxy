/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: UnsignedLongTest.java 302 2005-12-01 20:26:12Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/lib/number
 * /UnsignedLongTest.java $
 */
package rtspproxy.lib.number;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * @author Matteo Merli
 */
public class UnsignedLongTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(UnsignedLongTest.class);
  }

  public void test1() {
    UnsignedLong n = new UnsignedLong(0xFFFFFFFFFFFFFFFFL);

    assertEquals(0xFFFFFFFFFFFFFFFFL, n.longValue());
    assertEquals("FFFFFFFFFFFFFFFF", n.toHexString());
    assertEquals("18446744073709551615", n.toString());
    assertTrue(Arrays.equals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, n.getBytes()));
  }

  public void test2() {
    UnsignedLong n = new UnsignedLong(0xFFFFFFFFL);

    assertEquals(0xFFFFFFFF, n.intValue());
    assertEquals(0xFFFFFFFFL, n.longValue());
    assertEquals("FFFFFFFF", n.toHexString());
    assertEquals("00000000FFFFFFFF", n.toHexString(true));
    assertEquals("4294967295", n.toString());
    assertTrue(Arrays.equals(new byte[] {0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF}, n.getBytes()));

  }

  public void test3() {
    UnsignedLong n = UnsignedLong.fromString("FFFFFFFFFFFFFFFF", 16);

    assertEquals(0xFFFFFFFFFFFFFFFFL, n.longValue());
    assertEquals("FFFFFFFFFFFFFFFF", n.toHexString());
    assertEquals("18446744073709551615", n.toString());
    assertTrue(Arrays.equals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, n.getBytes()));
  }

  public void test4() {
    UnsignedLong n = UnsignedLong.fromString("18446744073709551615");

    assertEquals(0xFFFFFFFFFFFFFFFFL, n.longValue());
    assertEquals("FFFFFFFFFFFFFFFF", n.toHexString());
    assertEquals("18446744073709551615", n.toString());
    assertTrue(Arrays.equals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, n.getBytes()));
  }

  public void test5() {
    UnsignedLong n =
        UnsignedLong.fromBytes(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});

    assertEquals(0xFFFFFFFFFFFFFFFFL, n.longValue());
    assertEquals("FFFFFFFFFFFFFFFF", n.toHexString());
    assertEquals("18446744073709551615", n.toString());
    assertTrue(Arrays.equals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, n.getBytes()));
  }

  public void test6() {
    UnsignedLong n1 = new UnsignedLong(0);
    UnsignedLong n2 = new UnsignedLong(0xFFFF);
    UnsignedLong n3 = new UnsignedLong(0xFFFFFFFFL);
    UnsignedLong n4 = new UnsignedLong(0xFFFFFFFFFFFFFFFFL);
    UnsignedLong n5 =
        UnsignedLong.fromBytes(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});

    assertTrue(n1.compareTo(n1) == 0);
    assertTrue(n1.compareTo(n2) < 0);
    assertTrue(n1.compareTo(n3) < 0);
    assertTrue(n1.compareTo(n4) < 0);
    assertTrue(n1.compareTo(n5) < 0);
    assertTrue(n2.compareTo(n1) > 0);
    assertTrue(n2.compareTo(n2) == 0);
    assertTrue(n2.compareTo(n3) < 0);
    assertTrue(n2.compareTo(n4) < 0);
    assertTrue(n2.compareTo(n5) < 0);
    assertTrue(n3.compareTo(n1) > 0);
    assertTrue(n3.compareTo(n2) > 0);
    assertTrue(n3.compareTo(n3) == 0);
    assertTrue(n3.compareTo(n4) < 0);
    assertTrue(n3.compareTo(n5) < 0);
    assertTrue(n4.compareTo(n1) > 0);
    assertTrue(n4.compareTo(n2) > 0);
    assertTrue(n4.compareTo(n3) > 0);
    assertTrue(n4.compareTo(n4) == 0);
    assertTrue(n4.compareTo(n5) == 0);
    assertTrue(n5.compareTo(n1) > 0);
    assertTrue(n5.compareTo(n2) > 0);
    assertTrue(n5.compareTo(n3) > 0);
    assertTrue(n5.compareTo(n4) == 0);
    assertTrue(n5.compareTo(n5) == 0);
  }

  public void testShift() {
    UnsignedLong n = new UnsignedLong(0x01);

    n.shiftLeft(8);
    assertEquals(0x100, n.intValue());
    n.shiftLeft(16);
    assertEquals(0x1000000, n.intValue());
    n.shiftRight(24);
    assertEquals(0x01, n.intValue());

    n = new UnsignedLong(0xACL);
    n.shiftLeft(8);
    assertEquals(0xAC00, n.intValue());
    n.shiftLeft(24);
    assertEquals(0xAC00000000L, n.longValue());
    n.shiftLeft(24);
    assertEquals(0xAC00000000000000L, n.longValue());
    n.shiftRight(32);
    assertEquals(0xAC000000L, n.longValue());
  }
}
