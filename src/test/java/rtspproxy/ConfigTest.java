/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: ConfigTest.java 287 2005-11-13 23:00:47Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/test/java/rtspproxy/ConfigTest
 * .java $
 */

package rtspproxy;

import junit.framework.TestCase;

/**
 * @author Matteo Merli
 */
public class ConfigTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(ConfigTest.class);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    new Config();
  }

  /*
   * Test method for 'rtspproxy.Config.get(String, String)'
   */
  public final void testGet() {
    Config.set("testKey", "testValue");

    assertEquals("testValue", Config.get("testKey", null));
    assertNull(Config.get("notPresentKey", null));
    assertEquals("test", Config.get("notPresentKey", "test"));
  }

  /*
   * Test method for 'rtspproxy.Config.getInt(String, int)'
   */
  public final void testGetInt() {
    int value = 12345678;
    Config.setInt("testKeyInt", value);

    assertEquals(value, Config.getInt("testKeyInt", 0));
    assertEquals(0, Config.getInt("notPresentKey", 0));
  }

  /*
   * Test method for 'rtspproxy.Config.getIntArray(String, int)'
   */
  public final void testGetIntArray() {
    int values[] = new int[] {23, 4, 5, 62, -43, 23};
    Config.setIntArray("testIntArray", values);

    int results[] = Config.getIntArray("testIntArray", 0);

    assertEquals(values.length, results.length);

    for (int i = 0; i < values.length; i++) {
      assertEquals(values[i], results[i]);
    }
  }

  /*
   * Test method for 'rtspproxy.Config.getBoolean(String, boolean)'
   */
  public final void testGetBoolean() {
    Config.setBoolean("testTrue", true);
    Config.setBoolean("testFalse", false);

    assertTrue(Config.getBoolean("testTrue"));
    assertTrue(Config.getBoolean("testTrue", false));
    assertTrue(Config.getBoolean("testTrue", true));

    assertFalse(Config.getBoolean("testFalse"));
    assertFalse(Config.getBoolean("testFalse", false));
    assertFalse(Config.getBoolean("testFalse", true));

    assertFalse(Config.getBoolean("notPresentKey"));
    assertTrue(Config.getBoolean("notPresentKey", true));
    assertFalse(Config.getBoolean("notPresentKey", false));
  }

}
