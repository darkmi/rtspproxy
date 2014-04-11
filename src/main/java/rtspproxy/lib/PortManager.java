/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: PortManager.java 293 2005-11-24 19:50:47Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/lib/PortManager
 * .java $
 */

package rtspproxy.lib;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * The PortManager will keep a list of reserved ports
 */
public class PortManager {

  private static Logger log = Logger.getLogger(PortManager.class);

  protected static final int minUdpPort = 6790;
  protected static final int maxUdpPort = 49151;
  private static Set<Integer> reservedPorts = Collections.synchronizedSet(new HashSet<Integer>());

  // TODO: Using custom exceptions
  public static synchronized void reservePort(int port) throws Exception {
    if (reservedPorts.contains(port)) throw new Exception("Port " + port + "is reserved");

    reservedPorts.add(port);
  }

  public static synchronized void removePort(int port) {
    reservedPorts.remove(port);
  }

  /**
   * @param port To port to be tested
   * @return true if the port is already reserved, false if the port can be used.
   */
  public static synchronized boolean isPortReserved(int port) {
    return reservedPorts.contains(port);
  }

  /**
   * Get the first port (starting from <i>start</i>) that does not appear in the reservation list.
   * 
   * @param start the base port number to start from
   * @return the port number if found
   */
  public static synchronized int getNextNotReservedPort(int start) throws NoPortAvailableException {
    int port = start;
    while (reservedPorts.contains(port)) {
      if (port > maxUdpPort) {
        // port not found
        throw new NoPortAvailableException();
      }
      port += 1;
    }
    return port;
  }

  public static synchronized int[] findAvailablePorts(int nPorts, int startFrom)
      throws NoPortAvailableException {
    int dataPort, controlPort, startingPort;

    startingPort = startFrom;

    while (true) {

      startingPort = getNextNotReservedPort(startingPort);
      dataPort = getNextPortAvailable(startingPort);

      if (isPortReserved(dataPort)) {
        // The port is effectively unbound, but reserved in
        // PortManager.
        startingPort += nPorts;
        continue;
      }

      if (nPorts == 1) {
        // There is only the data port
        int[] a = {dataPort};
        log.debug("DataPort: " + dataPort);
        try {
          reservePort(dataPort);
        } catch (Exception e) {
          continue;
        }
        return a;

      } else if (nPorts == 2) {
        // We have to find 2 consequents free UDP ports.
        // also: dataPort must be an even number
        if ((dataPort % 2) != 0) {
          continue;

        } else {
          controlPort = getNextPortAvailable(dataPort + 1);

          if (controlPort != (dataPort + 1)) {
            // port are not consequents
            continue;
          } else if (isPortReserved(controlPort)) {
            continue;

          } else {
            try {
              reservePort(dataPort);
              reservePort(controlPort);
            } catch (Exception e) {
              continue;
            }

            int[] a = {dataPort, controlPort};
            log.debug("DataPort: " + dataPort + " - ControlPort: " + controlPort);
            return a;
          }
        }
      }
    }
  }

  private static int getNextPortAvailable(int startPort) throws NoPortAvailableException {

    for (int port = startPort; port <= maxUdpPort; port++) {
      DatagramSocket s = null;
      try {
        s = new DatagramSocket(port);
        s.close();
        return port;

      } catch (IOException e) {
        // Ignore
      }
    }

    // No port is available
    throw new NoPortAvailableException();
  }

}
